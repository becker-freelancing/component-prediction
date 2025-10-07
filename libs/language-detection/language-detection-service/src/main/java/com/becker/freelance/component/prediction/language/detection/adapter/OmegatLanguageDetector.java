package com.becker.freelance.component.prediction.language.detection.adapter;

import com.becker.freelance.component.prediction.language.detection.domain.model.Locale;
import com.becker.freelance.component.prediction.language.detection.spi.LanguageDetectorSpi;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;

import java.util.Optional;

public class OmegatLanguageDetector implements LanguageDetectorSpi {

    private final LanguageDetector detector;
    private final TextObjectFactory textObjectFactory;

    public OmegatLanguageDetector(LanguageDetector languageDetector, TextObjectFactory textObjectFactory) {
        this.detector = languageDetector;
        this.textObjectFactory = textObjectFactory;
    }

    @Override
    public Optional<Locale> detectLanguage(String query) {
        TextObject textObject = textObjectFactory.forText(query);
        Optional<LdLocale> detect = detector.detect(textObject).toJavaUtil();
        return detect.map(locale -> new Locale(locale.getLanguage()));
    }
}
