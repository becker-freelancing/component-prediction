package com.becker.freelance.component.prediction.ingest.adapter.embedding;

import com.becker.freelance.component.prediction.backend.embedding.ApiEmbeddingServiceGrpc;
import com.becker.freelance.component.prediction.ingest.spi.EmbeddingService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfiguration {

    @GrpcClient("backendembeddingservice")
    private ApiEmbeddingServiceGrpc.ApiEmbeddingServiceBlockingStub embeddingServiceBlockingStub;

    @GrpcClient("backendembeddingservice")
    private ApiEmbeddingServiceGrpc.ApiEmbeddingServiceStub embeddingServiceStub;

    @Bean
    public EmbeddingService embeddingService() {
        return new GrpcEmbeddingService(embeddingServiceStub, embeddingServiceBlockingStub);
    }
}
