package com.becker.freelance.component.prediction.extraction.api;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;

import java.util.UUID;

public interface ManualInputExtractionService {
    UUID prepareNewExtraction();

    boolean supports(UUID extractionId, ByteArraysBuffer buffer);

    void extract(UUID uuid, ByteArraysBuffer buffer, ExtractionConsumer extractionConsumer);
}
