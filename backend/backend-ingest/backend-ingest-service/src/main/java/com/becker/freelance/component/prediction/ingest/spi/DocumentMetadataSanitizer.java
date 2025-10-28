package com.becker.freelance.component.prediction.ingest.spi;

import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;

public interface DocumentMetadataSanitizer {

    public DocumentMetadata sanitize(DocumentMetadata documentMetadata);
}
