package com.becker.freelance.component.prediction.storage.adapter.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface AppsSpringDataJpaRepository extends JpaRepository<AppsEntity, UUID> {

    public Optional<AppsEntity> findByAppName(String appName);
}
