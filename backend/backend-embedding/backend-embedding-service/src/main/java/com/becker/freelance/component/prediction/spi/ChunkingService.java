package com.becker.freelance.component.prediction.spi;

import java.util.List;

public interface ChunkingService {

    public List<String> chunkText(String text);
}
