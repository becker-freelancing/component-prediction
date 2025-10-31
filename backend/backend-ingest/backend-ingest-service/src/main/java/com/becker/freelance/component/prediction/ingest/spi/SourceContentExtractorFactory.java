package com.becker.freelance.component.prediction.ingest.spi;

import java.io.InputStream;
import java.util.Optional;
import java.util.function.Supplier;

public interface SourceContentExtractorFactory {

    public Optional<SourceContentExtractorWithExtractionId> findAndBuffer(Supplier<InputStream> inputStream);
}
