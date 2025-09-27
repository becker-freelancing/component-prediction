package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.*;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;

import java.util.List;
import java.util.UUID;


@GrpcService
public class BackendStorageServiceImpl extends BackendStorageServiceApiGrpc.BackendStorageServiceApiImplBase {

    @Override
    public void findAll(FindAllDocumentsRequest request, StreamObserver<DocumentsResponse> responseObserver) {
        DocumentsResponse build = DocumentsResponse.newBuilder()
                .addAllDocuments(List.of(
                        DocumentResponse.newBuilder().setId(UUID.randomUUID().toString()).build(),
                        DocumentResponse.newBuilder().setId(UUID.randomUUID().toString()).build(),
                        DocumentResponse.newBuilder().setId(UUID.randomUUID().toString()).build()
                )).build();

        responseObserver.onNext(build);
        responseObserver.onCompleted();
    }
}
