package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.*;
import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.opentest4j.AssertionFailedError;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BackendDocumentMetadataStorageApiTest {

    private DocumentMetadataRepository metadataRepository;
    private BackendDocumentMetadataStorageApi storageApi;

    @BeforeEach
    void setUp() {
        metadataRepository = Mockito.mock(DocumentMetadataRepository.class);
        storageApi = new BackendDocumentMetadataStorageApi(metadataRepository);
    }

    @Test
    void save() {
        UUID id = UUID.randomUUID();
        UUID appId = UUID.randomUUID();
        UUID tagId = UUID.randomUUID();
        Mockito.when(metadataRepository.save(Mockito.any())).then((Answer<DocumentMetadata>) invocationOnMock -> {
            DocumentMetadata argument = invocationOnMock.getArgument(0, DocumentMetadata.class);
            return new DocumentMetadata(
                    id,
                    new App(appId, argument.getApp().getAppName()),
                    argument.getInAppActionPath(),
                    argument.getActionTitle(),
                    argument.getActionDescription(),
                    argument.getActionShortDescription(),
                    argument.getLocale(),
                    BigInteger.ONE,
                    argument.getCreatedAt(),
                    argument.getTags().stream().peek(tag -> tag.setId(tagId)).collect(Collectors.toSet())
            );
        });

        GrpcDocumentMetadata metadata = GrpcDocumentMetadata.newBuilder()
                .setApp(GrpcApp.newBuilder().setAppName("app").build())
                .setInAppActionPath("in-app")
                .setActionTitle("title")
                .setActionDescription("description")
                .setActionShortDescription("short-description")
                .setLocale("de")
                .setVersion(2)
                .setCreatedAt(ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC")).toString())
                .addAllTags(Set.of("t1", "t2").stream().map(t -> GrpcTag.newBuilder().setTag(t).build()).collect(Collectors.toSet()))
                .build();

        GrpcDocumentMetadata expected = GrpcDocumentMetadata.newBuilder(metadata)
                .setId(GrpcUUID.newBuilder().setId(id.toString()).build())
                .setApp(GrpcApp.newBuilder(metadata.getApp()).setId(GrpcUUID.newBuilder().setId(appId.toString()).build()).build())
                .setVersion(1)
                .clearTags()
                .addAllTags(metadata.getTagsList().stream().map(GrpcTag::newBuilder).map(builder -> builder.setId(GrpcUUID.newBuilder().setId(tagId.toString()).build())).map(GrpcTag.Builder::build).collect(Collectors.toSet()))
                .build();

        StreamObserverAssertion responseObserver = Mockito.spy(new StreamObserverAssertion(expected));
        storageApi.save(metadata, responseObserver);

        Mockito.verify(responseObserver, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(responseObserver, Mockito.times(1)).onCompleted();

    }

    @Test
    void saveWithNulls() {
        UUID id = UUID.randomUUID();
        UUID appId = UUID.randomUUID();
        UUID tagId = UUID.randomUUID();
        Mockito.when(metadataRepository.save(Mockito.any())).then((Answer<DocumentMetadata>) invocationOnMock -> {
            DocumentMetadata argument = invocationOnMock.getArgument(0, DocumentMetadata.class);
            return new DocumentMetadata(
                    id,
                    argument.getApp(),
                    argument.getInAppActionPath(),
                    argument.getActionTitle(),
                    argument.getActionDescription(),
                    argument.getActionShortDescription(),
                    argument.getLocale(),
                    BigInteger.ONE,
                    argument.getCreatedAt(),
                    argument.getTags().stream().peek(tag -> tag.setId(tagId)).collect(Collectors.toSet())
            );
        });

        GrpcDocumentMetadata metadata = GrpcDocumentMetadata.newBuilder()
                .setVersion(-1)
                .setApp(GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId(appId.toString()).build()).setAppName("nullable-app").build())
                .setCreatedAt(ZonedDateTime.of(LocalDateTime.MIN, ZoneId.of("UTC")).toString())
                .build();

        GrpcDocumentMetadata expected = GrpcDocumentMetadata.newBuilder(metadata)
                .setId(GrpcUUID.newBuilder().setId(id.toString()).buildPartial())
                .setVersion(1)
                .clearTags()
                .addAllTags(metadata.getTagsList().stream().map(GrpcTag::newBuilder).map(builder -> builder.setId(GrpcUUID.newBuilder().setId(tagId.toString()).build())).map(GrpcTag.Builder::build).collect(Collectors.toSet()))
                .build();

        StreamObserverAssertion responseObserver = Mockito.spy(new StreamObserverAssertion(expected));
        storageApi.save(metadata, responseObserver);

        Mockito.verify(responseObserver, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(responseObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void findAllWithEmptyResult() {
        Mockito.doReturn(List.of()).when(metadataRepository).findAll();

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findAll(Empty.getDefaultInstance(), streamObserver);

        GrpcDocumentMetadataList expected = GrpcDocumentMetadataList.newBuilder().build();

        Mockito.verify(streamObserver, Mockito.times(1)).onNext(expected);
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void findAll() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        DocumentMetadata metadata1 = new DocumentMetadata();
        metadata1.setId(id1);
        DocumentMetadata metadata2 = new DocumentMetadata();
        metadata2.setId(id2);

        Mockito.doReturn(List.of(metadata1, metadata2)).when(metadataRepository).findAll();

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findAll(Empty.getDefaultInstance(), streamObserver);

        GrpcMapper grpcMapper = new GrpcMapper();
        GrpcDocumentMetadataList expected = GrpcDocumentMetadataList.newBuilder()
                .addAllMetadata(Stream.of(metadata1, metadata2).map(grpcMapper::mapOutgoing).toList())
                .build();

        Mockito.verify(streamObserver, Mockito.times(1)).onNext(expected);
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void findByIdIfNotFound() {
        Mockito.when(metadataRepository.findById(Mockito.any())).thenReturn(Optional.empty());


        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findById(GrpcUUID.newBuilder().build(), streamObserver);

        Mockito.verify(streamObserver, Mockito.times(0)).onNext(Mockito.any());
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void findById() {
        UUID id1 = UUID.randomUUID();
        DocumentMetadata metadata1 = new DocumentMetadata();
        metadata1.setId(id1);
        Mockito.when(metadataRepository.findById(Mockito.any())).thenReturn(Optional.of(metadata1));

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findById(GrpcUUID.newBuilder().build(), streamObserver);

        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    private record StreamObserverAssertion(
            GrpcDocumentMetadata expected) implements StreamObserver<GrpcDocumentMetadata> {
        @Override
        public void onNext(GrpcDocumentMetadata grpcDocumentMetadata) {
            assertEquals(expected(), grpcDocumentMetadata);
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