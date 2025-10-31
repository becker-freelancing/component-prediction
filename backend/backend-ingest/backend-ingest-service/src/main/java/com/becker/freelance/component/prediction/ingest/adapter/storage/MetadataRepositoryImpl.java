package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiSourceMetadataWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcSourceMetadata;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.ingest.spi.MetadataRepository;

public class MetadataRepositoryImpl implements MetadataRepository {

    private final ApiSourceMetadataWriteRepositoryGrpc.ApiSourceMetadataWriteRepositoryBlockingStub metadataRepository;
    private final GrpcAdapterMapper mapper;

    public MetadataRepositoryImpl(ApiSourceMetadataWriteRepositoryGrpc.ApiSourceMetadataWriteRepositoryBlockingStub metadataRepository, GrpcAdapterMapper mapper) {
        this.metadataRepository = metadataRepository;
        this.mapper = mapper;
    }

    @Override
    public SourceMetadata save(SourceMetadata metadata) {
        GrpcSourceMetadata grpcMetadata = mapper.map(metadata);
        GrpcSourceMetadata saved = metadataRepository.save(grpcMetadata);
        return mapper.map(saved);
    }
}
