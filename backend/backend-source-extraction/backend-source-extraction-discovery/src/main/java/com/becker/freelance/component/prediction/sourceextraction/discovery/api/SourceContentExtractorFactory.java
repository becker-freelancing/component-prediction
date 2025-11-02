package com.becker.freelance.component.prediction.sourceextraction.discovery.api;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public interface SourceContentExtractorFactory {

    public List<? extends SourceContentExtractorIdProvider> findAll();

    public Optional<SourceContentExtractorWithExtractionId> findAndBuffer(Supplier<InputStream> inputStream);
}
