package com.becker.freelance.component.prediction.gateway.adapter.grcp;

import com.becker.freelance.component.prediction.backend.storage.BackendStorageServiceApiGrpc;
import com.becker.freelance.component.prediction.backend.storage.DocumentResponse;
import com.becker.freelance.component.prediction.backend.storage.DocumentsResponse;
import com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest;
import com.becker.freelance.component.prediction.gateway.api.DocumentDto;
import com.becker.freelance.component.prediction.gateway.spi.StorageService;
import com.google.protobuf.ProtocolStringList;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrpcStorageService implements StorageService {

    @GrpcClient("backendStorageService")
    private BackendStorageServiceApiGrpc.BackendStorageServiceApiBlockingStub stub;

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
                response.getId(),
                response.getAppName(),
                response.getInAppActionPath(),
                response.getActionTitle(),
                response.getActionDescription(),
                response.getActionShortDescription(),
                map(response.getTagsList()),
                response.getLocale(),
                response.getVersion(),
                response.getCreator(),
                response.getCreatedAt()
        );
    }

    private List<String> map(ProtocolStringList list) {
        return list.stream().toList();
    }
}
