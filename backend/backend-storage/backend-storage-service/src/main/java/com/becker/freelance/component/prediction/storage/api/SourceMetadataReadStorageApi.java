package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.*;
import com.becker.freelance.component.prediction.storage.domain.SourceMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

@GrpcService
public class SourceMetadataReadStorageApi extends ApiSourceMetadataReadRepositoryGrpc.ApiSourceMetadataReadRepositoryImplBase {

    private final DocumentMetadataRepository metadataRepository;
    private final GrpcMapper grpcMapper;

    @Autowired
    public SourceMetadataReadStorageApi(DocumentMetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
        this.grpcMapper = new GrpcMapper();
    }


    @Override
    public void findAll(Empty request, StreamObserver<GrpcSourceMetadataList> responseObserver) {
        List<GrpcSourceMetadata> found = metadataRepository.findAll().stream()
                .map(grpcMapper::mapOutgoing)
                .toList();

        GrpcSourceMetadataList build = GrpcSourceMetadataList.newBuilder().addAllMetadata(found).build();

        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }

    @Override
    public void findById(GrpcUUID request, StreamObserver<GrpcSourceMetadata> responseObserver) {
        Optional<SourceMetadata> found = metadataRepository.findById(grpcMapper.mapIncoming(request));
        found.map(grpcMapper::mapOutgoing).ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
