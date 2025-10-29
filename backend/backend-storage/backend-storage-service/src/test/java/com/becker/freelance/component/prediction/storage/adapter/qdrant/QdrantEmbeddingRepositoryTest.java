
package com.becker.freelance.component.prediction.storage.adapter.qdrant;

import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.DocumentEmbedding;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Collections;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.GenericContainer;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class QdrantEmbeddingRepositoryTest {


    private GenericContainer<?> qdrantContainer;
    private QdrantClient qdrantClient;
    private DocumentMetadataRepository metadataRepository;
    private QdrantCollectionEnsurer collectionEnsurer;
    private QdrantEmbeddingRepository embeddingRepository;

    @BeforeAll
    void startQdrant() {
        qdrantContainer = new GenericContainer<>("qdrant/qdrant:latest")
                .withExposedPorts(6334);
        qdrantContainer.start();

        String host = qdrantContainer.getHost();
        Integer port = qdrantContainer.getMappedPort(6334);

        qdrantClient = new QdrantClient(QdrantGrpcClient.newBuilder(host, port, false)
                .build());

        metadataRepository = mock(DocumentMetadataRepository.class);
        collectionEnsurer = new QdrantCollectionEnsurer(qdrantClient, 3, Collections.Distance.Cosine, false, Collections.Datatype.Float32);

        embeddingRepository = new QdrantEmbeddingRepository(metadataRepository, qdrantClient, collectionEnsurer);
    }

    @AfterAll
    void stopQdrant() {
        if (qdrantContainer != null) {
            qdrantContainer.stop();
        }
    }

    @Test
    void save_shouldInsertEmbedding_whenMetadataExists() throws ExecutionException, InterruptedException {
        UUID metadataId = UUID.randomUUID();
        UUID appId = UUID.randomUUID();
        DocumentMetadata metadata = createMetadata(metadataId, "My Doc", appId);
        when(metadataRepository.findById(metadataId)).thenReturn(Optional.of(metadata));

        float[][] vectors = new float[][]{
                {0.1f, 0.2f, 0.3f},
                {0.4f, 0.5f, 0.6f}
        };
        DocumentEmbedding embedding = new DocumentEmbedding(metadataId, vectors);

        DocumentMetadata savedMetadata = embeddingRepository.save(embedding);

        assertThat(savedMetadata.getId()).isEqualTo(metadataId);

        Optional<DocumentEmbedding> byMetadataId = embeddingRepository.findByMetadataId(metadataId);
        assertThat(byMetadataId).isPresent();
        assertArrayEquals(vectors, byMetadataId.get().embeddedActionDescription(), 0.001f);
    }

    private void assertArrayEquals(float[][] expected, float[][] actual, float delta) {
        for (int i = 0; i < expected.length; i++) {
            Assertions.assertArrayEquals(expected[i], actual[i], delta, "Row " + i + " differs");
        }
    }

    @Test
    void save_shouldThrowException_whenMetadataDoesNotExist() {
        UUID metadataId = UUID.randomUUID();
        when(metadataRepository.findById(metadataId)).thenReturn(Optional.empty());

        DocumentEmbedding embedding = new DocumentEmbedding(metadataId, new float[][]{{0.1f, 0.2f, 0.3f}});

        Assertions.assertThrows(IllegalStateException.class, () -> embeddingRepository.save(embedding));
    }

    @Test
    void save_shouldDeleteExistingVectors_forSameDocument() throws InterruptedException {
        UUID metadataId = UUID.randomUUID();
        UUID appId = UUID.randomUUID();
        DocumentMetadata metadata = createMetadata(metadataId, "My Doc", appId);
        when(metadataRepository.findById(metadataId)).thenReturn(Optional.of(metadata));

        float[][] vectors1 = new float[][]{{0.1f, 0.2f, 0.3f}};
        DocumentEmbedding embedding1 = new DocumentEmbedding(metadataId, vectors1);
        embeddingRepository.save(embedding1);

        float[][] vectors2 = new float[][]{{0.4f, 0.5f, 0.6f}};
        DocumentEmbedding embedding2 = new DocumentEmbedding(metadataId, vectors2);

        embeddingRepository.save(embedding2);

        Thread.sleep(1000);
        // Assert
        Optional<DocumentEmbedding> byMetadataId = embeddingRepository.findByMetadataId(metadataId);

        assertThat(byMetadataId).isPresent();
        assertArrayEquals(vectors2, byMetadataId.get().embeddedActionDescription(), 0.001f);
    }

    @Test
    void save_shouldThrowException_whenVectorDimensionsMismatch() {
        // Arrange
        UUID metadataId = UUID.randomUUID();
        UUID appId = UUID.randomUUID();
        DocumentMetadata metadata = createMetadata(metadataId, "My Doc", appId);
        when(metadataRepository.findById(metadataId)).thenReturn(Optional.of(metadata));

        // First embedding with 3 dimensions
        float[][] vectors1 = new float[][]{{0.1f, 0.2f, 0.3f}};
        embeddingRepository.save(new DocumentEmbedding(metadataId, vectors1));

        // Second embedding with 2 dimensions (mismatch)
        float[][] vectors2 = new float[][]{{0.4f, 0.5f}};
        DocumentEmbedding embedding2 = new DocumentEmbedding(metadataId, vectors2);

        // Act & Assert
        Assertions.assertThrows(IllegalStateException.class, () -> embeddingRepository.save(embedding2));
    }

    DocumentMetadata createMetadata(UUID id, String description, UUID appId) {
        App app = new App(appId, "App");
        DocumentMetadata metadata = mock(DocumentMetadata.class);
        doReturn(id).when(metadata).getId();
        doReturn(app).when(metadata).getApp();
        doReturn(description).when(metadata).getActionDescription();
        return metadata;
    }


}