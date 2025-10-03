package com.becker.freelance.component.prediction.storage.adapter;

import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.spi.AppRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class AppsRepositoryImplTest {

    @Autowired
    private AppRepository appRepository;

    App saveDefault() {
        return appRepository.save(new App("app"));
    }

    @Test
    void save() {
        App saved = saveDefault();

        assertNotNull(saved.getId());
        assertEquals("app", saved.getAppName());
    }

    @Test
    void saveWithUpdate() {
        App saved = saveDefault();

        App app = new App(
                saved.getId(),
                "new-name"
        );

        App updated = appRepository.save(app);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("new-name", updated.getAppName());
    }

    @Test
    void findAllWithEmptyDb() {

        List<App> all = appRepository.findAll();

        assertEquals(0, all.size());
    }

    @Test
    void findAll() {
        App save = saveDefault();
        App save1 = saveDefault();

        List<App> all = appRepository.findAll();

        assertEquals(List.of(save.getId(), save1.getId()), all.stream().map(App::getId).toList());
        assertEquals(List.of(save.getAppName(), save1.getAppName()), all.stream().map(App::getAppName).toList());
    }
}