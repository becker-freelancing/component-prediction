package com.becker.freelance.component.prediction.ingest.spi;

import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;

public interface SourceMetadataSanitizer {

    public SourceMetadata sanitize(SourceMetadata sourceMetadata);
}
