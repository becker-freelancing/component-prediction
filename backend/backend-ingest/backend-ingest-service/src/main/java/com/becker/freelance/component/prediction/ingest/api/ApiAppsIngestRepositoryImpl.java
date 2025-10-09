package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.backend.ingest.ApiAppsIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestApp;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryApp;
import com.becker.freelance.component.prediction.ingest.domain.model.App;
import com.becker.freelance.component.prediction.ingest.spi.AppsRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class ApiAppsIngestRepositoryImpl extends ApiAppsIngestRepositoryGrpc.ApiAppsIngestRepositoryImplBase {

    private final AppsRepository appsRepository;
    private final GrpcMapper mapper;

    @Autowired
    public ApiAppsIngestRepositoryImpl(AppsRepository appsRepository, GrpcMapper mapper) {
        this.appsRepository = appsRepository;
        this.mapper = mapper;
    }

    @Override
    public void save(GrpcIngestApp request, StreamObserver<GrpcQueryApp> responseObserver) {
        App app = mapper.map(request);
        App saved = appsRepository.save(app);
        responseObserver.onNext(mapper.map(saved));
        responseObserver.onCompleted();
    }
}
