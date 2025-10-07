package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.ApiAppsRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.backend.storage.GrpcAppList;
import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.spi.AppRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class BackendAppApi extends ApiAppsRepositoryGrpc.ApiAppsRepositoryImplBase {

    private final AppRepository appRepository;
    private final GrpcMapper mapper;

    @Autowired
    public BackendAppApi(AppRepository appRepository) {
        this.appRepository = appRepository;
        this.mapper = new GrpcMapper();
    }

    @Override
    public void save(GrpcApp request, StreamObserver<GrpcApp> responseObserver) {
        App app = mapper.mapIncoming(request);
        App saved = appRepository.save(app);
        responseObserver.onNext(mapper.mapOutgoing(saved));
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(Empty request, StreamObserver<GrpcAppList> responseObserver) {
        List<App> found = appRepository.findAll();
        List<GrpcApp> out = found.stream().map(mapper::mapOutgoing).toList();
        GrpcAppList grpcAppList = GrpcAppList.newBuilder().addAllApps(out).build();
        responseObserver.onNext(grpcAppList);
        responseObserver.onCompleted();
    }
}
