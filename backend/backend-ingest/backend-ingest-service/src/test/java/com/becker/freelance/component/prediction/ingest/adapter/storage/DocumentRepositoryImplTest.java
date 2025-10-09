package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.*;
import com.becker.freelance.component.prediction.ingest.domain.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.stubbing.Answer;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class DocumentRepositoryImplTest {

    ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryBlockingStub embeddingStub;
    ApiDocumentMetadataWriteRepositoryGrpc.ApiDocumentMetadataWriteRepositoryBlockingStub metadataStub;
    DocumentRepositoryImpl repository;

    @BeforeEach
    void setUp() {
        embeddingStub = mock(ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryBlockingStub.class);
        metadataStub = mock(ApiDocumentMetadataWriteRepositoryGrpc.ApiDocumentMetadataWriteRepositoryBlockingStub.class);
        repository = new DocumentRepositoryImpl(embeddingStub, metadataStub, new GrpcAdapterMapper());
    }

    @Test
    void save() {
        UUID id = UUID.randomUUID();
        UUID appId = UUID.randomUUID();
        UUID tagId1 = UUID.randomUUID();
        UUID tagId2 = UUID.randomUUID();
        ZonedDateTime created = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC"));

        GrpcDocumentMetadata expectedMetadataRepositoryArgument = GrpcDocumentMetadata.newBuilder()
                .setId(GrpcUUID.newBuilder().setId("").build())
                .setApp(GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId(appId.toString())).setAppName("app").build())
                .setInAppActionPath("in-app")
                .setActionTitle("title")
                .setActionDescription("desc")
                .setActionShortDescription("short-desc")
                .setLocale("de")
                .setVersion(1)
                .setCreatedAt(created.toString())
                .addAllTags(List.of(GrpcTag.newBuilder().setId(GrpcUUID.newBuilder().setId(tagId1.toString())).setTag("t1").build(),
                        GrpcTag.newBuilder().setId(GrpcUUID.newBuilder().setId(tagId2.toString())).setTag("t2").build()))
                .build();

        doAnswer((Answer<GrpcDocumentMetadata>) invocationOnMock -> {
            GrpcDocumentMetadata argument = invocationOnMock.getArgument(0, GrpcDocumentMetadata.class);
            return GrpcDocumentMetadata.newBuilder(argument)
                    .setId(GrpcUUID.newBuilder().setId(id.toString()).build())
                    .build();
        }).when(metadataStub).save(any());

        GrpcEmbedding expectedEmbedding = GrpcEmbedding.newBuilder()
                .addAllEmbeddings(List.of(
                        GrpcFloatArray.newBuilder().addAllArray(List.of(1f, 2f)).build(),
                        GrpcFloatArray.newBuilder().addAllArray(List.of(2f, 3f)).build()
                ))
                .build();
        GrpcDocumentEmbedding expectedDocumentEmbedding = GrpcDocumentEmbedding.newBuilder()
                .setMetadataId(GrpcUUID.newBuilder().setId(id.toString()).build())
                .setActionDescriptionEmbedding(expectedEmbedding)
                .build();

        doReturn(GrpcDocumentMetadata.newBuilder(expectedMetadataRepositoryArgument)
                .setId(GrpcUUID.newBuilder().setId(id.toString()).build())
                .build()
        ).when(embeddingStub).save(expectedDocumentEmbedding);

        DocumentMetadata documentMetadata = new DocumentMetadata(
                new App(appId, "app"),
                "in-app",
                "title",
                "desc",
                "short-desc",
                new Locale("de"),
                BigInteger.ONE,
                created,
                Set.of(new Tag(tagId1, "t1"), new Tag(tagId2, "t2"))
        );
        DocumentEmbedding embedding = new DocumentEmbedding(new float[][]{{1f, 2f}, {2f, 3f}});

        DocumentMetadata saved = repository.save(documentMetadata, embedding);

        assertEquals(id, saved.getId());
        assertEquals(new App(appId, "app"), saved.getApp());
        assertEquals("in-app", saved.getInAppActionPath());
        assertEquals("title", saved.getActionTitle());
        assertEquals("desc", saved.getActionDescription());
        assertEquals("short-desc", saved.getActionShortDescription());
        assertEquals(new Locale("de"), saved.getLocale());
        assertEquals(BigInteger.ONE, saved.getVersion());
        assertEquals(created, saved.getCreatedAt());
        assertEquals(Set.of(new Tag(tagId1, "t1"), new Tag(tagId2, "t2")), saved.getTags());
    }

}