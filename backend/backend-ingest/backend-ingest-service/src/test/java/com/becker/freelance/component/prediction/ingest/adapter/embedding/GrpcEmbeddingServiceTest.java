package com.becker.freelance.component.prediction.ingest.adapter.embedding;

import com.becker.freelance.component.prediction.backend.embedding.*;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GrpcEmbeddingServiceTest {

    private ApiEmbeddingServiceGrpc.ApiEmbeddingServiceStub asyncStub;
    private ApiEmbeddingServiceGrpc.ApiEmbeddingServiceBlockingStub blockingStub;
    private ByteArraysBuffer buffer;
    private GrpcEmbeddingService service;

    @BeforeEach
    void setUp() {
        asyncStub = mock(ApiEmbeddingServiceGrpc.ApiEmbeddingServiceStub.class);
        blockingStub = mock(ApiEmbeddingServiceGrpc.ApiEmbeddingServiceBlockingStub.class);
        buffer = mock(ByteArraysBuffer.class);

        service = new GrpcEmbeddingService(asyncStub, blockingStub);
    }

    @Test
    void embed_shouldSendChunksAndInvokeConsumer() throws Exception {
        // Arrange
        int preferredSize = 4;
        when(blockingStub.preferredChunkSize(any(Empty.class)))
                .thenReturn(GrpcPreferredChunkSize.newBuilder().setSize(preferredSize).build());

        byte[] testData = "abcdefgh".getBytes();
        InputStream is = new ByteArrayInputStream(testData);
        when(buffer.newInputStream("doc")).thenReturn(is);

        // Capture StreamObserver returned from stub.embed()
        @SuppressWarnings("unchecked")
        ArgumentCaptor<StreamObserver<GrpcEmbedding>> serverObserverCaptor = ArgumentCaptor.forClass(StreamObserver.class);

        StreamObserver<GrpcEmbeddingRequestChunk> clientObserver = mock(StreamObserver.class);
        when(asyncStub.embed(serverObserverCaptor.capture())).thenReturn(clientObserver);

        AtomicReference<float[]> result = new AtomicReference<>();
        Consumer<float[]> consumer = result::set;

        // Act
        service.embed(buffer, "doc", consumer);

        // Assert: should have sent 2 chunks (8 bytes / 4 size)
        verify(clientObserver, atLeast(2)).onNext(any(GrpcEmbeddingRequestChunk.class));
        verify(clientObserver).onCompleted();

        // Simulate server response
        StreamObserver<GrpcEmbedding> serverObserver = serverObserverCaptor.getValue();
        GrpcFloatArray floatArray = GrpcFloatArray.newBuilder()
                .addAllArray(List.of(1.0f, 2.0f, 3.0f))
                .build();
        serverObserver.onNext(GrpcEmbedding.newBuilder().setEmbeddings(floatArray).build());

        assertNotNull(result.get());
        assertArrayEquals(new float[]{1f, 2f, 3f}, result.get(), 0.0001f);
    }


    @Test
    void embeddingStreamObserver_shouldMapAndDeliverFloats() {
        AtomicReference<float[]> result = new AtomicReference<>();
        Consumer<float[]> consumer = result::set;

        GrpcEmbeddingService.EmbeddingStreamObserver observer =
                new GrpcEmbeddingService.EmbeddingStreamObserver(consumer);

        GrpcFloatArray array = GrpcFloatArray.newBuilder()
                .addAllArray(List.of(5.0f, 6.0f))
                .build();
        observer.onNext(GrpcEmbedding.newBuilder().setEmbeddings(array).build());

        assertArrayEquals(new float[]{5f, 6f}, result.get(), 0.0001f);
    }

    @Test
    void embeddingStreamObserver_onError_shouldThrowIllegalStateException() {
        GrpcEmbeddingService.EmbeddingStreamObserver observer =
                new GrpcEmbeddingService.EmbeddingStreamObserver(f -> {});

        assertThrows(IllegalStateException.class, () ->
                observer.onError(new RuntimeException("boom")));
    }
}
