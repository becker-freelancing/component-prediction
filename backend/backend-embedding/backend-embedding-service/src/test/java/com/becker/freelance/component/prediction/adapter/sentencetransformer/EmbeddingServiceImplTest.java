package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import com.becker.freelance.component.prediction.EmbeddingServiceStarter;
import com.becker.freelance.component.prediction.spi.EmbeddingConsumer;
import com.becker.freelance.component.prediction.spi.EmbeddingException;
import org.junit.jupiter.api.Test;
import org.opentest4j.AssertionFailedError;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EmbeddingServiceStarter.class)
class EmbeddingServiceImplTest {

    @Autowired
    EmbeddingServiceImpl embeddingService;

    @Test
    void testSimpleEmbed() throws Exception {
        String text = "Dies ist ein einfacher Testtext.";
        embeddingService.embed(new ByteArrayInputStream(text.getBytes(StandardCharsets.UTF_8)), new EmbeddingAssertion(1));

    }

    @Test
    void testChunkedEmbed() throws Exception {
        String longText = "Dies ist ein sehr langer Text, der mehrere Tokens enthält, um das Chunking-System zu testen. Wir fügen noch mehr Wörter hinzu damit die Länge die Chunk-Schwelle überschreitet. Ende.".repeat(100);


       embeddingService.embed(new ByteArrayInputStream(longText.getBytes(StandardCharsets.UTF_8)), new EmbeddingAssertion(12));
    }

    private static class EmbeddingAssertion implements EmbeddingConsumer{

        private final int expectedCount;
        private int count;

        public EmbeddingAssertion(int expectedCount) {
            this.expectedCount = expectedCount;
        }

        @Override
        public void onCompleted() {
            assertEquals(expectedCount, count);
        }

        @Override
        public void onError(EmbeddingException couldNotEmbedText) {
            throw new AssertionFailedError("No error expected", couldNotEmbedText);
        }

        @Override
        public void accept(float[][] floats) {
            count += floats.length;
            for (float[] emb : floats) {
                assertValidEmbedding(emb);
            }

        }

        void assertValidEmbedding(float[] array) {
            assertEquals(384, array.length);
            for (float f : array) {
                assertFalse(Float.isNaN(f));
                assertFalse(Float.isInfinite(f));
            }

        }
    }


}