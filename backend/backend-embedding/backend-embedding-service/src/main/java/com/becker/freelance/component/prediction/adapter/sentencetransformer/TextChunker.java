package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import com.becker.freelance.component.prediction.spi.ChunkingService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TextChunker implements ChunkingService {
    private final int chunkSize;
    private final int chunkOverlap;
    private final int chunkThreshold;

    public TextChunker(int chunkSize, int chunkOverlap, int chunkThreshold) {
        if (chunkSize <= chunkOverlap || chunkSize <= 0 || chunkOverlap < 0) {
            throw new IllegalArgumentException("Chunk-Parameters must be: chunkSize > chunkOverlap && chunkSize > 0 && chunkOverlap >= 0");
        }

        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.chunkThreshold = chunkThreshold;
    }

    @Override
    public List<String> chunkText(String text) {
        if (text == null || text.isEmpty()) {
            return List.of();
        }

        String[] tokens = text.split("\\s+");
        if (tokens.length <= chunkThreshold) {
            return List.of(text);
        }

        List<String> chunks = new ArrayList<>();
        int step = chunkSize - chunkOverlap;

        for (int i = 0; i < tokens.length; i += step) {
            int end = Math.min(tokens.length, i + chunkSize);
            String chunk = String.join(" ", Arrays.copyOfRange(tokens, i, end));
            chunks.add(chunk);
            if (end == tokens.length) break;
        }
        return chunks;
    }
}
