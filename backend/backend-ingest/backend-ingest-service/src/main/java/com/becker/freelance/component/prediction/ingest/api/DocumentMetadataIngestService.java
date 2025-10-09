package com.becker.freelance.component.prediction.ingest.api;

import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;

public interface DocumentMetadataIngestService {
    public DocumentMetadata ingest(DocumentMetadata documentMetadata);
}
