package com.becker.freelance.component.prediction.storage.adapter.qdrant;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.grpc.Collections;

import java.util.concurrent.ExecutionException;

public class QdrantCollectionEnsurer {

    private final QdrantClient client;
    private final long vectorSize;
    private final Collections.Distance distance;
    private final boolean onDisk;
    private final Collections.Datatype datatype;

    public QdrantCollectionEnsurer(QdrantClient client, long vectorSize, Collections.Distance distance, boolean onDisk, Collections.Datatype datatype) {
        this.client = client;
        this.vectorSize = vectorSize;
        this.distance = distance;
        this.onDisk = onDisk;
        this.datatype = datatype;
    }

    public void ensureCollectionExists(String collectionName) {
        if (collectionExists(collectionName)) {
            return;
        }

        Collections.VectorParams vectorParams = Collections.VectorParams.newBuilder()
                .setSize(vectorSize)
                .setDistance(distance)
                .setOnDisk(onDisk)
                .setDatatype(datatype)
                .build();

        Collections.VectorsConfig vectorsConfig = Collections.VectorsConfig.newBuilder()
                .setParamsMap(Collections.VectorParamsMap.newBuilder()
                        .putMap(QdrantEmbeddingRepository.EMBEDDING_TYPE_ACTION_DESCRIPTION, vectorParams)
                        .build())
                .build();

        Collections.CreateCollection createCollectionRequest = Collections.CreateCollection.newBuilder()
                .setCollectionName(collectionName)
                .setVectorsConfig(vectorsConfig)
                .build();

        Collections.CollectionOperationResponse collectionOperationResponse = null;
        try {
            collectionOperationResponse = client.createCollectionAsync(createCollectionRequest).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Could not create collection '" + collectionName + "'.");
        }

        if (!collectionOperationResponse.isInitialized()) {
            throw new IllegalStateException("Could not create collection '" + collectionName + "'. Qdrant-Response: " + collectionOperationResponse);
        }
    }

    private boolean collectionExists(String collectionName) {
        try {
            Collections.CollectionInfo collectionInfo = client.getCollectionInfoAsync(collectionName).get();
            return collectionInfo.isInitialized();
        } catch (ExecutionException | InterruptedException e) {
            return false;
        }
    }
}
