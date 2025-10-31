package com.becker.freelance.component.prediction.ingest.adapter.storage;

import com.becker.freelance.component.prediction.backend.storage.ApiAppsWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.ApiEmbeddingWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.ApiSourceMetadataWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.storage.ApiTagsWriteRepositoryGrpc;
import com.becker.freelance.component.prediction.ingest.spi.AppsRepository;
import com.becker.freelance.component.prediction.ingest.spi.EmbeddingRepository;
import com.becker.freelance.component.prediction.ingest.spi.MetadataRepository;
import com.becker.freelance.component.prediction.ingest.spi.TagsRepository;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class StorageConfig {

    @GrpcClient("backendappstorageservice")
    private ApiAppsWriteRepositoryGrpc.ApiAppsWriteRepositoryBlockingStub apiAppsWriteRepositoryBlockingStub;

    @GrpcClient("backendtagstorageservice")
    private ApiTagsWriteRepositoryGrpc.ApiTagsWriteRepositoryBlockingStub apiTagsWriteRepositoryBlockingStub;

    @GrpcClient("backenddocumentmetadatastorageservice")
    private ApiSourceMetadataWriteRepositoryGrpc.ApiSourceMetadataWriteRepositoryBlockingStub apiSourceMetadataWriteRepositoryBlockingStub;

    @GrpcClient("backendembeddingstorageservice")
    private ApiEmbeddingWriteRepositoryGrpc.ApiEmbeddingWriteRepositoryBlockingStub apiEmbeddingWriteRepositoryBlockingStub;

    @Bean
    public AppsRepository appsRepository(GrpcAdapterMapper grpcAdapterMapper) {
        return new AppsRepositoryImpl(apiAppsWriteRepositoryBlockingStub, grpcAdapterMapper);
    }

    @Bean
    public MetadataRepository documentRepository(GrpcAdapterMapper grpcAdapterMapper) {
        return new MetadataRepositoryImpl(apiSourceMetadataWriteRepositoryBlockingStub, grpcAdapterMapper);
    }

    @Bean
    public TagsRepository tagsRepository(GrpcAdapterMapper grpcAdapterMapper) {
        return new TagsRepositoryImpl(apiTagsWriteRepositoryBlockingStub, grpcAdapterMapper);
    }

    @Bean
    public EmbeddingRepository embeddingRepository(GrpcAdapterMapper grpcAdapterMapper){
        return new EmbeddingRepositoryImpl(apiEmbeddingWriteRepositoryBlockingStub, grpcAdapterMapper);
    }


}
