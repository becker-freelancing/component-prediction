package com.becker.freelance.component.prediction.gateway.api.dto;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;
import com.becker.freelance.junit.commons.equals.EqualsTest;

import java.util.UUID;
import java.util.stream.Stream;

class AppDtoTest extends DynamicBeanTest implements EqualsTest<AppDto> {

    static UUID uuid = UUID.randomUUID();

    @Override
    protected Stream<Class<?>> beanClasses() {
        return Stream.of(AppDto.class);
    }

    @Override
    public AppDto baseObject() {
        AppDto appDto = new AppDto();
        appDto.setId(uuid);
        appDto.setAppName("app");
        return appDto;
    }

    @Override
    public AppDto unequalObject() {
        AppDto appDto = new AppDto();
        appDto.setId(UUID.randomUUID());
        return appDto;
    }
}