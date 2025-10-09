package com.becker.freelance.component.prediction.ingest.adapter.sanitize;

import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.Locale;
import com.becker.freelance.component.prediction.ingest.spi.DocumentMetadataSanitizer;
import com.becker.freelance.component.prediction.ingest.spi.LanguageDetectionService;


public class LocaleSanitizer implements DocumentMetadataSanitizer {

    private final LanguageDetectionService languageDetectionService;

    public LocaleSanitizer(LanguageDetectionService languageDetectionService) {
        this.languageDetectionService = languageDetectionService;
    }

    @Override
    public DocumentMetadata sanitize(DocumentMetadata documentMetadata) {

        Locale locale = languageDetectionService.detectLanguage(documentMetadata.getActionDescription());
        documentMetadata.setLocale(locale);
        return documentMetadata;
    }
}
