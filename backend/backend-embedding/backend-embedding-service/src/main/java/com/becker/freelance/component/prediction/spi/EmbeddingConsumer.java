package com.becker.freelance.component.prediction.spi;

import java.util.function.Consumer;

public interface EmbeddingConsumer extends Consumer<float[][]> {

    public void onCompleted();

    void onError(EmbeddingException couldNotEmbedText);
}
