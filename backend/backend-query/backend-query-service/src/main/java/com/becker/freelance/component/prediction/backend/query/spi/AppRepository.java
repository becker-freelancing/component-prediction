package com.becker.freelance.component.prediction.backend.query.spi;

import com.becker.freelance.component.prediction.backend.query.domain.model.App;

import java.util.List;

public interface AppRepository {
    List<App> findAll();
}
