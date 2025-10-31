package com.becker.freelance.component.prediction.ingest.domain.model;

import com.becker.freelance.junit.commons.beans.DynamicBeanTest;
import com.becker.freelance.junit.commons.equals.EqualsTest;

import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

class SourceMetadataTest  implements EqualsTest<SourceMetadata> {

    @Override
    public SourceMetadata baseObject() {
        return new SourceMetadata();
    }

    @Override
    public SourceMetadata unequalObject() {
        return new SourceMetadata(
                UUID.randomUUID(),
                new App(UUID.randomUUID(), "app"),
                new Locale("de"),
                BigInteger.ONE,
                ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC")),
                ZonedDateTime.of(LocalDateTime.now(), ZoneId.of("UTC")),
                Set.of(),
                "file",
                null,
                true);
    }
}