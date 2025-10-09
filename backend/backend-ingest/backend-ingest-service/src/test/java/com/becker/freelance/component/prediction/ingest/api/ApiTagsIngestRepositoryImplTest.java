package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestTag;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryTag;
import com.becker.freelance.component.prediction.ingest.domain.model.Tag;
import com.becker.freelance.component.prediction.ingest.spi.TagsRepository;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.opentest4j.AssertionFailedError;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ApiTagsIngestRepositoryImplTest {

    TagsRepository tagsRepository;
    ApiTagsIngestRepositoryImpl apiRepository;

    @BeforeEach
    void setUp() {
        tagsRepository = mock(TagsRepository.class);
        apiRepository = new ApiTagsIngestRepositoryImpl(tagsRepository, new GrpcMapper());
    }

    @Test
    void save() {

        UUID id = UUID.randomUUID();

        doAnswer((Answer<Tag>) invocationOnMock -> {
            Tag argument = invocationOnMock.getArgument(0, Tag.class);
            argument.setId(id);
            return argument;
        }).when(tagsRepository).save(any());

        StreamObserverAssertion assertion = spy(new StreamObserverAssertion(id));

        apiRepository.save(GrpcIngestTag.newBuilder().setTag("tag").build(), assertion);

        verify(assertion, times(1)).onCompleted();
        verify(assertion, times(1)).onNext(any());

    }

    class StreamObserverAssertion implements StreamObserver<GrpcQueryTag> {

        private final UUID id;

        public StreamObserverAssertion(UUID id) {
            this.id = id;
        }

        @Override
        public void onNext(GrpcQueryTag grpcQueryApp) {
            assertEquals(id.toString(), grpcQueryApp.getId().getId());
            assertEquals("tag", grpcQueryApp.getTag());
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