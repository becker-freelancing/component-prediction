package com.becker.freelance.component.prediction.backend.query.api;

import com.becker.freelance.component.prediction.backend.query.GrpcQueryAppList;
import com.becker.freelance.component.prediction.backend.query.domain.model.App;
import com.becker.freelance.component.prediction.backend.query.spi.AppRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

class AppReadStorageApiTest {

    private AppRepository repository;
    private ApiAppsReadRepositoryImpl appApi;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(AppRepository.class);
        appApi = new ApiAppsReadRepositoryImpl(repository, new GrpcMapper());
    }


    @Test
    void findAll() {
        UUID appId = UUID.randomUUID();
        UUID appId2 = UUID.randomUUID();
        Mockito.when(repository.findAll()).thenReturn(List.of(
                new App(appId, "app"),
                new App(appId2, "app2")
        ));

        StreamObserver<GrpcQueryAppList> observer = Mockito.mock(StreamObserver.class);

        appApi.findAll(Empty.getDefaultInstance(), observer);

        Mockito.verify(observer, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(observer, Mockito.times(1)).onCompleted();
    }


}