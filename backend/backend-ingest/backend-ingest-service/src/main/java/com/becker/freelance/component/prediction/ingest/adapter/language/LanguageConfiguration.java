package com.becker.freelance.component.prediction.ingest.adapter.language;

import com.becker.freelance.component.prediction.ingest.spi.LanguageDetectionService;
import com.becker.freelance.component.prediction.language.detection.GrpcLanguageDetectorGrpc;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LanguageConfiguration {

    @GrpcClient("languageDetectionService")
    private GrpcLanguageDetectorGrpc.GrpcLanguageDetectorBlockingStub languageDetectorBlockingStub;

    @Bean
    public LanguageDetectionService languageDetectionService() {
        return new GrpcLanguageDetection(languageDetectorBlockingStub);
    }
}
