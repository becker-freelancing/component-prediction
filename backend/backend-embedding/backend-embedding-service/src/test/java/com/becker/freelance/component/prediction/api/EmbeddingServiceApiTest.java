package com.becker.freelance.component.prediction.api;

import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbedding;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbeddingRequest;
import com.becker.freelance.component.prediction.backend.embedding.GrpcFloatArray;
import com.becker.freelance.component.prediction.spi.EmbeddingException;
import com.becker.freelance.component.prediction.spi.EmbeddingService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EmbeddingServiceApiTest {

    EmbeddingService embeddingService;
    GrpcMapper mapper;
    EmbeddingServiceApi embeddingServiceApi;

    @BeforeEach
    void setUp() {
        embeddingService = Mockito.mock(EmbeddingService.class);
        mapper = new GrpcMapper();
        embeddingServiceApi = new EmbeddingServiceApi(embeddingService, mapper);
    }

    @Test
    void embed() throws EmbeddingException {
        Mockito.doReturn(new float[][]{{1, 2}, {2, 3}}).when(embeddingService).embed("Hello");

        StreamObserverAssertion streamObserverAssertion = Mockito.spy(new StreamObserverAssertion(GrpcEmbedding.newBuilder().addAllEmbeddings(List.of(
                GrpcFloatArray.newBuilder().addAllArray(List.of(1f, 2f)).build(),
                GrpcFloatArray.newBuilder().addAllArray(List.of(2f, 3f)).build()
        )).build()));

        embeddingServiceApi.embed(GrpcEmbeddingRequest.newBuilder().setText("Hello").build(), streamObserverAssertion);

        Mockito.verify(streamObserverAssertion, Mockito.times(0)).onError(Mockito.any());
        Mockito.verify(streamObserverAssertion, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(streamObserverAssertion, Mockito.times(1)).onCompleted();
    }

    @Test
    void embedOnError() throws EmbeddingException {
        Mockito.doThrow(EmbeddingException.class).when(embeddingService).embed("Hello");

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        embeddingServiceApi.embed(GrpcEmbeddingRequest.newBuilder().setText("Hello").build(), streamObserver);

        Mockito.verify(streamObserver, Mockito.times(1)).onError(Mockito.any(EmbeddingException.class));
    }

    private class StreamObserverAssertion implements StreamObserver<GrpcEmbedding> {

        private final GrpcEmbedding expected;

        public StreamObserverAssertion(GrpcEmbedding expected) {
            this.expected = expected;
        }

        @Override
        public void onNext(GrpcEmbedding grpcEmbedding) {
            assertEquals(expected, grpcEmbedding);
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onCompleted() {

        }
    }
}