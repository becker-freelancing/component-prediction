package com.becker.freelance.component.prediction.gateway.api;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;
import com.becker.freelance.junit.commons.equals.EqualsTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

class DocumentDtoTest extends DynamicBeanTest implements EqualsTest<DocumentDto> {

    @Override
    public DocumentDto baseObject() {
        return new DocumentDto(
                new DocumentId("123"),
                "component-prediction",
                "xy",
                "2323",
                "145",
                "jsdnf",
                List.of("ajdf", "psd"),
                "sde",
                "sad",
                "sd",
                LocalDateTime.of(2020, 1, 1, 12, 0)
        );
    }

    @Override
    public DocumentDto unequalObject() {
        return new DocumentDto(
                new DocumentId("123"),
                "component-prediction",
                "xy",
                "2323",
                "145",
                "jsdnf",
                List.of("ajdf", "psd"),
                "sde",
                "sad",
                "sd",
                LocalDateTime.of(2020, 1, 1, 12, 1)
        );
    }

    @Override
    protected Stream<Class<?>> beanClasses() {
        return Stream.of(DocumentDto.class);
    }
}