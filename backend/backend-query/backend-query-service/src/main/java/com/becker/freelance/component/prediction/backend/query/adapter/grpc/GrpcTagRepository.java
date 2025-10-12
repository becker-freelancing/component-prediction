package com.becker.freelance.component.prediction.backend.query.adapter.grpc;

import com.becker.freelance.component.prediction.backend.query.domain.model.Tag;
import com.becker.freelance.component.prediction.backend.query.spi.TagRepository;
import com.becker.freelance.component.prediction.backend.storage.ApiTagsReadRepositoryGrpc;
import com.google.protobuf.Empty;

import java.util.List;

public class GrpcTagRepository implements TagRepository {

    private final ApiTagsReadRepositoryGrpc.ApiTagsReadRepositoryBlockingStub stub;
    private final GrpcAdapterMapper mapper;

    public GrpcTagRepository(ApiTagsReadRepositoryGrpc.ApiTagsReadRepositoryBlockingStub stub, GrpcAdapterMapper mapper) {
        this.stub = stub;
        this.mapper = mapper;
    }

    @Override
    public List<Tag> findAll() {
        return stub.findAll(Empty.getDefaultInstance()).getTagsList().stream()
                .map(mapper::map)
                .toList();
    }
}
