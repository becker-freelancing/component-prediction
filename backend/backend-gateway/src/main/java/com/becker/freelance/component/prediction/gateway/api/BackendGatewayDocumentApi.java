package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.DocumentMetadataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/backend/api/documents")
public class BackendGatewayDocumentApi {

    private final DocumentMetadataStorageService documentMetadataStorageService;

    @Autowired
    public BackendGatewayDocumentApi(DocumentMetadataStorageService documentMetadataStorageService) {
        this.documentMetadataStorageService = documentMetadataStorageService;
    }

    @PutMapping("/metadata")
    public DocumentMetadataDto save(@RequestBody DocumentMetadataDto documentMetadataDto) {
        return documentMetadataStorageService.save(documentMetadataDto);
    }


}
