package com.becker.freelance.component.prediction.ingest.adapter.embedding;

import com.becker.freelance.component.prediction.backend.embedding.ApiEmbeddingServiceGrpc;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbedding;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbeddingRequestChunk;
import com.becker.freelance.component.prediction.backend.embedding.GrpcFloatArray;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.ingest.spi.EmbeddingService;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.io.InputStream;
import java.util.List;
import java.util.function.Consumer;

public class GrpcEmbeddingService implements EmbeddingService {

    private final ApiEmbeddingServiceGrpc.ApiEmbeddingServiceStub stub;
    private final ApiEmbeddingServiceGrpc.ApiEmbeddingServiceBlockingStub blockingStub;

    public GrpcEmbeddingService(ApiEmbeddingServiceGrpc.ApiEmbeddingServiceStub stub, ApiEmbeddingServiceGrpc.ApiEmbeddingServiceBlockingStub blockingStub) {
        this.stub = stub;
        this.blockingStub = blockingStub;
    }


    @Override
    public void embed(ByteArraysBuffer extractionBuffer, String name, Consumer<float[]> embedding) {
        int bufferSize = blockingStub.preferredChunkSize(Empty.newBuilder().build()).getSize();
        EmbeddingStreamObserver embeddingStreamObserver = new EmbeddingStreamObserver(embedding);

        StreamObserver<GrpcEmbeddingRequestChunk> os = stub.embed(embeddingStreamObserver);

        byte[] buffer = new byte[bufferSize];
        int len;
        try (InputStream is = extractionBuffer.newInputStream(name)){
            while ((len = is.read(buffer)) > 0){
                os.onNext(map(buffer, len));
            }
        } catch (Exception e) {
            os.onError(e);
            throw new IllegalStateException("Could not embed", e);
        }

        os.onCompleted();
    }

    private GrpcEmbeddingRequestChunk map(byte[] buffer, int len) {
        return GrpcEmbeddingRequestChunk.newBuilder()
                .setData(ByteString.copyFrom(buffer, 0, len))
                .build();
    }



    static class EmbeddingStreamObserver implements StreamObserver<GrpcEmbedding> {

        private final Consumer<float[]> consumer;

        EmbeddingStreamObserver(Consumer<float[]> consumer) {
            this.consumer = consumer;
        }

        @Override
        public void onNext(GrpcEmbedding grpcEmbedding) {
            float[] embedding = map(grpcEmbedding.getEmbeddings());
            consumer.accept(embedding);
        }

        @Override
        public void onError(Throwable throwable) {
            throw new IllegalStateException("Could not embed", throwable);
        }

        @Override
        public void onCompleted() {

        }

        private float[] map(GrpcFloatArray array) {
            List<Float> floats = array.getArrayList();
            float[] result = new float[floats.size()];
            for (int i = 0; i < result.length; i++) {
                result[i] = floats.get(i);
            }
            return result;
        }
    }
}
