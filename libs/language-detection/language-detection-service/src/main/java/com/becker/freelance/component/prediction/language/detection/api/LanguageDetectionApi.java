package com.becker.freelance.component.prediction.language.detection.api;

import com.becker.freelance.component.prediction.language.detection.GrpcLanguageDetectionQuery;
import com.becker.freelance.component.prediction.language.detection.GrpcLanguageDetectorGrpc;
import com.becker.freelance.component.prediction.language.detection.GrpcLocale;
import com.becker.freelance.component.prediction.language.detection.domain.model.Locale;
import com.becker.freelance.component.prediction.language.detection.spi.LanguageDetectorSpi;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@GrpcService
public class LanguageDetectionApi extends GrpcLanguageDetectorGrpc.GrpcLanguageDetectorImplBase {

    private final LanguageDetectorSpi languageDetector;

    @Autowired
    public LanguageDetectionApi(LanguageDetectorSpi languageDetector) {
        this.languageDetector = languageDetector;
    }

    @Override
    public void detectLanguage(GrpcLanguageDetectionQuery request, StreamObserver<GrpcLocale> responseObserver) {
        Optional<Locale> locale = languageDetector.detectLanguage(request.getQuery());
        locale.map(Locale::abbreviation).map(abbreviation -> GrpcLocale.newBuilder().setLocale(abbreviation).build()).ifPresent(responseObserver::onNext);
        responseObserver.onCompleted();
    }
}
