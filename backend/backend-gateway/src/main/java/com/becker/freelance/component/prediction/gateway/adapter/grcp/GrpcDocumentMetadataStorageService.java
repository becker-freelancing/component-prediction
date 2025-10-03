package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.ApiDocumentMetadataRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.DocumentMetadataStorageService;

public class GrpcDocumentMetadataStorageService implements DocumentMetadataStorageService {

    private final ApiDocumentMetadataRepositoryGrpc.ApiDocumentMetadataRepositoryBlockingStub stub;
    private final GrpcMapper mapper;

    public GrpcDocumentMetadataStorageService(ApiDocumentMetadataRepositoryGrpc.ApiDocumentMetadataRepositoryBlockingStub stub) {
        this.stub = stub;
        this.mapper = new GrpcMapper();
    }

    @Override
    public DocumentMetadataDto save(DocumentMetadataDto documentMetadataDto) {
        GrpcDocumentMetadata save = stub.save(mapper.map(documentMetadataDto));
        return mapper.map(save);
    }

}
