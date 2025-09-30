package com.becker.freelance.component.prediction.storage.adapter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;
import java.util.UUID;

public interface DocumentMetadataSpringDataJpaRepository extends JpaRepository<DocumentMetadataEntity, BigInteger> {
    Optional<DocumentMetadataEntity> findByDocumentId(UUID documentId);
}
