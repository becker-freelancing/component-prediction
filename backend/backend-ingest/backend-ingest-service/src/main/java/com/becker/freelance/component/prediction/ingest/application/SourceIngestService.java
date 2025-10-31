package com.becker.freelance.component.prediction.ingest.application;


import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;

public interface SourceIngestService {
    void ingest(String name, ByteArraysBuffer byteArraysBuffer);
}
