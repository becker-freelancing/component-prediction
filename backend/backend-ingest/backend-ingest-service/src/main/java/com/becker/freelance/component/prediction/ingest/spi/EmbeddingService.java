package com.becker.freelance.component.prediction.ingest.spi;

public interface EmbeddingService {

    public float[][] embedText(String text);
}
