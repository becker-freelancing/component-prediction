package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import com.becker.freelance.component.prediction.spi.ChunkingService;

import java.io.InputStream;

public class TextChunkerFactory {

    private final int chunkSize;
    private final int chunkOverlap;
    private final int chunkThreshold;

    public TextChunkerFactory(int chunkSize, int chunkOverlap, int chunkThreshold) {
        if (chunkSize <= chunkOverlap || chunkSize <= 0 || chunkOverlap < 0) {
            throw new IllegalArgumentException("Chunk-Parameters must be: chunkSize > chunkOverlap && chunkSize > 0 && chunkOverlap >= 0");
        }

        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.chunkThreshold = chunkThreshold;
    }

    public ChunkingService forInputStream(InputStream inputStream){
        return new TextChunker(chunkSize, chunkOverlap, chunkThreshold, inputStream);
    }
}
