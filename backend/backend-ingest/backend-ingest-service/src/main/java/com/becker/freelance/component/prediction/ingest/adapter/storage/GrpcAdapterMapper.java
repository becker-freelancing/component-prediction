package com.becker.freelance.component.prediction.ingest.adapter.storage;


import com.becker.freelance.component.prediction.backend.storage.*;
import com.becker.freelance.component.prediction.ingest.domain.model.*;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GrpcAdapterMapper {

    private static final GrpcApp NULL_APP = GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId("").build()).setAppName("nullable-app").build();
    private static final GrpcSourceMetadata NULL_METADATA = GrpcSourceMetadata.newBuilder().setId(GrpcUUID.newBuilder().setId("").build()).build();




    public App map(GrpcApp app) {
        if (NULL_APP.equals(app)) {
            return null;
        }
        return new App(
                map(app.getId()),
                mapIncoming(app.getAppName())
        );
    }

    public GrpcApp map(App app) {
        if (app == null) {
            return NULL_APP;
        }
        return GrpcApp.newBuilder()
                .setId(map(app.getId()))
                .setAppName(mapOutgoing(app.getAppName()))
                .build();
    }

    public Tag map(GrpcTag tag) {
        return new Tag(
                map(tag.getId()),
                mapIncoming(tag.getTag())
        );
    }

    public GrpcTag map(Tag tag) {
        return GrpcTag.newBuilder()
                .setId(map(tag.getId()))
                .setTag(mapOutgoing(tag.getTag()))
                .build();
    }

    private GrpcUUID map(UUID id) {
        return GrpcUUID.newBuilder()
                .setId(id == null ? "" : id.toString())
                .build();
    }

    private String mapOutgoing(String s) {
        return s == null ? "" : s;
    }

    private String mapIncoming(String s) {
        return s.isEmpty() ? null : s;
    }

    private UUID map(GrpcUUID id) {
        return id.getId().isEmpty() ? null : UUID.fromString(id.getId());
    }

    public SourceMetadata map(GrpcSourceMetadata metadata) {
        if (metadata == null){
            return null;
        }
        return new SourceMetadata(
                map(metadata.getId()),
                map(metadata.getApp()),
                mapLocale(metadata.getLocale()),
                map(metadata.getVersion()),
                map(metadata.getCreatedAt()),
                map(metadata.getLastModifiedAt()),
                map(metadata.getTagsList()),
                mapIncoming(metadata.getFileName()),
                map(metadata.getParent()),
                metadata.getHasChildren()
        );
    }

    private Locale mapLocale(String locale) {
        return locale.isEmpty() ? null : new Locale(locale);
    }

    public GrpcSourceMetadata map(SourceMetadata metadata) {
        if (metadata == null){
            return NULL_METADATA;
        }
        return GrpcSourceMetadata.newBuilder()
                .setId(map(metadata.getId()))
                .setApp(map(metadata.getApp()))
                .setLocale(mapOutgoing(metadata.getLocale().localeAbbreviation()))
                .setVersion(map(metadata.getVersion()))
                .setCreatedAt(map(metadata.getCreatedAt()))
                .setLastModifiedAt(map(metadata.getLastModifiedAt()))
                .addAllTags(map(metadata.getTags()))
                .setFileName(mapOutgoing(metadata.getFileName().orElse(null)))
                .setParent(map(metadata.getParent().orElse(null)))
                .setHasChildren(metadata.hasChildren())
                .build();

    }

    private Iterable<GrpcTag> map(Set<Tag> tags) {
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


    private Set<Tag> map(List<GrpcTag> tagsList) {
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

    private GrpcFloatArray map(float[] floats) {
        GrpcFloatArray.Builder builder = GrpcFloatArray.newBuilder();
        for (float f : floats) {
            builder.addArray(f);
        }
        return builder.build();
    }

    public GrpcSourceEmbedding map(SourceMetadata metadata, float[] embedding) {
        return GrpcSourceEmbedding.newBuilder()
                .setMetadataId(map(metadata.getId()))
                .setEmbeddings(map(embedding))
                .build();
    }
}
