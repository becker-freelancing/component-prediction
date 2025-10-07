package com.becker.freelance.component.prediction.language.detection.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LocaleTest {

    @Test
    void getter() {
        Locale locale = new Locale("de");

        assertEquals("de", locale.abbreviation());
    }

    @Test
    void throwsExceptionOnNullAbbreviation() {
        assertThrows(IllegalArgumentException.class, () -> new Locale(null));
    }

    @Test
    void throwsExceptionOnToLongAbbreviation() {
        assertThrows(IllegalArgumentException.class, () -> new Locale("de_DE"));
    }

}