package com.becker.freelance.component.prediction.backend.query.adapter.grpc;

import com.becker.freelance.component.prediction.backend.query.spi.AppRepository;
import com.becker.freelance.component.prediction.backend.query.spi.DocumentMetadataRepository;
import com.becker.freelance.component.prediction.backend.query.spi.TagRepository;
import com.becker.freelance.component.prediction.backend.storage.ApiAppsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.ApiDocumentMetadataReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.ApiTagsReadRepositoryGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcAdapterConfig {


    @GrpcClient("backendstoragereadservice")
    private ApiDocumentMetadataReadRepositoryGrpc.ApiDocumentMetadataReadRepositoryBlockingStub metadataReadRepositoryBlockingStub;

    @GrpcClient("backendstorageappreadservice")
    private ApiAppsReadRepositoryGrpc.ApiAppsReadRepositoryBlockingStub appsReadRepositoryBlockingStub;

    @GrpcClient("backendstoragetagreadservice")
    private ApiTagsReadRepositoryGrpc.ApiTagsReadRepositoryBlockingStub tagsReadRepositoryBlockingStub;

    @Bean
    public DocumentMetadataRepository documentMetadataRepository(GrpcAdapterMapper grpcAdapterMapper) {
        return new GrpcDocumentMetadataRepository(metadataReadRepositoryBlockingStub, grpcAdapterMapper);
    }

    @Bean
    public AppRepository appRepository(GrpcAdapterMapper grpcAdapterMapper) {
        return new GrpcAppRepository(appsReadRepositoryBlockingStub, grpcAdapterMapper);
    }

    @Bean
    public TagRepository tagRepository(GrpcAdapterMapper grpcAdapterMapper) {
        return new GrpcTagRepository(tagsReadRepositoryBlockingStub, grpcAdapterMapper);
    }
}
