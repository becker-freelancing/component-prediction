package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextChunkerTest {

    TextChunker textChunker;

    @BeforeEach
    void setUp() {
        textChunker = new TextChunker(3, 1, 5);
    }

    @Test
    void chunkTextIfToShort() {
        List<String> chunked = textChunker.chunkText("Ein Hund, Ein Pferd");

        assertEquals(List.of("Ein Hund, Ein Pferd"), chunked);
    }

    @Test
    void chunkText() {
        List<String> chunked = textChunker.chunkText("Ein Hund, Ein Pferd, Eine Katze, Ein Esel, Ein Dieb, Ein Elefant");

        assertEquals(List.of("Ein Hund, Ein",
                "Ein Pferd, Eine",
                "Eine Katze, Ein",
                "Ein Esel, Ein",
                "Ein Dieb, Ein",
                "Ein Elefant"), chunked);
    }

}