package com.becker.freelance.component.prediction.sourceextraction.discovery.application;

import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractor;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorWithExtractionId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class SourceContentExtractorFactoryImplTest {

    private SourceContentExtractor extractor1;
    private SourceContentExtractor extractor2;
    private SourceContentExtractorFactoryImpl factory;
    private Supplier<InputStream> inputStreamSupplier;

    @BeforeEach
    void setUp() {
        extractor1 = mock(SourceContentExtractor.class);
        extractor2 = mock(SourceContentExtractor.class);
        inputStreamSupplier = () -> new ByteArrayInputStream("data".getBytes());
        factory = new SourceContentExtractorFactoryImpl(List.of(extractor1, extractor2), List.of(), List.of());
    }

    @Test
    void findAndBuffer_shouldReturnExtractor_whenSupportsTrue() {
        UUID id1 = UUID.randomUUID();
        when(extractor1.prepareNewExtraction()).thenReturn(id1);
        when(extractor1.supportsOrReset(id1)).thenReturn(true);

        Optional<SourceContentExtractorWithExtractionId> result = factory.findAndBuffer(inputStreamSupplier);

        assertTrue(result.isPresent());
        assertEquals(extractor1, result.get().extractor());
        assertEquals(id1, result.get().extractionId());
        verify(extractor1).buffer(eq(id1), any(InputStream.class));
        verify(extractor2, never()).prepareNewExtraction();
    }

    @Test
    void findAndBuffer_shouldReturnSecondExtractor_whenFirstDoesNotSupport() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        when(extractor1.prepareNewExtraction()).thenReturn(id1);
        when(extractor1.supportsOrReset(id1)).thenReturn(false);
        when(extractor2.prepareNewExtraction()).thenReturn(id2);
        when(extractor2.supportsOrReset(id2)).thenReturn(true);

        Optional<SourceContentExtractorWithExtractionId> result = factory.findAndBuffer(inputStreamSupplier);

        assertTrue(result.isPresent());
        assertEquals(extractor2, result.get().extractor());
        assertEquals(id2, result.get().extractionId());
        verify(extractor1).buffer(eq(id1), any(InputStream.class));
        verify(extractor2).buffer(eq(id2), any(InputStream.class));
    }

    @Test
    void findAndBuffer_shouldReturnEmpty_whenNoExtractorSupports() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        when(extractor1.prepareNewExtraction()).thenReturn(id1);
        when(extractor1.supportsOrReset(id1)).thenReturn(false);
        when(extractor2.prepareNewExtraction()).thenReturn(id2);
        when(extractor2.supportsOrReset(id2)).thenReturn(false);

        Optional<SourceContentExtractorWithExtractionId> result = factory.findAndBuffer(inputStreamSupplier);

        assertTrue(result.isEmpty());
        verify(extractor1).buffer(eq(id1), any(InputStream.class));
        verify(extractor2).buffer(eq(id2), any(InputStream.class));
    }

    @Test
    void findAndBuffer_shouldReturnEmpty_whenExtractorListIsEmpty() {
        SourceContentExtractorFactoryImpl emptyFactory = new SourceContentExtractorFactoryImpl(List.of());
        Optional<SourceContentExtractorWithExtractionId> result = emptyFactory.findAndBuffer(inputStreamSupplier);
        assertTrue(result.isEmpty());
    }
}
