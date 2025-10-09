package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiAppsWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.ingest.domain.model.App;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class AppsRepositoryImplTest {

    ApiAppsWriteRepositoryGrpc.ApiAppsWriteRepositoryBlockingStub stub;
    AppsRepositoryImpl appsRepository;

    @BeforeEach
    void setUp() {
        stub = mock(ApiAppsWriteRepositoryGrpc.ApiAppsWriteRepositoryBlockingStub.class);
        appsRepository = new AppsRepositoryImpl(stub, new GrpcAdapterMapper());
    }


    @Test
    void save() {
        UUID id = UUID.randomUUID();
        doReturn(GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId(id.toString()).build()).setAppName("app").build())
                .when(stub).save(GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId("").build()).setAppName("app").build());

        App app = new App("app");
        App saved = appsRepository.save(app);

        assertEquals(id, saved.getId());
        assertEquals("app", saved.getAppName());
    }

    @Test
    void saveWithNull() {
        doReturn(GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId("").build()).setAppName("nullable-app").build())
                .when(stub).save(GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId("").build()).setAppName("nullable-app").build());

        App saved = appsRepository.save(null);

        assertNull(saved);
    }
}