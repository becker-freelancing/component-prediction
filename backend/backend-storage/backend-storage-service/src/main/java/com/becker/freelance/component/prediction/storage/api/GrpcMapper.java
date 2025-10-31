package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.*;
import com.becker.freelance.component.prediction.storage.domain.*;
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
    private static final GrpcApp NULL_APP = GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId("").build()).setAppName("nullable-app").build();
    private static final GrpcSourceMetadata NULL_METADATA = GrpcSourceMetadata.newBuilder().build();


    private BigInteger mapIncoming(Long l) {
        return l == -1 ? null : BigInteger.valueOf(l);
    }


    public GrpcSourceMetadata mapOutgoing(SourceMetadata saved) {
        if (saved == null){
            return NULL_METADATA;
        }
        return GrpcSourceMetadata.newBuilder()
                .setId(mapOutgoing(saved.getId()))
                .setApp(mapOutgoing(saved.getApp()))
                .setLocale(mapOutgoing(saved.getLocale()))
                .setVersion(mapOutgoing(saved.getVersion()))
                .setCreatedAt(mapOutgoingTime(saved.getCreatedAt()))
                .setLastModifiedAt(mapOutgoingTime(saved.getLastModifiedAt()))
                .addAllTags(mapOutgoing(saved.getTags()))
                .setFileName(mapOutgoing(saved.getFileName().orElse(null)))
                .setParent(mapOutgoing(saved.getParent().orElse(null)))
                .setHasChildren(saved.hasChildren())
                .build();
    }

    private String mapOutgoing(Locale locale) {
        return locale == null ? "" : locale.abbreviation();
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

    public SourceMetadata mapIncoming(GrpcSourceMetadata request) {
        if (NULL_METADATA.equals(request)){
            return null;
        }
        return new SourceMetadata(
                mapIncoming(request.getId()),
                mapIncoming(request.getApp()),
                mapIncomingLocale(request.getLocale()),
                mapIncoming(request.getVersion()),
                mapIncomingTime(request.getCreatedAt()),
                mapIncomingTime(request.getLastModifiedAt()),
                mapIncoming(request.getTagsList()),
                mapIncoming(request.getFileName()),
                mapIncoming(request.getParent()),
                request.getHasChildren()
        );
    }

    private Locale mapIncomingLocale(String locale) {
        return locale.isEmpty() ? null : new Locale(locale);
    }

    private ZonedDateTime mapIncomingTime(String createdAt) {
        if (createdAt.isEmpty()){
            return null;
        }
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

    public SourceEmbedding mapIncoming(GrpcSourceEmbedding request) {
        UUID metadataId = mapIncoming(request.getMetadataId());
        float[] embedding = map(request.getEmbeddings());
        return new SourceEmbedding(metadataId, new float[][]{embedding});
    }


    private float[] map(GrpcFloatArray grpcFloatArray) {
        List<Float> floats = grpcFloatArray.getArrayList();
        float[] arr = new float[floats.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = floats.get(i);
        }
        return arr;
    }
}
