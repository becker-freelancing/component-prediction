package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiAppsWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.ingest.domain.model.App;
import com.becker.freelance.component.prediction.ingest.spi.AppsRepository;

public class AppsRepositoryImpl implements AppsRepository {

    private final ApiAppsWriteRepositoryGrpc.ApiAppsWriteRepositoryBlockingStub stub;
    private final GrpcAdapterMapper mapper;

    public AppsRepositoryImpl(ApiAppsWriteRepositoryGrpc.ApiAppsWriteRepositoryBlockingStub stub, GrpcAdapterMapper mapper) {
        this.stub = stub;
        this.mapper = mapper;
    }

    @Override
    public App save(App app) {
        GrpcApp save = stub.save(mapper.map(app));
        return mapper.map(save);
    }
}
