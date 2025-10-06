package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.ApiTagsRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.backend.storage.GrpcTagList;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GrpcTagsStorageServiceTest {

    private ApiTagsRepositoryGrpc.ApiTagsRepositoryBlockingStub stub;
    private GrpcTagsStorageService storageService;

    @BeforeEach
    void setUp() {
        stub = Mockito.mock(ApiTagsRepositoryGrpc.ApiTagsRepositoryBlockingStub.class);
        storageService = new GrpcTagsStorageService(stub);
    }

    @Test
    void save() {
        UUID id = UUID.randomUUID();
        Mockito.doAnswer((Answer<GrpcTag>) invocationOnMock -> {
            GrpcTag argument = invocationOnMock.getArgument(0, GrpcTag.class);
            return GrpcTag.newBuilder(argument)
                    .setId(GrpcUUID.newBuilder().setId(id.toString()).build())
                    .build();
        }).when(stub).save(Mockito.any());

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
        Mockito.when(stub.findAll(Mockito.any())).thenReturn(GrpcTagList.newBuilder()
                .addAllTags(List.of(
                        GrpcTag.newBuilder().setId(GrpcUUID.newBuilder().setId(id1.toString()).build()).setTag("1").build(),
                        GrpcTag.newBuilder().setId(GrpcUUID.newBuilder().setId(id2.toString()).build()).setTag("2").build()
                )).build());

        List<TagDto> find = storageService.findAll();

        assertEquals(2, find.size());
        assertEquals(List.of(id1, id2), find.stream().map(TagDto::getId).toList());
        assertEquals(List.of("1", "2"), find.stream().map(TagDto::getTag).toList());
    }
}