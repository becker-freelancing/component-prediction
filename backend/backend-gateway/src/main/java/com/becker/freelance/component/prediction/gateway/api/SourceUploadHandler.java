package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.SourceMetadataDto;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface SourceUploadHandler {
    UUID prepareSave(SourceMetadataDto sourceMetadataDto);

    SourceMetadataDto handleSave(UUID uploadId, InputStream inputStream) throws IOException, ExecutionException, InterruptedException;
}
