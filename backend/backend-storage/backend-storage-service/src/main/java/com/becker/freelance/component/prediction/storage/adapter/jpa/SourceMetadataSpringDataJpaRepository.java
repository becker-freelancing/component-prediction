package com.becker.freelance.component.prediction.storage.adapter.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SourceMetadataSpringDataJpaRepository extends JpaRepository<SourceMetadataEntity, UUID> {

    public int countByParentId(UUID parentId);
}
