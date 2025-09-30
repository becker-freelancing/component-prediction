package com.becker.freelance.component.prediction.gateway.api.dto;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;
import com.becker.freelance.junit.commons.equals.EqualsTest;

import java.math.BigInteger;
import java.util.stream.Stream;

class DocumentMetadataDtoTest extends DynamicBeanTest implements EqualsTest<DocumentMetadataDto> {

    @Override
    protected Stream<Class<?>> beanClasses() {
        return Stream.of(DocumentMetadataDto.class);
    }

    @Override
    public DocumentMetadataDto baseObject() {
        return new DocumentMetadataDto();
    }

    @Override
    public DocumentMetadataDto unequalObject() {
        DocumentMetadataDto dto = new DocumentMetadataDto();
        dto.setId(BigInteger.ONE);
        return dto;
    }
}