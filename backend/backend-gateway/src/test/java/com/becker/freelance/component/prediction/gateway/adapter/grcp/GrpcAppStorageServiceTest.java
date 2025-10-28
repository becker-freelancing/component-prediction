package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.ApiAppsIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestApp;
import com.becker.freelance.component.prediction.backend.query.ApiAppsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryApp;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryAppList;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryUUID;
import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrpcAppStorageServiceTest {

    private ApiAppsReadRepositoryGrpc.ApiAppsReadRepositoryBlockingStub readStub;
    private ApiAppsIngestRepositoryGrpc.ApiAppsIngestRepositoryBlockingStub writeStub;
    private GrpcAppStorageService storageService;

    @BeforeEach
    void setUp() {
        readStub = Mockito.mock(ApiAppsReadRepositoryGrpc.ApiAppsReadRepositoryBlockingStub.class);
        writeStub = Mockito.mock(ApiAppsIngestRepositoryGrpc.ApiAppsIngestRepositoryBlockingStub.class);
        storageService = new GrpcAppStorageService(writeStub, readStub);
    }

    @Test
    void save() {
        UUID id = UUID.randomUUID();
        Mockito.when(writeStub.save(Mockito.any())).then((Answer<GrpcQueryApp>) invocationOnMock -> {
            GrpcIngestApp argument = invocationOnMock.getArgument(0, GrpcIngestApp.class);
            return GrpcQueryApp.newBuilder()
                    .setId(GrpcQueryUUID.newBuilder().setId(id.toString()).build())
                    .setAppName(argument.getAppName())
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
        Mockito.when(readStub.findAll(Mockito.any())).thenReturn(GrpcQueryAppList.newBuilder()
                .addAllApps(List.of(
                        GrpcQueryApp.newBuilder().setId(GrpcQueryUUID.newBuilder().setId(id1.toString()).build()).setAppName("1").build(),
                        GrpcQueryApp.newBuilder().setId(GrpcQueryUUID.newBuilder().setId(id2.toString()).build()).setAppName("2").build()
                )).build());

        List<AppDto> find = storageService.findAll();

        assertEquals(2, find.size());
        assertEquals(List.of(id1, id2), find.stream().map(AppDto::getId).toList());
        assertEquals(List.of("1", "2"), find.stream().map(AppDto::getAppName).toList());
    }


}