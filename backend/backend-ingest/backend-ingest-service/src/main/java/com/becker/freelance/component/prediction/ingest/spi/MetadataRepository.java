package com.becker.freelance.component.prediction.ingest.spi;

import com.becker.freelance.component.prediction.ingest.domain.model.DocumentEmbedding;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;

public interface MetadataRepository {

    public SourceMetadata save(SourceMetadata metadata);
}
