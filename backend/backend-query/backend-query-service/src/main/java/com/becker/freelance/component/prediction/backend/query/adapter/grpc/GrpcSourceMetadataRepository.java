package com.becker.freelance.component.prediction.backend.query.adapter.grpc;

import com.becker.freelance.component.prediction.backend.storage.ApiSourceMetadataReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.backend.query.spi.SourceMetadataRepository;
import com.becker.freelance.component.prediction.backend.storage.GrpcSourceMetadata;
import com.google.protobuf.Empty;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GrpcSourceMetadataRepository implements SourceMetadataRepository {

    private final ApiSourceMetadataReadRepositoryGrpc.ApiSourceMetadataReadRepositoryBlockingStub stub;
    private final GrpcAdapterMapper mapper;

    public GrpcSourceMetadataRepository(ApiSourceMetadataReadRepositoryGrpc.ApiSourceMetadataReadRepositoryBlockingStub stub, GrpcAdapterMapper mapper) {
        this.stub = stub;
        this.mapper = mapper;
    }

    @Override
    public List<SourceMetadata> findAll() {
        return stub.findAll(Empty.getDefaultInstance()).getMetadataList().stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public Optional<SourceMetadata> findById(UUID id) {
        GrpcSourceMetadata byId = stub.findById(mapper.map(id));
        return Optional.ofNullable(byId).map(mapper::map);
    }
}
