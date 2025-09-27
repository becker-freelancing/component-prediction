package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.BackendStorageServiceApiGrpc;
import com.becker.freelance.component.prediction.backend.storage.DocumentResponse;
import com.becker.freelance.component.prediction.backend.storage.DocumentsResponse;
import com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest;
import com.becker.freelance.component.prediction.gateway.api.DocumentDto;
import com.becker.freelance.component.prediction.gateway.api.DocumentId;
import com.becker.freelance.component.prediction.gateway.spi.StorageService;
import com.google.protobuf.ProtocolStringList;

import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

public class GrpcStorageService implements StorageService {

    private BackendStorageServiceApiGrpc.BackendStorageServiceApiBlockingStub stub;

    public GrpcStorageService(BackendStorageServiceApiGrpc.BackendStorageServiceApiBlockingStub stub) {
        this.stub = stub;
    }

    @Override
    public List<DocumentDto> findAll() {
        FindAllDocumentsRequest request = FindAllDocumentsRequest.newBuilder().build();
        DocumentsResponse response = stub.findAll(request);
        return map(response);
    }

    private List<DocumentDto> map(DocumentsResponse response) {
        return response.getDocumentsList().stream().map(this::map).toList();
    }

    private DocumentDto map(DocumentResponse response) {
        return new DocumentDto(
                replaceEmpty(response.getId(), DocumentId::new),
                replaceEmptyWithNull(response.getAppName()),
                replaceEmptyWithNull(response.getInAppActionPath()),
                replaceEmptyWithNull(response.getActionTitle()),
                replaceEmptyWithNull(response.getActionDescription()),
                replaceEmptyWithNull(response.getActionShortDescription()),
                map(response.getTagsList()),
                replaceEmptyWithNull(response.getLocale()),
                replaceEmptyWithNull(response.getVersion()),
                replaceEmptyWithNull(response.getCreator()),
                replaceEmpty(response.getCreatedAt(), LocalDateTime::parse)
        );
    }

    private List<String> map(ProtocolStringList list) {
        return list.stream().toList();
    }

    private String replaceEmptyWithNull(String s) {
        return replaceEmpty(s, t -> t);
    }

    private <T> T replaceEmpty(String s, Function<String, T> mapper) {
        return s.isEmpty() ? null : mapper.apply(s);
    }
}
