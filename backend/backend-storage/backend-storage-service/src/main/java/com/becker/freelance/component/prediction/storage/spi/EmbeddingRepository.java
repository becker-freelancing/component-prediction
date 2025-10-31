package com.becker.freelance.component.prediction.storage.spi;

import com.becker.freelance.component.prediction.storage.domain.SourceEmbedding;
import com.becker.freelance.component.prediction.storage.domain.SourceMetadata;

public interface EmbeddingRepository {

    public SourceMetadata save(SourceEmbedding embedding);
}
