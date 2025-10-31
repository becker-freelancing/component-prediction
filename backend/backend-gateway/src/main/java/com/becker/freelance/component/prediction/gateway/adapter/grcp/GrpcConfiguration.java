package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.ApiAppsIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.ApiSourceIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.ApiTagsIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.ApiAppsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.ApiSourceMetadataReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.ApiTagsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.gateway.spi.AppStorageService;
import com.becker.freelance.component.prediction.gateway.spi.SourceStorageService;
import com.becker.freelance.component.prediction.gateway.spi.TagsStorageService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfiguration {


    @GrpcClient("backendingestservice")
    private ApiSourceIngestRepositoryGrpc.ApiSourceIngestRepositoryStub sourceIngestRepositoryBlockingStub;

    @GrpcClient("backendappingestservice")
    private ApiAppsIngestRepositoryGrpc.ApiAppsIngestRepositoryBlockingStub appsIngestRepositoryBlockingStub;

    @GrpcClient("backendtagingestservice")
    private ApiTagsIngestRepositoryGrpc.ApiTagsIngestRepositoryBlockingStub tagsIngestRepositoryBlockingStub;

    @GrpcClient("backendreadservice")
    private ApiSourceMetadataReadRepositoryGrpc.ApiSourceMetadataReadRepositoryBlockingStub metadataReadRepositoryBlockingStub;

    @GrpcClient("backendappreadservice")
    private ApiAppsReadRepositoryGrpc.ApiAppsReadRepositoryBlockingStub appsReadRepositoryBlockingStub;

    @GrpcClient("backendtagreadservice")
    private ApiTagsReadRepositoryGrpc.ApiTagsReadRepositoryBlockingStub tagsReadRepositoryBlockingStub;

    @Bean
    public SourceStorageService documentMetadataStorageService() {
        return new GrpcSourceStorageService(sourceIngestRepositoryBlockingStub, metadataReadRepositoryBlockingStub);
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
