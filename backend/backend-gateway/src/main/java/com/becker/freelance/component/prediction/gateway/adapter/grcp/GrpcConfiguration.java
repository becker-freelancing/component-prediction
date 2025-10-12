package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.ApiAppsIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.ApiDocumentMetadataIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.ApiTagsIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.ApiAppsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.ApiDocumentMetadataReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.ApiTagsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.gateway.spi.AppStorageService;
import com.becker.freelance.component.prediction.gateway.spi.DocumentMetadataStorageService;
import com.becker.freelance.component.prediction.gateway.spi.TagsStorageService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfiguration {


    @GrpcClient("backendingestservice")
    private ApiDocumentMetadataIngestRepositoryGrpc.ApiDocumentMetadataIngestRepositoryBlockingStub metadataIngestRepositoryBlockingStub;

    @GrpcClient("backendappingestservice")
    private ApiAppsIngestRepositoryGrpc.ApiAppsIngestRepositoryBlockingStub appsIngestRepositoryBlockingStub;

    @GrpcClient("backendtagingestservice")
    private ApiTagsIngestRepositoryGrpc.ApiTagsIngestRepositoryBlockingStub tagsIngestRepositoryBlockingStub;

    @GrpcClient("backendreadservice")
    private ApiDocumentMetadataReadRepositoryGrpc.ApiDocumentMetadataReadRepositoryBlockingStub metadataReadRepositoryBlockingStub;

    @GrpcClient("backendappreadservice")
    private ApiAppsReadRepositoryGrpc.ApiAppsReadRepositoryBlockingStub appsReadRepositoryBlockingStub;

    @GrpcClient("backendtagreadservice")
    private ApiTagsReadRepositoryGrpc.ApiTagsReadRepositoryBlockingStub tagsReadRepositoryBlockingStub;

    @Bean
    public DocumentMetadataStorageService documentMetadataStorageService() {
        return new GrpcDocumentMetadataStorageService(metadataIngestRepositoryBlockingStub, metadataReadRepositoryBlockingStub);
    }

    @Bean
    public AppStorageService appStorageService() {
        return new GrpcAppStorageService(appsIngestRepositoryBlockingStub, appsReadRepositoryBlockingStub);
    }

    @Bean
    public TagsStorageService tagsStorageService() {
        return new GrpcTagsStorageService(tagsIngestRepositoryBlockingStub, tagsReadRepositoryBlockingStub);
    }
}
