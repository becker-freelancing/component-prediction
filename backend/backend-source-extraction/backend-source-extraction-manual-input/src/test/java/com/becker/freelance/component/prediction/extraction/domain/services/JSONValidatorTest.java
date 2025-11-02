package com.becker.freelance.component.prediction.extraction.domain.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.json.JsonFactory;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class JSONValidatorTest {

    private JSONValidator jsonValidator;

    @BeforeEach
    void setUp() {
        jsonValidator = new JSONValidator();
    }

    @Test
    void testIsJsonObject_validObject() {
        String json = "{\"key\":\"value\"}";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        assertTrue(jsonValidator.isJsonObject(input));
    }

    @Test
    void testIsJsonObject_invalidJson() {
        String json = "[\"not an object\"]";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        assertFalse(jsonValidator.isJsonObject(input));
    }

    @Test
    void testContainsActionDescription_present() {
        String json = "{\"actionDescription\":\"some action\",\"other\":\"data\"}";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        assertTrue(jsonValidator.containsActionDescription(input));
    }

    @Test
    void testContainsActionDescription_missing() {
        String json = "{\"noAction\":\"here\"}";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        assertFalse(jsonValidator.containsActionDescription(input));
    }

    @Test
    void testContainsActionDescription_invalidJson() {
        String json = "[\"not an object\"]";
        InputStream input = new ByteArrayInputStream(json.getBytes());
        assertFalse(jsonValidator.containsActionDescription(input));
    }
}
