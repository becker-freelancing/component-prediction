package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.ApiSourceMetadataWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcSourceMetadata;
import com.becker.freelance.component.prediction.storage.domain.SourceMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class SourceMetadataWriteStorageApi extends ApiSourceMetadataWriteRepositoryGrpc.ApiSourceMetadataWriteRepositoryImplBase {

    private final DocumentMetadataRepository metadataRepository;
    private final GrpcMapper grpcMapper;

    @Autowired
    public SourceMetadataWriteStorageApi(DocumentMetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
        this.grpcMapper = new GrpcMapper();
    }

    @Override
    public void save(GrpcSourceMetadata request, StreamObserver<GrpcSourceMetadata> responseObserver) {
        SourceMetadata sourceMetadata = grpcMapper.mapIncoming(request);
        SourceMetadata saved = metadataRepository.save(sourceMetadata);
        responseObserver.onNext(grpcMapper.mapOutgoing(saved));
        responseObserver.onCompleted();
    }
}
