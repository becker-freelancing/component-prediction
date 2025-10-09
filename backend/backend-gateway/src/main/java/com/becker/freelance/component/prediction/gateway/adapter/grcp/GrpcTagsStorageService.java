package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.ApiTagsIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.ApiTagsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryTag;
import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;
import com.becker.freelance.component.prediction.gateway.spi.TagsStorageService;
import com.google.protobuf.Empty;

import java.util.List;

public class GrpcTagsStorageService implements TagsStorageService {


    private final ApiTagsIngestRepositoryGrpc.ApiTagsIngestRepositoryBlockingStub writeStub;
    private final ApiTagsReadRepositoryGrpc.ApiTagsReadRepositoryBlockingStub readStub;
    private final GrpcMapper mapper;

    public GrpcTagsStorageService(ApiTagsIngestRepositoryGrpc.ApiTagsIngestRepositoryBlockingStub writeStub, ApiTagsReadRepositoryGrpc.ApiTagsReadRepositoryBlockingStub readStub) {
        this.writeStub = writeStub;
        this.readStub = readStub;
        this.mapper = new GrpcMapper();
    }

    @Override
    public List<TagDto> findAll() {
        return readStub.findAll(Empty.getDefaultInstance())
                .getTagsList()
                .stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public TagDto save(TagDto tagDto) {
        GrpcQueryTag save = writeStub.save(mapper.map(tagDto));
        return mapper.map(save);
    }
}
