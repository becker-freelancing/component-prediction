package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.domain.Tag;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

class GrpcMapper {


    private static final ZonedDateTime MIN_ZONED_DATE_TIME = ZonedDateTime.of(LocalDateTime.MIN, ZoneId.of("UTC"));
    private static final GrpcApp NULL_APP = GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId("").build()).setAppName("nullable-app").build();


    private BigInteger mapIncoming(Long l) {
        return l == -1 ? null : BigInteger.valueOf(l);
    }


    public GrpcDocumentMetadata mapOutgoing(DocumentMetadata saved) {
        return GrpcDocumentMetadata.newBuilder()
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

    private GrpcUUID mapOutgoing(UUID id) {
        return GrpcUUID.newBuilder().setId(Optional.ofNullable(id).map(UUID::toString).orElse("")).build();
    }

    public UUID mapIncoming(GrpcUUID id) {
        return id.getId().isEmpty() ? null : UUID.fromString(id.getId());
    }

    private Iterable<GrpcTag> mapOutgoing(Set<Tag> tags) {
        return Optional.ofNullable(tags).orElse(Set.of()).stream().map(this::mapOutgoing).collect(Collectors.toSet());
    }

    public GrpcTag mapOutgoing(Tag tag) {
        return GrpcTag.newBuilder()
                .setId(mapOutgoing(tag.getId()))
                .setTag(mapOutgoing(tag.getTag()))
                .build();
    }

    public GrpcApp mapOutgoing(App app) {
        if (app == null) {
            return NULL_APP;
        }
        return GrpcApp.newBuilder()
                .setId(mapOutgoing(app.getId()))
                .setAppName(app.getAppName())
                .build();
    }

    public DocumentMetadata mapIncoming(GrpcDocumentMetadata request) {
        return new DocumentMetadata(
                mapIncoming(request.getId()),
                mapIncoming(request.getApp()),
                mapIncoming(request.getInAppActionPath()),
                mapIncoming(request.getActionTitle()),
                mapIncoming(request.getActionDescription()),
                mapIncoming(request.getActionShortDescription()),
                mapIncoming(request.getLocale()),
                mapIncoming(request.getVersion()),
                mapIncomingTime(request.getCreatedAt()),
                mapIncoming(request.getTagsList())
        );
    }

    private ZonedDateTime mapIncomingTime(String createdAt) {
        ZonedDateTime parsed = ZonedDateTime.parse(createdAt);
        return parsed.equals(MIN_ZONED_DATE_TIME) ? null : parsed;
    }

    private String mapOutgoingTime(ZonedDateTime createdAt) {
        return createdAt == null ? MIN_ZONED_DATE_TIME.toString() : createdAt.toString();
    }

    private Set<Tag> mapIncoming(List<GrpcTag> tagsList) {
        return tagsList.stream().map(this::mapIncoming).collect(Collectors.toSet());
    }

    public Tag mapIncoming(GrpcTag grpcTag) {
        return new Tag(
                mapIncoming(grpcTag.getId()),
                mapIncoming(grpcTag.getTag())
        );
    }

    public App mapIncoming(GrpcApp app) {
        return app.equals(NULL_APP) ? null : new App(mapIncoming(app.getId()), mapIncoming(app.getAppName()));
    }

    private String mapIncoming(String s) {
        return s.isEmpty() ? null : s;
    }

    private String mapOutgoing(String s) {
        return s == null ? "" : s;
    }
}
