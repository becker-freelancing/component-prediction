package com.becker.freelance.component.prediction.ingest.spi;


import com.becker.freelance.component.prediction.ingest.domain.model.Locale;

public interface LanguageDetectionService {

    public Locale detectLanguage(String text);
}
