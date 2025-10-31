package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.ingest.ApiSourceIngestRepositoryGrpc;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestSourceChunk;
import com.becker.freelance.component.prediction.backend.ingest.GrpcIngestUUID;
import com.becker.freelance.component.prediction.backend.query.GrpcQuerySourceMetadata;
import com.becker.freelance.component.prediction.gateway.api.dto.SourceMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.SourceInputStreamHandler;
import com.google.protobuf.ByteString;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class SourceInputStreamHandlerImpl implements SourceInputStreamHandler {

    private final GrpcMapper grpcMapper;
    private final GrpcIngestUUID handleSaveId;
    private final ResultStreamObserver resultStreamObserver;
    private final StreamObserver<GrpcIngestSourceChunk> chunkStreamObserver;

    public SourceInputStreamHandlerImpl(GrpcMapper grpcMapper, GrpcIngestUUID handleSaveId, ApiSourceIngestRepositoryGrpc.ApiSourceIngestRepositoryStub writeStub) {
        this.grpcMapper = grpcMapper;
        this.handleSaveId = handleSaveId;
        this.resultStreamObserver = new ResultStreamObserver();
        this.chunkStreamObserver = writeStub.handleSave(resultStreamObserver);
    }

    @Override
    public void nextPart(String fileName, byte[] part, int len) {
        chunkStreamObserver.onNext(map(fileName, part, len));
    }

    private GrpcIngestSourceChunk map(String fileName, byte[] part, int len){
        GrpcIngestSourceChunk.Builder builder = GrpcIngestSourceChunk.newBuilder()
                .setFileName(fileName)
                .setUploadId(handleSaveId);
        if (len == 0){
            return builder
                    .setData(ByteString.EMPTY)
                    .build();
        }
        return builder
                .setData(ByteString.copyFrom(part, 0, len))
                .build();
    }

    @Override
    public SourceMetadataDto completed() {
        return grpcMapper.map(resultStreamObserver.getMetadata());
    }

    @Override
    public UUID getHandleSaveId() {
        return UUID.fromString(handleSaveId.getId());
    }

    private static class ResultStreamObserver implements StreamObserver<GrpcQuerySourceMetadata> {

        private GrpcQuerySourceMetadata metadata;

        @Override
        public void onNext(GrpcQuerySourceMetadata grpcIngestSourceMetadata) {
            this.metadata = grpcIngestSourceMetadata;
        }

        @Override
        public void onError(Throwable throwable) {
            throw new IllegalStateException(throwable);
        }

        @Override
        public void onCompleted() {

        }

        public GrpcQuerySourceMetadata getMetadata() {
            return metadata;
        }
    }
}
