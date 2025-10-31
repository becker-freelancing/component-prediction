package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.ApiEmbeddingWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcSourceEmbedding;
import com.becker.freelance.component.prediction.backend.storage.GrpcSourceMetadata;
import com.becker.freelance.component.prediction.storage.domain.SourceEmbedding;
import com.becker.freelance.component.prediction.storage.domain.SourceMetadata;
import com.becker.freelance.component.prediction.storage.spi.EmbeddingRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class EmbeddingWriteStorageApi extends ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryImplBase {

    private final EmbeddingRepository embeddingRepository;
    private final GrpcMapper grpcMapper;

    @Autowired
    public EmbeddingWriteStorageApi(EmbeddingRepository embeddingRepository, GrpcMapper grpcMapper) {
        this.embeddingRepository = embeddingRepository;
        this.grpcMapper = grpcMapper;
    }

    @Override
    public void save(GrpcSourceEmbedding request, StreamObserver<GrpcSourceMetadata> responseObserver) {
        SourceEmbedding embedding = grpcMapper.mapIncoming(request);
        SourceMetadata saved = embeddingRepository.save(embedding);
        responseObserver.onNext(grpcMapper.mapOutgoing(saved));
        responseObserver.onCompleted();
    }
}
