package com.becker.freelance.component.prediction.language.detection.spi;

import com.becker.freelance.component.prediction.language.detection.domain.model.Locale;

import java.util.Optional;

public interface LanguageDetectorSpi {
    Optional<Locale> detectLanguage(String query);
}
