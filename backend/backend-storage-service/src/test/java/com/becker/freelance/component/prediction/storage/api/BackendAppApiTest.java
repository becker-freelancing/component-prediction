package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.backend.storage.GrpcAppList;
import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.spi.AppRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.opentest4j.AssertionFailedError;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BackendAppApiTest {

    private AppRepository repository;
    private BackendAppApi appApi;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(AppRepository.class);
        appApi = new BackendAppApi(repository);
    }

    @Test
    void save() {
        Mockito.when(repository.save(Mockito.any())).then((Answer<App>) invocationOnMock -> {
            App argument = invocationOnMock.getArgument(0, App.class);
            return new App(
                    BigInteger.TWO,
                    argument.getAppName()
            );
        });

        StreamObserverAssertion observer = Mockito.spy(new StreamObserverAssertion(2L, "app"));

        appApi.save(GrpcApp.newBuilder().setAppName("app").build(), observer);

        Mockito.verify(observer, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(observer, Mockito.times(1)).onCompleted();
    }

    @Test
    void findAll() {
        Mockito.when(repository.findAll()).thenReturn(List.of(
                new App(BigInteger.ONE, "app"),
                new App(BigInteger.TWO, "app2")
        ));

        StreamObserver<GrpcAppList> observer = Mockito.mock(StreamObserver.class);

        appApi.findAll(Empty.getDefaultInstance(), observer);

        Mockito.verify(observer, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(observer, Mockito.times(1)).onCompleted();
    }

    private record StreamObserverAssertion(Long id, String appName) implements StreamObserver<GrpcApp> {
        @Override
        public void onNext(GrpcApp grpcApp) {
            assertEquals(id(), grpcApp.getId());
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