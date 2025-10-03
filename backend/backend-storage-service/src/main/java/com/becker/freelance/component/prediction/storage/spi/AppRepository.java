package com.becker.freelance.component.prediction.storage.spi;

import com.becker.freelance.component.prediction.storage.domain.App;

import java.util.List;

public interface AppRepository {
    App save(App app);

    List<App> findAll();
}
