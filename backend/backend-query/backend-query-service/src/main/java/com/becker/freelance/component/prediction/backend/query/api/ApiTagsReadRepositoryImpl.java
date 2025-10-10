package com.becker.freelance.component.prediction.backend.query.api;

import com.becker.freelance.component.prediction.backend.query.ApiTagsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryTagList;
import com.becker.freelance.component.prediction.backend.query.domain.model.Tag;
import com.becker.freelance.component.prediction.backend.query.spi.TagRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@GrpcService
public class ApiTagsReadRepositoryImpl extends ApiTagsReadRepositoryGrpc.ApiTagsReadRepositoryImplBase {

    private final TagRepository tagRepository;
    private final GrpcMapper grpcMapper;

    @Autowired
    public ApiTagsReadRepositoryImpl(TagRepository tagRepository, GrpcMapper grpcMapper) {
        this.tagRepository = tagRepository;
        this.grpcMapper = grpcMapper;
    }

    @Override
    public void findAll(Empty request, StreamObserver<GrpcQueryTagList> responseObserver) {
        List<Tag> tags = tagRepository.findAll();
        GrpcQueryTagList tagList = grpcMapper.mapOutgoing(tags);
        responseObserver.onNext(tagList);
        responseObserver.onCompleted();
    }
}
