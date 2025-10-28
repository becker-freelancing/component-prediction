package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestApp;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestDocumentMetadata;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestTag;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestUUID;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryApp;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryDocumentMetadata;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryTag;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryUUID;
import com.becker.freelance.component.prediction.ingest.domain.model.App;
import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.Locale;
import com.becker.freelance.component.prediction.ingest.domain.model.Tag;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GrpcMapper {

    private static final GrpcIngestApp INGEST_NULL_APP = GrpcIngestApp.newBuilder().setId(GrpcIngestUUID.newBuilder().setId("").build()).setAppName("nullable-app").build();
    private static final GrpcQueryApp QUERY_NULL_APP = GrpcQueryApp.newBuilder().setId(GrpcQueryUUID.newBuilder().setId("").build()).setAppName("nullable-app").build();


    public App map(GrpcIngestApp app) {
        if (INGEST_NULL_APP.equals(app)) {
            return null;
        }
        return new App(
                map(app.getId()),
                mapIncoming(app.getAppName())
        );
    }

    public GrpcQueryApp map(App app) {
        if (app == null) {
            return QUERY_NULL_APP;
        }
        return GrpcQueryApp.newBuilder()
                .setId(map(app.getId()))
                .setAppName(mapOutgoing(app.getAppName()))
                .build();
    }

    public Tag map(GrpcIngestTag tag) {
        return new Tag(
                map(tag.getId()),
                mapIncoming(tag.getTag())
        );
    }

    public GrpcQueryTag map(Tag tag) {
        return GrpcQueryTag.newBuilder()
                .setId(map(tag.getId()))
                .setTag(mapOutgoing(tag.getTag()))
                .build();
    }

    private GrpcQueryUUID map(UUID id) {
        return GrpcQueryUUID.newBuilder()
                .setId(id == null ? "" : id.toString())
                .build();
    }

    private String mapOutgoing(String s) {
        return s == null ? "" : s;
    }

    private String mapIncoming(String s) {
        return s.isEmpty() ? null : s;
    }

    private UUID map(GrpcIngestUUID id) {
        return id.getId().isEmpty() ? null : UUID.fromString(id.getId());
    }

    public DocumentMetadata map(GrpcIngestDocumentMetadata metadata) {
        return new DocumentMetadata(
                map(metadata.getId()),
                map(metadata.getApp()),
                mapIncoming(metadata.getInAppActionPath()),
                mapIncoming(metadata.getActionTitle()),
                mapIncoming(metadata.getActionDescription()),
                mapIncoming(metadata.getActionShortDescription()),
                map(metadata.getVersion()),
                map(metadata.getCreatedAt()),
                map(metadata.getTagsList())
        );
    }

    public GrpcQueryDocumentMetadata map(DocumentMetadata metadata) {
        return GrpcQueryDocumentMetadata.newBuilder()
                .setId(map(metadata.getId()))
                .setApp(map(metadata.getApp()))
                .setInAppActionPath(mapOutgoing(metadata.getInAppActionPath()))
                .setActionTitle(mapOutgoing(metadata.getActionTitle()))
                .setActionDescription(mapOutgoing(metadata.getActionDescription()))
                .setActionShortDescription(mapOutgoing(metadata.getActionShortDescription()))
                .setVersion(map(metadata.getVersion()))
                .setCreatedAt(map(metadata.getCreatedAt()))
                .addAllTags(map(metadata.getTags()))
                .setLocale(map(metadata.getLocale()))
                .build();

    }

    private String map(Locale locale) {
        return locale == null ? "" : mapOutgoing(locale.localeAbbreviation());
    }

    private Iterable<GrpcQueryTag> map(Set<Tag> tags) {
        return tags.stream()
                .map(this::map)
                .collect(Collectors.toSet());
    }

    private long map(BigInteger version) {
        return version == null ? -1 : version.longValue();
    }

    private String map(ZonedDateTime time) {
        return time == null ? "" : time.toString();
    }


    private Set<Tag> map(List<GrpcIngestTag> tagsList) {
        return tagsList.stream()
                .map(this::map)
                .collect(Collectors.toSet());
    }

    private ZonedDateTime map(String createdAt) {
        try {
            return ZonedDateTime.parse(createdAt);
        } catch (Exception e) {
            return null;
        }
    }

    private BigInteger map(long version) {
        return version == -1 ? null : BigInteger.valueOf(version);
    }
}
