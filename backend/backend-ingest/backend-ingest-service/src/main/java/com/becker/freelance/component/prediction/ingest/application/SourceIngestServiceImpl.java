package com.becker.freelance.component.prediction.ingest.application;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.ingest.spi.*;

import java.util.Optional;

public class SourceIngestServiceImpl implements SourceIngestService{


    private final SourceContentExtractorFactory sourceContentExtractorFactory;
    private final EmbeddingRepository embeddingRepository;
    private final EmbeddingService embeddingService;
    private final ToChildMetadataFunction nameToChildMetadataFunction;

    public SourceIngestServiceImpl(SourceContentExtractorFactory sourceContentExtractorFactory, EmbeddingRepository embeddingRepository, EmbeddingService embeddingService, ToChildMetadataFunction nameToChildMetadataFunction) {
        this.sourceContentExtractorFactory = sourceContentExtractorFactory;
        this.embeddingRepository = embeddingRepository;
        this.embeddingService = embeddingService;
        this.nameToChildMetadataFunction = nameToChildMetadataFunction;
    }

    @Override
    public void ingest(String name, ByteArraysBuffer byteArraysBuffer) {
        Optional<SourceContentExtractorWithExtractionId> sourceExtractor = sourceContentExtractorFactory.findAndBuffer(() -> byteArraysBuffer.newInputStream(name));

        SourceContentExtractorWithExtractionId sourceContentExtractor = sourceExtractor.orElseThrow(() -> new IllegalStateException("Could not extract content for source " + name));

        ByteArraysBuffer extractionBuffer = sourceContentExtractor.extractor().extract(sourceContentExtractor.extractionId());

        SourceMetadata metadata = nameToChildMetadataFunction.apply(new ToChildMetadataFunction.Param(name, new String(extractionBuffer.readNBytes(name, 1024))));
        embeddingService.embed(extractionBuffer, name, embedding -> embeddingRepository.save(metadata, embedding));
    }
}
