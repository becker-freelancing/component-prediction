package com.becker.freelance.component.prediction.storage.domain;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public record SourceEmbedding(UUID metadataId, float[][] embedding) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        SourceEmbedding other = (SourceEmbedding) o;
        return Objects.equals(metadataId, other.metadataId) && Arrays.deepEquals(embedding, other.embedding);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadataId, Arrays.deepHashCode(embedding));
    }
}
