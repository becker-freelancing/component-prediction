package com.becker.freelance.component.prediction.extraction.api;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.*;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBufferFactory;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

public class ApiManualInputExtractionService extends ApiSourceContentExtractionServiceGrpc.ApiSourceContentExtractionServiceImplBase {

    private final ManualInputExtractionService manualInputExtractionService;
    private final ByteArraysBuffer buffer;
    private final String id;

    public ApiManualInputExtractionService(ManualInputExtractionService manualInputExtractionService, String id) {
        this.manualInputExtractionService = manualInputExtractionService;
        this.id = id;
        this.buffer = ByteArraysBufferFactory.getInstance().createNew();
    }

    @Override
    public void prepareExtraction(Empty request, StreamObserver<GrpcSourceContentExtractionUUID> responseObserver) {
        UUID id = manualInputExtractionService.prepareNewExtraction();
        responseObserver.onNext(GrpcSourceContentExtractionUUID.newBuilder()
                .setId(id.toString())
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public StreamObserver<GrpcSourceContentExtractionRequestChunk> buffer(StreamObserver<Empty> responseObserver) {
        return new BufferingStreamObserver(buffer);
    }

    @Override
    public void supportsOrReset(GrpcSourceContentExtractionUUID request, StreamObserver<GrpcSourceContentExtractionSupportsResponse> responseObserver) {
        UUID extractionId = UUID.fromString(request.getId());
        boolean supports = manualInputExtractionService.supports(extractionId, buffer);
        if (!supports){
            buffer.clearBuffer(extractionId.toString());
        }
        responseObserver.onNext(GrpcSourceContentExtractionSupportsResponse.newBuilder()
                .setSupports(supports)
                .build());
        responseObserver.onCompleted();
    }

    @Override
    public void extract(GrpcSourceContentExtractionUUID request, StreamObserver<GrpcSourceContentExtractionChunk> responseObserver) {

        manualInputExtractionService.extract(
                UUID.fromString(request.getId()),
                buffer,
                new ExtractionConsumerImpl(request, responseObserver)
        );
    }

    @Override
    public void clearBuffer(GrpcSourceContentExtractionUUID request, StreamObserver<Empty> responseObserver) {
        buffer.clearBuffer(request.getId());
        responseObserver.onCompleted();
    }

    @Override
    public void getId(Empty request, StreamObserver<GrpcSourceContentExtractorId> responseObserver) {
        GrpcSourceContentExtractorId extractorId = GrpcSourceContentExtractorId.newBuilder()
                .setId(id)
                .build();

        responseObserver.onNext(extractorId);
        responseObserver.onCompleted();
    }

    static class ExtractionConsumerImpl implements ExtractionConsumer {

        private final GrpcSourceContentExtractionUUID extractionId;
        private final StreamObserver<GrpcSourceContentExtractionChunk> streamObserver;

        public ExtractionConsumerImpl(GrpcSourceContentExtractionUUID extractionId, StreamObserver<GrpcSourceContentExtractionChunk> streamObserver) {
            this.extractionId = extractionId;
            this.streamObserver = streamObserver;
        }

        @Override
        public void onCompleted() {
            streamObserver.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            streamObserver.onError(e);
        }

        @Override
        public void accept(String s) {
            streamObserver.onNext(GrpcSourceContentExtractionChunk.newBuilder()
                    .setId(extractionId)
                    .setChunk(s)
                    .build());
        }
    }
}
