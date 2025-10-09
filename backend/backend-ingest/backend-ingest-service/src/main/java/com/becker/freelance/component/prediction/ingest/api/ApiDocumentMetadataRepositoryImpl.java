package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.backend.ingest.ApiDocumentMetadataIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestDocumentMetadata;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryDocumentMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

@GrpcService
public class ApiDocumentMetadataRepositoryImpl extends ApiDocumentMetadataIngestRepositoryGrpc.ApiDocumentMetadataIngestRepositoryImplBase {

    private final DocumentMetadataIngestService ingestService;
    private final GrpcMapper grpcMapper;

    public ApiDocumentMetadataRepositoryImpl(DocumentMetadataIngestService ingestService, GrpcMapper grpcMapper) {
        this.ingestService = ingestService;
        this.grpcMapper = grpcMapper;
    }

    @Override
    public void save(GrpcIngestDocumentMetadata request, StreamObserver<GrpcQueryDocumentMetadata> responseObserver) {
        DocumentMetadata documentMetadata = grpcMapper.map(request);
        DocumentMetadata saved = ingestService.ingest(documentMetadata);
        responseObserver.onNext(grpcMapper.map(saved));
        responseObserver.onCompleted();
    }
}
