package com.becker.freelance.component.prediction.ingest.adapter.sanitize;

import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.Locale;
import com.becker.freelance.component.prediction.ingest.spi.SourceMetadataSanitizer;
import com.becker.freelance.component.prediction.ingest.spi.LanguageDetectionService;


public class LocaleSanitizer implements SourceMetadataSanitizer {

    private final LanguageDetectionService languageDetectionService;

    public LocaleSanitizer(LanguageDetectionService languageDetectionService) {
        this.languageDetectionService = languageDetectionService;
    }

    @Override
    public SourceMetadata sanitize(SourceMetadata sourceMetadata) {

        sourceMetadata.getContentPart().ifPresent(part -> {
            Locale locale = languageDetectionService.detectLanguage(part);
            sourceMetadata.setLocale(locale);
        });
        return sourceMetadata;
    }
}
