package com.becker.freelance.component.prediction.ingest.application;

import com.becker.freelance.component.prediction.ingest.api.DocumentMetadataIngestService;
import com.becker.freelance.component.prediction.ingest.domain.model.DocumentEmbedding;
import com.becker.freelance.component.prediction.ingest.domain.model.DocumentMetadata;
import com.becker.freelance.component.prediction.ingest.spi.DocumentMetadataSanitizer;
import com.becker.freelance.component.prediction.ingest.spi.DocumentRepository;
import com.becker.freelance.component.prediction.ingest.spi.EmbeddingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DocumentMetadataIngestServiceImpl implements DocumentMetadataIngestService {

    private final DocumentMetadataSanitizer metadataSanitizer;
    private final EmbeddingService embeddingService;
    private final DocumentRepository documentRepository;

    @Autowired
    public DocumentMetadataIngestServiceImpl(DocumentMetadataSanitizer metadataSanitizer, EmbeddingService embeddingService, DocumentRepository documentRepository) {
        this.metadataSanitizer = metadataSanitizer;
        this.embeddingService = embeddingService;
        this.documentRepository = documentRepository;
    }

    @Override
    public DocumentMetadata ingest(DocumentMetadata documentMetadata) {
        DocumentMetadata sanitize = metadataSanitizer.sanitize(documentMetadata);
        DocumentEmbedding embedding = embeddingService.embed(sanitize);
        DocumentMetadata saved = documentRepository.save(sanitize, embedding);
        return saved;
    }
}
