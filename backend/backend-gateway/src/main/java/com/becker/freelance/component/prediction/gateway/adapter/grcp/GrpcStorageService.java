package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.*;
import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;
import com.becker.freelance.component.prediction.gateway.spi.StorageService;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class GrpcStorageService implements StorageService {

    private static final ZonedDateTime MIN_ZONED_DATE_TIME = ZonedDateTime.of(LocalDateTime.MIN, ZoneId.of("UTC"));
    private static final GrpcApp NULL_APP = GrpcApp.newBuilder().setId(-1).setAppName("nullable-app").build();

    private final ApiDocumentMetadataRepositoryGrpc.ApiDocumentMetadataRepositoryBlockingStub stub;

    public GrpcStorageService(ApiDocumentMetadataRepositoryGrpc.ApiDocumentMetadataRepositoryBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public DocumentMetadataDto save(DocumentMetadataDto documentMetadataDto) {
        GrpcDocumentMetadata save = stub.save(map(documentMetadataDto));
        return map(save);
    }

    private DocumentMetadataDto map(GrpcDocumentMetadata save) {
        DocumentMetadataDto dto = new DocumentMetadataDto();
        dto.setId(BigInteger.valueOf(save.getId()));
        dto.setDocumentId(save.getDocumentId().getDocumentId().isEmpty() ? null : UUID.fromString(save.getDocumentId().getDocumentId()));
        dto.setApp(map(save.getApp()));
        dto.setInAppActionPath(save.getInAppActionPath().isEmpty() ? null : save.getInAppActionPath());
        dto.setActionTitle(save.getActionTitle().isEmpty() ? null : save.getActionTitle());
        dto.setActionDescription(save.getActionDescription().isEmpty() ? null : save.getActionDescription());
        dto.setActionShortDescription(save.getActionShortDescription().isEmpty() ? null : save.getActionShortDescription());
        dto.setLocale(save.getLocale().isEmpty() ? null : save.getLocale());
        dto.setVersion(BigInteger.valueOf(save.getVersion()));
        dto.setCreatedAt(ZonedDateTime.parse(save.getCreatedAt()).equals(MIN_ZONED_DATE_TIME) ? null : ZonedDateTime.parse(save.getCreatedAt()));
        dto.setTags(map(save.getTagsList()));
        return dto;
    }

    private Set<TagDto> map(List<GrpcTag> tagsList) {
        return tagsList.stream().map(this::map).collect(Collectors.toSet());
    }

    private TagDto map(GrpcTag grpcTag) {
        TagDto tagDto = new TagDto();
        tagDto.setId(BigInteger.valueOf(grpcTag.getId()));
        tagDto.setTag(grpcTag.getTag());
        return tagDto;
    }

    private AppDto map(GrpcApp app) {
        if (NULL_APP.equals(app)) {
            return null;
        }
        AppDto appDto = new AppDto();
        appDto.setId(BigInteger.valueOf(app.getId()));
        appDto.setAppName(app.getAppName());
        return appDto;
    }

    private GrpcDocumentMetadata map(DocumentMetadataDto documentMetadataDto) {
        return GrpcDocumentMetadata.newBuilder()
                .setId(documentMetadataDto.getId() == null ? -1 : documentMetadataDto.getId().longValue())
                .setDocumentId(GrpcDocumentId.newBuilder().setDocumentId(Optional.ofNullable(documentMetadataDto.getDocumentId()).map(UUID::toString).orElse("")).build())
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

    private Iterable<GrpcTag> map(Set<TagDto> tags) {
        if (tags == null) {
            return Set.of();
        }
        return tags.stream().map(this::map).collect(Collectors.toSet());
    }

    private GrpcTag map(TagDto tagDto) {
        if (tagDto == null) {
            throw new IllegalArgumentException("No null Tag Objects allowed");
        }
        return GrpcTag.newBuilder()
                .setId(tagDto.getId() == null ? -1 : tagDto.getId().longValue())
                .setTag(tagDto.getTag())
                .build();
    }

    private GrpcApp map(AppDto app) {
        if (app == null) {
            return NULL_APP;
        }
        return GrpcApp.newBuilder()
                .setId(app.getId() == null ? -1 : app.getId().longValue())
                .setAppName(app.getAppName())
                .build();
    }
}
