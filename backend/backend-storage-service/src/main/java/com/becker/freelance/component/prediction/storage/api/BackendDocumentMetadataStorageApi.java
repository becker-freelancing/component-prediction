package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.ApiDocumentMetadataRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentId;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class BackendDocumentMetadataStorageApi extends ApiDocumentMetadataRepositoryGrpc.ApiDocumentMetadataRepositoryImplBase {

    private final DocumentMetadataRepository metadataRepository;
    private final GrpcMapper grpcMapper;

    @Autowired
    public BackendDocumentMetadataStorageApi(DocumentMetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
        this.grpcMapper = new GrpcMapper();
    }
    @Override
    public void save(GrpcDocumentMetadata request, StreamObserver<GrpcDocumentMetadata> responseObserver) {
        DocumentMetadata documentMetadata = grpcMapper.mapIncoming(request);
        DocumentMetadata saved = metadataRepository.save(documentMetadata);
        responseObserver.onNext(grpcMapper.mapOutgoing(saved));
        responseObserver.onCompleted();
    }

    @Override
    public void findByRelatedDocumentId(GrpcDocumentId request, StreamObserver<GrpcDocumentMetadata> responseObserver) {
        Optional<DocumentMetadata> find = metadataRepository.findByRelatedDocumentId(grpcMapper.mapIncomingUUID(request.getDocumentId()));
        find.map(grpcMapper::mapOutgoing).ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }

}
