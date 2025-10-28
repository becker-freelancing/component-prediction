package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.ApiDocumentMetadataReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadataList;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class DocumentMetadataReadStorageApi extends ApiDocumentMetadataReadRepositoryGrpc.ApiDocumentMetadataReadRepositoryImplBase {

    private final DocumentMetadataRepository metadataRepository;
    private final GrpcMapper grpcMapper;

    @Autowired
    public DocumentMetadataReadStorageApi(DocumentMetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
        this.grpcMapper = new GrpcMapper();
    }


    @Override
    public void findAll(Empty request, StreamObserver<GrpcDocumentMetadataList> responseObserver) {
        List<GrpcDocumentMetadata> found = metadataRepository.findAll().stream()
                .map(grpcMapper::mapOutgoing)
                .toList();

        GrpcDocumentMetadataList build = GrpcDocumentMetadataList.newBuilder().addAllMetadata(found).build();

        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(GrpcUUID request, StreamObserver<GrpcDocumentMetadata> responseObserver) {
        Optional<DocumentMetadata> found = metadataRepository.findById(grpcMapper.mapIncoming(request));
        found.map(grpcMapper::mapOutgoing).ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
