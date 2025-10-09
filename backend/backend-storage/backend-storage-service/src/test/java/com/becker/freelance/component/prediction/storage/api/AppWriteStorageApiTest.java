package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.spi.AppRepository;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.opentest4j.AssertionFailedError;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppWriteStorageApiTest {

    private AppRepository repository;
    private AppWriteStorageApi appApi;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(AppRepository.class);
        appApi = new AppWriteStorageApi(repository);
    }

    @Test
    void save() {
        UUID appId = UUID.randomUUID();
        Mockito.when(repository.save(Mockito.any())).then((Answer<App>) invocationOnMock -> {
            App argument = invocationOnMock.getArgument(0, App.class);
            return new App(
                    appId,
                    argument.getAppName()
            );
        });

        StreamObserverAssertion observer = Mockito.spy(new StreamObserverAssertion(appId, "app"));

        appApi.save(GrpcApp.newBuilder().setAppName("app").build(), observer);

        Mockito.verify(observer, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(observer, Mockito.times(1)).onCompleted();
    }


    private record StreamObserverAssertion(UUID id, String appName) implements StreamObserver<GrpcApp> {
        @Override
        public void onNext(GrpcApp grpcApp) {
            assertEquals(GrpcUUID.newBuilder().setId(id.toString()).build(), grpcApp.getId());
            assertEquals(appName(), grpcApp.getAppName());
        }

        @Override
        public void onError(Throwable throwable) {
            throw new AssertionFailedError();
        }

        @Override
        public void onCompleted() {

        }
    }


}