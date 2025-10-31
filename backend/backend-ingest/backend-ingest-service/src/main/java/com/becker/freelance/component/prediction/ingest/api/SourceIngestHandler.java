package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;

import java.util.UUID;

public interface SourceIngestHandler {

    public UUID prepareIngest(SourceMetadata metadata);

    public IngestSourceChunkConsumer getForIngestId(UUID ingestId);
}
