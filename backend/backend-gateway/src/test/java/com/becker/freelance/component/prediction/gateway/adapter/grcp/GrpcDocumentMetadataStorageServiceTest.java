package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.*;
import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class GrpcDocumentMetadataStorageServiceTest {

    private ApiDocumentMetadataRepositoryGrpc.ApiDocumentMetadataRepositoryBlockingStub stub;
    private GrpcDocumentMetadataStorageService storageService;

    @BeforeEach
    void setUp() {
        stub = Mockito.mock(ApiDocumentMetadataRepositoryGrpc.ApiDocumentMetadataRepositoryBlockingStub.class);
        storageService = new GrpcDocumentMetadataStorageService(stub);
    }


    @Test
    void save() {
        UUID documentId = UUID.randomUUID();
        UUID id = UUID.randomUUID();
        UUID appId = UUID.randomUUID();
        UUID tagId = UUID.randomUUID();
        Mockito.when(stub.save(Mockito.any())).then((Answer<GrpcDocumentMetadata>) invocationOnMock -> {
            GrpcDocumentMetadata argument = invocationOnMock.getArgument(0, GrpcDocumentMetadata.class);
            return GrpcDocumentMetadata.newBuilder()
                    .setId(GrpcUUID.newBuilder().setId(id.toString()).build())
                    .setApp(GrpcApp.newBuilder(argument.getApp()).setId(GrpcUUID.newBuilder().setId(appId.toString()).build()).build())
                    .setInAppActionPath(argument.getInAppActionPath())
                    .setActionTitle(argument.getActionTitle())
                    .setActionDescription(argument.getActionDescription())
                    .setActionShortDescription(argument.getActionShortDescription())
                    .setLocale(argument.getLocale())
                    .setVersion(1)
                    .setCreatedAt(argument.getCreatedAt())
                    .addAllTags(argument.getTagsList().stream().map(GrpcTag::newBuilder).map(b -> b.setId(GrpcUUID.newBuilder().setId(tagId.toString()).build())).map(GrpcTag.Builder::build).toList())
                    .build();
        });

        DocumentMetadataDto dto = new DocumentMetadataDto();
        AppDto appDto = new AppDto();
        appDto.setAppName("test-app");
        dto.setApp(appDto);
        dto.setInAppActionPath("path");
        dto.setActionTitle("title");
        dto.setActionDescription("description");
        dto.setActionShortDescription("short-description");
        dto.setLocale("de");
        dto.setVersion(BigInteger.TEN);
        dto.setCreatedAt(ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC")));
        Set<String> tagNames = Set.of("t1", "t2");
        dto.setTags(tagNames.stream().map(t -> {
            TagDto tagDto = new TagDto();
            tagDto.setTag(t);
            return tagDto;
        }).collect(Collectors.toSet()));

        DocumentMetadataDto saved = storageService.save(dto);
        assertEquals(id, saved.getId());
        assertEquals(appId, saved.getApp().getId());
        assertEquals("test-app", saved.getApp().getAppName());
        assertEquals("path", saved.getInAppActionPath());
        assertEquals("title", saved.getActionTitle());
        assertEquals("description", saved.getActionDescription());
        assertEquals("short-description", saved.getActionShortDescription());
        assertEquals("de", saved.getLocale());
        assertEquals(BigInteger.ONE, saved.getVersion());
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC")), saved.getCreatedAt());
        assertEquals(2, saved.getTags().size());
        saved.getTags().forEach(tag -> assertEquals(tagId, tag.getId()));
        saved.getTags().forEach(tag -> assertTrue(tagNames.contains(tag.getTag())));
    }


    @Test
    void saveWithNulls() {
        Mockito.when(stub.save(Mockito.any())).then((Answer<GrpcDocumentMetadata>) invocationOnMock -> {
            GrpcDocumentMetadata argument = invocationOnMock.getArgument(0, GrpcDocumentMetadata.class);
            return GrpcDocumentMetadata.newBuilder()
                    .setId(GrpcUUID.newBuilder().setId("").build())
                    .setApp(argument.getApp())
                    .setInAppActionPath(argument.getInAppActionPath())
                    .setActionTitle(argument.getActionTitle())
                    .setActionDescription(argument.getActionDescription())
                    .setActionShortDescription(argument.getActionShortDescription())
                    .setLocale(argument.getLocale())
                    .setVersion(1)
                    .setCreatedAt(argument.getCreatedAt())
                    .addAllTags(argument.getTagsList())
                    .build();
        });

        DocumentMetadataDto dto = new DocumentMetadataDto();


        DocumentMetadataDto saved = storageService.save(dto);
        assertNull(saved.getId());
        assertNull(saved.getApp());
        assertNull(saved.getInAppActionPath());
        assertNull(saved.getActionTitle());
        assertNull(saved.getActionDescription());
        assertNull(saved.getActionShortDescription());
        assertNull(saved.getLocale());
        assertEquals(BigInteger.ONE, saved.getVersion());
        assertNull(saved.getCreatedAt());
        assertEquals(0, saved.getTags().size());
    }

    @Test
    void findAllWithEmptyList() {
        Mockito.when(stub.findAll(Mockito.any())).thenReturn(GrpcDocumentMetadataList.newBuilder().build());

        List<DocumentMetadataDto> all = storageService.findAll();

        assertEquals(0, all.size());
    }

    @Test
    void findAll() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Mockito.when(stub.findAll(Mockito.any())).thenReturn(GrpcDocumentMetadataList.newBuilder()
                .addAllMetadata(List.of(
                        GrpcDocumentMetadata.newBuilder().setId(GrpcUUID.newBuilder().setId(id1.toString()).build()).build(),
                        GrpcDocumentMetadata.newBuilder().setId(GrpcUUID.newBuilder().setId(id2.toString()).build()).build()
                )).build());

        List<DocumentMetadataDto> all = storageService.findAll();
        assertEquals(2, all.size());
        assertEquals(List.of(id1, id2), all.stream().map(DocumentMetadataDto::getId).toList());
    }
}