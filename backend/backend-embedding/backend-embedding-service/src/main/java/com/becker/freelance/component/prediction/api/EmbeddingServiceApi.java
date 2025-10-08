package com.becker.freelance.component.prediction.api;

import com.becker.freelance.component.prediction.backend.embedding.ApiEmbeddingServiceGrpc;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbedding;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbeddingRequest;
import com.becker.freelance.component.prediction.spi.EmbeddingException;
import com.becker.freelance.component.prediction.spi.EmbeddingService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class EmbeddingServiceApi extends ApiEmbeddingServiceGrpc.ApiEmbeddingServiceImplBase {

    private final EmbeddingService embeddingService;
    private final GrpcMapper grpcMapper;

    @Autowired
    public EmbeddingServiceApi(EmbeddingService embeddingService, GrpcMapper grpcMapper) {
        this.embeddingService = embeddingService;
        this.grpcMapper = grpcMapper;
    }


    @Override
    public void embed(GrpcEmbeddingRequest request, StreamObserver<GrpcEmbedding> responseObserver) {
        String text = grpcMapper.map(request);
        try {
            float[][] embedded = embeddingService.embed(text);
            GrpcEmbedding grpcEmbedding = grpcMapper.map(embedded);
            responseObserver.onNext(grpcEmbedding);
            responseObserver.onCompleted();
        } catch (EmbeddingException e) {
            responseObserver.onError(e);
        }
    }
}
