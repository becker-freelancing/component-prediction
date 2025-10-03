package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.ApiTagsRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;
import com.becker.freelance.component.prediction.gateway.spi.TagsStorageService;
import com.google.protobuf.Empty;

import java.util.List;

public class GrpcTagsStorageService implements TagsStorageService {


    private final ApiTagsRepositoryGrpc.ApiTagsRepositoryBlockingStub stub;
    private final GrpcMapper mapper;

    public GrpcTagsStorageService(ApiTagsRepositoryGrpc.ApiTagsRepositoryBlockingStub stub) {
        this.stub = stub;
        this.mapper = new GrpcMapper();
    }

    @Override
    public List<TagDto> findAll() {
        return stub.findAll(Empty.getDefaultInstance())
                .getTagsList()
                .stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public TagDto save(TagDto tagDto) {
        GrpcTag save = stub.save(mapper.map(tagDto));
        return mapper.map(save);
    }
}
