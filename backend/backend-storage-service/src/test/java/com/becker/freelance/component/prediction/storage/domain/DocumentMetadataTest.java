package com.becker.freelance.component.prediction.storage.domain;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;

import java.util.stream.Stream;

class DocumentMetadataTest extends DynamicBeanTest {

    @Override
    protected Stream<Class<?>> beanClasses() {
        return Stream.of(DocumentMetadata.class);
    }
}