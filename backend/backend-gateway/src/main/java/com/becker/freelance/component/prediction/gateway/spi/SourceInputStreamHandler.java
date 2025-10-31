package com.becker.freelance.component.prediction.gateway.spi;

import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestUUID;
import com.becker.freelance.component.prediction.gateway.api.dto.SourceMetadataDto;

import java.util.UUID;

public interface SourceInputStreamHandler {

    public void nextPart(String fileName, byte[] part, int len);

    public SourceMetadataDto completed();

    public UUID getHandleSaveId();
}
