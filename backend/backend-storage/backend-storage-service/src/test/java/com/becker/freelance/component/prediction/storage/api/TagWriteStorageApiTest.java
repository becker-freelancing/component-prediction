package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.TagsRepository;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.opentest4j.AssertionFailedError;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TagWriteStorageApiTest {

    private TagsRepository repository;
    private TagWriteStorageApi tagApi;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TagsRepository.class);
        tagApi = new TagWriteStorageApi(repository);
    }

    @Test
    void save() {
        UUID tagId = UUID.randomUUID();
        Mockito.when(repository.save(Mockito.any())).then((Answer<Tag>) invocationOnMock -> {
            Tag argument = invocationOnMock.getArgument(0, Tag.class);
            return new Tag(
                    tagId,
                    argument.getTag()
            );
        });

        StreamObserverAssertion observer = Mockito.spy(new StreamObserverAssertion(tagId, "tag"));

        tagApi.save(GrpcTag.newBuilder().setTag("tag").build(), observer);

        Mockito.verify(observer, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(observer, Mockito.times(1)).onCompleted();
    }


    private record StreamObserverAssertion(UUID id, String appName) implements StreamObserver<GrpcTag> {
        @Override
        public void onNext(GrpcTag grpcApp) {
            assertEquals(GrpcUUID.newBuilder().setId(id().toString()).build(), grpcApp.getId());
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