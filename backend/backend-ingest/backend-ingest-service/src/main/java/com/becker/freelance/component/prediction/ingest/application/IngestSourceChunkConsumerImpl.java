package com.becker.freelance.component.prediction.ingest.application;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.ingest.api.IngestSourceChunkConsumer;
import com.becker.freelance.component.prediction.ingest.domain.model.IngestSourceChunk;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;

import java.util.function.Function;

public class IngestSourceChunkConsumerImpl implements IngestSourceChunkConsumer {

    private final Function<IngestSourceChunkConsumer, SourceMetadata> onCompleted;
    private final SourceIngestService ingestService;
    private final ByteArraysBuffer byteArraysBuffer;

    private String currentName = null;

    public IngestSourceChunkConsumerImpl(Function<IngestSourceChunkConsumer, SourceMetadata> onCompleted, SourceIngestService ingestService, ByteArraysBuffer byteArraysBuffer) {
        this.onCompleted = onCompleted;
        this.ingestService = ingestService;
        this.byteArraysBuffer = byteArraysBuffer;
    }


    @Override
    public void consume(IngestSourceChunk sourceChunk) {
        String name = sourceChunk.getName();
        if (!name.equals(currentName)){
            ingestService.ingest(name, byteArraysBuffer);
            currentName = name;
        }
        byteArraysBuffer.buffer(name, sourceChunk.getData());
    }

    @Override
    public SourceMetadata onCompleted() {
        ingestService.ingest(currentName, byteArraysBuffer);
        return onCompleted.apply(this);
    }
}
