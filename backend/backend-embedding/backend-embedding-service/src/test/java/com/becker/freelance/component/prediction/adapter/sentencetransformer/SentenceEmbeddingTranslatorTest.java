package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;

class SentenceEmbeddingTranslatorTest {

    SentenceEmbeddingTranslator embeddingTranslator;
    TranslatorContext translatorContext;

    @BeforeEach
    void setUp() {
        embeddingTranslator = new SentenceEmbeddingTranslator(
                new MeanPooler(),
                new L2Normalizer(true)
        );
        translatorContext = Mockito.mock(TranslatorContext.class);
    }

    @Test
    void processInput() {
        NDList list = Mockito.mock(NDList.class);

        NDList actual = embeddingTranslator.processInput(translatorContext, list);

        assertEquals(list, actual);
    }

    @Test
    void processOutputWithEmptyOutput() {

        assertThrows(IllegalStateException.class, () -> embeddingTranslator.processOutput(translatorContext, new NDList()));
    }

    @Test
    void processOutputWithToLargeOutput() {

        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray arr = manager.create(new float[]{1, 2});
            assertThrows(IllegalStateException.class, () -> embeddingTranslator.processOutput(translatorContext, new NDList(arr, arr)));
        }
    }

    @Test
    void processOutput() {

        try (NDManager manager = NDManager.newBaseManager()) {
            NDArray arr = manager.create(new float[][]{{1, 2}, {2, 3}, {2, 6}});

            float[] actual = embeddingTranslator.processOutput(translatorContext, new NDList(arr));

            assertArrayEquals(new float[]{0.41380292f, 0.9103664f}, actual);
        }
    }

}