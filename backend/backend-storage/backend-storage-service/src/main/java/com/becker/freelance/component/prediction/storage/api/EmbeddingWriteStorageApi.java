package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.ApiEmbeddingWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentEmbedding;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.storage.domain.DocumentEmbedding;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
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
    public void save(GrpcDocumentEmbedding request, StreamObserver<GrpcDocumentMetadata> responseObserver) {
        DocumentEmbedding embedding = grpcMapper.mapIncoming(request);
        DocumentMetadata saved = embeddingRepository.save(embedding);
        responseObserver.onNext(grpcMapper.mapOutgoing(saved));
        responseObserver.onCompleted();
    }
}
