package com.becker.freelance.component.prediction.language.detection.api;

import com.becker.freelance.component.prediction.language.detection.GrpcLanguageDetectionQuery;
import com.becker.freelance.component.prediction.language.detection.GrpcLocale;
import com.becker.freelance.component.prediction.language.detection.domain.model.Locale;
import com.becker.freelance.component.prediction.language.detection.spi.LanguageDetectorSpi;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.opentest4j.AssertionFailedError;

import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LanguageDetectionApiTest {

    LanguageDetectionApi detectionApi;
    LanguageDetectorSpi spi;

    @BeforeEach
    void setUp() {
        spi = Mockito.mock(LanguageDetectorSpi.class);
        detectionApi = new LanguageDetectionApi(spi);
    }

    @Test
    void detectLanguage() {

        Mockito.doReturn(Optional.of(new Locale("de"))).when(spi).detectLanguage(Mockito.anyString());

        GrpcLocale expected = GrpcLocale.newBuilder()
                .setLocale("de")
                .build();

        LocaleStreamObserver streamObserver = Mockito.spy(new LocaleStreamObserver(expected));

        detectionApi.detectLanguage(GrpcLanguageDetectionQuery.newBuilder().build(), streamObserver);

        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void detectLanguageIfNotDetected() {

        Mockito.doReturn(Optional.empty()).when(spi).detectLanguage(Mockito.anyString());

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        detectionApi.detectLanguage(GrpcLanguageDetectionQuery.newBuilder().build(), streamObserver);

        Mockito.verify(streamObserver, Mockito.times(0)).onNext(Mockito.any());
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    static class LocaleStreamObserver implements StreamObserver<GrpcLocale> {
        private final GrpcLocale expected;

        LocaleStreamObserver(GrpcLocale expected) {
            this.expected = expected;
        }

        @Override
        public void onNext(GrpcLocale grpcLocale) {
            assertEquals(expected(), grpcLocale);
        }

        @Override
        public void onError(Throwable throwable) {
            throw new AssertionFailedError();
        }

        @Override
        public void onCompleted() {

        }

        public GrpcLocale expected() {
            return expected;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (obj == null || obj.getClass() != this.getClass()) return false;
            var that = (LocaleStreamObserver) obj;
            return Objects.equals(this.expected, that.expected);
        }

        @Override
        public int hashCode() {
            return Objects.hash(expected);
        }

        @Override
        public String toString() {
            return "LocaleStreamObserver[" +
                    "expected=" + expected + ']';
        }

    }
}