package com.becker.freelance.component.prediction.gateway.spi;

import com.becker.freelance.component.prediction.gateway.api.dto.SourceMetadataDto;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public interface SourceStorageService {

    public SourceInputStreamHandler save(SourceMetadataDto sourceMetadataDto) throws ExecutionException, InterruptedException;

    public List<SourceMetadataDto> findAll();

    public SourceMetadataDto findById(UUID id);
}
