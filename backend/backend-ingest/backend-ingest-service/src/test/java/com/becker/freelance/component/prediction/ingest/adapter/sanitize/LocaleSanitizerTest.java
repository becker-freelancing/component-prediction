package com.becker.freelance.component.prediction.ingest.adapter.sanitize;

import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.Locale;
import com.becker.freelance.component.prediction.ingest.spi.LanguageDetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class LocaleSanitizerTest {


    LanguageDetectionService languageDetectionService;
    LocaleSanitizer localeSanitizer;

    @BeforeEach
    void setUp() {
        languageDetectionService = mock(LanguageDetectionService.class);
        localeSanitizer = new LocaleSanitizer(languageDetectionService);
    }

    @Test
    void sanitize() {
        doReturn(new Locale("de")).when(languageDetectionService).detectLanguage("Hallo");

        DocumentMetadata documentMetadata = new DocumentMetadata();
        documentMetadata.setActionDescription("Hallo");

        DocumentMetadata sanitize = localeSanitizer.sanitize(documentMetadata);

        assertEquals(new Locale("de"), sanitize.getLocale());
    }
}