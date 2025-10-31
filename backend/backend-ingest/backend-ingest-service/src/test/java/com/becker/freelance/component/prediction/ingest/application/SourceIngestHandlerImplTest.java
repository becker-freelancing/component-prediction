package com.becker.freelance.component.prediction.ingest.application;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBufferFactory;
import com.becker.freelance.component.prediction.ingest.api.IngestSourceChunkConsumer;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.ingest.spi.*;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.InvocationTargetException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SourceIngestHandlerImplTest {

    private SourceMetadataSanitizer metadataSanitizer;
    private MetadataRepository metadataRepository;
    private ByteArraysBufferFactory bufferFactory;
    private SourceContentExtractorFactory extractorFactory;
    private EmbeddingRepository embeddingRepository;
    private EmbeddingService embeddingService;

    private SourceIngestHandlerImpl handler;

    @BeforeEach
    void setUp() {
        metadataSanitizer = mock(SourceMetadataSanitizer.class);
        metadataRepository = mock(MetadataRepository.class);
        bufferFactory = mock(ByteArraysBufferFactory.class);
        extractorFactory = mock(SourceContentExtractorFactory.class);
        embeddingRepository = mock(EmbeddingRepository.class);
        embeddingService = mock(EmbeddingService.class);

        ByteArraysBuffer buffer = mock(ByteArraysBuffer.class);
        when(bufferFactory.createNew()).thenReturn(buffer);

        handler = new SourceIngestHandlerImpl(
                metadataSanitizer,
                metadataRepository,
                extractorFactory,
                embeddingRepository,
                embeddingService
        );
    }

    @Test
    void prepareIngest_shouldReturnUUIDAndRegisterConsumer() {
        SourceMetadata metadata = new SourceMetadata();
        SourceMetadata sanitized = new SourceMetadata();
        SourceMetadata saved = new SourceMetadata();

        when(metadataSanitizer.sanitize(metadata)).thenReturn(sanitized);
        when(metadataRepository.save(sanitized)).thenReturn(saved);

        UUID ingestId = handler.prepareIngest(metadata);

        assertNotNull(ingestId);
        IngestSourceChunkConsumer consumer = handler.getForIngestId(ingestId);
        assertNotNull(consumer);
    }

    @Test
    void getForIngestId_shouldReturnNullForUnknownId() {
        UUID unknownId = UUID.randomUUID();
        assertNull(handler.getForIngestId(unknownId));
    }

    @Test
    void completed_shouldSaveAndRemoveMetadata() throws InvocationTargetException, IllegalAccessException {
        SourceMetadata metadata = new SourceMetadata();
        SourceMetadata sanitized = new SourceMetadata();
        SourceMetadata saved = new SourceMetadata();

        when(metadataSanitizer.sanitize(metadata)).thenReturn(sanitized);
        when(metadataRepository.save(sanitized)).thenReturn(saved);

        UUID ingestId = handler.prepareIngest(metadata);
        IngestSourceChunkConsumer consumer = handler.getForIngestId(ingestId);

        // simulate completion
        SourceMetadata result = handler.completed(consumer);

        assertNotNull(result);
        assertEquals(saved, result);
        assertNull(handler.getForIngestId(ingestId));
    }
}
