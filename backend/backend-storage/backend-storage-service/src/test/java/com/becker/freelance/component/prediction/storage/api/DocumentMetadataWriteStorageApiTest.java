package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
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
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentMetadataWriteStorageApiTest {

    private DocumentMetadataRepository metadataRepository;
    private DocumentMetadataWriteStorageApi storageApi;

    @BeforeEach
    void setUp() {
        metadataRepository = Mockito.mock(DocumentMetadataRepository.class);
        storageApi = new DocumentMetadataWriteStorageApi(metadataRepository);
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