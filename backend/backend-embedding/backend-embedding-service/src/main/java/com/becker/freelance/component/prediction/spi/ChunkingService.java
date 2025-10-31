package com.becker.freelance.component.prediction.spi;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public interface ChunkingService {

    public void chunkText(ChunkedTextConsumer chunkedTextConsumer) throws IOException;
}
