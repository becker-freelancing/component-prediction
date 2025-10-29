package com.becker.freelance.component.prediction.storage.adapter.qdrant;

import com.becker.freelance.component.prediction.storage.domain.DocumentEmbedding;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import com.becker.freelance.component.prediction.storage.spi.EmbeddingRepository;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;
import jakarta.transaction.Transactional;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Transactional
public class QdrantEmbeddingRepository implements EmbeddingRepository {

    private static final String PAYLOAD_KEY_DOCUMENT_ID = "metadata_id";
    private static final String PAYLOAD_KEY_EMBEDDING_TYPE = "embedding_type";
    public static final String EMBEDDING_TYPE_ACTION_DESCRIPTION = "action_description";
    private static final String PAYLOAD_ORIGINAL_VECTOR_L2_NORM = "original_l2_norm";
    private static final String PAYLOAD_INSERT_ORDER = "insert_order";

    private final DocumentMetadataRepository metadataRepository;
    private final QdrantClient client;
    private final QdrantCollectionEnsurer collectionEnsurer;

    public QdrantEmbeddingRepository(DocumentMetadataRepository metadataRepository, QdrantClient client, QdrantCollectionEnsurer collectionEnsurer) {
        this.metadataRepository = metadataRepository;
        this.client = client;
        this.collectionEnsurer = collectionEnsurer;
    }

    @Override
    public DocumentMetadata save(DocumentEmbedding embedding) {
        DocumentMetadata relatedMetadata = metadataRepository.findById(embedding.metadataId()).orElseThrow(() -> new IllegalStateException("No Metadata with id '" + embedding.metadataId() + "' found"));

        collectionEnsurer.ensureCollectionExists(getCollectionName(relatedMetadata));

        deleteVectorsWithDocumentId(relatedMetadata);

        upsertEmbeddings(embedding, relatedMetadata);

        return relatedMetadata;
    }

    public Optional<DocumentEmbedding> findByMetadataId(UUID metadataId) {
        // Get Metadata
        DocumentMetadata relatedMetadata = metadataRepository.findById(metadataId)
                .orElse(null);
        if (relatedMetadata == null) {
            return Optional.empty();
        }

        String collectionName = getCollectionName(relatedMetadata);

        // Filter by metadata_id
        Points.Filter filter = Points.Filter.newBuilder()
                .addMust(Points.Condition.newBuilder()
                        .setField(Points.FieldCondition.newBuilder()
                                .setKey(PAYLOAD_KEY_DOCUMENT_ID)
                                .setMatch(Points.Match.newBuilder().setKeyword(metadataId.toString()).build())
                                .build())
                        .build())
                .build();

        Points.WithVectorsSelector vectorsSelector = Points.WithVectorsSelector.newBuilder()
                .setEnable(true)
                .setInclude(Points.VectorsSelector.newBuilder()
                        .addNames(EMBEDDING_TYPE_ACTION_DESCRIPTION).build())
                .build();

        Points.ScrollPoints request = Points.ScrollPoints.newBuilder()
                .setCollectionName(collectionName)
                .setFilter(filter)
                .setLimit(Integer.MAX_VALUE)
                .setWithVectors(vectorsSelector)
                .build();

        List<float[]> embeddings = new ArrayList<>();

        try {
            Points.ScrollResponse response = client.scrollAsync(request).get();
            response.getResultList().stream().sorted(Comparator.comparing(point -> point.getPayloadOrThrow(PAYLOAD_INSERT_ORDER).getIntegerValue())).forEach(point -> {
                List<Float> vector = point.getVectors().getVectors().getVectorsOrThrow(EMBEDDING_TYPE_ACTION_DESCRIPTION).getDataList();
                float l2Norm = (float) point.getPayloadOrThrow(PAYLOAD_ORIGINAL_VECTOR_L2_NORM).getDoubleValue();
                float[] vecArray = new float[vector.size()];
                for (int i = 0; i < vector.size(); i++) {
                    vecArray[i] = denormalize(vector.get(i), l2Norm);
                }
                embeddings.add(vecArray);
            });
        } catch (Exception e) {
            throw new IllegalStateException("Fehler beim Abrufen von Embeddings für Metadata-ID: " + metadataId, e);
        }

        if (embeddings.isEmpty()) {
            return Optional.empty();
        }

        return Optional.of(new DocumentEmbedding(metadataId, embeddings.toArray(new float[0][])));
    }

