package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.backend.ingest.ApiTagsIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestTag;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryTag;
import com.becker.freelance.component.prediction.ingest.domain.model.Tag;
import com.becker.freelance.component.prediction.ingest.spi.TagsRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class ApiTagsIngestRepositoryImpl extends ApiTagsIngestRepositoryGrpc.ApiTagsIngestRepositoryImplBase {

    private final TagsRepository tagsRepository;
    private final GrpcMapper grpcMapper;

    @Autowired
    public ApiTagsIngestRepositoryImpl(TagsRepository tagsRepository, GrpcMapper grpcMapper) {
        this.tagsRepository = tagsRepository;
        this.grpcMapper = grpcMapper;
    }

    @Override
    public void save(GrpcIngestTag request, StreamObserver<GrpcQueryTag> responseObserver) {
        Tag tag = grpcMapper.map(request);
        Tag save = tagsRepository.save(tag);
        responseObserver.onNext(grpcMapper.map(save));
        responseObserver.onCompleted();
    }
}
