package com.becker.freelance.component.prediction.extraction.application;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.extraction.api.ExtractionConsumer;
import com.becker.freelance.component.prediction.extraction.domain.services.JSONReader;
import com.becker.freelance.component.prediction.extraction.domain.services.JSONValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ManualInputExtractionServiceImplTest {

    private JSONValidator jsonValidator;
    private JSONReader jsonReader;
    private ByteArraysBuffer buffer;
    private ExtractionConsumer extractionConsumer;
    private ManualInputExtractionServiceImpl service;

    @BeforeEach
    void setUp() {
        jsonValidator = mock(JSONValidator.class);
        jsonReader = mock(JSONReader.class);
        buffer = mock(ByteArraysBuffer.class);
        extractionConsumer = mock(ExtractionConsumer.class);
        service = new ManualInputExtractionServiceImpl(jsonValidator, jsonReader);
    }

    @Test
    void testPrepareNewExtraction_returnsUUID() {
        UUID result = service.prepareNewExtraction();
        assertNotNull(result);
    }

    @Test
    void testSupports_trueWhenJsonValidAndContainsActionDescription() {
        UUID id = UUID.randomUUID();
        InputStream stream = new ByteArrayInputStream("{}".getBytes());
        when(buffer.newInputStream(id.toString())).thenReturn(stream);
        when(jsonValidator.isJsonObject(any())).thenReturn(true);
        when(jsonValidator.containsActionDescription(any())).thenReturn(true);

        boolean result = service.supports(id, buffer);
        assertTrue(result);
    }

    @Test
    void testSupports_falseWhenInvalidJson() {
        UUID id = UUID.randomUUID();
        InputStream stream = new ByteArrayInputStream("{}".getBytes());
        when(buffer.newInputStream(id.toString())).thenReturn(stream);
        when(jsonValidator.isJsonObject(any())).thenReturn(false);

        boolean result = service.supports(id, buffer);
        assertFalse(result);
    }

    @Test
    void testExtract_successful() throws IOException {
        UUID id = UUID.randomUUID();
        InputStream stream = new ByteArrayInputStream("{\"actionDescription\":\"test\"}".getBytes());
        when(buffer.newInputStream(id.toString())).thenReturn(stream);

        service.extract(id, buffer, extractionConsumer);

        verify(jsonReader, times(1)).readActionDescriptionChunks(any(), eq(extractionConsumer));
        verify(extractionConsumer, times(1)).onCompleted();
        verify(extractionConsumer, never()).onError(any());
    }

    @Test
    void testExtract_withIOException_callsOnError() throws IOException {
        UUID id = UUID.randomUUID();
        InputStream stream = new ByteArrayInputStream("{\"actionDescription\":\"test\"}".getBytes());
        when(buffer.newInputStream(id.toString())).thenReturn(stream);
        doThrow(new IOException("test")).when(jsonReader).readActionDescriptionChunks(any(), eq(extractionConsumer));

        service.extract(id, buffer, extractionConsumer);

        verify(extractionConsumer, times(1)).onError(any(IOException.class));
        verify(extractionConsumer, times(1)).onCompleted();
    }
}
