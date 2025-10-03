package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.ApiAppsRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcApp;
import com.becker.freelance.component.prediction.backend.storage.GrpcAppList;
import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.math.BigInteger;
import java.util.List;

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
        Mockito.when(storageService.save(Mockito.any())).then((Answer<GrpcApp>) invocationOnMock -> {
            GrpcApp argument = invocationOnMock.getArgument(0, GrpcApp.class);
            return GrpcApp.newBuilder(argument)
                    .setId(2)
                    .build();
        });

        AppDto appDto = new AppDto();
        appDto.setAppName("app");

        AppDto save = storageService.save(appDto);

        assertEquals(BigInteger.TWO, save.getId());
        assertEquals("app", save.getAppName());
    }

    @Test
    void findAll() {
        Mockito.when(stub.findAll(Mockito.any())).thenReturn(GrpcAppList.newBuilder()
                .addAllApps(List.of(
                        GrpcApp.newBuilder().setId(1).setAppName("1").build(),
                        GrpcApp.newBuilder().setId(2).setAppName("2").build()
                )).build());

        List<AppDto> find = storageService.findAll();

        assertEquals(2, find.size());
        assertEquals(List.of(BigInteger.ONE, BigInteger.TWO), find.stream().map(AppDto::getId).toList());
        assertEquals(List.of("1", "2"), find.stream().map(AppDto::getAppName).toList());
    }


}