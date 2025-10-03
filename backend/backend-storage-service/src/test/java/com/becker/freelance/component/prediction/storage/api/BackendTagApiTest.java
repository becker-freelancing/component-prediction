package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.backend.storage.GrpcTagList;
import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.TagsRepository;
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

class BackendTagApiTest {

    private TagsRepository repository;
    private BackendTagApi tagApi;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TagsRepository.class);
        tagApi = new BackendTagApi(repository);
    }

    @Test
    void save() {
        Mockito.when(repository.save(Mockito.any())).then((Answer<Tag>) invocationOnMock -> {
            Tag argument = invocationOnMock.getArgument(0, Tag.class);
            return new Tag(
                    BigInteger.TWO,
                    argument.getTag()
            );
        });

        StreamObserverAssertion observer = Mockito.spy(new StreamObserverAssertion(2L, "tag"));

        tagApi.save(GrpcTag.newBuilder().setTag("tag").build(), observer);

        Mockito.verify(observer, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(observer, Mockito.times(1)).onCompleted();
    }

    @Test
    void findAll() {
        Mockito.when(repository.findAll()).thenReturn(List.of(
                new Tag(BigInteger.ONE, "tag"),
                new Tag(BigInteger.TWO, "tag2")
        ));

        StreamObserver<GrpcTagList> observer = Mockito.mock(StreamObserver.class);

        tagApi.findAll(Empty.getDefaultInstance(), observer);

        Mockito.verify(observer, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(observer, Mockito.times(1)).onCompleted();
    }

    private record StreamObserverAssertion(Long id, String appName) implements StreamObserver<GrpcTag> {
        @Override
        public void onNext(GrpcTag grpcApp) {
            assertEquals(id(), grpcApp.getId());
            assertEquals(appName(), grpcApp.getTag());
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