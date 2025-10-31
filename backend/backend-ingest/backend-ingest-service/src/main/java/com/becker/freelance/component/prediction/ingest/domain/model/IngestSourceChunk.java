package com.becker.freelance.component.prediction.ingest.domain.model;

import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

public class IngestSourceChunk {
    private final UUID ingestId;
    private final String name;
    private final byte[] data;

    public IngestSourceChunk(UUID ingestId, String name, byte[] data) {
        this.ingestId = ingestId;
        this.name = name;
        this.data = data;
    }

    public UUID getIngestId() {
        return ingestId;
    }

    public String getName() {
        return name;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public String toString() {
        return "IngestSourceChunk{" +
                "ingestId=" + ingestId +
                ", fileName='" + name + '\'' +
                ", data=" + Arrays.toString(data) +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        IngestSourceChunk that = (IngestSourceChunk) o;
        return Objects.equals(ingestId, that.ingestId) && Objects.equals(name, that.name) && Objects.deepEquals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingestId, name, Arrays.hashCode(data));
    }
}
