package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.ApiTagsRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.backend.storage.GrpcTagList;
import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.TagsRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class BackendTagApi extends ApiTagsRepositoryGrpc.ApiTagsRepositoryImplBase {

    private final TagsRepository tagRepository;
    private final GrpcMapper mapper;

    @Autowired
    public BackendTagApi(TagsRepository tagsRepository) {
        this.tagRepository = tagsRepository;
        this.mapper = new GrpcMapper();
    }

    @Override
    public void save(GrpcTag request, StreamObserver<GrpcTag> responseObserver) {
        Tag tag = mapper.mapIncoming(request);
        Tag saved = tagRepository.save(tag);
        responseObserver.onNext(mapper.mapOutgoing(saved));
        responseObserver.onCompleted();
    }

    @Override
    public void findAll(Empty request, StreamObserver<GrpcTagList> responseObserver) {
        List<Tag> found = tagRepository.findAll();
        List<GrpcTag> out = found.stream().map(mapper::mapOutgoing).toList();
        GrpcTagList grpcTagList = GrpcTagList.newBuilder().addAllTags(out).build();
        responseObserver.onNext(grpcTagList);
        responseObserver.onCompleted();
    }
}
