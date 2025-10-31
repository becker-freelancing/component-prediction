package com.becker.freelance.component.prediction.api;

import com.becker.freelance.component.prediction.backend.embedding.ApiEmbeddingServiceGrpc;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbedding;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbeddingRequestChunk;
import com.becker.freelance.component.prediction.backend.embedding.GrpcPreferredChunkSize;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBufferFactory;
import com.becker.freelance.component.prediction.spi.EmbeddingException;
import com.becker.freelance.component.prediction.spi.EmbeddingService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class EmbeddingServiceApi extends ApiEmbeddingServiceGrpc.ApiEmbeddingServiceImplBase {

    private final EmbeddingService embeddingService;

    @Autowired
    public EmbeddingServiceApi(EmbeddingService embeddingService) {
        this.embeddingService = embeddingService;
    }

    @Override
    public void preferredChunkSize(Empty request, StreamObserver<GrpcPreferredChunkSize> responseObserver) {
        GrpcPreferredChunkSize preferredChunkSize = GrpcPreferredChunkSize.newBuilder()
                .setSize(1024)
                .build();

        responseObserver.onNext(preferredChunkSize);
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GrpcEmbeddingRequestChunk> embed(StreamObserver<GrpcEmbedding> responseObserver) {
        return new EmbeddingStreamObserver(embeddingService, ByteArraysBufferFactory.getInstance().createNew(), responseObserver);
    }


}
