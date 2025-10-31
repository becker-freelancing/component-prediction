package com.becker.freelance.component.prediction.ingest.application;

import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;

import java.util.function.Function;

public interface ToChildMetadataFunction extends Function<ToChildMetadataFunction.Param, SourceMetadata> {

    public static record Param(String name, String contentPart){}
}
