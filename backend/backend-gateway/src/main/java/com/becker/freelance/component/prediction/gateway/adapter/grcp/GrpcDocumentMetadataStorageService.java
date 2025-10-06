package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.ApiDocumentMetadataRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.DocumentMetadataStorageService;
import com.google.protobuf.Empty;

import java.util.List;
import java.util.UUID;

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

    @Override
    public List<DocumentMetadataDto> findAll() {
        return stub.findAll(Empty.getDefaultInstance()).getMetadataList().stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public DocumentMetadataDto findById(UUID id) {
        GrpcDocumentMetadata byId = stub.findById(mapper.map(id));
        return mapper.map(byId);
    }

}
