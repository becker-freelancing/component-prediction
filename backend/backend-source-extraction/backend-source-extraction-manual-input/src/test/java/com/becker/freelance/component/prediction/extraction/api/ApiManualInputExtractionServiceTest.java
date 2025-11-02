package com.becker.freelance.component.prediction.extraction.api;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.*;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBufferFactory;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class ApiManualInputExtractionServiceTest {

    private ManualInputExtractionService manualInputExtractionService;
    private ByteArraysBuffer buffer;
    private ApiManualInputExtractionService service;
    private MockedStatic<ByteArraysBufferFactory> factoryMockedStatic;

    @BeforeEach
    void setUp() {
        manualInputExtractionService = mock(ManualInputExtractionService.class);
        buffer = mock(ByteArraysBuffer.class);
        ByteArraysBufferFactory factoryMock = mock(ByteArraysBufferFactory.class);
        when(factoryMock.createNew()).thenReturn(buffer);
        factoryMockedStatic = mockStatic(ByteArraysBufferFactory.class);
        factoryMockedStatic.when(ByteArraysBufferFactory::getInstance).thenReturn(factoryMock);

        service = new ApiManualInputExtractionService(manualInputExtractionService);
    }

    @AfterEach
    void tearDown(){
        factoryMockedStatic.close();
    }

    @Test
    void testPrepareExtraction_sendsUUID() {
        UUID id = UUID.randomUUID();
        when(manualInputExtractionService.prepareNewExtraction()).thenReturn(id);
        @SuppressWarnings("unchecked")
        StreamObserver<GrpcSourceContentExtractionUUID> observer = mock(StreamObserver.class);

        service.prepareExtraction(Empty.getDefaultInstance(), observer);

        verify(observer).onNext(argThat(resp -> resp.getId().equals(id.toString())));
        verify(observer).onCompleted();
    }

    @Test
    void testBuffer_returnsBufferingStreamObserver() {
        @SuppressWarnings("unchecked")
        StreamObserver<Empty> responseObserver = mock(StreamObserver.class);

        StreamObserver<GrpcSourceContentExtractionRequestChunk> result = service.buffer(responseObserver);

        assert result instanceof BufferingStreamObserver;
    }

    @Test
    void testSupportsOrReset_supportsTrue() {
        UUID id = UUID.randomUUID();
        when(manualInputExtractionService.supports(eq(id), any())).thenReturn(true);
        @SuppressWarnings("unchecked")
        StreamObserver<GrpcSourceContentExtractionSupportsResponse> observer = mock(StreamObserver.class);

        service.supportsOrReset(
                GrpcSourceContentExtractionUUID.newBuilder().setId(id.toString()).build(),
                observer
        );

        verify(buffer, never()).clearBuffer(anyString());
        verify(observer).onNext(argThat(r -> r.getSupports()));
        verify(observer).onCompleted();
    }

    @Test
    void testSupportsOrReset_supportsFalse_clearsBuffer() {
        UUID id = UUID.randomUUID();
        when(manualInputExtractionService.supports(eq(id), any())).thenReturn(false);
        @SuppressWarnings("unchecked")
        StreamObserver<GrpcSourceContentExtractionSupportsResponse> observer = mock(StreamObserver.class);

        service.supportsOrReset(
                GrpcSourceContentExtractionUUID.newBuilder().setId(id.toString()).build(),
                observer
        );

        verify(buffer).clearBuffer(id.toString());
        verify(observer).onNext(argThat(r -> !r.getSupports()));
        verify(observer).onCompleted();
    }

    @Test
    void testExtract_invokesManualService() {
        UUID id = UUID.randomUUID();
        @SuppressWarnings("unchecked")
        StreamObserver<GrpcSourceContentExtractionChunk> observer = mock(StreamObserver.class);

        service.extract(
                GrpcSourceContentExtractionUUID.newBuilder().setId(id.toString()).build(),
                observer
        );

        verify(manualInputExtractionService).extract(eq(id), any(), any(ExtractionConsumer.class));
    }

    @Test
    void testClearBuffer_clearsAndCompletes() {
        UUID id = UUID.randomUUID();
        @SuppressWarnings("unchecked")
        StreamObserver<Empty> observer = mock(StreamObserver.class);

        service.clearBuffer(
                GrpcSourceContentExtractionUUID.newBuilder().setId(id.toString()).build(),
                observer
        );

        verify(buffer).clearBuffer(id.toString());
        verify(observer).onCompleted();
    }

    @Test
    void testExtractionConsumerImpl_flow() {
        UUID id = UUID.randomUUID();
        @SuppressWarnings("unchecked")
        StreamObserver<GrpcSourceContentExtractionChunk> observer = mock(StreamObserver.class);
        GrpcSourceContentExtractionUUID grpcId = GrpcSourceContentExtractionUUID.newBuilder().setId(id.toString()).build();

        ApiManualInputExtractionService.ExtractionConsumerImpl consumer =
                new ApiManualInputExtractionService.ExtractionConsumerImpl(grpcId, observer);

        consumer.accept("test");
        consumer.onCompleted();
        consumer.onError(new RuntimeException("error"));

        verify(observer).onNext(argThat(chunk -> chunk.getId().equals(grpcId) && chunk.getChunk().equals("test")));
        verify(observer).onCompleted();
        verify(observer).onError(any(RuntimeException.class));
    }
}
