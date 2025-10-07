package com.becker.freelance.component.prediction.storage.adapter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DocumentMetadataSpringDataJpaRepository extends JpaRepository<DocumentMetadataEntity, UUID> {

}
