package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;
import com.becker.freelance.component.prediction.gateway.spi.TagsStorageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class BackendGatewayTagsApiTest {

    private TagsStorageService storageService;
    private BackendGatewayTagsApi restApi;

    @BeforeEach
    void setUp() {
        storageService = Mockito.mock(TagsStorageService.class);
        restApi = new BackendGatewayTagsApi(storageService);
    }

    @Test
    void save() {
        TagDto mock = Mockito.mock(TagDto.class);

        restApi.save(mock);

        Mockito.verify(storageService, Mockito.times(1)).save(mock);
    }

    @Test
    void findAll() {
        restApi.findAll();

        Mockito.verify(storageService, Mockito.times(1)).findAll();
    }
}