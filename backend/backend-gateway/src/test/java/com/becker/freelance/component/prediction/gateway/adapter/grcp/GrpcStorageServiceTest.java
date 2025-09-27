package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.BackendStorageServiceApiGrpc;
import com.becker.freelance.component.prediction.backend.storage.DocumentResponse;
import com.becker.freelance.component.prediction.backend.storage.DocumentsResponse;
import com.becker.freelance.component.prediction.gateway.api.DocumentDto;
import com.becker.freelance.component.prediction.gateway.api.DocumentId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrpcStorageServiceTest {

    private BackendStorageServiceApiGrpc.BackendStorageServiceApiBlockingStub stub;
    private GrpcStorageService storageService;

    @BeforeEach
    void setUp() {
        stub = Mockito.mock(BackendStorageServiceApiGrpc.BackendStorageServiceApiBlockingStub.class);
        storageService = new GrpcStorageService(stub);
    }


    @Test
    void findAll() {
        Mockito.when(stub.findAll(Mockito.any()))
                .thenReturn(DocumentsResponse.newBuilder()
                        .addAllDocuments(List.of(
                                DocumentResponse.newBuilder().setId("1234").build(),
                                DocumentResponse.newBuilder().setId("2345").build(),
                                DocumentResponse.newBuilder().setId("3456").build()
                        ))
                        .build());

        List<DocumentDto> all = storageService.findAll();

        assertEquals(3, all.size());
        assertEquals(Set.of("1234", "2345", "3456").stream()
                        .map(DocumentId::new)
                        .collect(Collectors.toSet()),
                all.stream().map(DocumentDto::getDocumentId).collect(Collectors.toSet()));
    }
}