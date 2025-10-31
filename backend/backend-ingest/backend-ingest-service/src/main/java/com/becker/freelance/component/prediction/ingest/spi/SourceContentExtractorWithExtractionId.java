package com.becker.freelance.component.prediction.ingest.spi;

import java.util.UUID;

public record SourceContentExtractorWithExtractionId(SourceContentExtractor extractor, UUID extractionId) {
}
