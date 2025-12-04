package com.becker.freelance.component.prediction.sourceextraction.discovery.application;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.ApiSourceContentExtractionLabelProviderGrpc;
import com.becker.freelance.component.prediction.backend.SourceContentExtraction.GrpcGetLabelRequest;
import com.becker.freelance.component.prediction.backend.SourceContentExtraction.GrpcGetLabelResponse;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.Label;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractorLabelProvider;
import com.google.protobuf.Empty;

class SourceContentExtractorLabelProviderImpl implements SourceContentExtractorLabelProvider {

    private final ApiSourceContentExtractionLabelProviderGrpc.ApiSourceContentExtractionLabelProviderBlockingStub stub;

    public SourceContentExtractorLabelProviderImpl(ApiSourceContentExtractionLabelProviderGrpc.ApiSourceContentExtractionLabelProviderBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public String getId() {
        return stub.getId(Empty.newBuilder().build()).getId();
    }

    @Override
    public Label getLabel(String identifier) {
        return map(stub.getLabel(map(identifier)));
    }

    private GrpcGetLabelRequest map(String identifier) {
        return GrpcGetLabelRequest.newBuilder()
                .setIdentifier(identifier)
                .build();
    }

    private Label map(GrpcGetLabelResponse response) {
        return new Label(
                response.getIdentifier(),
                response.getLabel()
        );
    }
}
