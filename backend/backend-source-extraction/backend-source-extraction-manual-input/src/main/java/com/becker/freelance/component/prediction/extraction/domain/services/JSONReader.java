package com.becker.freelance.component.prediction.extraction.domain.services;

import org.springframework.stereotype.Service;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.json.JsonFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Consumer;

@Service
public class JSONReader {

    public void readActionDescriptionChunks(InputStream input, Consumer<String> handler) throws IOException {
        JsonFactory factory = JsonFactory.builder().build();

        try (JsonParser parser = factory.createParser(input)) {
            while (parser.nextToken() != null) {
                if (parser.currentToken() == JsonToken.PROPERTY_NAME &&
                        "actionDescription".equals(parser.currentName())) {

                    JsonToken token = parser.nextToken();

                    if (token == JsonToken.VALUE_STRING) {
                        String string = parser.getString();
                        handler.accept(string);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            throw new IOException("Could not read object", e);
        }
    }
}
