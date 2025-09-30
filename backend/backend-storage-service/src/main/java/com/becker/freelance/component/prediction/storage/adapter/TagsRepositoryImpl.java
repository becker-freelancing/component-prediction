package com.becker.freelance.component.prediction.storage.adapter;

import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.TagsRepository;

import java.util.List;

public class TagsRepositoryImpl implements TagsRepository {

    private final TagsSpringDataJpaRepository repository;

    public TagsRepositoryImpl(TagsSpringDataJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Tag> findAll() {
        return repository.findAll().stream()
                .map(this::map)
                .toList();
    }

    @Override
    public Tag save(Tag tag) {
        TagsEntity entity = map(tag);
        TagsEntity save = repository.save(entity);
        return map(save);
    }

    private TagsEntity map(Tag tag) {
        TagsEntity tagsEntity = new TagsEntity();
        tagsEntity.setId(tag.getId());
        tagsEntity.setTag(tag.getTag());
        return tagsEntity;
    }

    private Tag map(TagsEntity tagsEntity) {
        return new Tag(tagsEntity.getId(), tagsEntity.getTag());
    }
}
