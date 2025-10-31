package com.becker.freelance.component.prediction.backend.query.spi;

import com.becker.freelance.component.prediction.backend.query.domain.model.SourceMetadata;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SourceMetadataRepository {
    List<SourceMetadata> findAll();

    Optional<SourceMetadata> findById(UUID id);
}
