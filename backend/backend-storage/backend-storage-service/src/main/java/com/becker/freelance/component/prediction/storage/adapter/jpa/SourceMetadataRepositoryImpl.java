package com.becker.freelance.component.prediction.storage.adapter.jpa;

import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.Locale;
import com.becker.freelance.component.prediction.storage.domain.SourceMetadata;
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
public class SourceMetadataRepositoryImpl implements DocumentMetadataRepository {

    private final SourceMetadataSpringDataJpaRepository repository;
    private final TagsSpringDataJpaRepository tagsRepository;
    private final AppsSpringDataJpaRepository appsRepository;

    public SourceMetadataRepositoryImpl(SourceMetadataSpringDataJpaRepository repository, TagsSpringDataJpaRepository tagsRepository, AppsSpringDataJpaRepository appsRepository) {
        this.repository = repository;
        this.tagsRepository = tagsRepository;
        this.appsRepository = appsRepository;
    }

    @Override
    public SourceMetadata save(SourceMetadata metadata) {
        SourceMetadataEntity entity = map(metadata);
        SourceMetadataEntity save = repository.save(entity);
        return map(save);
    }

    @Override
    public List<SourceMetadata> findAll() {
        return repository.findAll().stream()
                .map(this::map)
                .toList();
    }

    @Override
    public Optional<SourceMetadata> findById(UUID id) {
        return repository.findById(id).map(this::map);
    }

    private SourceMetadataEntity map(SourceMetadata metadata) {
        SourceMetadataEntity entity = new SourceMetadataEntity();
        entity.setId(metadata.getId());
        AppsEntity app = map(metadata.getApp());
        app = appsRepository.save(app);
        entity.setApp(app);
        Set<TagsEntity> tags = metadata.getTags().stream().map(this::map).map(tagsRepository::save).collect(Collectors.toSet());
        entity.setTags(tags);
        entity.setLocale(Optional.ofNullable(metadata.getLocale()).map(Locale::abbreviation).orElse(null));
        entity.setVersion(Optional.ofNullable(metadata.getVersion()).map(v -> v.add(BigInteger.ONE)).orElse(BigInteger.ONE));
        entity.setCreatedAt(metadata.getCreatedAt());
        entity.setLastModifiedAt(metadata.getLastModifiedAt());
        entity.setFileName(metadata.getFileName().orElse(null));
        entity.setParent(metadata.getParent().map(this::map).orElse(null));

        return entity;
    }


    private SourceMetadata map(SourceMetadataEntity entity) {
        return new SourceMetadata(
                entity.getId(),
                map(entity.getApp()),
                entity.getLocale() == null ? null : new Locale(entity.getLocale()),
                BigInteger.ONE,
                entity.getCreatedAt(),
                entity.getLastModifiedAt(),
                entity.getTags().stream().map(this::map).collect(Collectors.toSet()),
                entity.getFileName(),
                Optional.ofNullable(entity.getParent()).map(this::map).orElse(null),
                repository.countByParentId(entity.getId()) > 0
                );
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
