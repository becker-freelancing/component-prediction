package com.becker.freelance.component.prediction.ingest.adapter.sanitize;

import com.becker.freelance.component.prediction.ingest.domain.model.Locale;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.ingest.spi.LanguageDetectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class LocaleSanitizerTest {

    private LanguageDetectionService languageDetectionService;
    private LocaleSanitizer sanitizer;

    @BeforeEach
    void setUp() {
        languageDetectionService = mock(LanguageDetectionService.class);
        sanitizer = new LocaleSanitizer(languageDetectionService);
    }

    @Test
    void sanitize_shouldDetectLanguageAndSetLocale() {
        SourceMetadata metadata = new SourceMetadata();
        metadata.setContentPart("some text");

        Locale detectedLocale = new Locale("en");
        when(languageDetectionService.detectLanguage("some text")).thenReturn(detectedLocale);

        SourceMetadata result = sanitizer.sanitize(metadata);

        verify(languageDetectionService).detectLanguage("some text");
        assertEquals(detectedLocale, result.getLocale());
    }

    @Test
    void sanitize_shouldNotCallDetectionIfContentPartEmpty() {
        SourceMetadata metadata = new SourceMetadata();

        SourceMetadata result = sanitizer.sanitize(metadata);

        verifyNoInteractions(languageDetectionService);
        assertNull(result.getLocale());
    }
}
