package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiTagsWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.ingest.domain.model.Tag;
import com.becker.freelance.component.prediction.ingest.spi.TagsRepository;

public class TagsRepositoryImpl implements TagsRepository {

    private final ApiTagsWriteRepositoryGrpc.ApiTagsWriteRepositoryBlockingStub stub;
    private final GrpcAdapterMapper grpcMapper;

    public TagsRepositoryImpl(ApiTagsWriteRepositoryGrpc.ApiTagsWriteRepositoryBlockingStub stub, GrpcAdapterMapper grpcMapper) {
        this.stub = stub;
        this.grpcMapper = grpcMapper;
    }

    @Override
    public Tag save(Tag tag) {
        GrpcTag save = stub.save(grpcMapper.map(tag));
        return grpcMapper.map(save);
    }
}
