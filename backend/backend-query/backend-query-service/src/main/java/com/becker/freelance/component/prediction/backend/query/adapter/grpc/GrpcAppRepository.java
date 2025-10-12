package com.becker.freelance.component.prediction.backend.query.adapter.grpc;

import com.becker.freelance.component.prediction.backend.query.domain.model.App;
import com.becker.freelance.component.prediction.backend.query.spi.AppRepository;
import com.becker.freelance.component.prediction.backend.storage.ApiAppsReadRepositoryGrpc;
import com.google.protobuf.Empty;

import java.util.List;

public class GrpcAppRepository implements AppRepository {

    private final ApiAppsReadRepositoryGrpc.ApiAppsReadRepositoryBlockingStub stub;
    private final GrpcAdapterMapper mapper;

    public GrpcAppRepository(ApiAppsReadRepositoryGrpc.ApiAppsReadRepositoryBlockingStub stub, GrpcAdapterMapper mapper) {
        this.stub = stub;
        this.mapper = mapper;
    }

    @Override
    public List<App> findAll() {
        return stub.findAll(Empty.getDefaultInstance()).getAppsList().stream()
                .map(mapper::map)
                .toList();
    }
}
