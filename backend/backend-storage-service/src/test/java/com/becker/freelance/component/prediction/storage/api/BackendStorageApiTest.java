package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentId;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BackendStorageApiTest {

    private DocumentMetadataRepository metadataRepository;
    private BackendDocumentMetadataStorageApi storageApi;

    @BeforeEach
    void setUp() {
        metadataRepository = Mockito.mock(DocumentMetadataRepository.class);
        storageApi = new BackendDocumentMetadataStorageApi(metadataRepository);
    }

    @Test
    void save() {
        UUID documentId = UUID.randomUUID();
        Mockito.when(metadataRepository.save(Mockito.any())).then((Answer<DocumentMetadata>) invocationOnMock -> {
            DocumentMetadata argument = invocationOnMock.getArgument(0, DocumentMetadata.class);
            return new DocumentMetadata(
                    BigInteger.TWO,
                    documentId,
                    new App(BigInteger.ONE, argument.getApp().getAppName()),
                    argument.getInAppActionPath(),
                    argument.getActionTitle(),
                    argument.getActionDescription(),
                    argument.getActionShortDescription(),
                    argument.getLocale(),
                    BigInteger.ONE,
                    argument.getCreatedAt(),
                    argument.getTags().stream().peek(tag -> tag.setId(BigInteger.ONE)).collect(Collectors.toSet())
            );
        });

        GrpcDocumentMetadata metadata = GrpcDocumentMetadata.newBuilder()
                .setDocumentId(GrpcDocumentId.newBuilder().setDocumentId(documentId.toString()).build())
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
                .setId(2)
                .setApp(GrpcApp.newBuilder(metadata.getApp()).setId(1).build())
                .setVersion(1)
                .clearTags()
                .addAllTags(metadata.getTagsList().stream().map(GrpcTag::newBuilder).map(builder -> builder.setId(1)).map(GrpcTag.Builder::build).collect(Collectors.toSet()))
                .build();

        StreamObserverAssertion responseObserver = Mockito.spy(new StreamObserverAssertion(expected));
        storageApi.save(metadata, responseObserver);

        Mockito.verify(responseObserver, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(responseObserver, Mockito.times(1)).onCompleted();

    }

    @Test
    void saveWithNulls() {
        Mockito.when(metadataRepository.save(Mockito.any())).then((Answer<DocumentMetadata>) invocationOnMock -> {
            DocumentMetadata argument = invocationOnMock.getArgument(0, DocumentMetadata.class);
            return new DocumentMetadata(
                    BigInteger.TWO,
                    argument.getDocumentId(),
                    argument.getApp(),
                    argument.getInAppActionPath(),
                    argument.getActionTitle(),
                    argument.getActionDescription(),
                    argument.getActionShortDescription(),
                    argument.getLocale(),
                    BigInteger.ONE,
                    argument.getCreatedAt(),
                    argument.getTags().stream().peek(tag -> tag.setId(BigInteger.ONE)).collect(Collectors.toSet())
            );
        });

        GrpcDocumentMetadata metadata = GrpcDocumentMetadata.newBuilder()
                .setId(-1)
                .setVersion(-1)
                .setApp(GrpcApp.newBuilder().setId(-1).setAppName("nullable-app").build())
                .setCreatedAt(ZonedDateTime.of(LocalDateTime.MIN, ZoneId.of("UTC")).toString())
                .build();

        GrpcDocumentMetadata expected = GrpcDocumentMetadata.newBuilder(metadata)
                .setId(2)
                .setDocumentId(GrpcDocumentId.newBuilder().build())
                .setVersion(1)
                .clearTags()
                .addAllTags(metadata.getTagsList().stream().map(GrpcTag::newBuilder).map(builder -> builder.setId(1)).map(GrpcTag.Builder::build).collect(Collectors.toSet()))
                .build();

        StreamObserverAssertion responseObserver = Mockito.spy(new StreamObserverAssertion(expected));
        storageApi.save(metadata, responseObserver);

        Mockito.verify(responseObserver, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(responseObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void findByRelatedDocumentIdIfNotFound() {
        Mockito.when(metadataRepository.findByRelatedDocumentId(Mockito.any())).thenReturn(Optional.empty());

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findByRelatedDocumentId(GrpcDocumentId.newBuilder().setDocumentId(UUID.randomUUID().toString()).build(), streamObserver);

        Mockito.verify(streamObserver, Mockito.times(0)).onNext(Mockito.any());
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void findByRelatedDocumentId() {
        Mockito.when(metadataRepository.findByRelatedDocumentId(Mockito.any())).thenReturn(Optional.empty());

        Mockito.when(metadataRepository.findByRelatedDocumentId(Mockito.any())).thenReturn(Optional.of(new DocumentMetadata(
                BigInteger.TWO,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                BigInteger.ONE,
                null,
                Set.of()
        )));

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findByRelatedDocumentId(GrpcDocumentId.newBuilder().setDocumentId(UUID.randomUUID().toString()).build(), streamObserver);

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