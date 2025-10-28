package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.ApiTagsWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.TagsRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class TagWriteStorageApi extends ApiTagsWriteRepositoryGrpc.ApiTagsWriteRepositoryImplBase {

    private final TagsRepository tagRepository;
    private final GrpcMapper mapper;

    @Autowired
    public TagWriteStorageApi(TagsRepository tagsRepository) {
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

}
