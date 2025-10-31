package com.becker.freelance.component.prediction.storage.api;

import com.becker.freelance.component.prediction.backend.storage.GrpcSourceMetadata;
import com.becker.freelance.component.prediction.backend.storage.GrpcSourceMetadataList;
import com.becker.freelance.component.prediction.backend.storage.GrpcUUID;
import com.becker.freelance.component.prediction.storage.domain.SourceMetadata;
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

class SourceMetadataReadStorageApiTest {

    private DocumentMetadataRepository metadataRepository;
    private SourceMetadataReadStorageApi storageApi;

    @BeforeEach
    void setUp() {
        metadataRepository = Mockito.mock(DocumentMetadataRepository.class);
        storageApi = new SourceMetadataReadStorageApi(metadataRepository);
    }

    @Test
    void findAllWithEmptyResult() {
        Mockito.doReturn(List.of()).when(metadataRepository).findAll();

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findAll(Empty.getDefaultInstance(), streamObserver);

        GrpcSourceMetadataList expected = GrpcSourceMetadataList.newBuilder().build();

        Mockito.verify(streamObserver, Mockito.times(1)).onNext(expected);
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    @Test
    void findAll() {
        UUID id1 = UUID.randomUUID();
        UUID id2 = UUID.randomUUID();
        SourceMetadata metadata1 = new SourceMetadata();
        metadata1.setId(id1);
        SourceMetadata metadata2 = new SourceMetadata();
        metadata2.setId(id2);

        Mockito.doReturn(List.of(metadata1, metadata2)).when(metadataRepository).findAll();

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findAll(Empty.getDefaultInstance(), streamObserver);

        GrpcMapper grpcMapper = new GrpcMapper();
        GrpcSourceMetadataList expected = GrpcSourceMetadataList.newBuilder()
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
        SourceMetadata metadata1 = new SourceMetadata();
        metadata1.setId(id1);
        Mockito.when(metadataRepository.findById(Mockito.any())).thenReturn(Optional.of(metadata1));

        StreamObserver streamObserver = Mockito.mock(StreamObserver.class);

        storageApi.findById(GrpcUUID.newBuilder().build(), streamObserver);

        Mockito.verify(streamObserver, Mockito.times(1)).onNext(Mockito.any());
        Mockito.verify(streamObserver, Mockito.times(1)).onCompleted();
    }

    private record StreamObserverAssertion(
            GrpcSourceMetadata expected) implements StreamObserver<GrpcSourceMetadata> {
        @Override
        public void onNext(GrpcSourceMetadata GrpcSourceMetadata) {
            assertEquals(expected(), GrpcSourceMetadata);
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