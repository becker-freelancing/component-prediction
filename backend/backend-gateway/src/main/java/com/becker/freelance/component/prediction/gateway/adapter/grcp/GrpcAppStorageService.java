package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.ApiAppsRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import com.becker.freelance.component.prediction.gateway.spi.AppStorageService;
import com.google.protobuf.Empty;

import java.util.List;

public class GrpcAppStorageService implements AppStorageService {

    private final ApiAppsRepositoryGrpc.ApiAppsRepositoryBlockingStub stub;
    private final GrpcMapper mapper;

    public GrpcAppStorageService(ApiAppsRepositoryGrpc.ApiAppsRepositoryBlockingStub stub) {
        this.stub = stub;
        this.mapper = new GrpcMapper();
    }

    @Override
    public List<AppDto> findAll() {
        return stub.findAll(Empty.getDefaultInstance())
                .getAppsList()
                .stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public AppDto save(AppDto appDto) {
        GrpcApp save = stub.save(mapper.map(appDto));
        return mapper.map(save);
    }
}
