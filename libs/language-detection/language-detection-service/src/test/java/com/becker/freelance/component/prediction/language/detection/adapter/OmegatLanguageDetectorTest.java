package com.becker.freelance.component.prediction.language.detection.adapter;

import com.becker.freelance.component.prediction.language.detection.domain.model.Locale;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class OmegatLanguageDetectorTest {

    @Autowired
    OmegatLanguageDetector languageDetector;

    static Stream<Arguments> languages() {
        return Stream.of(
                Arguments.of("en", "Hello, how are you today?"),
                Arguments.of("de", "Hallo, wie geht es dir?"),
                Arguments.of("fr", "Bonjour, comment ça va aujourd'hui?"),
                Arguments.of("es", "Hola, ¿cómo estás hoy?"),
                Arguments.of("it", "Ciao, come stai oggi?")
        );
    }

    @ParameterizedTest
    @MethodSource("languages")
    void detectLanguage(String expectedLanguage, String sentence) {
        Optional<Locale> locale = languageDetector.detectLanguage(sentence);

        assertTrue(locale.isPresent());
        assertEquals(expectedLanguage, locale.get().abbreviation());
    }
}