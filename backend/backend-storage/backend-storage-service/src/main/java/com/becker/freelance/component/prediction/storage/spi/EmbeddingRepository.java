package com.becker.freelance.component.prediction.storage.spi;

import com.becker.freelance.component.prediction.storage.domain.DocumentEmbedding;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;

public interface EmbeddingRepository {

    public DocumentMetadata save(DocumentEmbedding embedding);
}