    private float denormalize(Float f, float l2Norm) {
        return f * l2Norm;
    }

    private void upsertEmbeddings(DocumentEmbedding embedding, DocumentMetadata relatedMetadata) {

        List<Points.PointStruct> points = buildPoints(embedding, relatedMetadata);

        Points.UpsertPoints upsertRequest = Points.UpsertPoints.newBuilder()
                .setCollectionName(getCollectionName(relatedMetadata))
                .addAllPoints(points)
                .setWait(true)
                .build();

        Points.UpdateResult updateResult = null;
        try {
            updateResult = client.upsertAsync(upsertRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Could not upsert embeddings for metadata id '" + relatedMetadata.getId() + "'.", e);
        }
        if (updateResult.getStatus() != Points.UpdateStatus.Completed) {
            throw new IllegalStateException("Could not upsert embeddings for metadata id '" + relatedMetadata.getId() + "'. Status: " + updateResult.getStatus());
        }
    }

    private List<Points.PointStruct> buildPoints(DocumentEmbedding embedding, DocumentMetadata relatedMetadata) {
        List<Points.PointStruct> points = new ArrayList<>();
        float[][] embeddeded = embedding.embeddedActionDescription();
        for (int i = 0; i < embeddeded.length; i++) {
            float[] embed = embeddeded[i];
            Points.Vector vector = Points.Vector.newBuilder().addAllData(toFloatList(embed)).build();
            Points.NamedVectors namedVectors = Points.NamedVectors.newBuilder().putAllVectors(Map.of(EMBEDDING_TYPE_ACTION_DESCRIPTION, vector)).build();
            Points.Vectors vectors = Points.Vectors.newBuilder().setVectors(namedVectors).build();
            Points.PointStruct struct = Points.PointStruct.newBuilder()
                    .setId(Points.PointId.newBuilder().setUuid(UUID.randomUUID().toString()))
                    .setVectors(vectors)
                    .putPayload(PAYLOAD_KEY_DOCUMENT_ID, JsonWithInt.Value.newBuilder().setStringValue(relatedMetadata.getId().toString()).build())
                    .putPayload(PAYLOAD_KEY_EMBEDDING_TYPE, JsonWithInt.Value.newBuilder().setStringValue(EMBEDDING_TYPE_ACTION_DESCRIPTION).build())
                    .putPayload(PAYLOAD_INSERT_ORDER, JsonWithInt.Value.newBuilder().setIntegerValue(i).build())
                    .putPayload(PAYLOAD_ORIGINAL_VECTOR_L2_NORM, JsonWithInt.Value.newBuilder().setDoubleValue(calcL2Norm(embed)).build())
                    .build();

            points.add(struct);
        }
        return points;
    }

    private double calcL2Norm(float[] embed) {
        float sum = 0f;
        for (float f : embed) {
            sum += f * f;
        }
        return Math.sqrt(sum);
    }

    private List<Float> toFloatList(float[] arr) {
        List<Float> list = new ArrayList<>(arr.length);
        for (float f : arr) list.add(f);
        return list;
    }

    private void deleteVectorsWithDocumentId(DocumentMetadata metadata) {
        Points.Filter documentIdPayloadFilter = Points.Filter.newBuilder()
                .addMust(Points.Condition.newBuilder()
                        .setField(Points.FieldCondition.newBuilder()
                                .setKey(PAYLOAD_KEY_DOCUMENT_ID)
                                .setMatch(Points.Match.newBuilder().setKeyword(metadata.getId().toString()).build()).build()).build()).build();

        Points.PointsSelector documentIdPointsSelector = Points.PointsSelector.newBuilder()
                .setFilter(documentIdPayloadFilter)
                .build();

        Points.DeletePoints deleteByDocumentIdRequest = Points.DeletePoints.newBuilder()
                .setCollectionName(getCollectionName(metadata))
                .setPoints(documentIdPointsSelector)
                .setWait(true)
                .build();

        Points.UpdateResult updateResult = null;
        try {
            updateResult = client.deleteAsync(deleteByDocumentIdRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Could not delete embeddings for metadata id '" + metadata.getId() + "'.", e);
        }
        if (updateResult.getStatus() != Points.UpdateStatus.Completed) {
            throw new IllegalStateException("Could not delete embeddings for metadata id '" + metadata.getId() + "'. Status: " + updateResult.getStatus());
        }

    }

    private String getCollectionName(DocumentMetadata metadata) {
        return metadata.getApp().getId().toString();
    }

}
