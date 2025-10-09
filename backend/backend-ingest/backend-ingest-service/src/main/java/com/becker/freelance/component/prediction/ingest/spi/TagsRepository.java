package com.becker.freelance.component.prediction.ingest.spi;

import com.becker.freelance.component.prediction.ingest.domain.model.Tag;

public interface TagsRepository {

    public Tag save(Tag tag);
}
