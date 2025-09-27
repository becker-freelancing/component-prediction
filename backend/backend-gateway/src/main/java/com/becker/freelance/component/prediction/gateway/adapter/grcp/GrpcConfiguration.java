package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.BackendStorageServiceApiGrpc;
import com.becker.freelance.component.prediction.gateway.spi.StorageService;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GrpcConfiguration {


    @GrpcClient("backendStorageService")
    private BackendStorageServiceApiGrpc.BackendStorageServiceApiBlockingStub stub;

    @Bean
    public StorageService storageService() {
        return new GrpcStorageService(stub);
    }
}
