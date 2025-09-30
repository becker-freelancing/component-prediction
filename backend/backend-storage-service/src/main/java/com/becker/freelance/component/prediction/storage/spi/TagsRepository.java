package com.becker.freelance.component.prediction.storage.spi;

import com.becker.freelance.component.prediction.storage.domain.Tag;

import java.util.List;

public interface TagsRepository {

    public List<Tag> findAll();

    public Tag save(Tag tag);
}
