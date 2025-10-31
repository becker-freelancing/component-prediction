package com.becker.freelance.component.prediction.ingest.application;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.ingest.api.IngestSourceChunkConsumer;
import com.becker.freelance.component.prediction.ingest.domain.model.IngestSourceChunk;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertSame;

class IngestSourceChunkConsumerImplTest {

    private SourceIngestService ingestService;
    private ByteArraysBuffer buffer;
    private Function<IngestSourceChunkConsumer, SourceMetadata> onCompletedFunction;
    private IngestSourceChunkConsumerImpl consumer;

    @BeforeEach
    void setUp() {
        ingestService = mock(SourceIngestService.class);
        buffer = mock(ByteArraysBuffer.class);
        onCompletedFunction = mock(Function.class);
        consumer = new IngestSourceChunkConsumerImpl(onCompletedFunction, ingestService, buffer);
    }

    @Test
    void consume_shouldBufferAndCallIngestOnNewName() {
        IngestSourceChunk chunk1 = mock(IngestSourceChunk.class);
        IngestSourceChunk chunk2 = mock(IngestSourceChunk.class);

        when(chunk1.getName()).thenReturn("file1");
        when(chunk1.getData()).thenReturn(new byte[]{1, 2, 3});
        when(chunk2.getName()).thenReturn("file1");
        when(chunk2.getData()).thenReturn(new byte[]{4, 5});

        consumer.consume(chunk1);
        consumer.consume(chunk2);

        verify(ingestService, times(1)).ingest("file1", buffer);
        verify(buffer).buffer("file1", new byte[]{1, 2, 3});
        verify(buffer).buffer("file1", new byte[]{4, 5});
    }

    @Test
    void consume_shouldCallIngestAgainWhenNameChanges() {
        IngestSourceChunk chunk1 = mock(IngestSourceChunk.class);
        IngestSourceChunk chunk2 = mock(IngestSourceChunk.class);

        when(chunk1.getName()).thenReturn("file1");
        when(chunk2.getName()).thenReturn("file2");
        when(chunk1.getData()).thenReturn(new byte[]{1});
        when(chunk2.getData()).thenReturn(new byte[]{2});

        consumer.consume(chunk1);
        consumer.consume(chunk2);

        verify(ingestService).ingest("file1", buffer);
        verify(ingestService).ingest("file2", buffer);
        verify(buffer).buffer("file1", new byte[]{1});
        verify(buffer).buffer("file2", new byte[]{2});
    }

    @Test
    void onCompleted_shouldCallIngestAndReturnResult() {
        IngestSourceChunkConsumerImpl spyConsumer = spy(consumer);
        SourceMetadata metadata = mock(SourceMetadata.class);
        doReturn(metadata).when(onCompletedFunction).apply(spyConsumer);

        // Simulate a chunk consumed
        IngestSourceChunk chunk = mock(IngestSourceChunk.class);
        when(chunk.getName()).thenReturn("file1");
        when(chunk.getData()).thenReturn(new byte[]{1});
        spyConsumer.consume(chunk);

        SourceMetadata result = spyConsumer.onCompleted();

        verify(ingestService, times(2)).ingest("file1", buffer); // once on consume, once on onCompleted
        assertSame(metadata, result);
    }
}
