package com.becker.freelance.component.prediction.storage.adapter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ZonedDateTimeConverterTest {

    private ZonedDateTimeConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ZonedDateTimeConverter();
    }

    @Test
    public void convertToDatabaseColumn() {
        ZonedDateTime time = ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC"));

        String convert = converter.convertToDatabaseColumn(time);

        assertEquals("2020-01-01T00:00Z[UTC]", convert);
    }

    @Test
    public void convertToEntityAttribute() {

        ZonedDateTime convert = converter.convertToEntityAttribute("2020-01-01T00:00Z[UTC]");

        assertEquals(ZonedDateTime.of(LocalDateTime.of(2020, 1, 1, 0, 0, 0), ZoneId.of("UTC")), convert);
    }

}