package com.becker.freelance.component.prediction.ingest.domain.model;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;
import com.becker.freelance.junit.commons.equals.EqualsTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

class DocumentMetadataTest extends DynamicBeanTest implements EqualsTest<DocumentMetadata> {

    @Override
    protected Stream<Class<?>> beanClasses() {
        return Stream.of(DocumentMetadata.class);
    }

    @Override
    public DocumentMetadata baseObject() {
        return new DocumentMetadata();
    }

    @Override
    public DocumentMetadata unequalObject() {
        return new DocumentMetadata(
                new App(UUID.randomUUID(), "app"),
                "in-app",
                "title",
                "desc",
                "short-desc",
                new Locale("de"),
                BigInteger.ONE,
                ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC")),
                Set.of());
    }
}