package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.ApiSourceIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestSourceMetadata;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestUUID;
import com.becker.freelance.component.prediction.backend.query.*;
import com.becker.freelance.component.prediction.gateway.api.dto.AppDto;
import com.becker.freelance.component.prediction.gateway.api.dto.SourceMetadataDto;
import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;
import com.becker.freelance.component.prediction.gateway.spi.SourceInputStreamHandler;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GrpcSourceStorageServiceTest {

    private ApiSourceIngestRepositoryGrpc.ApiSourceIngestRepositoryStub writeStub;
    private ApiSourceMetadataReadRepositoryGrpc.ApiSourceMetadataReadRepositoryBlockingStub readStub;
    private GrpcSourceStorageService storageService;

    @BeforeEach
    void setUp() {
        writeStub = Mockito.mock(ApiSourceIngestRepositoryGrpc.ApiSourceIngestRepositoryStub.class);
        readStub = Mockito.mock(ApiSourceMetadataReadRepositoryGrpc.ApiSourceMetadataReadRepositoryBlockingStub.class);
        storageService = new GrpcSourceStorageService(writeStub, readStub);
    }


    @Test
    void save() throws ExecutionException, InterruptedException {
        UUID uuid = UUID.randomUUID();
        GrpcIngestUUID uploadId = GrpcIngestUUID.newBuilder().setId(uuid.toString()).build();

        doAnswer((Answer<Void>) invocationOnMock -> {
            StreamObserver observer = invocationOnMock.getArgument(1, StreamObserver.class);
            observer.onNext(uploadId);
            observer.onCompleted();
            return null;
        }).when(writeStub).prepareSave(any(), any());



        SourceMetadataDto dto = new SourceMetadataDto();
        AppDto appDto = new AppDto();
        appDto.setAppName("test-app");
        dto.setApp(appDto);
        dto.setLocale("de");
        dto.setVersion(BigInteger.TEN);
        dto.setCreatedAt(ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC")));
        Set<String> tagNames = Set.of("t1", "t2");
        dto.setTags(tagNames.stream().map(t -> {
            TagDto tagDto = new TagDto();
            tagDto.setTag(t);
            return tagDto;
        }).collect(Collectors.toSet()));

        SourceInputStreamHandler inputStreamHandler = storageService.save(dto);

        assertEquals(uuid, inputStreamHandler.getHandleSaveId());
    }


    @Test
    void findAllWithEmptyList() {
        Mockito.when(readStub.findAll(Mockito.any())).thenReturn(GrpcQuerySourceMetadataList.newBuilder().build());

        List<SourceMetadataDto> all = storageService.findAll();

        assertEquals(0, all.size());
    }

    @Test
    void findAll() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Mockito.when(readStub.findAll(Mockito.any())).thenReturn(GrpcQuerySourceMetadataList.newBuilder()
                .addAllMetadata(List.of(
                        GrpcQuerySourceMetadata.newBuilder().setId(GrpcQueryUUID.newBuilder().setId(id1.toString()).build()).build(),
                        GrpcQuerySourceMetadata.newBuilder().setId(GrpcQueryUUID.newBuilder().setId(id2.toString()).build()).build()
                )).build());

        List<SourceMetadataDto> all = storageService.findAll();
        assertEquals(2, all.size());
        assertEquals(List.of(id1, id2), all.stream().map(SourceMetadataDto::getId).toList());
    }
}