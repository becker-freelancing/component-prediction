package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.ApiTagsRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.backend.storage.GrpcTagList;
import com.becker.freelance.component.prediction.gateway.api.dto.TagDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;

import java.math.BigInteger;
import java.util.List;

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
        Mockito.doAnswer((Answer<GrpcTag>) invocationOnMock -> {
            GrpcTag argument = invocationOnMock.getArgument(0, GrpcTag.class);
            return GrpcTag.newBuilder(argument)
                    .setId(2)
                    .build();
        }).when(stub).save(Mockito.any());

        TagDto appDto = new TagDto();
        appDto.setTag("tag");

        TagDto save = storageService.save(appDto);

        assertEquals(BigInteger.TWO, save.getId());
        assertEquals("tag", save.getTag());
    }

    @Test
    void findAll() {
        Mockito.when(stub.findAll(Mockito.any())).thenReturn(GrpcTagList.newBuilder()
                .addAllTags(List.of(
                        GrpcTag.newBuilder().setId(1).setTag("1").build(),
                        GrpcTag.newBuilder().setId(2).setTag("2").build()
                )).build());

        List<TagDto> find = storageService.findAll();

        assertEquals(2, find.size());
        assertEquals(List.of(BigInteger.ONE, BigInteger.TWO), find.stream().map(TagDto::getId).toList());
        assertEquals(List.of("1", "2"), find.stream().map(TagDto::getTag).toList());
    }
}