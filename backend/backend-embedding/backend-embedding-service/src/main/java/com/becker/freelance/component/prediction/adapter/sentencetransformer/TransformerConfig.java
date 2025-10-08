package com.becker.freelance.component.prediction.adapter.sentencetransformer;


import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import com.becker.freelance.component.prediction.spi.EmbeddingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class TransformerConfig {

    private Path modelDir() {
        try {
            return Paths.get(TransformerConfig.class.getResource("/models/paraphrase-multilingual-MiniLM-L12-v2").toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Could not determine path to model directory", e);
        }
    }

    private Path modelPath() {
        return modelDir().resolve("model.onnx");
    }

    private Path tokenizerPath() {
        return modelDir().resolve("tokenizer.json");
    }

    @Bean
    public HuggingFaceTokenizer huggingFaceTokenizer() throws IOException {
        return HuggingFaceTokenizer.newInstance(tokenizerPath());
    }

    @Bean
    public L2Normalizer l2Normalizer(@Value("${embedding.use_l2_normalization}") boolean useL2Normalization) {
        return new L2Normalizer(useL2Normalization);
    }

    @Bean
    public MeanPooler meanPooler() {
        return new MeanPooler();
    }

    @Bean
    public TextChunker textChunker(@Value("${embedding.chunk_size}") int chunkSize,
                                   @Value("${embedding.chunk_overlap}") int chunkOverlap,
                                   @Value("${embedding.chunk_threshold}") int chunkThreshold) {
        return new TextChunker(chunkSize, chunkOverlap, chunkThreshold);
    }

    @Bean
    public TokenMapper tokenMapper(Model model) {
        return new ModelTokenMapper(model);
    }

    @Bean
    public TokenizationService tokenizationService(HuggingFaceTokenizer huggingFaceTokenizer,
                                                   TokenMapper tokenMapper) {
        return new TokenizationService(huggingFaceTokenizer, tokenMapper);
    }

    @Bean
    public SentenceEmbeddingTranslator sentenceEmbeddingTranslator(TokenizationService tokenizationService,
                                                                   TextChunker textChunker,
                                                                   MeanPooler meanPooler,
                                                                   L2Normalizer l2Normalizer) {
        return new SentenceEmbeddingTranslator(tokenizationService, textChunker, meanPooler, l2Normalizer);
    }

    @Bean
    public Model model() throws MalformedModelException, IOException {
        Model model = Model.newInstance("embedding-model", "OnnxRuntime");
        model.load(modelPath());
        return model;
    }

    @Bean
    public EmbeddingService embeddingService(Model model,
                                             SentenceEmbeddingTranslator translator) {
        return new EmbeddingServiceImpl(model, translator);
    }
}
