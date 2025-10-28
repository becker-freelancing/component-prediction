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

    public DocumentMetadata map(GrpcDocumentMetadata metadata) {
        return new DocumentMetadata(
                map(metadata.getId()),
                map(metadata.getApp()),
                mapIncoming(metadata.getInAppActionPath()),
                mapIncoming(metadata.getActionTitle()),
                mapIncoming(metadata.getActionDescription()),
                mapIncoming(metadata.getActionShortDescription()),
                mapLocale(metadata.getLocale()),
                map(metadata.getVersion()),
                map(metadata.getCreatedAt()),
                map(metadata.getTagsList())
        );
    }

    private Locale mapLocale(String locale) {
        return locale.isEmpty() ? null : new Locale(locale);
    }

    public GrpcDocumentMetadata map(DocumentMetadata metadata) {
        return GrpcDocumentMetadata.newBuilder()
                .setId(map(metadata.getId()))
                .setApp(map(metadata.getApp()))
                .setInAppActionPath(mapOutgoing(metadata.getInAppActionPath()))
                .setActionTitle(mapOutgoing(metadata.getActionTitle()))
                .setActionDescription(mapOutgoing(metadata.getActionDescription()))
                .setActionShortDescription(mapOutgoing(metadata.getActionShortDescription()))
                .setLocale(mapOutgoing(metadata.getLocale().localeAbbreviation()))
                .setVersion(map(metadata.getVersion()))
                .setCreatedAt(map(metadata.getCreatedAt()))
                .addAllTags(map(metadata.getTags()))
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

    public GrpcDocumentEmbedding map(DocumentEmbedding embedding, GrpcUUID metadataId) {
        return GrpcDocumentEmbedding.newBuilder()
                .setMetadataId(metadataId)
                .setActionDescriptionEmbedding(map(embedding.embeddedActionDescription()))
                .build();
    }

    public GrpcEmbedding map(float[][] embedding) {
        GrpcEmbedding.Builder builder = GrpcEmbedding.newBuilder();
        for (float[] floats : embedding) {
            builder.addEmbeddings(map(floats));
        }
        return builder.build();
    }

    private GrpcFloatArray map(float[] floats) {
        GrpcFloatArray.Builder builder = GrpcFloatArray.newBuilder();
        for (float f : floats) {
            builder.addArray(f);
        }
        return builder.build();
    }
}
