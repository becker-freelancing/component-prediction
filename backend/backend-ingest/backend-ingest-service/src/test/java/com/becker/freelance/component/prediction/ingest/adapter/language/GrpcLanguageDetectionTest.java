package com.becker.freelance.component.prediction.ingest.adapter.language;

import com.becker.freelance.component.prediction.ingest.domain.model.Locale;
import com.becker.freelance.component.prediction.language.detection.GrpcLanguageDetectionQuery;
import com.becker.freelance.component.prediction.language.detection.GrpcLanguageDetectorGrpc;
import com.becker.freelance.component.prediction.language.detection.GrpcLocale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class GrpcLanguageDetectionTest {

    GrpcLanguageDetectorGrpc.GrpcLanguageDetectorBlockingStub stub;
    GrpcLanguageDetection detection;

    @BeforeEach
    void setUp() {
        stub = mock(GrpcLanguageDetectorGrpc.GrpcLanguageDetectorBlockingStub.class);
        detection = new GrpcLanguageDetection(stub);
    }

    @Test
    void detectLanguage() {
        doReturn(GrpcLocale.newBuilder().setLocale("de").build()).when(stub).detectLanguage(GrpcLanguageDetectionQuery.newBuilder().setQuery("Hallo").build());

        Locale locale = detection.detectLanguage("Hallo");

        assertEquals("de", locale.localeAbbreviation());
    }

}