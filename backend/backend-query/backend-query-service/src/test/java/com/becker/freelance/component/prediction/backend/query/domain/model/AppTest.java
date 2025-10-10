package com.becker.freelance.component.prediction.backend.query.domain.model;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;

import java.util.stream.Stream;

class AppTest extends DynamicBeanTest {

    @Override
    protected Stream<Class<?>> beanClasses() {
        return Stream.of(App.class);
    }
}