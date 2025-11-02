package com.becker.freelance.component.prediction.extraction.api;

import java.util.function.Consumer;

public interface ExtractionConsumer extends Consumer<String> {

    public void onCompleted();

    public void onError(Throwable e);
}
