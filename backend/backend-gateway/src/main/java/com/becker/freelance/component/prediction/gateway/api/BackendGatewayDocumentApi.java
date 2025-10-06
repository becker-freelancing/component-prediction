package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.DocumentMetadataStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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

    @GetMapping("/metadata/all")
    public List<DocumentMetadataDto> findAll() {
        return documentMetadataStorageService.findAll();
    }

    @GetMapping("/metadata")
    public DocumentMetadataDto findById(@RequestParam("id") UUID id) {
        return documentMetadataStorageService.findById(id);
    }


}
