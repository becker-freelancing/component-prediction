package com.becker.freelance.component.prediction.storage.adapter.qdrant;

import com.becker.freelance.component.prediction.storage.domain.DocumentEmbedding;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import com.becker.freelance.component.prediction.storage.spi.EmbeddingRepository;
import com.google.protobuf.Descriptors;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class QdrantEmbeddingRepository implements EmbeddingRepository {

    private static final String PAYLOAD_KEY_DOCUMENT_ID = "metadata_id";
    private static final String PAYLOAD_KEY_EMBEDDING_TYPE = "embedding_type";
    private static final String EMBEDDING_TYPE_ACTION_DESCRIPTION = "action_description";

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

        Points.PointsSelector request = Points.PointsSelector.newBuilder()
                .setFilter(filter)
                .build();

        Points.GetPoints.newBuilder()
                .setCollectionName(collectionName)
                .setWithPayload(Points.WithPayloadSelector.newBuilder()
                        .setInclude(Points.PayloadIncludeSelector.newBuilder()
                                .setField(Descriptors.FieldDescriptor.).build())
                        .build())

        List<float[]> embeddings = new ArrayList<>();

        try {
            Points.ScrollPointsResponse response = client.ret(request);
            response.getResultList().forEach(point -> {
                List<Float> vector = point.getVectors().getVector().getDataList();
                float[] vecArray = new float[vector.size()];
                for (int i = 0; i < vector.size(); i++) {
                    vecArray[i] = vector.get(i);
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
        for (float[] embed : embedding.embeddedActionDescription()) {
            Points.PointStruct struct = Points.PointStruct.newBuilder()
                    .setId(Points.PointId.newBuilder().setUuid(UUID.randomUUID().toString()))
                    .setVectors(Points.Vectors.newBuilder().setVector(Points.Vector.newBuilder().addAllData(toFloatList(embed)).build()).build())
                    .putPayload(PAYLOAD_KEY_DOCUMENT_ID, JsonWithInt.Value.newBuilder().setStringValue(relatedMetadata.getId().toString()).build())
                    .putPayload(PAYLOAD_KEY_EMBEDDING_TYPE, JsonWithInt.Value.newBuilder().setStringValue(EMBEDDING_TYPE_ACTION_DESCRIPTION).build())
                    .build();

            points.add(struct);
        }
        return points;
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
