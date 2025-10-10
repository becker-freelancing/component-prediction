package com.becker.freelance.component.prediction.backend.query.adapter.grpc;

import com.becker.freelance.component.prediction.backend.query.GrpcQueryApp;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryDocumentMetadata;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryTag;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryUUID;
import com.becker.freelance.component.prediction.backend.query.domain.model.App;
import com.becker.freelance.component.prediction.backend.query.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.backend.query.domain.model.Tag;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class GrpcAdapterMapper {


    private static final ZonedDateTime MIN_ZONED_DATE_TIME = ZonedDateTime.of(LocalDateTime.MIN, ZoneId.of("UTC"));
    private static final GrpcQueryApp NULL_APP = GrpcQueryApp.newBuilder().setId(GrpcQueryUUID.newBuilder().setId("").build()).setAppName("nullable-app").build();


    private BigInteger map(Long l) {
        return l == -1 ? null : BigInteger.valueOf(l);
    }


    public UUID map(GrpcQueryUUID id) {
        return id.getId().isEmpty() ? null : UUID.fromString(id.getId());
    }


    public DocumentMetadata map(GrpcQueryDocumentMetadata request) {
        return new DocumentMetadata(
                map(request.getId()),
                map(request.getApp()),
                map(request.getInAppActionPath()),
                map(request.getActionTitle()),
                map(request.getActionDescription()),
                map(request.getActionShortDescription()),
                map(request.getLocale()),
                map(request.getVersion()),
                mapTime(request.getCreatedAt()),
                map(request.getTagsList())
        );
    }

    private ZonedDateTime mapTime(String createdAt) {
        ZonedDateTime parsed = ZonedDateTime.parse(createdAt);
        return parsed.equals(MIN_ZONED_DATE_TIME) ? null : parsed;
    }

    private Set<Tag> map(List<GrpcQueryTag> tagsList) {
        return tagsList.stream().map(this::map).collect(Collectors.toSet());
    }

    public Tag map(GrpcQueryTag grpcTag) {
        return new Tag(
                map(grpcTag.getId()),
                map(grpcTag.getTag())
        );
    }

    public App map(GrpcQueryApp app) {
        return app.equals(NULL_APP) ? null : new App(map(app.getId()), map(app.getAppName()));
    }

    private String map(String s) {
        return s.isEmpty() ? null : s;
    }

    public GrpcQueryUUID map(UUID id) {
        return GrpcQueryUUID.newBuilder().setId(id.toString()).build();
    }
}
