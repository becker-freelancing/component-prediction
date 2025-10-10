package com.becker.freelance.component.prediction.storage.adapter.qdrant;

import com.becker.freelance.component.prediction.storage.domain.DocumentEmbedding;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import com.becker.freelance.component.prediction.storage.spi.EmbeddingRepository;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.JsonWithInt;
import io.qdrant.client.grpc.Points;

import java.util.ArrayList;
import java.util.List;
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

    private void upsertEmbeddings(DocumentEmbedding embedding, DocumentMetadata relatedMetadata) {

        List<Points.PointStruct> points = buildPoints(embedding, relatedMetadata);

        Points.UpsertPoints upsertRequest = Points.UpsertPoints.newBuilder()
                .setCollectionName(getCollectionName(relatedMetadata))
                .addAllPoints(points)
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
