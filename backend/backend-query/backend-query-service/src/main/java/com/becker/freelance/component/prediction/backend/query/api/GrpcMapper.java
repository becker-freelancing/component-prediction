package com.becker.freelance.component.prediction.backend.query.api;

import com.becker.freelance.component.prediction.backend.query.*;
import com.becker.freelance.component.prediction.backend.query.domain.model.App;
import com.becker.freelance.component.prediction.backend.query.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.backend.query.domain.model.Tag;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
class GrpcMapper {


    private static final ZonedDateTime MIN_ZONED_DATE_TIME = ZonedDateTime.of(LocalDateTime.MIN, ZoneId.of("UTC"));
    private static final GrpcQueryApp NULL_APP = GrpcQueryApp.newBuilder().setId(GrpcQueryUUID.newBuilder().setId("").build()).setAppName("nullable-app").build();

    public GrpcQueryDocumentMetadata mapOutgoing(DocumentMetadata saved) {
        return GrpcQueryDocumentMetadata.newBuilder()
                .setId(mapOutgoing(saved.getId()))
                .setApp(mapOutgoing(saved.getApp()))
                .setInAppActionPath(mapOutgoing(saved.getInAppActionPath()))
                .setActionTitle(mapOutgoing(saved.getActionTitle()))
                .setActionDescription(mapOutgoing(saved.getActionDescription()))
                .setActionShortDescription(mapOutgoing(saved.getActionShortDescription()))
                .setLocale(mapOutgoing(saved.getLocale()))
                .setVersion(mapOutgoing(saved.getVersion()))
                .setCreatedAt(mapOutgoingTime(saved.getCreatedAt()))
                .addAllTags(mapOutgoing(saved.getTags()))
                .build();
    }

    private long mapOutgoing(BigInteger version) {
        return version == null ? -1 : version.longValue();
    }

    private GrpcQueryUUID mapOutgoing(UUID id) {
        return GrpcQueryUUID.newBuilder().setId(Optional.ofNullable(id).map(UUID::toString).orElse("")).build();
    }

    public UUID mapIncoming(GrpcQueryUUID id) {
        return id.getId().isEmpty() ? null : UUID.fromString(id.getId());
    }

    private Iterable<GrpcQueryTag> mapOutgoing(Set<Tag> tags) {
        return Optional.ofNullable(tags).orElse(Set.of()).stream().map(this::mapOutgoing).collect(Collectors.toSet());
    }

    public GrpcQueryTag mapOutgoing(Tag tag) {
        return GrpcQueryTag.newBuilder()
                .setId(mapOutgoing(tag.getId()))
                .setTag(mapOutgoing(tag.getTag()))
                .build();
    }

    public GrpcQueryApp mapOutgoing(App app) {
        if (app == null) {
            return NULL_APP;
        }
        return GrpcQueryApp.newBuilder()
                .setId(mapOutgoing(app.getId()))
                .setAppName(app.getAppName())
                .build();
    }


    private String mapOutgoingTime(ZonedDateTime createdAt) {
        return createdAt == null ? MIN_ZONED_DATE_TIME.toString() : createdAt.toString();
    }


    private String mapOutgoing(String s) {
        return s == null ? "" : s;
    }

    public GrpcQueryAppList mapOutgoingApps(List<App> apps) {
        return GrpcQueryAppList.newBuilder()
                .addAllApps(apps.stream().map(this::mapOutgoing).toList())
                .build();
    }

    public GrpcQueryDocumentMetadataList mapOutgoingMetadata(List<DocumentMetadata> metadata) {
        return GrpcQueryDocumentMetadataList.newBuilder()
                .addAllMetadata(metadata.stream().map(this::mapOutgoing).toList())
                .build();
    }

    public GrpcQueryTagList mapOutgoing(List<Tag> tags) {
        return GrpcQueryTagList.newBuilder()
                .addAllTags(tags.stream().map(this::mapOutgoing).toList())
                .build();
    }
}

