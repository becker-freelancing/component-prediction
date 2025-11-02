package com.becker.freelance.component.prediction.extraction.api;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.GrpcSourceContentExtractionRequestChunk;
import com.becker.freelance.component.prediction.backend.SourceContentExtraction.GrpcSourceContentExtractionUUID;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.google.protobuf.ByteString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;

public class BufferingStreamObserverTest {

    private ByteArraysBuffer buffer;
    private BufferingStreamObserver observer;

    @BeforeEach
    void setUp() {
        buffer = mock(ByteArraysBuffer.class);
        observer = new BufferingStreamObserver(buffer);
    }

    @Test
    void testOnNext_buffersData() {
        UUID id = UUID.randomUUID();
        byte[] bytes = "testdata".getBytes();

        GrpcSourceContentExtractionUUID grpcId =
                GrpcSourceContentExtractionUUID.newBuilder().setId(id.toString()).build();

        GrpcSourceContentExtractionRequestChunk chunk =
                GrpcSourceContentExtractionRequestChunk.newBuilder()
                        .setId(grpcId)
                        .setData(ByteString.copyFrom(bytes))
                        .build();

        observer.onNext(chunk);

        verify(buffer, times(1)).buffer(id.toString(), bytes);
    }

    @Test
    void testOnError_doesNothing() {
        observer.onError(new RuntimeException("test"));
        verifyNoInteractions(buffer);
    }

    @Test
    void testOnCompleted_doesNothing() {
        observer.onCompleted();
        verifyNoInteractions(buffer);
    }
}
