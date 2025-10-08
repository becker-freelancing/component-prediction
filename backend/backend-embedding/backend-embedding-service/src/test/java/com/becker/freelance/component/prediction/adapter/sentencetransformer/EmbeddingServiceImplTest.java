package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EmbeddingServiceImplTest {

    @Autowired
    EmbeddingServiceImpl embeddingService;

    @Test
    void testSimpleEmbed() throws Exception {
        String text = "Dies ist ein einfacher Testtext.";
        float[][] embeddings = embeddingService.embed(text);

        assertNotNull(embeddings);
        assertEquals(1, embeddings.length);
        assertValidEmbedding(embeddings[0]);
    }

    @Test
    void testChunkedEmbed() throws Exception {
        String longText = "Dies ist ein sehr langer Text, der mehrere Tokens enthält, um das Chunking-System zu testen. Wir fügen noch mehr Wörter hinzu damit die Länge die Chunk-Schwelle überschreitet. Ende.".repeat(100);


        float[][] embeddings = embeddingService.embed(longText);

        assertNotNull(embeddings);
        assertEquals(12, embeddings.length);
        for (float[] embedding : embeddings) {
            assertValidEmbedding(embedding);
        }

    }

    void assertValidEmbedding(float[] array) {
        assertTrue(array.length > 0);
        for (float f : array) {
            assertFalse(Float.isNaN(f));
            assertFalse(Float.isInfinite(f));
        }

    }

}