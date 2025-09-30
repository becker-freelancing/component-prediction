package com.becker.freelance.component.prediction.storage.adapter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface AppsSpringDataJpaRepository extends JpaRepository<AppsEntity, BigInteger> {

    public Optional<AppsEntity> findByAppName(String appName);
}
