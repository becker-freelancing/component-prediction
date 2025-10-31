package com.becker.freelance.component.prediction.ingest.application;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.ingest.spi.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

import static org.mockito.Mockito.*;

class SourceIngestServiceImplTest {

    private SourceContentExtractorFactory extractorFactory;
    private EmbeddingRepository embeddingRepository;
    private EmbeddingService embeddingService;
    private ToChildMetadataFunction nameToChildMetadataFunction;
    private ByteArraysBuffer byteArraysBuffer;

    private SourceIngestServiceImpl service;

    @BeforeEach
    void setUp() {
        extractorFactory = mock(SourceContentExtractorFactory.class);
        embeddingRepository = mock(EmbeddingRepository.class);
        embeddingService = mock(EmbeddingService.class);
        nameToChildMetadataFunction = mock(ToChildMetadataFunction.class);
        byteArraysBuffer = mock(ByteArraysBuffer.class);

        service = new SourceIngestServiceImpl(extractorFactory, embeddingRepository, embeddingService, nameToChildMetadataFunction);
    }

    @Test
    void ingest_shouldCallExtractorAndEmbedding() {
        String name = "file.txt";
        SourceContentExtractorWithExtractionId extractorWithId = mock(SourceContentExtractorWithExtractionId.class);
        SourceContentExtractor extractor = mock(SourceContentExtractor.class);
        ByteArraysBuffer extractionBuffer = mock(ByteArraysBuffer.class);
        SourceMetadata metadata = new SourceMetadata();

        UUID id = UUID.randomUUID();
        when(byteArraysBuffer.newInputStream(name)).thenReturn(new ByteArrayInputStream("test".getBytes()));
        when(extractorFactory.findAndBuffer(any())).thenReturn(Optional.of(extractorWithId));
        when(extractorWithId.extractor()).thenReturn(extractor);
        when(extractorWithId.extractionId()).thenReturn(id);
        when(extractor.extract(id)).thenReturn(extractionBuffer);
        when(extractionBuffer.readNBytes(name, 1024)).thenReturn("content".getBytes());
        when(nameToChildMetadataFunction.apply(any())).thenReturn(metadata);

        doAnswer(invocationOnMock -> {
            Consumer consumer = invocationOnMock.getArgument(2, Consumer.class);
            consumer.accept(new float[0]);
            return null;
        }).when(embeddingService).embed(any(), any(), any());

        service.ingest(name, byteArraysBuffer);

        verify(embeddingService).embed(eq(extractionBuffer), eq(name), any());
        verify(embeddingRepository).save(eq(metadata), any());
    }

    @Test
    void ingest_shouldThrowIfExtractorNotFound() {
        String name = "file.txt";
        when(byteArraysBuffer.newInputStream(name)).thenReturn(new ByteArrayInputStream("test".getBytes()));
        when(extractorFactory.findAndBuffer(any())).thenReturn(Optional.empty());

        try {
            service.ingest(name, byteArraysBuffer);
        } catch (IllegalStateException e) {
            assert e.getMessage().contains("Could not extract content for source " + name);
        }
    }
}
