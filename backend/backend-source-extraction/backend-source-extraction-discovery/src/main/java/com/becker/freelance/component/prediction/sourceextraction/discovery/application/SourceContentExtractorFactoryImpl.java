package com.becker.freelance.component.prediction.sourceextraction.discovery.application;


import com.becker.freelance.component.prediction.sourceextraction.discovery.api.*;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class SourceContentExtractorFactoryImpl implements SourceContentExtractorFactory {

    private final List<SourceContentExtractor> extractors;
    private final List<SourceContentExtractorLabelProvider> labelProviders;
    private final List<SourceContentExtractorConfigProvider> configProviders;

    public SourceContentExtractorFactoryImpl(List<SourceContentExtractor> extractors, List<SourceContentExtractorLabelProvider> labelProviders, List<SourceContentExtractorConfigProvider> configProviders) {
        this.extractors = extractors;
        this.labelProviders = labelProviders;
        this.configProviders = configProviders;
    }

    @Override
    public List<? extends SourceContentExtractorIdProvider> findAllIdProvider() {
        return extractors;
    }

    @Override
    public List<? extends SourceContentExtractorConfigProvider> findAllConfigProvider() {
        return configProviders;
    }

    @Override
    public List<? extends SourceContentExtractorLabelProvider> findAllLabelProvider() {
        return labelProviders;
    }

    @Override
    public Optional<SourceContentExtractorWithExtractionId> findAndBuffer(Supplier<InputStream> inputStream) {

        for (SourceContentExtractor extractor : extractors) {
            UUID extractionId = extractor.prepareNewExtraction();
            extractor.buffer(extractionId, inputStream.get());
            if (extractor.supportsOrReset(extractionId)){
                return Optional.of(new SourceContentExtractorWithExtractionId(extractor, extractionId));
            }
        }

        return Optional.empty();
    }
}
