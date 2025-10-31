package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.backend.ingest.*;
import com.becker.freelance.component.prediction.backend.query.GrpcQuerySourceMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.IngestSourceChunk;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.UUID;

@GrpcService
public class ApiSourceIngestRepositoryImpl extends ApiSourceIngestRepositoryGrpc.ApiSourceIngestRepositoryImplBase {

    private final SourceIngestHandler ingestService;
    private final GrpcMapper grpcMapper;

    public ApiSourceIngestRepositoryImpl(SourceIngestHandler ingestService, GrpcMapper grpcMapper) {
        this.ingestService = ingestService;
        this.grpcMapper = grpcMapper;
    }

    @Override
    public void prepareSave(GrpcIngestSourceMetadata request, StreamObserver<GrpcIngestUUID> responseObserver) {
        UUID handleIngestId = ingestService.prepareIngest(grpcMapper.map(request));
        responseObserver.onNext(GrpcIngestUUID.newBuilder().setId(handleIngestId.toString()).build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GrpcIngestSourceChunk> handleSave(StreamObserver<GrpcQuerySourceMetadata> responseObserver) {
        return new IngestSourceChunkStreamObserver(responseObserver, ingestService, grpcMapper);
    }

    static class IngestSourceChunkStreamObserver implements StreamObserver<GrpcIngestSourceChunk> {

        private final StreamObserver<GrpcQuerySourceMetadata> resultObserver;
        private final SourceIngestHandler sourceIngestHandler;
        private final GrpcMapper mapper;
        private IngestSourceChunkConsumer chunkConsumer;

        public IngestSourceChunkStreamObserver(StreamObserver<GrpcQuerySourceMetadata> resultObserver, SourceIngestHandler sourceIngestHandler, GrpcMapper mapper) {
            this.resultObserver = resultObserver;
            this.sourceIngestHandler = sourceIngestHandler;
            this.mapper = mapper;
        }

        @Override
        public void onNext(GrpcIngestSourceChunk grpcIngestSourceChunk) {
            IngestSourceChunk sourceChunk = mapper.map(grpcIngestSourceChunk);
            if (chunkConsumer == null) {
                chunkConsumer = sourceIngestHandler.getForIngestId(sourceChunk.getIngestId());
            }
            chunkConsumer.consume(sourceChunk);
        }

        @Override
        public void onError(Throwable throwable) {
            resultObserver.onError(throwable);
        }

        @Override
        public void onCompleted() {
            SourceMetadata result = chunkConsumer.onCompleted();
            resultObserver.onNext(mapper.map(result));
            resultObserver.onCompleted();
        }
    }
}
