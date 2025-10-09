package com.becker.freelance.component.prediction.ingest.spi;

import java.util.Locale;

public interface LanguageDetectionService {

    public Locale detectLanguage(String text);
}
