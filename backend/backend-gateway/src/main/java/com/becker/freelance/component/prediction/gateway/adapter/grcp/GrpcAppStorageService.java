package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.ApiAppsIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.ApiAppsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryApp;
import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import com.becker.freelance.component.prediction.gateway.spi.AppStorageService;
import com.google.protobuf.Empty;

import java.util.List;

public class GrpcAppStorageService implements AppStorageService {

    private final ApiAppsIngestRepositoryGrpc.ApiAppsIngestRepositoryBlockingStub writeStub;
    private final ApiAppsReadRepositoryGrpc.ApiAppsReadRepositoryBlockingStub readStub;
    private final GrpcMapper mapper;

    public GrpcAppStorageService(ApiAppsIngestRepositoryGrpc.ApiAppsIngestRepositoryBlockingStub writeStub, ApiAppsReadRepositoryGrpc.ApiAppsReadRepositoryBlockingStub readStub) {
        this.writeStub = writeStub;
        this.readStub = readStub;
        this.mapper = new GrpcMapper();
    }

    @Override
    public List<AppDto> findAll() {
        return readStub.findAll(Empty.getDefaultInstance())
                .getAppsList()
                .stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public AppDto save(AppDto appDto) {
        GrpcQueryApp save = writeStub.save(mapper.map(appDto));
        return mapper.map(save);
    }
}
