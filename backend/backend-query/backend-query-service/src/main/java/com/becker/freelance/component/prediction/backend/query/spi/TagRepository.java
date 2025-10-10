package com.becker.freelance.component.prediction.backend.query.spi;

import com.becker.freelance.component.prediction.backend.query.domain.model.Tag;

import java.util.List;

public interface TagRepository {
    List<Tag> findAll();
}
