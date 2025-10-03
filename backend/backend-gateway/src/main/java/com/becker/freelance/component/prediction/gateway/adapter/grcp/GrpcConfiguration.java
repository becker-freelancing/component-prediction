package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.ApiAppsRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.ApiDocumentMetadataRepositoryGrpc;
import com.becker.freelance.component.prediction.gateway.spi.AppStorageService;
import com.becker.freelance.component.prediction.gateway.spi.DocumentMetadataStorageService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfiguration {


    @GrpcClient("backendStorageService")
    private ApiDocumentMetadataRepositoryGrpc.ApiDocumentMetadataRepositoryBlockingStub stub;

    @GrpcClient("backendAppStorageService")
    private ApiAppsRepositoryGrpc.ApiAppsRepositoryBlockingStub appStub;

    @Bean
    public DocumentMetadataStorageService documentMetadataStorageService() {
        return new GrpcDocumentMetadataStorageService(stub);
    }

    @Bean
    public AppStorageService appStorageService() {
        return new GrpcAppStorageService(appStub);
    }
}
