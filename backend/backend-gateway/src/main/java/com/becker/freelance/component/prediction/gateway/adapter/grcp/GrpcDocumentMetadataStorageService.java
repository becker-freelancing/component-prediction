package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.ApiDocumentMetadataIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.ApiDocumentMetadataReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.GrpcQueryDocumentMetadata;
import com.becker.freelance.component.prediction.gateway.api.dto.DocumentMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.DocumentMetadataStorageService;
import com.google.protobuf.Empty;

import java.util.List;
import java.util.UUID;

public class GrpcDocumentMetadataStorageService implements DocumentMetadataStorageService {

    private final ApiDocumentMetadataIngestRepositoryGrpc.ApiDocumentMetadataIngestRepositoryBlockingStub writeStub;
    private final ApiDocumentMetadataReadRepositoryGrpc.ApiDocumentMetadataReadRepositoryBlockingStub readStub;
    private final GrpcMapper mapper;

    public GrpcDocumentMetadataStorageService(ApiDocumentMetadataIngestRepositoryGrpc.ApiDocumentMetadataIngestRepositoryBlockingStub writeStub, ApiDocumentMetadataReadRepositoryGrpc.ApiDocumentMetadataReadRepositoryBlockingStub readStub) {
        this.writeStub = writeStub;
        this.readStub = readStub;
        this.mapper = new GrpcMapper();
    }

    @Override
    public DocumentMetadataDto save(DocumentMetadataDto documentMetadataDto) {
        GrpcQueryDocumentMetadata save = writeStub.save(mapper.map(documentMetadataDto));
        return mapper.map(save);
    }

    @Override
    public List<DocumentMetadataDto> findAll() {
        return readStub.findAll(Empty.getDefaultInstance()).getMetadataList().stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public DocumentMetadataDto findById(UUID id) {
        GrpcQueryDocumentMetadata byId = readStub.findById(mapper.mapForQuery(id));
        return mapper.map(byId);
    }

}
