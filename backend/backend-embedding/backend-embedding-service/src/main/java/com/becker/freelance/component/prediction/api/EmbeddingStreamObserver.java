package com.becker.freelance.component.prediction.api;

import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbedding;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbeddingRequestChunk;
import com.becker.freelance.component.prediction.backend.embedding.GrpcFloatArray;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.spi.EmbeddingConsumer;
import com.becker.freelance.component.prediction.spi.EmbeddingException;
import com.becker.freelance.component.prediction.spi.EmbeddingService;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class EmbeddingStreamObserver implements StreamObserver<GrpcEmbeddingRequestChunk> {


    private final EmbeddingService embeddingService;
    private final ByteArraysBuffer buffer;
    private final StreamObserver<GrpcEmbedding> responseObserver;
    private final String embeddingId;

    public EmbeddingStreamObserver(EmbeddingService embeddingService, ByteArraysBuffer buffer, StreamObserver<GrpcEmbedding> responseObserver) {
        this.embeddingService = embeddingService;
        this.buffer = buffer;
        this.responseObserver = responseObserver;
        this.embeddingId = UUID.randomUUID().toString();
    }

    @Override
    public void onNext(GrpcEmbeddingRequestChunk grpcEmbeddingRequestChunk) {
        byte[] data = grpcEmbeddingRequestChunk.getData().toByteArray();
        buffer.buffer(embeddingId, data);
    }

    @Override
    public void onError(Throwable throwable) {
        buffer.clearBuffer(embeddingId);
    }

    @Override
    public void onCompleted() {
        embeddingService.embed(buffer.newInputStream(embeddingId), new EmbeddingConsumerImpl(responseObserver));
        buffer.clearBuffer(embeddingId);
    }

    private static class EmbeddingConsumerImpl implements EmbeddingConsumer {

        private final StreamObserver<GrpcEmbedding> responseObserver;

        public EmbeddingConsumerImpl(StreamObserver<GrpcEmbedding> responseObserver) {
            this.responseObserver = responseObserver;
        }

        @Override
        public void accept(float[][] embeddings) {
            for (float[] embedding : embeddings) {
                responseObserver.onNext(map(embedding));
            }
        }

        @Override
        public void onCompleted() {
            responseObserver.onCompleted();
        }

        @Override
        public void onError(EmbeddingException couldNotEmbedText) {
            responseObserver.onError(couldNotEmbedText);
        }

        private GrpcEmbedding map(float[] embedding) {
            GrpcFloatArray.Builder builder = GrpcFloatArray.newBuilder();
            for (float f : embedding) {
                builder.addArray(f);
            }
            return GrpcEmbedding.newBuilder().setEmbeddings(builder.build()).build();
        }
    }
}
