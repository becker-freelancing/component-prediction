package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiSourceMetadataWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcSourceMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.Mockito.*;

class MetadataRepositoryImplTest {

    private ApiSourceMetadataWriteRepositoryGrpc.ApiSourceMetadataWriteRepositoryBlockingStub stub;
    private GrpcAdapterMapper mapper;
    private MetadataRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        stub = mock(ApiSourceMetadataWriteRepositoryGrpc.ApiSourceMetadataWriteRepositoryBlockingStub.class);
        mapper = mock(GrpcAdapterMapper.class);
        repository = new MetadataRepositoryImpl(stub, mapper);
    }

    @Test
    void save_shouldMapGrpcAndCallStub() {
        SourceMetadata input = mock(SourceMetadata.class);
        GrpcSourceMetadata grpcInput = GrpcSourceMetadata.newBuilder().build();
        GrpcSourceMetadata grpcSaved = GrpcSourceMetadata.newBuilder().build();
        SourceMetadata output = mock(SourceMetadata.class);

        when(mapper.map(input)).thenReturn(grpcInput);
        when(stub.save(grpcInput)).thenReturn(grpcSaved);
        when(mapper.map(grpcSaved)).thenReturn(output);

        SourceMetadata result = repository.save(input);

        assertSame(output, result);
        verify(mapper).map(input);
        verify(stub).save(grpcInput);
        verify(mapper).map(grpcSaved);
    }

    @Test
    void save_shouldPropagateException_whenStubThrows() {
        SourceMetadata input = mock(SourceMetadata.class);
        GrpcSourceMetadata grpcInput = GrpcSourceMetadata.newBuilder().build();

        when(mapper.map(input)).thenReturn(grpcInput);
        when(stub.save(grpcInput)).thenThrow(new RuntimeException("gRPC error"));

        try {
            repository.save(input);
        } catch (RuntimeException e) {
            verify(mapper).map(input);
            verify(stub).save(grpcInput);
        }
    }
}
