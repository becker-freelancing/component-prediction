package com.becker.freelance.component.prediction.ingest.domain.model;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;
import com.becker.freelance.junit.commons.equals.EqualsTest;

import java.util.UUID;
import java.util.stream.Stream;

class AppTest extends DynamicBeanTest implements EqualsTest<App> {

    static UUID id = UUID.randomUUID();

    @Override
    protected Stream<Class<?>> beanClasses() {
        return Stream.of(App.class);
    }

    @Override
    public App baseObject() {
        return new App(id, "app");
    }

    @Override
    public App unequalObject() {
        return new App("app");
    }
}