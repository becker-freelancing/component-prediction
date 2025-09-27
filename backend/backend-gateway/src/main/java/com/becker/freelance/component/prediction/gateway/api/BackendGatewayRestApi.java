package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.spi.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/api")
public class BackendGatewayRestApi {

    private final StorageService storageService;

    @Autowired
    public BackendGatewayRestApi(StorageService storageService) {
        this.storageService = storageService;
    }

    @PutMapping("/documents")
    public ResponseEntity<DocumentId> save(@RequestBody DocumentDto documentDto){
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/documents")
    public ResponseEntity<List<String>> findAll(){

        List<String> documentIds = storageService.findAll().stream().map(DocumentDto::getDocumentId).toList();

        return ResponseEntity.ok(documentIds);
    }



}
