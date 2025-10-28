package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.ApiAppsWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.spi.AppRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class AppWriteStorageApi extends ApiAppsWriteRepositoryGrpc.ApiAppsWriteRepositoryImplBase {

    private final AppRepository appRepository;
    private final GrpcMapper mapper;

    @Autowired
    public AppWriteStorageApi(AppRepository appRepository) {
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

}
