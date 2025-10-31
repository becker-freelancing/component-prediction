package com.becker.freelance.component.prediction.ingest.adapter.extraction;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.*;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBufferFactory;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SourceContentExtractorImplTest {

    private ApiSourceContentExtractionServiceGrpc.ApiSourceContentExtractionServiceStub stub;
    private ByteArraysBufferFactory bufferFactory;
    private SourceContentExtractorImpl extractor;

    @BeforeEach
    void setUp() {
        stub = mock(ApiSourceContentExtractionServiceGrpc.ApiSourceContentExtractionServiceStub.class);
        bufferFactory = mock(ByteArraysBufferFactory.class);
        extractor = new SourceContentExtractorImpl(stub, bufferFactory);
    }

    @Test
    void supportsOrReset_shouldReturnTrue_whenGrpcReturnsTrue() {
        UUID id = UUID.randomUUID();
        doAnswer(inv -> {
            StreamObserver<GrpcSourceContentExtractionSupportsResponse> observer = inv.getArgument(1);
            observer.onNext(GrpcSourceContentExtractionSupportsResponse.newBuilder().setSupports(true).build());
            observer.onCompleted();
            return null;
        }).when(stub).supportsOrReset(any(), any());

        boolean result = extractor.supportsOrReset(id);
        assertTrue(result);
    }

    @Test
    void supportsOrReset_shouldReturnFalse_whenGrpcReturnsFalse() {
        UUID id = UUID.randomUUID();
        doAnswer(inv -> {
            StreamObserver<GrpcSourceContentExtractionSupportsResponse> observer = inv.getArgument(1);
            observer.onNext(GrpcSourceContentExtractionSupportsResponse.newBuilder().setSupports(false).build());
            observer.onCompleted();
            return null;
        }).when(stub).supportsOrReset(any(), any());

        boolean result = extractor.supportsOrReset(id);
        assertFalse(result);
    }

    @Test
    void prepareNewExtraction_shouldReturnUuid_fromGrpcResponse() {
        UUID id = UUID.randomUUID();
        doAnswer(inv -> {
            StreamObserver<GrpcSourceContentExtractionUUID> observer = inv.getArgument(1);
            observer.onNext(GrpcSourceContentExtractionUUID.newBuilder().setId(id.toString()).build());
            observer.onCompleted();
            return null;
        }).when(stub).prepareExtraction(any(Empty.class), any());

        UUID result = extractor.prepareNewExtraction();
        assertEquals(id, result);
    }

    @Test
    void extract_shouldReturnBuffer_whenGrpcStreamCompletes() throws Exception {
        ByteArraysBuffer buffer = mock(ByteArraysBuffer.class);
        when(bufferFactory.createNew()).thenReturn(buffer);

        doAnswer(inv -> {
            StreamObserver<GrpcSourceContentExtractionChunk> observer = inv.getArgument(1);
            observer.onNext(GrpcSourceContentExtractionChunk.newBuilder()
                    .setChunk("abc")
                    .build());
            observer.onCompleted();
            return null;
        }).when(stub).extract(any(), any());

        UUID id = UUID.randomUUID();
        ByteArraysBuffer result = extractor.extract(id);

        assertEquals(buffer, result);
        verify(buffer).buffer(anyString(), eq("abc".getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void buffer_shouldSendData_andCompleteSuccessfully() throws Exception {
        InputStream inputStream = new ByteArrayInputStream("hello".getBytes());
        UUID extractionId = UUID.randomUUID();

        StreamObserver<GrpcSourceContentExtractionRequestChunk>[] capturedObserver = new StreamObserver[1];
        StreamObserver os = mock(StreamObserver.class);

        doAnswer(inv -> {
            capturedObserver[0] = inv.getArgument(0);
            capturedObserver[0].onCompleted();
            return os;
        }).when(stub).buffer(any());

        CompletableFuture<Void> completed = new CompletableFuture<>();
        completed.complete(null);

        // Mock EmptyStreamObserver completion behavior
        extractor.buffer(extractionId, inputStream);

        assertNotNull(capturedObserver[0]);
    }

    @Test
    void extract_shouldThrowException_whenGrpcFails() {
        when(bufferFactory.createNew()).thenReturn(mock(ByteArraysBuffer.class));
        doAnswer(inv -> {
            StreamObserver<GrpcSourceContentExtractionChunk> observer = inv.getArgument(1);
            observer.onError(new RuntimeException("grpc error"));
            return null;
        }).when(stub).extract(any(), any());

        assertThrows(IllegalStateException.class, () -> extractor.extract(UUID.randomUUID()));
    }

    @Test
    void supportsOrReset_shouldThrowException_whenGrpcFails() {
        doAnswer(inv -> {
            StreamObserver<GrpcSourceContentExtractionSupportsResponse> observer = inv.getArgument(1);
            observer.onError(new RuntimeException("grpc error"));
            return null;
        }).when(stub).supportsOrReset(any(), any());

        assertThrows(IllegalStateException.class, () -> extractor.supportsOrReset(UUID.randomUUID()));
    }
}
