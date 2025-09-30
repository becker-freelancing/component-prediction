package com.becker.freelance.component.prediction.storage.adapter;

import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DocumentMetadataRepositoryImplTest {

    @Autowired
    private DocumentMetadataRepository documentMetadataRepository;

    private static void assertDefaultSaved(DefaultSaveResult result) {
        assertNotNull(result.saved().getId());
        assertEquals(result.documentId(), result.saved().getDocumentId());
        assertEquals("in-app", result.saved().getInAppActionPath());
        assertEquals("title", result.saved().getActionTitle());
        assertEquals("description", result.saved().getActionDescription());
        assertEquals("short-description", result.saved().getActionShortDescription());
        assertEquals("de", result.saved().getLocale());
        assertEquals(BigInteger.ONE, result.saved().getVersion());
        assertEquals(result.createdAt(), result.saved().getCreatedAt());

        assertNotNull(result.saved().getApp().getId());
        assertEquals(result.app().getAppName(), result.saved().getApp().getAppName());

        result.saved().getTags().stream().forEach(tag -> assertNotNull(tag.getId()));
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
        UUID documentId = UUID.randomUUID();
        ZonedDateTime createdAt = ZonedDateTime.of(LocalDateTime.of(2021, 1, 1, 0, 0, 0), ZoneId.of("UTC"));
        DocumentMetadata documentMetadata = new DocumentMetadata(
                result.saved().getId(),
                documentId,
                app,
                "in-app2",
                "title2",
                "description2",
                "short-description2",
                "en",
                BigInteger.ONE,
                createdAt,
                tags
        );

        DocumentMetadata saved = documentMetadataRepository.save(documentMetadata);

        assertNotNull(saved.getId());
        assertEquals(documentId, saved.getDocumentId());
        assertEquals("in-app2", saved.getInAppActionPath());
        assertEquals("title2", saved.getActionTitle());
        assertEquals("description2", saved.getActionDescription());
        assertEquals("short-description2", saved.getActionShortDescription());
        assertEquals("en", saved.getLocale());
        assertEquals(BigInteger.ONE, saved.getVersion());
        assertEquals(createdAt, saved.getCreatedAt());

        assertNotNull(saved.getApp().getId());
        assertEquals(app.getAppName(), saved.getApp().getAppName());

        saved.getTags().stream().forEach(tag -> assertNotNull(tag.getId()));
        assertEquals(tags.stream().map(Tag::getTag).collect(Collectors.toSet()), saved.getTags().stream().map(Tag::getTag).collect(Collectors.toSet()));

    }

    @Test
    void findByRelatedDocumentId() {
        DefaultSaveResult result = saveDefault();

        assertDefaultSaved(result);

        Optional<DocumentMetadata> byRelatedDocumentId = documentMetadataRepository.findByRelatedDocumentId(result.documentId());

        assertTrue(byRelatedDocumentId.isPresent());
        assertDefaultSaved(new DefaultSaveResult(
                result.app(),
                result.tags(),
                result.documentId(),
                result.createdAt(),
                byRelatedDocumentId.get()
        ));
    }

    private DefaultSaveResult saveDefault() {
        App app = new App("test-app");
        Set<Tag> tags = Set.of(new Tag("t1"), new Tag("t2"));
        UUID documentId = UUID.randomUUID();
        ZonedDateTime createdAt = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC"));
        DocumentMetadata documentMetadata = new DocumentMetadata(
                documentId,
                app,
                "in-app",
                "title",
                "description",
                "short-description",
                "de",
                BigInteger.ONE,
                createdAt,
                tags
        );

        DocumentMetadata saved = documentMetadataRepository.save(documentMetadata);
        return new DefaultSaveResult(app, tags, documentId, createdAt, saved);
    }

    private record DefaultSaveResult(App app, Set<Tag> tags, UUID documentId, ZonedDateTime createdAt,
                                     DocumentMetadata saved) {
    }

}