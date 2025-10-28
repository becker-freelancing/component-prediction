package com.becker.freelance.component.prediction.ingest.adapter.sanitize;

import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.ingest.spi.DocumentMetadataSanitizer;
import com.becker.freelance.component.prediction.ingest.spi.LanguageDetectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Configuration
public class SanitizeConfiguration {

    @Bean
    public LocaleSanitizer localeSanitizer(LanguageDetectionService languageDetectionService) {
        return new LocaleSanitizer(languageDetectionService);
    }

    @Bean
    @Primary
    public DocumentMetadataSanitizer documentMetadataSanitizer(List<DocumentMetadataSanitizer> sanitizers) {
        return new SanitizeBroadcast(
                sanitizers.stream()
                        .filter(sanitizer -> !(sanitizer instanceof SanitizeBroadcast))
                        .toList()
        );
    }

    private static record SanitizeBroadcast(
            List<DocumentMetadataSanitizer> sanitizers) implements DocumentMetadataSanitizer {
        @Override
        public DocumentMetadata sanitize(DocumentMetadata documentMetadata) {
            for (DocumentMetadataSanitizer sanitizer : sanitizers()) {
                documentMetadata = sanitizer.sanitize(documentMetadata);
            }
            return documentMetadata;
        }
    }
}
