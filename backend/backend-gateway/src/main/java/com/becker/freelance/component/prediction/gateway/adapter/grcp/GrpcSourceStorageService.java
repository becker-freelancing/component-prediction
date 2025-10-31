package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.ApiSourceIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestUUID;
import com.becker.freelance.component.prediction.backend.query.ApiSourceMetadataReadRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.query.GrpcQuerySourceMetadata;
import com.becker.freelance.component.prediction.gateway.api.dto.SourceMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.SourceInputStreamHandler;
import com.becker.freelance.component.prediction.gateway.spi.SourceStorageService;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class GrpcSourceStorageService implements SourceStorageService {

    private final ApiSourceIngestRepositoryGrpc.ApiSourceIngestRepositoryStub writeStub;
    private final ApiSourceMetadataReadRepositoryGrpc.ApiSourceMetadataReadRepositoryBlockingStub readStub;
    private final GrpcMapper mapper;

    public GrpcSourceStorageService(ApiSourceIngestRepositoryGrpc.ApiSourceIngestRepositoryStub writeStub, ApiSourceMetadataReadRepositoryGrpc.ApiSourceMetadataReadRepositoryBlockingStub readStub) {
        this.writeStub = writeStub;
        this.readStub = readStub;
        this.mapper = new GrpcMapper();
    }

    @Override
    public SourceInputStreamHandler save(SourceMetadataDto sourceMetadataDto) throws ExecutionException, InterruptedException {

        PrepareSaveObserver prepareSaveObserver = new PrepareSaveObserver();
        Future<GrpcIngestUUID> completablePrepareSaveId = prepareSaveObserver.getResult();
        writeStub.prepareSave(mapper.map(sourceMetadataDto), prepareSaveObserver);
        GrpcIngestUUID handleSaveId = completablePrepareSaveId.get();

        return new SourceInputStreamHandlerImpl(mapper, handleSaveId, writeStub);
    }

    @Override
    public List<SourceMetadataDto> findAll() {
        return readStub.findAll(Empty.getDefaultInstance()).getMetadataList().stream()
                .map(mapper::map)
                .toList();
    }

    @Override
    public SourceMetadataDto findById(UUID id) {
        GrpcQuerySourceMetadata byId = readStub.findById(mapper.mapForQuery(id));
        return mapper.map(byId);
    }

    private static class PrepareSaveObserver implements StreamObserver<GrpcIngestUUID> {

        private final CompletableFuture<GrpcIngestUUID> future;
        private GrpcIngestUUID grpcIngestUUID;


        public PrepareSaveObserver() {
            this.future = new CompletableFuture<>();
        }

        public Future<GrpcIngestUUID> getResult(){
            return this.future;
        }

        @Override
        public void onNext(GrpcIngestUUID grpcIngestUUID) {
            if (this.grpcIngestUUID != null){
                throw new IllegalStateException("Only one onNext call expected");
            }
            this.grpcIngestUUID = grpcIngestUUID;
        }

        @Override
        public void onError(Throwable throwable) {
            throw new IllegalStateException("Could not prepare save", throwable);
        }

        @Override
        public void onCompleted() {
            if (grpcIngestUUID == null){
                throw new IllegalStateException("No handle ID received");
            }
            future.complete(grpcIngestUUID);
        }
    }

}
