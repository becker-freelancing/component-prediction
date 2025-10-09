package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiTagsWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcTag;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.ingest.domain.model.Tag;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

class TagsRepositoryImplTest {

    ApiTagsWriteRepositoryGrpc.ApiTagsWriteRepositoryBlockingStub stub;
    TagsRepositoryImpl tagsRepository;

    @BeforeEach
    void setUp() {
        stub = mock(ApiTagsWriteRepositoryGrpc.ApiTagsWriteRepositoryBlockingStub.class);
        tagsRepository = new TagsRepositoryImpl(stub, new GrpcAdapterMapper());
    }


    @Test
    void save() {
        UUID id = UUID.randomUUID();
        doReturn(GrpcTag.newBuilder().setId(GrpcUUID.newBuilder().setId(id.toString()).build()).setTag("tag").build())
                .when(stub).save(GrpcTag.newBuilder().setId(GrpcUUID.newBuilder().setId("").build()).setTag("tag").build());

        Tag app = new Tag("tag");
        Tag saved = tagsRepository.save(app);

        assertEquals(id, saved.getId());
        assertEquals("tag", saved.getTag());
    }

}