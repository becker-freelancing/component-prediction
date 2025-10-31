package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiEmbeddingWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcSourceEmbedding;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class EmbeddingRepositoryImplTest {

    private ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryBlockingStub stub;
    private GrpcAdapterMapper mapper;
    private EmbeddingRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        stub = mock(ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryBlockingStub.class);
        mapper = mock(GrpcAdapterMapper.class);
        repository = new EmbeddingRepositoryImpl(stub, mapper);
    }

    @Test
    void save_shouldMapAndCallStubSave() {
        SourceMetadata metadata = mock(SourceMetadata.class);
        float[] embedding = new float[]{0.1f, 0.2f};
        GrpcSourceEmbedding grpcEmbedding = GrpcSourceEmbedding.newBuilder().build();

        when(mapper.map(metadata, embedding)).thenReturn(grpcEmbedding);

        repository.save(metadata, embedding);

        verify(mapper).map(metadata, embedding);
        verify(stub).save(grpcEmbedding);
    }

    @Test
    void save_shouldPropagateException_whenStubThrows() {
        SourceMetadata metadata = mock(SourceMetadata.class);
        float[] embedding = new float[]{0.1f, 0.2f};
        GrpcSourceEmbedding grpcEmbedding = GrpcSourceEmbedding.newBuilder().build();

        when(mapper.map(metadata, embedding)).thenReturn(grpcEmbedding);
        doThrow(new RuntimeException("gRPC error")).when(stub).save(any(GrpcSourceEmbedding.class));

        try {
            repository.save(metadata, embedding);
        } catch (RuntimeException e) {
            verify(mapper).map(metadata, embedding);
            verify(stub).save(grpcEmbedding);
        }
    }
}
