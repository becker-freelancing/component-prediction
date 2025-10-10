package com.becker.freelance.component.prediction.storage.adapter.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TagsSpringDataJpaRepository extends JpaRepository<TagsEntity, UUID> {

    public Optional<TagsEntity> findByTag(String tag);
}
