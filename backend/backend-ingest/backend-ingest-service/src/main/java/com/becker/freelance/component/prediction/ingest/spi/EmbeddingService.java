package com.becker.freelance.component.prediction.ingest.spi;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;

import java.util.function.Consumer;

public interface EmbeddingService {


    void embed(ByteArraysBuffer extractionBuffer, String name, Consumer<float[]> embedding);
}
