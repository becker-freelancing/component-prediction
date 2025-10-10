package com.becker.freelance.component.prediction.backend.query.api;

import com.becker.freelance.component.prediction.backend.query.GrpcQueryTagList;
import com.becker.freelance.component.prediction.backend.query.domain.model.Tag;
import com.becker.freelance.component.prediction.backend.query.spi.TagRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

class TagReadStorageApiTest {

    private TagRepository repository;
    private ApiTagsReadRepositoryImpl tagApi;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TagRepository.class);
        tagApi = new ApiTagsReadRepositoryImpl(repository, new GrpcMapper());
    }

    @Test
    void findAll() {
        UUID tagId = UUID.randomUUID();
        UUID tagId2 = UUID.randomUUID();
        Mockito.when(repository.findAll()).thenReturn(List.of(
                new Tag(tagId, "tag"),
                new Tag(tagId2, "tag2")
        ));

        StreamObserver<GrpcQueryTagList> observer = Mockito.mock(StreamObserver.class);

        tagApi.findAll(Empty.getDefaultInstance(), observer);

        Mockito.verify(observer, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(observer, Mockito.times(1)).onCompleted();
    }

}