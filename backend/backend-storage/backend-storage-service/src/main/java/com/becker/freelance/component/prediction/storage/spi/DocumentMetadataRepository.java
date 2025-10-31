package com.becker.freelance.component.prediction.storage.spi;

import com.becker.freelance.component.prediction.storage.domain.SourceMetadata;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DocumentMetadataRepository {

    public SourceMetadata save(SourceMetadata metadata);

    List<SourceMetadata> findAll();

    Optional<SourceMetadata> findById(UUID id);
}
