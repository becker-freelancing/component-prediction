package com.becker.freelance.component.prediction.api;

import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbedding;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbeddingRequest;
import com.becker.freelance.component.prediction.backend.embedding.GrpcFloatArray;
import org.springframework.stereotype.Service;

@Service
public class GrpcMapper {

    public String map(GrpcEmbeddingRequest request) {
        return request.getText();
    }

    public GrpcEmbedding map(float[][] embedding) {
        GrpcEmbedding.Builder builder = GrpcEmbedding.newBuilder();
        for (float[] floats : embedding) {
            builder.addEmbeddings(map(floats));
        }
        return builder.build();
    }

    private GrpcFloatArray map(float[] floats) {
        GrpcFloatArray.Builder builder = GrpcFloatArray.newBuilder();
        for (float f : floats) {
            builder.addArray(f);
        }
        return builder.build();
    }
}
