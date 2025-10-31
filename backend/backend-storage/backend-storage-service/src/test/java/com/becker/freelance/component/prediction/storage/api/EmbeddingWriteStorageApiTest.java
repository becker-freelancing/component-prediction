package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.*;
import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.SourceEmbedding;
import com.becker.freelance.component.prediction.storage.domain.SourceMetadata;
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


        SourceMetadata sourceMetadata = new SourceMetadata(
                id,
                new App(appId, "app"),
                BigInteger.ONE,
                created,
                created,
                Set.of(new Tag(tagId1, "t1"), new Tag(tagId2, "t2"))
        );
        SourceEmbedding embedding = new SourceEmbedding(id, new float[][]{{1f, 2f}});


        doReturn(sourceMetadata).when(embeddingRepository).save(embedding);

        GrpcSourceEmbedding request = GrpcSourceEmbedding.newBuilder()
                .setMetadataId(GrpcUUID.newBuilder().setId(id.toString()).build())
                .setEmbeddings(GrpcFloatArray.newBuilder().addArray(1).addArray(2).build())
                .build();

        StreamObserverAssertion streamObserverAssertion = new StreamObserverAssertion(id.toString());

        api.save(request, streamObserverAssertion);


    }

    class StreamObserverAssertion implements StreamObserver<GrpcSourceMetadata> {

        String id;

        public StreamObserverAssertion(String id) {
            this.id = id;
        }

        @Override
        public void onNext(GrpcSourceMetadata saved) {
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