package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDArray;
import ai.djl.ndarray.NDArrays;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.Translator;
import ai.djl.translate.TranslatorContext;

import java.util.ArrayList;
import java.util.List;

public class SentenceEmbeddingTranslator implements Translator<String, float[][]> {

    private final TokenizationService tokenizer;
    private final TextChunker chunker;
    private final MeanPooler pooler;
    private final L2Normalizer normalizer;

    public SentenceEmbeddingTranslator(TokenizationService tokenizer, TextChunker chunker, MeanPooler pooler, L2Normalizer normalizer) {
        this.tokenizer = tokenizer;
        this.chunker = chunker;
        this.pooler = pooler;
        this.normalizer = normalizer;
    }

    @Override
    public NDList processInput(TranslatorContext ctx, String input) {
        NDManager manager = ctx.getNDManager();
        List<String> chunks = chunker.chunkText(input);

        if (chunks.isEmpty()) {
            chunks.add(input);
        }
// 1
//        // Tokenisiere alle Chunks und füge sie zu einer einzigen NDList zusammen
//        NDList combined = new NDList();
//        for (String chunk : chunks) {
//            NDList encoded = tokenizer.encodeToNDList(manager, chunk);
//            combined.addAll(encoded); // fügt alle NDArray-Elemente hinzu
//        }
//
//        return combined;
        List<NDArray> tokenArrays = new ArrayList<>();
        for (String chunk : chunks) {
            NDList encoded = tokenizer.encodeToNDList(manager, chunk);
            // Angenommen, jedes encoded enthält nur ein NDArray pro Chunk
            tokenArrays.add(encoded.singletonOrThrow());
        }

        // Alle NDArray entlang der Batch-Dimension zusammenführen
        NDArray combinedArray = NDArrays.concat(new NDList(tokenArrays), 0);

        return new NDList(combinedArray);
    }

    @Override
    public float[][] processOutput(TranslatorContext ctx, NDList output) {
        NDManager manager = ctx.getNDManager();

        List<float[]> embeddings = new ArrayList<>();
        for (NDArray array : output) {
            NDArray pooled = pooler.meanPool(new NDList(array));
            NDArray normalized = normalizer.normalizeIfEnabled(manager, pooled);
            embeddings.add(normalized.toFloatArray());
        }
        return embeddings.toArray(new float[0][]);
    }

    // Hilfsmethode zum Chunk-basierten Inferenz-Workflow
    public List<float[]> embedText(Model model, String text) throws Exception {
        List<String> chunks = chunker.chunkText(text);
        if (chunks.isEmpty()) chunks.add(text);

        List<float[]> embeddings = new ArrayList<>();

        try (Predictor<String, float[][]> predictor = model.newPredictor(this)) {
            for (String chunk : chunks) {
                float[][] result = predictor.predict(chunk);
                embeddings.add(result[0]);
            }
        }
        return embeddings;
    }
}
