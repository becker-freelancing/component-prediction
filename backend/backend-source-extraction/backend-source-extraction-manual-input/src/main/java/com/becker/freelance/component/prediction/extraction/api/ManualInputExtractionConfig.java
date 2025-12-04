package com.becker.freelance.component.prediction.extraction.api;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.ApiSourceContentExtractionServiceGrpc;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class ManualInputExtractionConfig {

    @Value("${service_id}")
    private String serviceId;


    @GrpcService
    ApiSourceContentExtractionServiceGrpc.ApiSourceContentExtractionServiceImplBase apiSourceContentExtractionServiceImplBase(
            ManualInputExtractionService manualInputExtractionService
    ){
        return new ApiManualInputExtractionService(
                manualInputExtractionService,
                serviceId
        );
    }
}
