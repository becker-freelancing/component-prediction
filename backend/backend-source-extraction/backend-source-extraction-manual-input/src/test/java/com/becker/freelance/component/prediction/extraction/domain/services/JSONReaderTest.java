package com.becker.freelance.component.prediction.extraction.domain.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

public class JSONReaderTest {

    private JSONReader jsonReader;

    @BeforeEach
    void setUp() {
        jsonReader = new JSONReader();
    }

    @Test
    void testReadActionDescriptionChunks_found() throws IOException {
        String json = "{\"actionDescription\":\"Do something\",\"other\":\"value\"}";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        @SuppressWarnings("unchecked")
        Consumer<String> handler = mock(Consumer.class);

        jsonReader.readActionDescriptionChunks(input, handler);

        verify(handler, times(1)).accept("Do something");
    }

    @Test
    void testReadActionDescriptionChunks_notFound() throws IOException {
        String json = "{\"noAction\":\"none\"}";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        @SuppressWarnings("unchecked")
        Consumer<String> handler = mock(Consumer.class);

        jsonReader.readActionDescriptionChunks(input, handler);

        verify(handler, never()).accept(anyString());
    }

    @Test
    void testReadActionDescriptionChunks_emptyJson() throws IOException {
        String json = "{}";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        @SuppressWarnings("unchecked")
        Consumer<String> handler = mock(Consumer.class);

        jsonReader.readActionDescriptionChunks(input, handler);

        verify(handler, never()).accept(anyString());
    }

    @Test
    void testReadActionDescriptionChunks_invalidJson() throws IOException {
        String json = "not a json";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        @SuppressWarnings("unchecked")
        Consumer<String> handler = mock(Consumer.class);

        Assertions.assertThrows(IOException.class, () -> jsonReader.readActionDescriptionChunks(input, handler));

        verify(handler, never()).accept(anyString());
    }
}
