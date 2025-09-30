package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.StorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BackendGatewayRestApiTest {

    private StorageService storageService;
    private BackendGatewayRestApi restApi;

    @BeforeEach
    void setUp() {
        storageService = Mockito.mock(StorageService.class);
        restApi = new BackendGatewayRestApi(storageService);
    }

    @Test
    void save() {
        DocumentMetadataDto mock = Mockito.mock(DocumentMetadataDto.class);

        restApi.save(mock);

        Mockito.verify(storageService, Mockito.times(1)).save(mock);
    }

}