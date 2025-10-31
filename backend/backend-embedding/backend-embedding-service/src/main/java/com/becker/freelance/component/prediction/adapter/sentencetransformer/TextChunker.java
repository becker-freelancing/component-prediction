package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import com.becker.freelance.component.prediction.spi.ChunkedTextConsumer;
import com.becker.freelance.component.prediction.spi.ChunkingService;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Consumer;

public class TextChunker implements ChunkingService {

    private final int chunkSize;
    private final int chunkOverlap;
    private final int chunkThreshold;
    private final InputStream inputStream;

    public TextChunker(int chunkSize, int chunkOverlap, int chunkThreshold, InputStream inputStream) {
        this.inputStream = inputStream;
        if (chunkSize <= chunkOverlap || chunkSize <= 0 || chunkOverlap < 0) {
            throw new IllegalArgumentException("Chunk-Parameters must be: chunkSize > chunkOverlap && chunkSize > 0 && chunkOverlap >= 0");
        }

        this.chunkSize = chunkSize;
        this.chunkOverlap = chunkOverlap;
        this.chunkThreshold = chunkThreshold;
    }

    public void chunkText(ChunkedTextConsumer chunkedTextConsumer) throws IOException {
        if (inputStream == null) {
            chunkedTextConsumer.onCompleted();
            return;
        }

        List<String> chunks = new ArrayList<>();
        Deque<String> tokenBuffer = new ArrayDeque<>();
        int currentTokenCount = 0;

        byte[] buffer = new byte[2048];
        StringBuilder remainder = new StringBuilder();
        StringBuilder totalText = new StringBuilder();
        int totalTokensRead = 0;

        try (BufferedInputStream bis = new BufferedInputStream(inputStream)) {
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                // Bytes in String umwandeln (UTF-8)
                String textChunk = new String(buffer, 0, bytesRead, StandardCharsets.UTF_8);

                remainder.append(textChunk);



                // Letztes unvollständiges Token am Chunk-Ende behalten
                int lastWhitespace = findLastWhitespace(textChunk);
                String processable = (lastWhitespace >= 0) ? textChunk.substring(0, lastWhitespace) : "";
                remainder.setLength(0);
                if (lastWhitespace >= 0 && lastWhitespace < textChunk.length()) {
                    remainder.append(textChunk.substring(lastWhitespace)); // Rest behalten
                }

                // Tokens verarbeiten
                String[] tokens = processable.split("\\s+");

                totalTokensRead += tokens.length;
                if (totalTokensRead < chunkThreshold) {
                    totalText.append(textChunk);
                }

                for (String token : tokens) {
                    if (token.isEmpty()) continue;
                    tokenBuffer.add(token);
                    currentTokenCount++;

                    if (currentTokenCount >= chunkSize) {
                        // Chunk bilden
                        chunks.add(String.join(" ", tokenBuffer));

                        // Overlap handhaben
                        int overlapToKeep = Math.min(chunkOverlap, chunkSize);
                        while (tokenBuffer.size() > overlapToKeep) {
                            tokenBuffer.pollFirst();
                        }
                        currentTokenCount = tokenBuffer.size();
                    }
                }
            }

            // Nach dem Lesen: Rest behandeln
            if (totalTokensRead < chunkThreshold) {
                chunks.clear();
                chunks.add(totalText.toString().trim());
            } else {
                if (!remainder.isEmpty()) {
                    String[] tokens = remainder.toString().split("\\s+");
                    for (String token : tokens) {
                        if (token.isEmpty()) continue;
                        tokenBuffer.add(token);
                    }
                }
                if (!tokenBuffer.isEmpty()) {
                    chunks.add(String.join(" ", tokenBuffer));
                }
            }


        }

        chunkedTextConsumer.accept(chunks);
        chunkedTextConsumer.onCompleted();
    }

    // Hilfsfunktion: letzte Whitespace-Position finden
    private int findLastWhitespace(String s) {
        for (int i = s.length() - 1; i >= 0; i--) {
            if (Character.isWhitespace(s.charAt(i))) {
                return i;
            }
        }
        return -1;
    }
}
