package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import com.becker.freelance.component.prediction.spi.ChunkedTextConsumer;
import com.becker.freelance.component.prediction.spi.ChunkingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TextChunkerTest {

    TextChunkerFactory textChunker;

    @BeforeEach
    void setUp() {
        textChunker = new TextChunkerFactory(3, 1, 5);
    }

    @Test
    void chunkTextIfToShort() throws IOException {

        String text = "Ein Hund, Ein Pferd";

        ChunkingService chunkingService = textChunker.forInputStream(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));

        chunkingService.chunkText(new ChunkAssertion(
                List.of("Ein Hund, Ein Pferd")
        ));
    }

    @Test
    void chunkText() throws IOException {
        String text = "Ein Hund, Ein Pferd, Eine Katze, Ein Esel, Ein Dieb, Ein Elefant";

        ChunkingService chunkingService = textChunker.forInputStream(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)));

        chunkingService.chunkText(new ChunkAssertion(
                List.of("Ein Hund, Ein",
                        "Ein Pferd, Eine",
                        "Eine Katze, Ein",
                        "Ein Esel, Ein",
                        "Ein Dieb, Ein",
                        "Ein Elefant")
        ));

    }

    private static class ChunkAssertion implements ChunkedTextConsumer {

        private final Queue<List<String>> expected;

        public ChunkAssertion(List<String>... expected){
            this.expected = new LinkedList<>(Arrays.asList(expected));
        }


        @Override
        public void onCompleted() {
            assertEquals(0, expected.size());
        }

        @Override
        public void accept(List<String> strings) {
            List<String> next = expected.poll();
            assertEquals(next, strings);
        }
    }

}