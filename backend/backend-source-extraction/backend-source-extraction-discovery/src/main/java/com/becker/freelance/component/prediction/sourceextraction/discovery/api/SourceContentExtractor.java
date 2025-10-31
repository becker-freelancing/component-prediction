package com.becker.freelance.component.prediction.sourceextraction.discovery.api;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;

import java.io.InputStream;
import java.util.UUID;

public interface SourceContentExtractor {

    public boolean supportsOrReset(UUID extractionId);

    public ByteArraysBuffer extract(UUID uuid);

    void buffer(UUID extractionId, InputStream inputStream);

    UUID prepareNewExtraction();
}
