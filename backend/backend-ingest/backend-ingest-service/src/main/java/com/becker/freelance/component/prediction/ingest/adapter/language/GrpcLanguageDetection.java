package com.becker.freelance.component.prediction.ingest.adapter.language;

import com.becker.freelance.component.prediction.ingest.domain.model.Locale;
import com.becker.freelance.component.prediction.ingest.spi.LanguageDetectionService;
import com.becker.freelance.component.prediction.language.detection.GrpcLanguageDetectionQuery;
import com.becker.freelance.component.prediction.language.detection.GrpcLanguageDetectorGrpc;
import com.becker.freelance.component.prediction.language.detection.GrpcLocale;


public class GrpcLanguageDetection implements LanguageDetectionService {

    private final GrpcLanguageDetectorGrpc.GrpcLanguageDetectorBlockingStub stub;

    public GrpcLanguageDetection(GrpcLanguageDetectorGrpc.GrpcLanguageDetectorBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public Locale detectLanguage(String text) {

        GrpcLocale grpcLocale = stub.detectLanguage(GrpcLanguageDetectionQuery.newBuilder().setQuery(text).build());
        return new Locale(grpcLocale.getLocale());
    }
}
