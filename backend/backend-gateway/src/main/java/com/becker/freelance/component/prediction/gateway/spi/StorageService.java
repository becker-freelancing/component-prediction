package com.becker.freelance.component.prediction.gateway.spi;

import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;

public interface StorageService {

    public DocumentMetadataDto save(DocumentMetadataDto documentMetadataDto);
}
