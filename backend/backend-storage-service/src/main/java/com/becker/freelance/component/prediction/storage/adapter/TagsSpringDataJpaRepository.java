package com.becker.freelance.component.prediction.storage.adapter;

import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface TagsSpringDataJpaRepository extends JpaRepository<TagsEntity, BigInteger> {

    public Optional<TagsEntity> findByTag(String tag);
}
