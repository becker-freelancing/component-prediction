package com.becker.freelance.component.prediction.storage.adapter;

import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import jakarta.transaction.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Transactional
public class DocumentMetadataRepositoryImpl implements DocumentMetadataRepository {

    private final DocumentMetadataSpringDataJpaRepository repository;
    private final TagsSpringDataJpaRepository tagsRepository;
    private final AppsSpringDataJpaRepository appsRepository;

    public DocumentMetadataRepositoryImpl(DocumentMetadataSpringDataJpaRepository repository, TagsSpringDataJpaRepository tagsRepository, AppsSpringDataJpaRepository appsRepository) {
        this.repository = repository;
        this.tagsRepository = tagsRepository;
        this.appsRepository = appsRepository;
    }

    @Override
    public DocumentMetadata save(DocumentMetadata metadata) {
        DocumentMetadataEntity entity = map(metadata);
        DocumentMetadataEntity save = repository.save(entity);
        return map(save);
    }

    @Override
    public List<DocumentMetadata> findAll() {
        return repository.findAll().stream()
                .map(this::map)
                .toList();
    }

    @Override
    public Optional<DocumentMetadata> findById(UUID id) {
        return repository.findById(id).map(this::map);
    }

    private DocumentMetadataEntity map(DocumentMetadata metadata) {
        DocumentMetadataEntity entity = new DocumentMetadataEntity();
        entity.setId(metadata.getId());
        AppsEntity app = map(metadata.getApp());
        app = appsRepository.save(app);
        entity.setApp(app);
        Set<TagsEntity> tags = metadata.getTags().stream().map(this::map).map(tagsRepository::save).collect(Collectors.toSet());
        entity.setTags(tags);
        entity.setInAppActionPath(metadata.getInAppActionPath());
        entity.setActionTitle(metadata.getActionTitle());
        entity.setActionDescription(metadata.getActionDescription());
        entity.setActionShortDescription(metadata.getActionShortDescription());
        entity.setLocale(metadata.getLocale());
        entity.setVersion(BigInteger.ONE);
        entity.setCreatedAt(metadata.getCreatedAt());
        return entity;
    }


    private DocumentMetadata map(DocumentMetadataEntity entity) {
        return new DocumentMetadata(
                entity.getId(),
                map(entity.getApp()),
                entity.getInAppActionPath(),
                entity.getActionTitle(),
                entity.getActionDescription(),
                entity.getActionShortDescription(),
                entity.getLocale(),
                BigInteger.ONE,
                entity.getCreatedAt(),
                entity.getTags().stream().map(this::map).collect(Collectors.toSet()));
    }

    private Tag map(TagsEntity tagsEntity) {
        return new Tag(tagsEntity.getId(), tagsEntity.getTag());
    }

    private App map(AppsEntity app) {
        return new App(app.getId(), app.getAppName());
    }

    private TagsEntity map(Tag tag) {
        return Optional.ofNullable(tag.getId())
                .flatMap(tagsRepository::findById)
                .map(persisted -> {
                    persisted.setTag(tag.getTag());
                    return persisted;
                }).orElseGet(() -> {
                    TagsEntity tagsEntity = new TagsEntity();
                    tagsEntity.setTag(tag.getTag());
                    return tagsEntity;
                });
    }

    private AppsEntity map(App app) {

        return Optional.ofNullable(app.getId())
                .flatMap(appsRepository::findById)
                .map(persisted -> {
                    persisted.setAppName(app.getAppName());
                    return persisted;
                }).orElseGet(() -> {
                    AppsEntity appsEntity = new AppsEntity();
                    appsEntity.setAppName(app.getAppName());
                    return appsEntity;
                });
    }
}
