package com.becker.freelance.component.prediction.gateway.api.dto;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;
import com.becker.freelance.junit.commons.equals.EqualsTest;

import java.util.UUID;
import java.util.stream.Stream;

class SourceMetadataDtoTest implements EqualsTest<SourceMetadataDto> {

    @Override
    public SourceMetadataDto baseObject() {
        return new SourceMetadataDto();
    }

    @Override
    public SourceMetadataDto unequalObject() {
        SourceMetadataDto dto = new SourceMetadataDto();
        dto.setId(UUID.randomUUID());
        return dto;
    }
}