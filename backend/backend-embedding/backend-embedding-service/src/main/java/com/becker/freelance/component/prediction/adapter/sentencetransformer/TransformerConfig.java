package com.becker.freelance.component.prediction.adapter.sentencetransformer;


import ai.djl.MalformedModelException;
import ai.djl.Model;
import ai.djl.huggingface.tokenizers.HuggingFaceTokenizer;
import com.becker.freelance.component.prediction.spi.EmbeddingService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Configuration
public class TransformerConfig {

    private static final String MODEL_RESOURCE_BASE_PATH = "/models/paraphrase-multilingual-MiniLM-L12-v2/";


    private Path resolveModelPath(String resourceBasePath, String filename, String fileExtension) throws URISyntaxException, IOException {
        String resourcePath = resourceBasePath + filename + "." + fileExtension;
        URL resourceUrl = getClass().getResource(resourcePath);

        if (resourceUrl == null) {
            throw new IllegalStateException("resource not found: " + resourcePath);
        }

        if ("file".equals(resourceUrl.getProtocol())) {
            return Paths.get(resourceUrl.toURI());
        }

        if ("jar".equals(resourceUrl.getProtocol())) {
            try (InputStream is = getClass().getResourceAsStream(resourcePath)) {
                Path modelTmpFile = Files.createTempFile("embedding-model", filename + "." + fileExtension);
                Files.copy(is, modelTmpFile, StandardCopyOption.REPLACE_EXISTING);
                modelTmpFile.toFile().deleteOnExit();

                return modelTmpFile;
            }
        }

        throw new IllegalStateException("Unsupported resource protocol: " + resourceUrl.getProtocol());
    }

    private Path modelPath() {
        try {
            return resolveModelPath(MODEL_RESOURCE_BASE_PATH, "model", "onnx");
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Could not read or copy embedding model", e);
        }
    }

    private Path tokenizerPath() {
        try {
            return resolveModelPath(MODEL_RESOURCE_BASE_PATH, "tokenizer", "json");
        } catch (URISyntaxException | IOException e) {
            throw new IllegalStateException("Could not read or copy tokenizer", e);
        }
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
    public SentenceEmbeddingTranslator sentenceEmbeddingTranslator(MeanPooler meanPooler,
                                                                   L2Normalizer l2Normalizer) {
        return new SentenceEmbeddingTranslator(meanPooler, l2Normalizer);
    }

    @Bean
    public Model model() throws MalformedModelException, IOException {
        Model model = Model.newInstance("embedding-model", "OnnxRuntime");
        model.load(modelPath());
        return model;
    }

    @Bean
    public EmbeddingService embeddingService(Model model,
                                             SentenceEmbeddingTranslator translator,
                                             TokenizationService tokenizationService,
                                             TextChunker textChunker) {
        return new EmbeddingServiceImpl(model, translator, tokenizationService, textChunker);
    }
}
