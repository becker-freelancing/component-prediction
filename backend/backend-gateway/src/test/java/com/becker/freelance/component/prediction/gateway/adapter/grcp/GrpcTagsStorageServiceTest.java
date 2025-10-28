package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.ApiTagsIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestTag;
import com.becker.freelance.component.prediction.backend.query.ApiTagsReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryTag;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryTagList;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryUUID;
import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrpcTagsStorageServiceTest {

    private ApiTagsIngestRepositoryGrpc.ApiTagsIngestRepositoryBlockingStub writeStub;
    private ApiTagsReadRepositoryGrpc.ApiTagsReadRepositoryBlockingStub readStub;
    private GrpcTagsStorageService storageService;

    @BeforeEach
    void setUp() {
        writeStub = Mockito.mock(ApiTagsIngestRepositoryGrpc.ApiTagsIngestRepositoryBlockingStub.class);
        readStub = Mockito.mock(ApiTagsReadRepositoryGrpc.ApiTagsReadRepositoryBlockingStub.class);
        storageService = new GrpcTagsStorageService(writeStub, readStub);
    }

    @Test
    void save() {
        UUID id = UUID.randomUUID();
        Mockito.doAnswer((Answer<GrpcQueryTag>) invocationOnMock -> {
            GrpcIngestTag argument = invocationOnMock.getArgument(0, GrpcIngestTag.class);
            return GrpcQueryTag.newBuilder()
                    .setId(GrpcQueryUUID.newBuilder().setId(id.toString()).build())
                    .setTag(argument.getTag())
                    .build();
        }).when(writeStub).save(Mockito.any());

        TagDto appDto = new TagDto();
        appDto.setTag("tag");

        TagDto save = storageService.save(appDto);

        assertEquals(id, save.getId());
        assertEquals("tag", save.getTag());
    }

    @Test
    void findAll() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        Mockito.when(readStub.findAll(Mockito.any())).thenReturn(GrpcQueryTagList.newBuilder()
                .addAllTags(List.of(
                        GrpcQueryTag.newBuilder().setId(GrpcQueryUUID.newBuilder().setId(id1.toString()).build()).setTag("1").build(),
                        GrpcQueryTag.newBuilder().setId(GrpcQueryUUID.newBuilder().setId(id2.toString()).build()).setTag("2").build()
                )).build());

        List<TagDto> find = storageService.findAll();

        assertEquals(2, find.size());
        assertEquals(List.of(id1, id2), find.stream().map(TagDto::getId).toList());
        assertEquals(List.of("1", "2"), find.stream().map(TagDto::getTag).toList());
    }
}