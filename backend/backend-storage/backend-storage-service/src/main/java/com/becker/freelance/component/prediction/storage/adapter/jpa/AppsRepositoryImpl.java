package com.becker.freelance.component.prediction.storage.adapter.jpa;

import com.becker.freelance.component.prediction.storage.domain.App;
import com.becker.freelance.component.prediction.storage.spi.AppRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AppsRepositoryImpl implements AppRepository {

    private final AppsSpringDataJpaRepository repository;

    @Autowired
    public AppsRepositoryImpl(AppsSpringDataJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public App save(App app) {
        AppsEntity save = repository.save(map(app));
        return map(save);
    }

    @Override
    public List<App> findAll() {
        return repository.findAll().stream()
                .map(this::map)
                .toList();
    }

    private AppsEntity map(App app) {
        AppsEntity appsEntity = new AppsEntity();
        appsEntity.setId(app.getId());
        appsEntity.setAppName(app.getAppName());
        return appsEntity;
    }

    private App map(AppsEntity entity) {
        return new App(
                entity.getId(),
                entity.getAppName()
        );
    }
}
