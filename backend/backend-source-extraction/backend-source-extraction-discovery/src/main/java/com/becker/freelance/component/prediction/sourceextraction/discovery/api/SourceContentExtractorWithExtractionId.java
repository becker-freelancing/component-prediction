package com.becker.freelance.component.prediction.sourceextraction.discovery.api;

import java.util.UUID;

public record SourceContentExtractorWithExtractionId(SourceContentExtractor extractor, UUID extractionId) {
}
