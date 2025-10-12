package com.becker.freelance.component.prediction.backend.query.adapter.grpc;

import com.becker.freelance.component.prediction.backend.query.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.backend.query.spi.DocumentMetadataRepository;
import com.becker.freelance.component.prediction.backend.storage.ApiDocumentMetadataReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.google.protobuf.Empty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GrpcDocumentMetadataRepository implements DocumentMetadataRepository {

    private final ApiDocumentMetadataReadRepositoryGrpc.ApiDocumentMetadataReadRepositoryBlockingStub stub;
    private final GrpcAdapterMapper mapper;

    public GrpcDocumentMetadataRepository(ApiDocumentMetadataReadRepositoryGrpc.ApiDocumentMetadataReadRepositoryBlockingStub stub, GrpcAdapterMapper mapper) {
        this.stub = stub;
        this.mapper = mapper;
    }

    @Override
    public List<DocumentMetadata> findAll() {
        return stub.findAll(Empty.getDefaultInstance()).getMetadataList().stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public Optional<DocumentMetadata> findById(UUID id) {
        GrpcDocumentMetadata byId = stub.findById(mapper.map(id));
        return Optional.ofNullable(byId).map(mapper::map);
    }
}
