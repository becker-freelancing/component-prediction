package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestApp;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestSourceMetadata;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestTag;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestUUID;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryApp;
import com.becker.freelance.component.prediction.backend.query.GrpcQuerySourceMetadata;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryTag;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryUUID;
import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import com.becker.freelance.component.prediction.gateway.api.dto.SourceMetadataDto;
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
    public static final GrpcIngestApp INGEST_NULL_APP = GrpcIngestApp.newBuilder().setId(GrpcIngestUUID.newBuilder().setId("").build()).setAppName("nullable-app").build();
    public static final GrpcQueryApp QUERY_NULL_APP = GrpcQueryApp.newBuilder().setId(GrpcQueryUUID.newBuilder().setId("").build()).setAppName("nullable-app").build();
    public static final GrpcQuerySourceMetadata QUERY_NULL_METADATA = GrpcQuerySourceMetadata.newBuilder().build();
    public static final GrpcIngestSourceMetadata INGEST_NULL_METADATA = GrpcIngestSourceMetadata.newBuilder().build();

    public SourceMetadataDto map(GrpcQuerySourceMetadata save) {
        if (QUERY_NULL_METADATA.equals(save)){
            return null;
        }
        SourceMetadataDto dto = new SourceMetadataDto();
        dto.setId(map(save.getId()));
        dto.setApp(map(save.getApp()));
        dto.setLocale(save.getLocale().isEmpty() ? null : save.getLocale());
        dto.setVersion(BigInteger.valueOf(save.getVersion()));
        dto.setCreatedAt(map(save.getCreatedAt()));
        dto.setLastModifiedAt(map(save.getLastModifiedAt()));
        dto.setTags(map(save.getTagsList()));
        dto.setFileName(mapString(save.getFileName()));
        dto.setParent(map(save.getParent()));
        dto.setHasChildren(save.getHasChildren());
        return dto;
    }

    private String mapString(String fileName) {
        return fileName.isEmpty() ? null : fileName;
    }

    private ZonedDateTime map(String createdAt) {
        try {
            return ZonedDateTime.parse(createdAt).equals(MIN_ZONED_DATE_TIME) ? null : ZonedDateTime.parse(createdAt);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    public Set<TagDto> map(List<GrpcQueryTag> tagsList) {
        return tagsList.stream().map(this::map).collect(Collectors.toSet());
    }

    public TagDto map(GrpcQueryTag grpcTag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(map(grpcTag.getId()));
        tagDto.setTag(grpcTag.getTag());
        return tagDto;
    }

    public AppDto map(GrpcQueryApp app) {
        if (app == null || QUERY_NULL_APP.equals(app)) {
            return null;
        }
        AppDto appDto = new AppDto();
        appDto.setId(map(app.getId()));
        appDto.setAppName(app.getAppName());
        return appDto;
    }

    public GrpcIngestSourceMetadata map(SourceMetadataDto sourceMetadataDto) {
        return GrpcIngestSourceMetadata.newBuilder()
                .setId(map(sourceMetadataDto.getId()))
                .setApp(map(sourceMetadataDto.getApp()))
                .setVersion(sourceMetadataDto.getVersion() == null ? -1 : sourceMetadataDto.getVersion().longValue())
                .setCreatedAt(sourceMetadataDto.getCreatedAt() == null ? MIN_ZONED_DATE_TIME.toString() : sourceMetadataDto.getCreatedAt().toString())
                .setLastModifiedAt(sourceMetadataDto.getLastModifiedAt() == null ? MIN_ZONED_DATE_TIME.toString() : sourceMetadataDto.getLastModifiedAt().toString())
                .addAllTags(map(sourceMetadataDto.getTags()))
                .setFileName(mapOutgoingString(sourceMetadataDto.getFileName()))
                .setParent(sourceMetadataDto.getParent() == null ? INGEST_NULL_METADATA : map(sourceMetadataDto.getParent()))
                .setHasChildren(sourceMetadataDto.isHasChildren())
                .build();
    }

    private String mapOutgoingString(String fileName) {
        return fileName == null ? "" : fileName;
    }

    public GrpcIngestUUID map(UUID id) {
        return GrpcIngestUUID.newBuilder()
                .setId(id == null ? "" : id.toString())
                .build();
    }

    public GrpcQueryUUID mapForQuery(UUID id) {
        return GrpcQueryUUID.newBuilder()
                .setId(id == null ? "" : id.toString())
                .build();
    }

    public UUID map(GrpcQueryUUID id) {
        return id.getId().isEmpty() ? null : UUID.fromString(id.getId());
    }

    public Iterable<GrpcIngestTag> map(Set<TagDto> tags) {
        if (tags == null) {
            return Set.of();
        }
        return tags.stream().map(this::map).collect(Collectors.toSet());
    }

    public GrpcIngestTag map(TagDto tagDto) {
        if (tagDto == null) {
            throw new IllegalArgumentException("No null Tag Objects allowed");
        }
        return GrpcIngestTag.newBuilder()
                .setId(map(tagDto.getId()))
                .setTag(tagDto.getTag())
                .build();
    }

    public GrpcIngestApp map(AppDto app) {
        if (app == null) {
            return INGEST_NULL_APP;
        }
        return GrpcIngestApp.newBuilder()
                .setId(map(app.getId()))
                .setAppName(app.getAppName())
                .build();
    }
}
