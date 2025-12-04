package com.becker.freelance.component.prediction.sourceextraction.discovery.api;

public interface SourceContentExtractorLabelProvider {

    public String getId();

    public Label getLabel(String identifier);
}
