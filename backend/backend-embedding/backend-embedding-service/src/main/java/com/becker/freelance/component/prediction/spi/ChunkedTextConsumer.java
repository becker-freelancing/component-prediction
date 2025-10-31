package com.becker.freelance.component.prediction.spi;

import java.util.List;
import java.util.function.Consumer;

public interface ChunkedTextConsumer extends Consumer<List<String>> {

    public void onCompleted();
}
