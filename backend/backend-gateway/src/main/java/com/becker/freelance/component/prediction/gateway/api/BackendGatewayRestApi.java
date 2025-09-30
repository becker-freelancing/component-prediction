package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/api/documents")
public class BackendGatewayRestApi {

    private final StorageService storageService;

    @Autowired
    public BackendGatewayRestApi(StorageService storageService) {
        this.storageService = storageService;
    }

    @PutMapping("/metadata")
    public DocumentMetadataDto save(DocumentMetadataDto documentMetadataDto) {
        return storageService.save(documentMetadataDto);
    }


}
