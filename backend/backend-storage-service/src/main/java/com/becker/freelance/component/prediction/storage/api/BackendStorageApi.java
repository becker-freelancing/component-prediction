package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.*;
import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@GrpcService
public class BackendStorageApi extends ApiDocumentMetadataRepositoryGrpc.ApiDocumentMetadataRepositoryImplBase {

    private static final ZonedDateTime MIN_ZONED_DATE_TIME = ZonedDateTime.of(LocalDateTime.MIN, ZoneId.of("UTC"));
    private static final GrpcApp NULL_APP = GrpcApp.newBuilder().setId(-1).setAppName("nullable-app").build();

    private final DocumentMetadataRepository metadataRepository;

    @Autowired
    public BackendStorageApi(DocumentMetadataRepository metadataRepository) {
        this.metadataRepository = metadataRepository;
    }

    private static BigInteger mapIncoming(Long l) {
        return l == -1 ? null : BigInteger.valueOf(l);
    }

    @Override
    public void save(GrpcDocumentMetadata request, StreamObserver<GrpcDocumentMetadata> responseObserver) {
        DocumentMetadata documentMetadata = mapIncoming(request);
        DocumentMetadata saved = metadataRepository.save(documentMetadata);
        responseObserver.onNext(mapOutgoing(saved));
        responseObserver.onCompleted();
    }

    @Override
    public void findByRelatedDocumentId(GrpcDocumentId request, StreamObserver<GrpcDocumentMetadata> responseObserver) {
        Optional<DocumentMetadata> find = metadataRepository.findByRelatedDocumentId(mapIncomingUUID(request.getDocumentId()));
        find.map(this::mapOutgoing).ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }

    private GrpcDocumentMetadata mapOutgoing(DocumentMetadata saved) {
        return GrpcDocumentMetadata.newBuilder()
                .setId(saved.getId().longValue())
                .setDocumentId(GrpcDocumentId.newBuilder().setDocumentId(Optional.ofNullable(saved.getDocumentId()).map(UUID::toString).orElse("")).build())
                .setApp(mapOutgoing(saved.getApp()))
                .setInAppActionPath(mapOutgoing(saved.getInAppActionPath()))
                .setActionTitle(mapOutgoing(saved.getActionTitle()))
                .setActionDescription(mapOutgoing(saved.getActionDescription()))
                .setActionShortDescription(mapOutgoing(saved.getActionShortDescription()))
                .setLocale(mapOutgoing(saved.getLocale()))
                .setVersion(saved.getVersion().longValue())
                .setCreatedAt(mapOutgoingTime(saved.getCreatedAt()))
                .addAllTags(mapOutgoing(saved.getTags()))
                .build();
    }

    private Iterable<GrpcTag> mapOutgoing(Set<Tag> tags) {
        return tags.stream().map(this::mapOutgoing).collect(Collectors.toSet());
    }

    private GrpcTag mapOutgoing(Tag tag) {
        return GrpcTag.newBuilder()
                .setId(tag.getId().longValue())
                .setTag(mapOutgoing(tag.getTag()))
                .build();
    }

    private GrpcApp mapOutgoing(App app) {
        if (app == null) {
            return NULL_APP;
        }
        return GrpcApp.newBuilder()
                .setId(app.getId().longValue())
                .setAppName(app.getAppName())
                .build();
    }

    private DocumentMetadata mapIncoming(GrpcDocumentMetadata request) {
        return new DocumentMetadata(
                mapIncoming(request.getId()),
                mapIncomingUUID(request.getDocumentId().getDocumentId()),
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

    private UUID mapIncomingUUID(String uuid) {
        return uuid.isEmpty() ? null : UUID.fromString(uuid);
    }

    private Set<Tag> mapIncoming(List<GrpcTag> tagsList) {
        return tagsList.stream().map(this::mapIncoming).collect(Collectors.toSet());
    }

    private Tag mapIncoming(GrpcTag grpcTag) {
        return new Tag(
                mapIncoming(grpcTag.getId()),
                mapIncoming(grpcTag.getTag())
        );
    }

    private App mapIncoming(GrpcApp app) {
        return app.equals(NULL_APP) ? null : new App(mapIncoming(app.getId()), mapIncoming(app.getAppName()));
    }

    private String mapIncoming(String s) {
        return s.isEmpty() ? null : s;
    }

    private String mapOutgoing(String s) {
        return s == null ? "" : s;
    }
}
