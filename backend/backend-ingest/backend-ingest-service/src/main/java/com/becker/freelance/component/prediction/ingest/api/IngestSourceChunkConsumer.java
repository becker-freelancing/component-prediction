package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.ingest.domain.model.IngestSourceChunk;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;

public interface IngestSourceChunkConsumer {

    public void consume(IngestSourceChunk sourceChunk);

    SourceMetadata onCompleted();
}
