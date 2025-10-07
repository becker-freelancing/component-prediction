package com.becker.freelance.component.prediction.language.detection.adapter;

import com.becker.freelance.component.prediction.language.detection.spi.LanguageDetectorSpi;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.util.List;

@Configuration
public class AdapterConfig {

    @Bean
    public LanguageDetector languageDetector() throws IOException {
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();
        return LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .minimalConfidence(0.2)
                .build();
    }

    @Bean
    public TextObjectFactory textObjectFactory() {
        return CommonTextObjectFactories.forDetectingOnLargeText();
    }

    @Bean
    public LanguageDetectorSpi languageDetectorSpi(LanguageDetector languageDetector, TextObjectFactory textObjectFactory) {
        return new OmegatLanguageDetector(languageDetector, textObjectFactory);
    }
}
