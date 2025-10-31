package com.becker.freelance.component.prediction.backend.query.api;

import com.becker.freelance.component.prediction.backend.query.ApiSourceMetadataReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.GrpcQuerySourceMetadata;
import com.becker.freelance.component.prediction.backend.query.GrpcQuerySourceMetadataList;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryUUID;
import com.becker.freelance.component.prediction.backend.query.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.backend.query.spi.SourceMetadataRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@GrpcService
public class ApiSourceMetadataReadRepositoryImpl extends ApiSourceMetadataReadRepositoryGrpc.ApiSourceMetadataReadRepositoryImplBase {

    private final SourceMetadataRepository metadataRepository;
    private final GrpcMapper grpcMapper;

    @Autowired
    public ApiSourceMetadataReadRepositoryImpl(SourceMetadataRepository metadataRepository, GrpcMapper grpcMapper) {
        this.metadataRepository = metadataRepository;
        this.grpcMapper = grpcMapper;
    }

    @Override
    public void findAll(Empty request, StreamObserver<GrpcQuerySourceMetadataList> responseObserver) {
        List<SourceMetadata> metadata = metadataRepository.findAll();
        GrpcQuerySourceMetadataList metadataList = grpcMapper.mapOutgoingMetadata(metadata);
        responseObserver.onNext(metadataList);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(GrpcQueryUUID request, StreamObserver<GrpcQuerySourceMetadata> responseObserver) {
        UUID id = grpcMapper.mapIncoming(request);
        Optional<SourceMetadata> find = metadataRepository.findById(id);
        find.map(grpcMapper::mapOutgoing).ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
