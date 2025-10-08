package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import com.becker.freelance.component.prediction.spi.EmbeddingException;
import com.becker.freelance.component.prediction.spi.EmbeddingService;

import java.util.List;

public class EmbeddingServiceImpl implements EmbeddingService {


    private final Model model;
    private final Translator<NDList, float[]> translator;
    private final TokenizationService tokenizationService;
    private final TextChunker textChunker;

    public EmbeddingServiceImpl(Model model, Translator<NDList, float[]> translator, TokenizationService tokenizationService, TextChunker textChunker) {
        this.model = model;
        this.translator = translator;
        this.tokenizationService = tokenizationService;
        this.textChunker = textChunker;
    }

    @Override
    public float[][] embed(String text) throws EmbeddingException {

        NDManager ndManager = model.getNDManager();
        List<String> chunks = textChunker.chunkText(text);
        List<NDList> allCombined = chunks.stream().map(chunk -> {
            NDList combined = new NDList();
            NDList encoded = tokenizationService.encodeToNDList(ndManager, chunk);
            combined.addAll(encoded);
            return combined;
        }).toList();
        try (Predictor<NDList, float[]> predictor = model.newPredictor(translator)) {
            return predictor.batchPredict(allCombined).toArray(new float[0][]);
        } catch (TranslateException e) {
            throw new EmbeddingException("Could not embed text '" + text + "'", e);
        }
    }
}
