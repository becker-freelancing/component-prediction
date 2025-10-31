package com.becker.freelance.component.prediction.sourceextraction.discovery.application;

import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractor;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ExtractionConfig {

    @Bean
    SourceContentExtractorDiscovery sourceContentExtractorDiscovery(@Value("${sourceextraction.content_extractor_service_prefix}") String contentExtractorServicePrefix,
                                                                    @Value("${sourceextraction.content_extractor_service_grpc_port}") int contentExtractorServiceGrpcPort
                                                                    ){
        return new SourceContentExtractorDiscovery(contentExtractorServicePrefix, contentExtractorServiceGrpcPort);
    }

    @Bean
    List<SourceContentExtractor> sourceContentExtractors(SourceContentExtractorDiscovery sourceContentExtractorDiscovery){
        return sourceContentExtractorDiscovery.findAll();
    }

    @Bean
    SourceContentExtractorFactory sourceContentExtractorFactory(List<SourceContentExtractor> sourceContentExtractors){
        return new SourceContentExtractorFactoryImpl(sourceContentExtractors);
    }
}
