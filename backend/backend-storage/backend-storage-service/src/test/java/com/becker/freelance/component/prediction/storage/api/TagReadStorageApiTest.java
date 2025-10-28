package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.GrpcTagList;
import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.TagsRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.UUID;

class TagReadStorageApiTest {

    private TagsRepository repository;
    private TagReadStorageApi tagApi;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(TagsRepository.class);
        tagApi = new TagReadStorageApi(repository);
    }

    @Test
    void findAll() {
        UUID tagId = UUID.randomUUID();
        UUID tagId2 = UUID.randomUUID();
        Mockito.when(repository.findAll()).thenReturn(List.of(
                new Tag(tagId, "tag"),
                new Tag(tagId2, "tag2")
        ));

        StreamObserver<GrpcTagList> observer = Mockito.mock(StreamObserver.class);

        tagApi.findAll(Empty.getDefaultInstance(), observer);

        Mockito.verify(observer, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(observer, Mockito.times(1)).onCompleted();
    }

}