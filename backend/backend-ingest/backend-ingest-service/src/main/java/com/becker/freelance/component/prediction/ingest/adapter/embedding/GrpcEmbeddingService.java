package com.becker.freelance.component.prediction.ingest.adapter.embedding;

import com.becker.freelance.component.prediction.backend.embedding.ApiEmbeddingServiceGrpc;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbedding;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbeddingRequest;
import com.becker.freelance.component.prediction.backend.embedding.GrpcFloatArray;
import com.becker.freelance.component.prediction.ingest.spi.EmbeddingService;

import java.util.List;

public class GrpcEmbeddingService implements EmbeddingService {

    private final ApiEmbeddingServiceGrpc.ApiEmbeddingServiceBlockingStub stub;

    public GrpcEmbeddingService(ApiEmbeddingServiceGrpc.ApiEmbeddingServiceBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public float[][] embedText(String text) {
        GrpcEmbedding embed = stub.embed(map(text));
        return map(embed);
    }

    private float[][] map(GrpcEmbedding embed) {
        List<float[]> embeddingsList = embed.getEmbeddingsList().stream()
                .map(this::map)
                .toList();

        float[][] embedding = new float[embeddingsList.size()][];
        for (int i = 0; i < embedding.length; i++) {
            embedding[i] = embeddingsList.get(i);
        }
        return embedding;
    }

    private float[] map(GrpcFloatArray array) {
        List<Float> floats = array.getArrayList();
        float[] result = new float[floats.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = floats.get(i);
        }
        return result;
    }

    private GrpcEmbeddingRequest map(String text) {
        return GrpcEmbeddingRequest.newBuilder()
                .setText(text)
                .build();
    }
}
