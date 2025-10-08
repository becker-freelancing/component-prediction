package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDList;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

public class SentenceEmbeddingTranslator implements Translator<NDList, float[]> {

    private final MeanPooler pooler;
    private final L2Normalizer normalizer;

    public SentenceEmbeddingTranslator(MeanPooler pooler, L2Normalizer normalizer) {
        this.pooler = pooler;
        this.normalizer = normalizer;
    }

    @Override
    public NDList processInput(TranslatorContext ctx, NDList input) {
        return input;
    }

    @Override
    public float[] processOutput(TranslatorContext ctx, NDList output) {
        if (output.size() != 1) {
            throw new IllegalStateException("Expected exactly 1 output, but was " + output.size());
        }

        NDArray out = output.get(0);
        NDArray pooled = pooler.meanPool(out);
        NDArray normalized = normalizer.normalizeIfEnabled(pooled);

        return normalized.toFloatArray();
    }

}
