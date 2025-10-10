package com.becker.freelance.component.prediction.backend.query.api;

import com.becker.freelance.component.prediction.backend.query.ApiAppsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryAppList;
import com.becker.freelance.component.prediction.backend.query.domain.model.App;
import com.becker.freelance.component.prediction.backend.query.spi.AppRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class ApiAppsReadRepositoryImpl extends ApiAppsReadRepositoryGrpc.ApiAppsReadRepositoryImplBase {

    private final AppRepository appRepository;
    private final GrpcMapper mapper;

    @Autowired
    public ApiAppsReadRepositoryImpl(AppRepository appRepository, GrpcMapper mapper) {
        this.appRepository = appRepository;
        this.mapper = mapper;
    }

    @Override
    public void findAll(Empty request, StreamObserver<GrpcQueryAppList> responseObserver) {
        List<App> apps = appRepository.findAll();
        responseObserver.onNext(mapper.mapOutgoingApps(apps));
        responseObserver.onCompleted();
    }
}
