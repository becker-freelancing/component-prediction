package com.becker.freelance.component.prediction.language.detection.domain.model;

public record Locale(String abbreviation) {

    public Locale {
        if (abbreviation == null || abbreviation.length() > 2) {
            throw new IllegalArgumentException("Locale Abbreviation must not be null and the length must < 2, but was " + abbreviation);
        }
    }
}
