package com.becker.freelance.component.prediction.spi;

import com.becker.freelance.component.prediction.api.EmbeddingStreamObserver;

import java.io.InputStream;
import java.util.UUID;

public interface EmbeddingService {

    void embed(InputStream inputStream, EmbeddingConsumer embeddingConsumer);
}
