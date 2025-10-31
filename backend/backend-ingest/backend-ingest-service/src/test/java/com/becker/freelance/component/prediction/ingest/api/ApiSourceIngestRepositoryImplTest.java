package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestSourceChunk;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestSourceMetadata;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestUUID;
import com.becker.freelance.component.prediction.backend.query.GrpcQuerySourceMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.IngestSourceChunk;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertSame;

class ApiSourceIngestRepositoryImplTest {

    private SourceIngestHandler ingestService;
    private GrpcMapper grpcMapper;
    private ApiSourceIngestRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        ingestService = mock(SourceIngestHandler.class);
        grpcMapper = mock(GrpcMapper.class);
        repository = new ApiSourceIngestRepositoryImpl(ingestService, grpcMapper);
    }

    @Test
    void prepareSave_shouldMapRequestAndReturnUUID() {
        GrpcIngestSourceMetadata request = mock(GrpcIngestSourceMetadata.class);
        SourceMetadata metadata = mock(SourceMetadata.class);
        UUID uuid = UUID.randomUUID();

        when(grpcMapper.map(request)).thenReturn(metadata);
        when(ingestService.prepareIngest(metadata)).thenReturn(uuid);

        StreamObserver<GrpcIngestUUID> observer = mock(StreamObserver.class);

        repository.prepareSave(request, observer);

        verify(grpcMapper).map(request);
        verify(ingestService).prepareIngest(metadata);
        verify(observer).onNext(GrpcIngestUUID.newBuilder().setId(uuid.toString()).build());
        verify(observer).onCompleted();
    }

    @Test
    void handleSave_shouldConsumeChunksAndReturnMetadata() {
        StreamObserver<GrpcQuerySourceMetadata> responseObserver = mock(StreamObserver.class);
        ApiSourceIngestRepositoryImpl.IngestSourceChunkStreamObserver streamObserver =
                new ApiSourceIngestRepositoryImpl.IngestSourceChunkStreamObserver(responseObserver, ingestService, grpcMapper);

        IngestSourceChunk chunk = mock(IngestSourceChunk.class);
        SourceMetadata metadata = mock(SourceMetadata.class);
        IngestSourceChunkConsumer consumer = mock(IngestSourceChunkConsumer.class);

        when(grpcMapper.map(any(GrpcIngestSourceChunk.class))).thenReturn(chunk);
        when(ingestService.getForIngestId(any())).thenReturn(consumer);
        when(consumer.onCompleted()).thenReturn(metadata);

        streamObserver.onNext(mock(com.becker.freelance.component.prediction.backend.ingest.GrpcIngestSourceChunk.class));
        streamObserver.onCompleted();

        verify(consumer).consume(chunk);
        verify(consumer).onCompleted();
        verify(grpcMapper).map(metadata);
        verify(responseObserver).onNext(grpcMapper.map(metadata));
        verify(responseObserver).onCompleted();
    }

    @Test
    void handleSave_onError_shouldPropagate() {
        StreamObserver<GrpcQuerySourceMetadata> responseObserver = mock(StreamObserver.class);
        ApiSourceIngestRepositoryImpl.IngestSourceChunkStreamObserver streamObserver =
                new ApiSourceIngestRepositoryImpl.IngestSourceChunkStreamObserver(responseObserver, ingestService, grpcMapper);

        Throwable throwable = new RuntimeException("error");
        streamObserver.onError(throwable);

        verify(responseObserver).onError(throwable);
    }
}
