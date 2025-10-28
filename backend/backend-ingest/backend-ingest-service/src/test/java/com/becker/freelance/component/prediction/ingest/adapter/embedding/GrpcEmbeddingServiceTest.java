package com.becker.freelance.component.prediction.ingest.adapter.embedding;

import com.becker.freelance.component.prediction.backend.embedding.ApiEmbeddingServiceGrpc;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbedding;
import com.becker.freelance.component.prediction.backend.embedding.GrpcEmbeddingRequest;
import com.becker.freelance.component.prediction.backend.embedding.GrpcFloatArray;
import com.becker.freelance.component.prediction.ingest.domain.model.DocumentEmbedding;
import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class GrpcEmbeddingServiceTest {

    ApiEmbeddingServiceGrpc.ApiEmbeddingServiceBlockingStub stub;
    GrpcEmbeddingService service;

    @BeforeEach
    void setUp() {
        stub = mock(ApiEmbeddingServiceGrpc.ApiEmbeddingServiceBlockingStub.class);
        service = new GrpcEmbeddingService(stub);
    }

    @Test
    void embed() {
        doReturn(GrpcEmbedding.newBuilder().addAllEmbeddings(List.of(
                GrpcFloatArray.newBuilder().addAllArray(List.of(1f, 2f)).build(),
                GrpcFloatArray.newBuilder().addAllArray(List.of(2f, 3f)).build())
        ).build()).when(stub).embed(GrpcEmbeddingRequest.newBuilder().setText("Hello").build());


        DocumentMetadata metadata = mock(DocumentMetadata.class);
        doReturn("Hello").when(metadata).getActionDescription();

        DocumentEmbedding embedding = service.embed(metadata);

        assertArrayEquals(new float[][]{{1f, 2f}, {2f, 3f}}, embedding.embeddedActionDescription());
    }

}