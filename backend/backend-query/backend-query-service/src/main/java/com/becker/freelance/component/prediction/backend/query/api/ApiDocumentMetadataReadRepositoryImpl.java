package com.becker.freelance.component.prediction.backend.query.api;

import com.becker.freelance.component.prediction.backend.query.ApiDocumentMetadataReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryDocumentMetadata;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryDocumentMetadataList;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryUUID;
import com.becker.freelance.component.prediction.backend.query.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.backend.query.spi.DocumentMetadataRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@GrpcService
public class ApiDocumentMetadataReadRepositoryImpl extends ApiDocumentMetadataReadRepositoryGrpc.ApiDocumentMetadataReadRepositoryImplBase {

    private final DocumentMetadataRepository metadataRepository;
    private final GrpcMapper grpcMapper;

    @Autowired
    public ApiDocumentMetadataReadRepositoryImpl(DocumentMetadataRepository metadataRepository, GrpcMapper grpcMapper) {
        this.metadataRepository = metadataRepository;
        this.grpcMapper = grpcMapper;
    }

    @Override
    public void findAll(Empty request, StreamObserver<GrpcQueryDocumentMetadataList> responseObserver) {
        List<DocumentMetadata> metadata = metadataRepository.findAll();
        GrpcQueryDocumentMetadataList metadataList = grpcMapper.mapOutgoingMetadata(metadata);
        responseObserver.onNext(metadataList);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(GrpcQueryUUID request, StreamObserver<GrpcQueryDocumentMetadata> responseObserver) {
        UUID id = grpcMapper.mapIncoming(request);
        Optional<DocumentMetadata> find = metadataRepository.findById(id);
        find.map(grpcMapper::mapOutgoing).ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
