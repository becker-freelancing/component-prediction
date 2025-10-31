package com.becker.freelance.component.prediction.storage.adapter.jpa;

import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.Locale;
import com.becker.freelance.component.prediction.storage.domain.SourceMetadata;
import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class SourceMetadataRepositoryImplTest {

    @Autowired
    private DocumentMetadataRepository documentMetadataRepository;

    private static void assertDefaultSaved(DefaultSaveResult result) {
        assertNotNull(result.saved().getId());
        assertEquals("de", result.saved().getLocale().abbreviation());
        assertEquals(BigInteger.ONE, result.saved().getVersion());
        assertEquals(result.createdAt(), result.saved().getCreatedAt());

        assertNotNull(result.saved().getApp().getId());
        assertEquals(result.app().getAppName(), result.saved().getApp().getAppName());

        result.saved().getTags().forEach(tag -> assertNotNull(tag.getId()));
        assertEquals(result.tags().stream().map(Tag::getTag).collect(Collectors.toSet()), result.saved().getTags().stream().map(Tag::getTag).collect(Collectors.toSet()));
    }

    @Test
    void save() {
        DefaultSaveResult result = saveDefault();

        assertDefaultSaved(result);
    }

    @Test
    void saveWithUpdate() {
        DefaultSaveResult result = saveDefault();

        assertDefaultSaved(result);

        App app = new App("test-app-2");
        Set<Tag> tags = Set.of(new Tag("t12"), new Tag("t22"));
        ZonedDateTime createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0, 0), ZoneId.of("UTC"));
        SourceMetadata sourceMetadata = new SourceMetadata(
                result.saved().getId(),
                app,
                new Locale("en"),
                BigInteger.ONE,
                createdAt,
                createdAt,
                tags
        );

        SourceMetadata saved = documentMetadataRepository.save(sourceMetadata);

        assertNotNull(saved.getId());
        assertEquals("en", saved.getLocale().abbreviation());
        assertEquals(BigInteger.ONE, saved.getVersion());
        assertEquals(createdAt, saved.getCreatedAt());

        assertNotNull(saved.getApp().getId());
        assertEquals(app.getAppName(), saved.getApp().getAppName());

        saved.getTags().forEach(tag -> assertNotNull(tag.getId()));
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), saved.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()));

    }


    @Test
    void findAllWithEmptyDb() {

        List<SourceMetadata> all = documentMetadataRepository.findAll();

        assertEquals(0, all.size());
    }

    @Test
    void findAll() {
        DefaultSaveResult saved1 = saveDefault();
        DefaultSaveResult saved2 = saveDefault();

        List<SourceMetadata> all = documentMetadataRepository.findAll();

        assertEquals(2, all.size());
        assertEquals(Stream.of(saved1, saved2).map(DefaultSaveResult::saved).map(SourceMetadata::getId).collect(Collectors.toSet()), all.stream().map(SourceMetadata::getId).collect(Collectors.toSet()));
    }

    @Test
    void findById() {
        DefaultSaveResult saved = saveDefault();
        Optional<SourceMetadata> find = documentMetadataRepository.findById(saved.saved().getId());

        assertTrue(find.isPresent());
        assertEquals(saved.saved().getId(), find.get().getId());
    }

    @Test
    void findByIdIfNotFound() {
        saveDefault();

        Optional<SourceMetadata> byId = documentMetadataRepository.findById(UUID.randomUUID());

        assertTrue(byId.isEmpty());
    }

    private DefaultSaveResult saveDefault() {
        App app = new App("test-app");
        Set<Tag> tags = Set.of(new Tag("t1"), new Tag("t2"));
        ZonedDateTime createdAt = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC"));
        SourceMetadata sourceMetadata = new SourceMetadata(
                app,
                new Locale("de"),
                BigInteger.ONE,
                createdAt,
                createdAt,
                tags
        );

        SourceMetadata saved = documentMetadataRepository.save(sourceMetadata);
        return new DefaultSaveResult(app, tags, createdAt, saved);
    }

    private record DefaultSaveResult(App app, Set<Tag> tags, ZonedDateTime createdAt,
                                     SourceMetadata saved) {
    }

}