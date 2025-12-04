package com.becker.freelance.component.prediction.sourceextraction.discovery.application;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.ApiSourceContentExtractionConfigProviderGrpc;
import com.becker.freelance.component.prediction.backend.SourceContentExtraction.GrpcSourceContentExtractionConfig;
import com.becker.freelance.component.prediction.backend.SourceContentExtraction.GrpcSourceContentExtractionField;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorConfig;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorConfigField;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorConfigProvider;
import com.google.protobuf.Empty;

class SourceContentExtractorConfigProviderImpl implements SourceContentExtractorConfigProvider {

    private final ApiSourceContentExtractionConfigProviderGrpc.ApiSourceContentExtractionConfigProviderBlockingStub stub;

    public SourceContentExtractorConfigProviderImpl(ApiSourceContentExtractionConfigProviderGrpc.ApiSourceContentExtractionConfigProviderBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public String getId() {
        return stub.getId(Empty.newBuilder().build()).getId();
    }

    @Override
    public SourceContentExtractorConfig getConfig() {
        GrpcSourceContentExtractionConfig config = stub.getConfig(Empty.newBuilder().build());
        return map(config);
    }

    private SourceContentExtractorConfig map(GrpcSourceContentExtractionConfig config) {
        return new SourceContentExtractorConfig(
                config.getFieldsList().stream().map(this::map).toList()
        );
    }

    private SourceContentExtractorConfigField map(GrpcSourceContentExtractionField field){
        return new SourceContentExtractorConfigField(
                field.getId(),
                field.getType(),
                field.getRequired()
        );
    }
}
