package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import com.becker.freelance.component.prediction.spi.EmbeddingException;
import com.becker.freelance.component.prediction.spi.EmbeddingService;

public class EmbeddingServiceImpl implements EmbeddingService {


    private final Model model;
    private final Translator<String, float[][]> translator;

    public EmbeddingServiceImpl(Model model, Translator<String, float[][]> translator) {
        this.model = model;
        this.translator = translator;
    }

    public float[][] embed(String text) throws EmbeddingException {
        try (Predictor<String, float[][]> predictor = model.newPredictor(translator)) {
            return predictor.predict(text);
        } catch (TranslateException e) {
            throw new EmbeddingException("Could not embed text '" + text + "'", e);
        }
    }
}
