package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.SourceMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.SourceStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/backend/api/sources")
public class BackendGatewaySourcesApi {

    private final SourceStorageService sourceStorageService;
    private final SourceUploadHandler uploadHandler;

    @Autowired
    public BackendGatewaySourcesApi(SourceStorageService sourceStorageService, SourceUploadHandler uploadHandler) {
        this.sourceStorageService = sourceStorageService;
        this.uploadHandler = uploadHandler;
    }

    @PutMapping("/metadata")
    public UUID save(@RequestBody SourceMetadataDto sourceMetadataDto) {
        return uploadHandler.prepareSave(sourceMetadataDto);
    }

    @PutMapping("/upload")
    public SourceMetadataDto upload(@RequestHeader("X-UploadId") UUID uploadId, HttpServletRequest request) throws IOException, ExecutionException, InterruptedException {
        return uploadHandler.handleSave(uploadId, request.getInputStream());
    }

    @GetMapping("/metadata/all")
    public List<SourceMetadataDto> findAll() {
        return sourceStorageService.findAll();
    }

    @GetMapping("/metadata")
    public SourceMetadataDto findById(@RequestParam("id") UUID id) {
        return sourceStorageService.findById(id);
    }


}
