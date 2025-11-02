package com.becker.freelance.component.prediction.extraction.application;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.extraction.api.ExtractionConsumer;
import com.becker.freelance.component.prediction.extraction.api.ManualInputExtractionService;
import com.becker.freelance.component.prediction.extraction.domain.services.JSONReader;
import com.becker.freelance.component.prediction.extraction.domain.services.JSONValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.UUID;

@Service
public class ManualInputExtractionServiceImpl implements ManualInputExtractionService {

    private final JSONValidator jsonValidator;
    private final JSONReader jsonReader;

    @Autowired
    public ManualInputExtractionServiceImpl(JSONValidator jsonValidator, JSONReader jsonReader) {
        this.jsonValidator = jsonValidator;
        this.jsonReader = jsonReader;
    }

    @Override
    public UUID prepareNewExtraction() {
        return UUID.randomUUID();
    }

    @Override
    public boolean supports(UUID extractionId, ByteArraysBuffer buffer) {
        return jsonValidator.isJsonObject(buffer.newInputStream(extractionId.toString())) &&
                jsonValidator.containsActionDescription(buffer.newInputStream(extractionId.toString()));
    }

    @Override
    public void extract(UUID uuid, ByteArraysBuffer buffer, ExtractionConsumer extractionConsumer) {
        try {
            jsonReader.readActionDescriptionChunks(
                    buffer.newInputStream(uuid.toString()),
                    extractionConsumer
            );
        } catch (IOException e) {
            extractionConsumer.onError(e);
        }
        extractionConsumer.onCompleted();
    }
}
