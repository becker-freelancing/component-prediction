package com.becker.freelance.component.prediction.storage.adapter.jpa;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;

import java.util.stream.Stream;

public class EntityBeanTest extends DynamicBeanTest {
    @Override
    protected Stream<Class<?>> beanClasses() {
        return Stream.of(
                AppsEntity.class,
                DocumentMetadataEntity.class,
                TagsEntity.class
        );
    }
}
