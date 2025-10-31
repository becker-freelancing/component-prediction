package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.component.prediction.gateway.api.dto.SourceMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.SourceStorageService;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;

class BackendGatewaySourcesApiTest {

    private SourceStorageService sourceStorageService;
    private SourceUploadHandler sourceUploadHandler;
    private BackendGatewaySourcesApi restApi;

    @BeforeEach
    void setUp() {
        sourceStorageService = Mockito.mock(SourceStorageService.class);
        sourceUploadHandler = Mockito.mock(SourceUploadHandler.class);
        restApi = new BackendGatewaySourcesApi(sourceStorageService, sourceUploadHandler);
    }

    @Test
    void save() {
        SourceMetadataDto mock = Mockito.mock(SourceMetadataDto.class);

        restApi.save(mock);

        verify(sourceUploadHandler, Mockito.times(1)).prepareSave(mock);
    }

    @Test
    void upload() throws IOException, ExecutionException, InterruptedException {
        HttpServletRequest servletRequest = Mockito.mock(HttpServletRequest.class);
        ServletInputStream inputStream = mock(ServletInputStream.class);
        doReturn(inputStream).when(servletRequest).getInputStream();

        UUID uploadId = UUID.randomUUID();

        restApi.upload(uploadId, servletRequest);

        verify(sourceUploadHandler, times(1)).handleSave(uploadId, inputStream);
    }

    @Test
    void findAll() {
        restApi.findAll();

        verify(sourceStorageService, Mockito.times(1)).findAll();
    }

    @Test
    void findById() {

        UUID uuid = UUID.randomUUID();

        restApi.findById(uuid);

        verify(sourceStorageService, Mockito.times(1)).findById(uuid);
    }

}