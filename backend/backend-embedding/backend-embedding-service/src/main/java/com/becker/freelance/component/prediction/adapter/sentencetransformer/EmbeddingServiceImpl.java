package com.becker.freelance.component.prediction.adapter.sentencetransformer;

import ai.djl.Model;
import ai.djl.inference.Predictor;
import ai.djl.ndarray.NDList;
import ai.djl.ndarray.NDManager;
import ai.djl.translate.TranslateException;
import ai.djl.translate.Translator;
import com.becker.freelance.component.prediction.spi.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class EmbeddingServiceImpl implements EmbeddingService {


    private final Model model;
    private final Translator<NDList, float[]> translator;
    private final TokenizationService tokenizationService;
    private final TextChunkerFactory textChunkerFactory;

    public EmbeddingServiceImpl(Model model, Translator<NDList, float[]> translator, TokenizationService tokenizationService, TextChunkerFactory textChunkerFactory) {
        this.model = model;
        this.translator = translator;
        this.tokenizationService = tokenizationService;
        this.textChunkerFactory = textChunkerFactory;
    }

    @Override
    public void embed(InputStream inputStream, EmbeddingConsumer embeddingConsumer) {
        ChunkingService textChunker = textChunkerFactory.forInputStream(inputStream);

        ChunkedTextConsumerImpl chunkedTextConsumer = new ChunkedTextConsumerImpl(
                model,
                translator,
                tokenizationService,
                embeddingConsumer
        );


        try {
            textChunker.chunkText(chunkedTextConsumer);
        } catch (IOException e) {
            embeddingConsumer.onError(new EmbeddingException("Could not embed text", e));
            throw new IllegalStateException("Could not embed text", e);
        }
    }

    private static class ChunkedTextConsumerImpl implements ChunkedTextConsumer {

        private final Model model;
        private final Translator<NDList, float[]> translator;
        private final TokenizationService tokenizationService;
        private final NDManager ndManager;
        private final EmbeddingConsumer embeddingConsumer;

        public ChunkedTextConsumerImpl(Model model, Translator<NDList, float[]> translator, TokenizationService tokenizationService, EmbeddingConsumer embeddingConsumer) {
            this.model = model;
            this.translator = translator;
            this.tokenizationService = tokenizationService;
            this.ndManager = model.getNDManager();
            this.embeddingConsumer = embeddingConsumer;
        }

        @Override
        public void onCompleted() {

        }

        @Override
        public void accept(List<String> chunks) {
            List<NDList> allCombined = chunks.stream().map(chunk -> {
                NDList combined = new NDList();
                NDList encoded = tokenizationService.encodeToNDList(ndManager, chunk);
                combined.addAll(encoded);
                return combined;
            }).toList();
            try (Predictor<NDList, float[]> predictor = model.newPredictor(translator)) {
                float[][] embedding = predictor.batchPredict(allCombined).toArray(new float[0][]);
                embeddingConsumer.accept(embedding);
            } catch (TranslateException e) {
                 embeddingConsumer.onError(new EmbeddingException("Could not embed text", e));
            }

            embeddingConsumer.onCompleted();
        }
    }
}
