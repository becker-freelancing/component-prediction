package com.becker.freelance.component.prediction.ingest.adapter.extraction;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.*;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBufferFactory;
import com.becker.freelance.component.prediction.ingest.spi.SourceContentExtractor;
import com.google.protobuf.ByteString;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class SourceContentExtractorImpl implements SourceContentExtractor {

    private final ApiSourceContentExtractionServiceGrpc.ApiSourceContentExtractionServiceStub stub;
    private final ByteArraysBufferFactory byteArraysBufferFactory;

    public SourceContentExtractorImpl(ApiSourceContentExtractionServiceGrpc.ApiSourceContentExtractionServiceStub stub, ByteArraysBufferFactory byteArraysBufferFactory) {
        this.stub = stub;
        this.byteArraysBufferFactory = byteArraysBufferFactory;
    }

    @Override
    public boolean supportsOrReset(UUID extractionId) {
        SupportsStreamObserver supportsStreamObserver = new SupportsStreamObserver();
        CompletableFuture<Boolean> result = supportsStreamObserver.getResult();
        stub.supportsOrReset(map(extractionId), supportsStreamObserver);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Could not request supports", e);
        }
    }

    @Override
    public ByteArraysBuffer extract(UUID uuid) {
        ExtractStreamObserver extractStreamObserver = new ExtractStreamObserver(byteArraysBufferFactory.createNew());
        CompletableFuture<ByteArraysBuffer> result = extractStreamObserver.getResult();
        stub.extract(map(uuid), extractStreamObserver);
        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Could not extract", e);
        }
    }

    @Override
    public void buffer(UUID extractionId, InputStream inputStream) {
        EmptyStreamObserver emptyStreamObserver = new EmptyStreamObserver();
        CompletableFuture<Void> result = emptyStreamObserver.getResult();
        StreamObserver<GrpcSourceContentExtractionRequestChunk> outputStream = stub.buffer(emptyStreamObserver);

        byte[] buffer = new byte[1024];
        int length;
        try {
            while ((length = inputStream.read(buffer)) > 0){
                GrpcSourceContentExtractionRequestChunk chunk = GrpcSourceContentExtractionRequestChunk.newBuilder()
                        .setId(map(extractionId))
                        .setData(ByteString.copyFrom(buffer, 0, length))
                        .build();

                outputStream.onNext(chunk);
            }
        } catch (IOException e) {
            outputStream.onError(e);
            throw new IllegalStateException("Could not buffer", e);
        }

        outputStream.onCompleted();

        try {
            result.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Could not buffer", e);
        }
    }

    private GrpcSourceContentExtractionUUID map(UUID extractionId) {
        return GrpcSourceContentExtractionUUID.newBuilder().setId(extractionId.toString()).build();
    }

    @Override
    public UUID prepareNewExtraction() {
        PrepareStreamObserver prepareStreamObserver = new PrepareStreamObserver();
        CompletableFuture<GrpcSourceContentExtractionUUID> id = prepareStreamObserver.getResult();
        stub.prepareExtraction(Empty.newBuilder().build(), prepareStreamObserver);
        try {
            return UUID.fromString(id.get().getId());
        } catch (InterruptedException | ExecutionException e) {
            throw new IllegalStateException("Could not prepare extraction", e);
        }
    }

    private static class PrepareStreamObserver implements StreamObserver<GrpcSourceContentExtractionUUID> {

        private final CompletableFuture<GrpcSourceContentExtractionUUID> result = new CompletableFuture<>();

        @Override
        public void onNext(GrpcSourceContentExtractionUUID grpcSourceContentExtractionUUID) {
            result.complete(grpcSourceContentExtractionUUID);
        }

        @Override
        public void onError(Throwable throwable) {
            throw new IllegalStateException(throwable);
        }

        @Override
        public void onCompleted() {

        }

        public CompletableFuture<GrpcSourceContentExtractionUUID> getResult() {
            return result;
        }
    }

    private static class EmptyStreamObserver implements StreamObserver<Empty> {

        private final CompletableFuture<Void> result = new CompletableFuture<>();

        @Override
        public void onNext(Empty empty) {

        }

        @Override
        public void onError(Throwable throwable) {
            throw new IllegalStateException(throwable);
        }

        @Override
        public void onCompleted() {
            result.complete(null);
        }

        public CompletableFuture<Void> getResult() {
            return result;
        }
    }

    private static class SupportsStreamObserver implements StreamObserver<GrpcSourceContentExtractionSupportsResponse> {

        private final CompletableFuture<Boolean> result = new CompletableFuture<>();

        @Override
        public void onNext(GrpcSourceContentExtractionSupportsResponse empty) {
            result.complete(empty.getSupports());
        }

        @Override
        public void onError(Throwable throwable) {
            throw new IllegalStateException(throwable);
        }

        @Override
        public void onCompleted() {
        }

        public CompletableFuture<Boolean> getResult() {
            return result;
        }
    }

    private static class ExtractStreamObserver implements StreamObserver<GrpcSourceContentExtractionChunk> {

        private final ByteArraysBuffer buffer;
        private final String name;
        private final CompletableFuture<ByteArraysBuffer> result = new CompletableFuture<>();

        public ExtractStreamObserver(ByteArraysBuffer buffer) {
            this.buffer = buffer;
            this.name = UUID.randomUUID().toString();
        }

        @Override
        public void onNext(GrpcSourceContentExtractionChunk grpcSourceContentExtractionChunk) {
            buffer.buffer(name, grpcSourceContentExtractionChunk.getChunk().getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public void onError(Throwable throwable) {
            throw new IllegalStateException("Could not extract", throwable);
        }

        @Override
        public void onCompleted() {
            result.complete(buffer);
        }

        public CompletableFuture<ByteArraysBuffer> getResult() {
            return result;
        }
    }
}
