package com.becker.freelance.component.prediction.ingest.application;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBufferFactory;
import com.becker.freelance.component.prediction.ingest.api.IngestSourceChunkConsumer;
import com.becker.freelance.component.prediction.ingest.api.SourceIngestHandler;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.ingest.spi.*;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class SourceIngestHandlerImpl implements SourceIngestHandler {

    private final SourceMetadataSanitizer metadataSanitizer;
    private final MetadataRepository metadataRepository;
    private final ByteArraysBufferFactory byteArraysBufferFactory;
    private final SourceContentExtractorFactory sourceContentExtractorFactory;
    private final EmbeddingRepository embeddingRepository;
    private final EmbeddingService embeddingService;

    private final Map<UUID, IngestSourceChunkConsumer> sourceChunkConsumers = new ConcurrentHashMap<>();
    private final Map<IngestSourceChunkConsumer, UUID> ids = new ConcurrentHashMap<>();
    private final Map<UUID, SourceMetadata> metadatas = new ConcurrentHashMap<>();

    @Autowired
    public SourceIngestHandlerImpl(SourceMetadataSanitizer metadataSanitizer, MetadataRepository metadataRepository, SourceContentExtractorFactory sourceContentExtractorFactory, EmbeddingRepository embeddingRepository, EmbeddingService embeddingService) {
        this.metadataSanitizer = metadataSanitizer;
        this.metadataRepository = metadataRepository;
        this.byteArraysBufferFactory = ByteArraysBufferFactory.getInstance();
        this.sourceContentExtractorFactory = sourceContentExtractorFactory;
        this.embeddingRepository = embeddingRepository;
        this.embeddingService = embeddingService;
    }


    @Override
    public UUID prepareIngest(SourceMetadata metadata) {
        UUID ingestId = UUID.randomUUID();

        SourceMetadata saved = save(metadata);

        SourceIngestService sourceIngestService = new SourceIngestServiceImpl(
                sourceContentExtractorFactory,
                embeddingRepository,
                embeddingService,
                param -> getMetadata(param, saved));
        IngestSourceChunkConsumerImpl ingestSourceChunkConsumer = new IngestSourceChunkConsumerImpl(
                this::completed,
                sourceIngestService,
                byteArraysBufferFactory.createNew());

        ids.put(ingestSourceChunkConsumer, ingestId);
        sourceChunkConsumers.put(ingestId, ingestSourceChunkConsumer);
        metadatas.put(ingestId, metadata);

        return ingestId;
    }

    private SourceMetadata getMetadata(ToChildMetadataFunction.Param param, SourceMetadata parent) {
        SourceMetadata child = parent.cloneAsChild();
        child.setFileName(param.name());
        child.setContentPart(param.contentPart());
        return save(child);
    }

    @Override
    public IngestSourceChunkConsumer getForIngestId(UUID ingestId) {
        return sourceChunkConsumers.get(ingestId);
    }

    protected SourceMetadata completed(IngestSourceChunkConsumer ingestSourceChunkConsumer){
        UUID uuid = ids.remove(ingestSourceChunkConsumer);
        SourceMetadata metadata = metadatas.remove(uuid);
        sourceChunkConsumers.remove(uuid);

        return save(metadata);
    }

    private SourceMetadata save(SourceMetadata metadata) {
        SourceMetadata sanitize = metadataSanitizer.sanitize(metadata);
        return metadataRepository.save(sanitize);
    }

}
