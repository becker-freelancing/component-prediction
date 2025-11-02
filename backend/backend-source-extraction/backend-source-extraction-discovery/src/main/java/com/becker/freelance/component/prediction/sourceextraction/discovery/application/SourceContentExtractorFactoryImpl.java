package com.becker.freelance.component.prediction.sourceextraction.discovery.application;


import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractor;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorFactory;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorIdProvider;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorWithExtractionId;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;

public class SourceContentExtractorFactoryImpl implements SourceContentExtractorFactory {

    private final List<SourceContentExtractor> extractors;

    public SourceContentExtractorFactoryImpl(List<SourceContentExtractor> extractors) {
        this.extractors = extractors;
    }

    @Override
    public List<? extends SourceContentExtractorIdProvider> findAll() {
        return extractors;
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
