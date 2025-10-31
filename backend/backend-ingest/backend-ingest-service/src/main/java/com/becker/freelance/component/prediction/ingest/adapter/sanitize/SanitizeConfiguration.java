package com.becker.freelance.component.prediction.ingest.adapter.sanitize;

import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.ingest.spi.SourceMetadataSanitizer;
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
    public SourceMetadataSanitizer documentMetadataSanitizer(List<SourceMetadataSanitizer> sanitizers) {
        return new SanitizeBroadcast(
                sanitizers.stream()
                        .filter(sanitizer -> !(sanitizer instanceof SanitizeBroadcast))
                        .toList()
        );
    }

    private static record SanitizeBroadcast(
            List<SourceMetadataSanitizer> sanitizers) implements SourceMetadataSanitizer {
        @Override
        public SourceMetadata sanitize(SourceMetadata sourceMetadata) {
            for (SourceMetadataSanitizer sanitizer : sanitizers()) {
                sourceMetadata = sanitizer.sanitize(sourceMetadata);
            }
            return sourceMetadata;
        }
    }
}
