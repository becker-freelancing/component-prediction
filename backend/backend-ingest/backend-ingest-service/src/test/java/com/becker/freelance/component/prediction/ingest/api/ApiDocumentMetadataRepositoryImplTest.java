package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestApp;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestDocumentMetadata;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestTag;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestUUID;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryDocumentMetadata;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryTag;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryUUID;
import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.Locale;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;
import org.opentest4j.AssertionFailedError;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ApiDocumentMetadataRepositoryImplTest {

    DocumentMetadataIngestService ingestService;
    ApiDocumentMetadataRepositoryImpl apiRepository;

    @BeforeEach
    void setUp() {
        ingestService = mock(DocumentMetadataIngestService.class);
        apiRepository = new ApiDocumentMetadataRepositoryImpl(ingestService, new GrpcMapper());
    }

    @Test
    void save() {
        UUID id = UUID.randomUUID();
        UUID appId = UUID.randomUUID();
        UUID tagId1 = UUID.randomUUID();
        UUID tagId2 = UUID.randomUUID();
        ZonedDateTime created = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC"));

        doAnswer((Answer<DocumentMetadata>) invocationOnMock -> {
            DocumentMetadata argument = invocationOnMock.getArgument(0, DocumentMetadata.class);
            argument.setId(id);
            argument.setLocale(new Locale("de"));
            return argument;
        }).when(ingestService).ingest(any());

        StreamObserverAssertion assertion = spy(new StreamObserverAssertion(id, appId, created, tagId1, tagId2));

        apiRepository.save(GrpcIngestDocumentMetadata.newBuilder()
                .setId(GrpcIngestUUID.newBuilder().setId("").build())
                .setApp(GrpcIngestApp.newBuilder().setId(GrpcIngestUUID.newBuilder().setId(appId.toString())).setAppName("app").build())
                .setInAppActionPath("in-app")
                .setActionTitle("title")
                .setActionDescription("desc")
                .setActionShortDescription("short-desc")
                .setVersion(1)
                .setCreatedAt(created.toString())
                .addAllTags(List.of(GrpcIngestTag.newBuilder().setId(GrpcIngestUUID.newBuilder().setId(tagId1.toString())).setTag("t1").build(),
                        GrpcIngestTag.newBuilder().setId(GrpcIngestUUID.newBuilder().setId(tagId2.toString())).setTag("t2").build()))
                .build(), assertion);

        verify(assertion, times(1)).onCompleted();
        verify(assertion, times(1)).onNext(any());

    }

    class StreamObserverAssertion implements StreamObserver<GrpcQueryDocumentMetadata> {

        private final UUID id;
        private final UUID appId;
        private final ZonedDateTime created;
        private final UUID tagId1;
        private final UUID tagId2;

        public StreamObserverAssertion(UUID id, UUID appId, ZonedDateTime created, UUID tagId1, UUID tagId2) {
            this.id = id;
            this.appId = appId;
            this.created = created;
            this.tagId1 = tagId1;
            this.tagId2 = tagId2;
        }

        @Override
        public void onNext(GrpcQueryDocumentMetadata saved) {
            assertEquals(id.toString(), saved.getId().getId());
            assertEquals("de", saved.getLocale());
            assertEquals(appId.toString(), saved.getApp().getId().getId());
            assertEquals("app", saved.getApp().getAppName());
            assertEquals("in-app", saved.getInAppActionPath());
            assertEquals("title", saved.getActionTitle());
            assertEquals("desc", saved.getActionDescription());
            assertEquals("short-desc", saved.getActionShortDescription());
            assertEquals("de", saved.getLocale());
            assertEquals(1, saved.getVersion());
            assertEquals(created.toString(), saved.getCreatedAt());
            assertEquals(Stream.of(tagId1, tagId2).map(UUID::toString).collect(Collectors.toSet()), saved.getTagsList().stream().map(GrpcQueryTag::getId).map(GrpcQueryUUID::getId).collect(Collectors.toSet()));
            assertEquals(Set.of("t1", "t2"), saved.getTagsList().stream().map(GrpcQueryTag::getTag).collect(Collectors.toSet()));
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