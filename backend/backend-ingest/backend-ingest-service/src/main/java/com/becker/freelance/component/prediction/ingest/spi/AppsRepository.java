package com.becker.freelance.component.prediction.ingest.spi;

import com.becker.freelance.component.prediction.ingest.domain.model.App;

public interface AppsRepository {

    public App save(App app);
}
