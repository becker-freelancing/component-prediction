package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiEmbeddingWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.GrpcSourceEmbedding;
import com.becker.freelance.component.prediction.ingest.domain.model.SourceMetadata;
import com.becker.freelance.component.prediction.ingest.spi.EmbeddingRepository;

public class EmbeddingRepositoryImpl implements EmbeddingRepository {

    private final ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryBlockingStub stub;
    private final GrpcAdapterMapper mapper;

    public EmbeddingRepositoryImpl(ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryBlockingStub stub, GrpcAdapterMapper mapper) {
        this.stub = stub;
        this.mapper = mapper;
    }

    @Override
    public void save(SourceMetadata metadata, float[] embedding) {
        GrpcSourceEmbedding sourceEmbedding = mapper.map(metadata, embedding);
        stub.save(sourceEmbedding);
    }
}
