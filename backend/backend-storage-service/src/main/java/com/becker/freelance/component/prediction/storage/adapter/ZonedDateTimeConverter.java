package com.becker.freelance.component.prediction.storage.adapter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.time.ZonedDateTime;

@Converter(autoApply = true)
public class ZonedDateTimeConverter implements AttributeConverter<ZonedDateTime, String> {
    @Override
    public String convertToDatabaseColumn(ZonedDateTime zonedDateTime) {
        return zonedDateTime != null ? zonedDateTime.toString() : null;
    }

    @Override
    public ZonedDateTime convertToEntityAttribute(String dbData) {
        return dbData != null ? ZonedDateTime.parse(dbData) : null;
    }
}
