package com.becker.freelance.component.prediction.gateway.spi;

import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;

import java.util.List;
import java.util.UUID;

public interface DocumentMetadataStorageService {

    public DocumentMetadataDto save(DocumentMetadataDto documentMetadataDto);

    public List<DocumentMetadataDto> findAll();

    public DocumentMetadataDto findById(UUID id);
}
