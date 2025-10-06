package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class GrpcMapper {


    public static final ZonedDateTime MIN_ZONED_DATE_TIME = ZonedDateTime.of(LocalDateTime.MIN, ZoneId.of("UTC"));
    public static final GrpcApp NULL_APP = GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId("").build()).setAppName("nullable-app").build();


    public DocumentMetadataDto map(GrpcDocumentMetadata save) {
        DocumentMetadataDto dto = new DocumentMetadataDto();
        dto.setId(map(save.getId()));
        dto.setApp(map(save.getApp()));
        dto.setInAppActionPath(save.getInAppActionPath().isEmpty() ? null : save.getInAppActionPath());
        dto.setActionTitle(save.getActionTitle().isEmpty() ? null : save.getActionTitle());
        dto.setActionDescription(save.getActionDescription().isEmpty() ? null : save.getActionDescription());
        dto.setActionShortDescription(save.getActionShortDescription().isEmpty() ? null : save.getActionShortDescription());
        dto.setLocale(save.getLocale().isEmpty() ? null : save.getLocale());
        dto.setVersion(BigInteger.valueOf(save.getVersion()));
        dto.setCreatedAt(map(save.getCreatedAt()));
        dto.setTags(map(save.getTagsList()));
        return dto;
    }

    private ZonedDateTime map(String createdAt) {
        try {
            return ZonedDateTime.parse(createdAt).equals(MIN_ZONED_DATE_TIME) ? null : ZonedDateTime.parse(createdAt);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public Set<TagDto> map(List<GrpcTag> tagsList) {
        return tagsList.stream().map(this::map).collect(Collectors.toSet());
    }

    public TagDto map(GrpcTag grpcTag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(map(grpcTag.getId()));
        tagDto.setTag(grpcTag.getTag());
        return tagDto;
    }

    public AppDto map(GrpcApp app) {
        if (app == null || NULL_APP.equals(app)) {
            return null;
        }
        AppDto appDto = new AppDto();
        appDto.setId(map(app.getId()));
        appDto.setAppName(app.getAppName());
        return appDto;
    }

    public GrpcDocumentMetadata map(DocumentMetadataDto documentMetadataDto) {
        return GrpcDocumentMetadata.newBuilder()
                .setId(map(documentMetadataDto.getId()))
                .setApp(map(documentMetadataDto.getApp()))
                .setInAppActionPath(documentMetadataDto.getInAppActionPath() == null ? "" : documentMetadataDto.getInAppActionPath())
                .setActionTitle(documentMetadataDto.getActionTitle() == null ? "" : documentMetadataDto.getActionTitle())
                .setActionDescription(documentMetadataDto.getActionDescription() == null ? "" : documentMetadataDto.getActionDescription())
                .setActionShortDescription(documentMetadataDto.getActionShortDescription() == null ? "" : documentMetadataDto.getActionShortDescription())
                .setLocale(documentMetadataDto.getLocale() == null ? "" : documentMetadataDto.getLocale())
                .setVersion(documentMetadataDto.getVersion() == null ? -1 : documentMetadataDto.getVersion().longValue())
                .setCreatedAt(documentMetadataDto.getCreatedAt() == null ? MIN_ZONED_DATE_TIME.toString() : documentMetadataDto.getCreatedAt().toString())
                .addAllTags(map(documentMetadataDto.getTags()))
                .build();
    }

    public GrpcUUID map(UUID id) {
        return GrpcUUID.newBuilder()
                .setId(id == null ? "" : id.toString())
                .build();
    }

    public UUID map(GrpcUUID id) {
        return id.getId().isEmpty() ? null : UUID.fromString(id.getId());
    }

    public Iterable<GrpcTag> map(Set<TagDto> tags) {
        if (tags == null) {
            return Set.of();
        }
        return tags.stream().map(this::map).collect(Collectors.toSet());
    }

    public GrpcTag map(TagDto tagDto) {
        if (tagDto == null) {
            throw new IllegalArgumentException("No null Tag Objects allowed");
        }
        return GrpcTag.newBuilder()
                .setId(map(tagDto.getId()))
                .setTag(tagDto.getTag())
                .build();
    }

    public GrpcApp map(AppDto app) {
        if (app == null) {
            return NULL_APP;
        }
        return GrpcApp.newBuilder()
                .setId(map(app.getId()))
                .setAppName(app.getAppName())
                .build();
    }
}
