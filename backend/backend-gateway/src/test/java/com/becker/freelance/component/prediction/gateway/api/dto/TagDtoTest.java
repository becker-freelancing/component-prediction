package com.becker.freelance.component.prediction.gateway.api.dto;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;
import com.becker.freelance.junit.commons.equals.EqualsTest;

import java.util.UUID;
import java.util.stream.Stream;

class TagDtoTest extends DynamicBeanTest implements EqualsTest<TagDto> {

    @Override
    protected Stream<Class<?>> beanClasses() {
        return Stream.of(TagDto.class);
    }

    @Override
    public TagDto baseObject() {
        return new TagDto();
    }

    @Override
    public TagDto unequalObject() {
        TagDto tagDto = new TagDto();
        tagDto.setId(UUID.randomUUID());
        tagDto.setTag("tag");
        return tagDto;
    }
}