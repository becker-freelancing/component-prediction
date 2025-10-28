package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadata;
import com.becker.freelance.component.prediction.backend.storage.GrpcDocumentMetadataList;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.storage.domain.DocumentMetadata;
import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import com.google.protobuf.Empty;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.opentest4j.AssertionFailedError;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DocumentMetadataReadStorageApiTest {

    private DocumentMetadataRepository metadataRepository;
    private DocumentMetadataReadStorageApi storageApi;

    @BeforeEach
    void setUp() {
        metadataRepository = Mockito.mock(DocumentMetadataRepository.class);
        storageApi = new DocumentMetadataReadStorageApi(metadataRepository);
    }

    @Test
    void findAllWithEmptyResult() {
        Mockito.doReturn(List.of()).when(metadataRepository).findAll();

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findAll(Empty.getDefaultInstance(), streamObserver);

        GrpcDocumentMetadataList expected = GrpcDocumentMetadataList.newBuilder().build();

        Mockito.verify(streamObserver, Mockito.times(1)).onNext(expected);
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void findAll() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        DocumentMetadata metadata1 = new DocumentMetadata();
        metadata1.setId(id1);
        DocumentMetadata metadata2 = new DocumentMetadata();
        metadata2.setId(id2);

        Mockito.doReturn(List.of(metadata1, metadata2)).when(metadataRepository).findAll();

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findAll(Empty.getDefaultInstance(), streamObserver);

        GrpcMapper grpcMapper = new GrpcMapper();
        GrpcDocumentMetadataList expected = GrpcDocumentMetadataList.newBuilder()
                .addAllMetadata(Stream.of(metadata1, metadata2).map(grpcMapper::mapOutgoing).toList())
                .build();

        Mockito.verify(streamObserver, Mockito.times(1)).onNext(expected);
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void findByIdIfNotFound() {
        Mockito.when(metadataRepository.findById(Mockito.any())).thenReturn(Optional.empty());


        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findById(GrpcUUID.newBuilder().build(), streamObserver);

        Mockito.verify(streamObserver, Mockito.times(0)).onNext(Mockito.any());
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void findById() {
        UUID id1 = UUID.randomUUID();
        DocumentMetadata metadata1 = new DocumentMetadata();
        metadata1.setId(id1);
        Mockito.when(metadataRepository.findById(Mockito.any())).thenReturn(Optional.of(metadata1));

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findById(GrpcUUID.newBuilder().build(), streamObserver);

        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    private record StreamObserverAssertion(
            GrpcDocumentMetadata expected) implements StreamObserver<GrpcDocumentMetadata> {
        @Override
        public void onNext(GrpcDocumentMetadata grpcDocumentMetadata) {
            assertEquals(expected(), grpcDocumentMetadata);
        }

        @Override
        public void onError(Throwable throwable) {
            throw new AssertionFailedError();
        }

        @Override
        public void onCompleted() {

        }
    }

}