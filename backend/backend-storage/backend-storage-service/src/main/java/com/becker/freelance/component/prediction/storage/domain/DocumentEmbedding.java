package com.becker.freelance.component.prediction.storage.domain;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public record DocumentEmbedding(UUID metadataId, float[][] embeddedActionDescription) {

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DocumentEmbedding embedding = (DocumentEmbedding) o;
        return Objects.equals(metadataId, embedding.metadataId) && Arrays.deepEquals(embeddedActionDescription, embedding.embeddedActionDescription);
    }

    @Override
    public int hashCode() {
        return Objects.hash(metadataId, Arrays.deepHashCode(embeddedActionDescription));
    }
}
