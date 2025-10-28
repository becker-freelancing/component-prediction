package com.becker.freelance.component.prediction.backend.query.spi;

import com.becker.freelance.component.prediction.backend.query.domain.model.DocumentMetadata;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentMetadataRepository {
    List<DocumentMetadata> findAll();

    Optional<DocumentMetadata> findById(UUID id);
}
