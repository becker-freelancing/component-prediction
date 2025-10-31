package com.becker.freelance.component.prediction.api;

import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbedding;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbeddingRequestChunk;
import com.becker.freelance.component.prediction.backend.embedding.GrpcPreferredChunkSize;
import com.becker.freelance.component.prediction.spi.EmbeddingService;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class EmbeddingServiceApiTest {

    @Mock
    private EmbeddingService embeddingService;

    @Mock
    private StreamObserver<GrpcPreferredChunkSize> chunkSizeObserver;

    @Mock
    private StreamObserver<GrpcEmbedding> embeddingObserver;

    @InjectMocks
    private EmbeddingServiceApi embeddingServiceApi;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    
    
    
    @Test
    void testPreferredChunkSize_ShouldSendResponseAndComplete() {
        
        embeddingServiceApi.preferredChunkSize(com.google.protobuf.Empty.getDefaultInstance(), chunkSizeObserver);

        
        ArgumentCaptor<GrpcPreferredChunkSize> captor = ArgumentCaptor.forClass(GrpcPreferredChunkSize.class);
        verify(chunkSizeObserver).onNext(captor.capture());
        verify(chunkSizeObserver).onCompleted();

        GrpcPreferredChunkSize response = captor.getValue();
        assertNotNull(response);
        assertEquals(1024, response.getSize());
    }

    
    
    
    @Test
    void testEmbed_ShouldReturnNonNullStreamObserver() {
        
        StreamObserver<GrpcEmbeddingRequestChunk> observer =
                embeddingServiceApi.embed(embeddingObserver);

        
        assertNotNull(observer, "embed() should return a valid StreamObserver instance");

        
        
        assertEquals("EmbeddingStreamObserver", observer.getClass().getSimpleName());
    }

}
