package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.ApiAppsRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.backend.storage.GrpcAppList;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrpcAppStorageServiceTest {

    private ApiAppsRepositoryGrpc.ApiAppsRepositoryBlockingStub stub;
    private GrpcAppStorageService storageService;

    @BeforeEach
    void setUp() {
        stub = Mockito.mock(ApiAppsRepositoryGrpc.ApiAppsRepositoryBlockingStub.class);
        storageService = new GrpcAppStorageService(stub);
    }

    @Test
    void save() {
        UUID id = UUID.randomUUID();
        Mockito.when(storageService.save(Mockito.any())).then((Answer<GrpcApp>) invocationOnMock -> {
            GrpcApp argument = invocationOnMock.getArgument(0, GrpcApp.class);
            return GrpcApp.newBuilder(argument)
                    .setId(GrpcUUID.newBuilder().setId(id.toString()).build())
                    .build();
        });

        AppDto appDto = new AppDto();
        appDto.setAppName("app");

        AppDto save = storageService.save(appDto);

        assertEquals(id, save.getId());
        assertEquals("app", save.getAppName());
    }

    @Test
    void findAll() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Mockito.when(stub.findAll(Mockito.any())).thenReturn(GrpcAppList.newBuilder()
                .addAllApps(List.of(
                        GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId(id1.toString()).build()).setAppName("1").build(),
                        GrpcApp.newBuilder().setId(GrpcUUID.newBuilder().setId(id2.toString()).build()).setAppName("2").build()
                )).build());

        List<AppDto> find = storageService.findAll();

        assertEquals(2, find.size());
        assertEquals(List.of(id1, id2), find.stream().map(AppDto::getId).toList());
        assertEquals(List.of("1", "2"), find.stream().map(AppDto::getAppName).toList());
    }


}