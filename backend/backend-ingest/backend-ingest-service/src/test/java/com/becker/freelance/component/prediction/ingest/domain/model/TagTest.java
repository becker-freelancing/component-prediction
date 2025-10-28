package com.becker.freelance.component.prediction.ingest.domain.model;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;
import com.becker.freelance.junit.commons.equals.EqualsTest;

import java.util.stream.Stream;

class TagTest extends DynamicBeanTest implements EqualsTest<Tag> {

    @Override
    protected Stream<Class<?>> beanClasses() {
        return Stream.of(Tag.class);
    }

    @Override
    public Tag baseObject() {
        return new Tag();
    }

    @Override
    public Tag unequalObject() {
        return new Tag("tag");
    }
}