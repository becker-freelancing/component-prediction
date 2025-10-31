package com.becker.freelance.component.prediction.ingest.adapter.language;

import com.becker.freelance.component.prediction.ingest.domain.model.Locale;
import com.becker.freelance.component.prediction.language.detection.GrpcLanguageDetectionQuery;
import com.becker.freelance.component.prediction.language.detection.GrpcLanguageDetectorGrpc;
import com.becker.freelance.component.prediction.language.detection.GrpcLocale;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GrpcLanguageDetectionTest {

    private GrpcLanguageDetectorGrpc.GrpcLanguageDetectorBlockingStub stub;
    private GrpcLanguageDetection detectionService;

    @BeforeEach
    void setUp() {
        stub = mock(GrpcLanguageDetectorGrpc.GrpcLanguageDetectorBlockingStub.class);
        detectionService = new GrpcLanguageDetection(stub);
    }

    @Test
    void detectLanguage_shouldReturnLocale_whenGrpcReturnsLocale() {
        GrpcLocale grpcLocale = GrpcLocale.newBuilder().setLocale("en").build();
        when(stub.detectLanguage(any(GrpcLanguageDetectionQuery.class))).thenReturn(grpcLocale);

        Locale result = detectionService.detectLanguage("Hello world");

        assertNotNull(result);
        assertEquals("en", result.localeAbbreviation());
        verify(stub).detectLanguage(any(GrpcLanguageDetectionQuery.class));
    }

    @Test
    void detectLanguage_shouldThrowException_whenGrpcThrows() {
        when(stub.detectLanguage(any(GrpcLanguageDetectionQuery.class)))
                .thenThrow(new RuntimeException("gRPC error"));

        assertThrows(RuntimeException.class, () -> detectionService.detectLanguage("Bonjour"));
    }
}
