package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiAppsWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.ApiDocumentMetadataWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.ApiEmbeddingWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.ApiTagsWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.ingest.spi.AppsRepository;
import com.becker.freelance.component.prediction.ingest.spi.DocumentRepository;
import com.becker.freelance.component.prediction.ingest.spi.TagsRepository;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @GrpcClient("backendAppStorageService")
    private ApiAppsWriteRepositoryGrpc.ApiAppsWriteRepositoryBlockingStub apiAppsWriteRepositoryBlockingStub;

    @GrpcClient("backendTagStorageService")
    private ApiTagsWriteRepositoryGrpc.ApiTagsWriteRepositoryBlockingStub apiTagsWriteRepositoryBlockingStub;

    @GrpcClient("backendDocumentMetadataStorageService")
    private ApiDocumentMetadataWriteRepositoryGrpc.ApiDocumentMetadataWriteRepositoryBlockingStub apiDocumentMetadataWriteRepositoryBlockingStub;

    @GrpcClient("backendEmbeddingStorageService")
    private ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryBlockingStub apiEmbeddingWriteRepositoryBlockingStub;

    @Bean
    public AppsRepository appsRepository(GrpcAdapterMapper grpcAdapterMapper) {
        return new AppsRepositoryImpl(apiAppsWriteRepositoryBlockingStub, grpcAdapterMapper);
    }

    @Bean
    public DocumentRepository documentRepository(GrpcAdapterMapper grpcAdapterMapper) {
        return new DocumentRepositoryImpl(apiEmbeddingWriteRepositoryBlockingStub,
                apiDocumentMetadataWriteRepositoryBlockingStub,
                grpcAdapterMapper);
    }

    @Bean
    public TagsRepository tagsRepository(GrpcAdapterMapper grpcAdapterMapper) {
        return new TagsRepositoryImpl(apiTagsWriteRepositoryBlockingStub, grpcAdapterMapper);
    }


}
