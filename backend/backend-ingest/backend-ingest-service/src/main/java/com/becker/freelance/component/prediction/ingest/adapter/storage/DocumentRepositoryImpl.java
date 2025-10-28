package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiDocumentMetadataWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.ApiEmbeddingWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.DocumentEmbedding;
import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.ingest.spi.DocumentRepository;

public class DocumentRepositoryImpl implements DocumentRepository {

    private final ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryBlockingStub embeddingRepository;
    private final ApiDocumentMetadataWriteRepositoryGrpc.ApiDocumentMetadataWriteRepositoryBlockingStub metadataRepository;
    private final GrpcAdapterMapper mapper;

    public DocumentRepositoryImpl(ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryBlockingStub embeddingRepository, ApiDocumentMetadataWriteRepositoryGrpc.ApiDocumentMetadataWriteRepositoryBlockingStub metadataRepository, GrpcAdapterMapper mapper) {
        this.embeddingRepository = embeddingRepository;
        this.metadataRepository = metadataRepository;
        this.mapper = mapper;
    }

    @Override
    public DocumentMetadata save(DocumentMetadata metadata, DocumentEmbedding embedding) {
        GrpcDocumentMetadata grpcMetadata = mapper.map(metadata);
        GrpcDocumentMetadata saved = metadataRepository.save(grpcMetadata);
        GrpcDocumentMetadata savedWithEmbedding = embeddingRepository.save(mapper.map(embedding, saved.getId()));
        return mapper.map(savedWithEmbedding);
    }
}
