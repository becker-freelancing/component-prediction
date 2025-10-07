package com.becker.freelance.component.prediction.storage.spi;

import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentMetadataRepository {

    public DocumentMetadata save(DocumentMetadata metadata);

    List<DocumentMetadata> findAll();

    Optional<DocumentMetadata> findById(UUID id);
}
