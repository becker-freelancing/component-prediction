package com.becker.freelance.component.prediction.extraction.domain.services;

import org.springframework.stereotype.Service;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.json.JsonFactory;

import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

@Service
public class JSONValidator {

    public boolean isJsonObject(InputStream input) {
        JsonFactory factory = new JsonFactory();
        try (JsonParser parser = factory.createParser(input)) {
            JsonToken first = parser.nextToken();
            return first == JsonToken.START_OBJECT;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean containsActionDescription(InputStream input){
        JsonFactory factory = new JsonFactory();
        Set<String> requiredFields = Set.of("actionDescription");
        Set<String> seen = new HashSet<>();

        try (JsonParser parser = factory.createParser(input)) {
            if (parser.nextToken() != JsonToken.START_OBJECT) return false;

            while (parser.nextToken() != JsonToken.END_OBJECT) {
                String fieldName = parser.currentName();
                parser.nextToken();
                seen.add(fieldName);
            }
            return seen.containsAll(requiredFields);
        } catch (Exception e) {
            return false;
        }
    }
}
