package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.*;
import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.DocumentEmbedding;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.EmbeddingRepository;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class EmbeddingWriteStorageApiTest {

    EmbeddingRepository embeddingRepository;
    EmbeddingWriteStorageApi api;

    @BeforeEach
    void setUp() {
        embeddingRepository = mock(EmbeddingRepository.class);
        api = new EmbeddingWriteStorageApi(embeddingRepository, new GrpcMapper());
    }

    @Test
    void save() {
        UUID id = UUID.randomUUID();
        UUID appId = UUID.randomUUID();
        UUID tagId1 = UUID.randomUUID();
        UUID tagId2 = UUID.randomUUID();
        ZonedDateTime created = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC"));


        DocumentMetadata documentMetadata = new DocumentMetadata(
                id,
                new App(appId, "app"),
                "in-app",
                "title",
                "desc",
                "short-desc",
                "de",
                BigInteger.ONE,
                created,
                Set.of(new Tag(tagId1, "t1"), new Tag(tagId2, "t2"))
        );
        DocumentEmbedding embedding = new DocumentEmbedding(id, new float[][]{{1f, 2f}, {2f, 3f}});


        doReturn(documentMetadata).when(embeddingRepository).save(embedding);

        GrpcDocumentEmbedding request = GrpcDocumentEmbedding.newBuilder()
                .setMetadataId(GrpcUUID.newBuilder().setId(id.toString()).build())
                .setActionDescriptionEmbedding(GrpcEmbedding.newBuilder()
                        .addEmbeddings(GrpcFloatArray.newBuilder().addArray(1).addArray(2).build())
                        .addEmbeddings(GrpcFloatArray.newBuilder().addArray(2).addArray(3).build())
                        .build()).build();

        StreamObserverAssertion streamObserverAssertion = new StreamObserverAssertion(id.toString());

        api.save(request, streamObserverAssertion);


    }

    class StreamObserverAssertion implements StreamObserver<GrpcDocumentMetadata> {

        String id;

        public StreamObserverAssertion(String id) {
            this.id = id;
        }

        @Override
        public void onNext(GrpcDocumentMetadata saved) {
            assertEquals(id, saved.getId().getId());
        }

        @Override
        public void onError(Throwable throwable) {
            throw new AssertionFailedError();
        }

        @Override
        public void onCompleted() {

        }
    }

}