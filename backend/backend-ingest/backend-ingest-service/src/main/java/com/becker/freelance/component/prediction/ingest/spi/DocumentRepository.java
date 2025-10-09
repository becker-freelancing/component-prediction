package com.becker.freelance.component.prediction.ingest.spi;

import com.becker.freelance.component.prediction.ingest.domain.model.DocumentEmbedding;
import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;

public interface DocumentRepository {

    public DocumentMetadata save(DocumentMetadata metadata, DocumentEmbedding embedding);
}
