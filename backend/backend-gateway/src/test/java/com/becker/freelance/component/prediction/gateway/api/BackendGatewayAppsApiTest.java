package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import com.becker.freelance.component.prediction.gateway.spi.AppStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BackendGatewayAppsApiTest {

    private AppStorageService storageService;
    private BackendGatewayAppsApi restApi;

    @BeforeEach
    void setUp() {
        storageService = Mockito.mock(AppStorageService.class);
        restApi = new BackendGatewayAppsApi(storageService);
    }

    @Test
    void save() {
        AppDto mock = Mockito.mock(AppDto.class);

        restApi.save(mock);

        Mockito.verify(storageService, Mockito.times(1)).save(mock);
    }

    @Test
    void findAll() {
        restApi.findAll();

        Mockito.verify(storageService, Mockito.times(1)).findAll();
    }
}