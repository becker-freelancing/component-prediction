package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.DocumentMetadataStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BackendGatewayDocumentApiTest {

    private DocumentMetadataStorageService documentMetadataStorageService;
    private BackendGatewayDocumentApi restApi;

    @BeforeEach
    void setUp() {
        documentMetadataStorageService = Mockito.mock(DocumentMetadataStorageService.class);
        restApi = new BackendGatewayDocumentApi(documentMetadataStorageService);
    }

    @Test
    void save() {
        DocumentMetadataDto mock = Mockito.mock(DocumentMetadataDto.class);

        restApi.save(mock);

        Mockito.verify(documentMetadataStorageService, Mockito.times(1)).save(mock);
    }

}