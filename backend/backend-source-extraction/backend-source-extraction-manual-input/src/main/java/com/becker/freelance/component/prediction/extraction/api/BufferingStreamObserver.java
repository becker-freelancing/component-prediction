package com.becker.freelance.component.prediction.extraction.api;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.GrpcSourceContentExtractionRequestChunk;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import io.grpc.stub.StreamObserver;

import java.util.UUID;

public class BufferingStreamObserver implements StreamObserver<GrpcSourceContentExtractionRequestChunk> {

    private final ByteArraysBuffer buffer;

    public BufferingStreamObserver(ByteArraysBuffer buffer) {
        this.buffer = buffer;
    }

    @Override
    public void onNext(GrpcSourceContentExtractionRequestChunk grpcSourceContentExtractionRequestChunk) {
        UUID id = UUID.fromString(grpcSourceContentExtractionRequestChunk.getId().getId());
        byte[] data = grpcSourceContentExtractionRequestChunk.getData().toByteArray();

        buffer.buffer(id.toString(), data);
    }

    @Override
    public void onError(Throwable throwable) {
    }

    @Override
    public void onCompleted() {

    }
}
