package com.becker.freelance.component.prediction.storage.adapter;

import com.becker.freelance.component.prediction.storage.spi.AppRepository;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import com.becker.freelance.component.prediction.storage.spi.TagsRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AdapterConfig {

    @Bean
    public TagsRepository tagsRepository(TagsSpringDataJpaRepository tagsSpringDataJpaRepository) {
        return new TagsRepositoryImpl(tagsSpringDataJpaRepository);
    }

    @Bean
    public DocumentMetadataRepository documentMetadataRepository(
            DocumentMetadataSpringDataJpaRepository metadataSpringDataJpaRepository,
            AppsSpringDataJpaRepository appsSpringDataJpaRepository,
            TagsSpringDataJpaRepository tagsSpringDataJpaRepository
    ) {
        return new DocumentMetadataRepositoryImpl(
                metadataSpringDataJpaRepository,
                tagsSpringDataJpaRepository,
                appsSpringDataJpaRepository
        );
    }

    @Bean
    public AppRepository appRepository(
            AppsSpringDataJpaRepository appsSpringDataJpaRepository
    ) {
        return new AppsRepositoryImpl(appsSpringDataJpaRepository);
    }
}
