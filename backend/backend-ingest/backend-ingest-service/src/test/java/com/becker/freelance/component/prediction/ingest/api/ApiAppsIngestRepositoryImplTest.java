package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestApp;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryApp;
import com.becker.freelance.component.prediction.ingest.domain.model.App;
import com.becker.freelance.component.prediction.ingest.spi.AppsRepository;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.opentest4j.AssertionFailedError;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ApiAppsIngestRepositoryImplTest {

    AppsRepository appsRepository;
    ApiAppsIngestRepositoryImpl apiRepository;

    @BeforeEach
    void setUp() {
        appsRepository = mock(AppsRepository.class);
        apiRepository = new ApiAppsIngestRepositoryImpl(appsRepository, new GrpcMapper());
    }

    @Test
    void save() {

        UUID id = UUID.randomUUID();

        doAnswer((Answer<App>) invocationOnMock -> {
            App argument = invocationOnMock.getArgument(0, App.class);
            argument.setId(id);
            return argument;
        }).when(appsRepository).save(any());

        StreamObserverAssertion assertion = spy(new StreamObserverAssertion(id));

        apiRepository.save(GrpcIngestApp.newBuilder().setAppName("app").build(), assertion);

        verify(assertion, times(1)).onCompleted();
        verify(assertion, times(1)).onNext(any());

    }

    class StreamObserverAssertion implements StreamObserver<GrpcQueryApp> {

        private final UUID id;

        public StreamObserverAssertion(UUID id) {
            this.id = id;
        }

        @Override
        public void onNext(GrpcQueryApp grpcQueryApp) {
            assertEquals(id.toString(), grpcQueryApp.getId().getId());
            assertEquals("app", grpcQueryApp.getAppName());
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