package com.becker.freelance.component.prediction.storage.adapter.jpa;

import com.becker.freelance.component.prediction.storage.domain.Tag;
import com.becker.freelance.component.prediction.storage.spi.TagsRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class TagsRepositoryImplTest {

    @Autowired
    private TagsRepository tagsRepository;

    @Test
    void findAll() {
        Set<String> tags = Set.of("t1", "t2");
        tags.stream().map(Tag::new).forEach(tagsRepository::save);

        List<Tag> all = tagsRepository.findAll();

        all.forEach(tag -> assertNotNull(tag.getId()));
        assertEquals(tags, all.stream().map(Tag::getTag).collect(Collectors.toSet()));
    }

    @Test
    void save() {
        Tag tag = new Tag("t1");

        Tag save = tagsRepository.save(tag);

        assertNotNull(save.getId());
        assertEquals("t1", save.getTag());
    }

    @Test
    void saveAndUpdate() {
        Tag tag = new Tag("t1");

        Tag save = tagsRepository.save(tag);

        assertNotNull(save.getId());
        assertEquals("t1", save.getTag());

        save.setTag("t2");

        Tag updated = tagsRepository.save(save);

        assertEquals(save.getId(), updated.getId());
        assertEquals("t2", updated.getTag());
    }

}