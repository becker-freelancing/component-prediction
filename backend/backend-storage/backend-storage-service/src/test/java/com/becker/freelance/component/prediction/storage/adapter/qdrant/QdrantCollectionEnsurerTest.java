package com.becker.freelance.component.prediction.storage.adapter.qdrant;

import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Collections;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class QdrantCollectionEnsurerTest {

    private static final DockerImageName QDRANT_IMAGE = DockerImageName.parse("qdrant/qdrant:latest");
    private GenericContainer<?> qdrantContainer;
    private QdrantClient client;

    @BeforeAll
    void setUpContainer() {
        qdrantContainer = new GenericContainer<>(QDRANT_IMAGE)
                .withExposedPorts(6334);
        qdrantContainer.start();

        String host = qdrantContainer.getHost();
        Integer port = qdrantContainer.getMappedPort(6334);

        client = new QdrantClient(QdrantGrpcClient.newBuilder(host, port, false)
                .build());
    }

    @AfterAll
    void tearDownContainer() {
        if (qdrantContainer != null) {
            qdrantContainer.stop();
        }
    }

    @Test
    void testEnsureCollectionExists_createsNewCollection() throws ExecutionException, InterruptedException {
        String collectionName = "test_collection";

        assertThrows(ExecutionException.class, () -> client.getCollectionInfoAsync(collectionName).get());

        QdrantCollectionEnsurer ensurer = new QdrantCollectionEnsurer(
                client,
                4,
                Collections.Distance.Cosine,
                false,
                Collections.Datatype.Float32
        );

        ensurer.ensureCollectionExists(collectionName);

        Collections.CollectionInfo info = client.getCollectionInfoAsync(collectionName).get();
        assertTrue(info.isInitialized());
    }

    @Test
    void testEnsureCollectionExists_existingCollection() throws ExecutionException, InterruptedException {
        String collectionName = "existing_collection";

        QdrantCollectionEnsurer ensurer = new QdrantCollectionEnsurer(
                client,
                4,
                Collections.Distance.Cosine,
                false,
                Collections.Datatype.Float32
        );
        ensurer.ensureCollectionExists(collectionName);

        assertDoesNotThrow(() -> ensurer.ensureCollectionExists(collectionName));

        Collections.CollectionInfo info = client.getCollectionInfoAsync(collectionName).get();
        assertTrue(info.isInitialized());
    }
}
