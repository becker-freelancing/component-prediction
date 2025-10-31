package com.becker.freelance.component.prediction.sourceextraction.discovery.application;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.ApiSourceContentExtractionServiceGrpc;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class SourceContentExtractorDiscoveryTest {

    @Test
    void findAll_shouldDiscoverMultipleServices() throws Exception {
        // Arrange
        InetAddress addr1 = mock(InetAddress.class);
        InetAddress addr2 = mock(InetAddress.class);

        when(addr1.isReachable(anyInt())).thenReturn(true);
        when(addr2.isReachable(anyInt())).thenReturn(true);

        // Mock InetAddress.getByName to return our fake addresses for 1 and 2, then throw for 3
        try (MockedStatic<InetAddress> inetMock = mockStatic(InetAddress.class)) {
            inetMock.when(() -> InetAddress.getByName(eq("extractor-1"))).thenReturn(addr1);
            inetMock.when(() -> InetAddress.getByName(eq("extractor-2"))).thenReturn(addr2);
            inetMock.when(() -> InetAddress.getByName(eq("extractor-3"))).thenThrow(new UnknownHostException("no more hosts"));

            // We also mock ManagedChannelBuilder
            ManagedChannel channel = mock(ManagedChannel.class);
            try (MockedStatic<ManagedChannelBuilder> channelMock = mockStatic(ManagedChannelBuilder.class)) {
                ManagedChannelBuilder<?> builder = mock(ManagedChannelBuilder.class, RETURNS_SELF);
                when(builder.build()).thenReturn(channel);
                channelMock.when(() -> ManagedChannelBuilder.forAddress(anyString(), anyInt())).thenReturn(builder);

                try (MockedStatic<ApiSourceContentExtractionServiceGrpc> grpcMock = mockStatic(ApiSourceContentExtractionServiceGrpc.class)) {
                    ApiSourceContentExtractionServiceGrpc.ApiSourceContentExtractionServiceStub stub =
                            mock(ApiSourceContentExtractionServiceGrpc.ApiSourceContentExtractionServiceStub.class);
                    grpcMock.when(() -> ApiSourceContentExtractionServiceGrpc.newStub(any(ManagedChannel.class)))
                            .thenReturn(stub);

                    // Act
                    SourceContentExtractorDiscovery discovery = new SourceContentExtractorDiscovery("extractor", 6565);
                    List<SourceContentExtractor> extractors = discovery.findAll();

                    // Assert
                    assertEquals(2, extractors.size(), "Should find 2 extractors");
                    grpcMock.verify(() -> ApiSourceContentExtractionServiceGrpc.newStub(channel), atLeastOnce());
                    channelMock.verify(() -> ManagedChannelBuilder.forAddress(anyString(), eq(6565)), atLeast(2));
                }
            }
        }
    }

    @Test
    void findAll_shouldReturnEmptyList_whenNoServiceReachable() throws Exception {
        InetAddress addr = mock(InetAddress.class);
        when(addr.isReachable(anyInt())).thenReturn(false);

        try (MockedStatic<InetAddress> inetMock = mockStatic(InetAddress.class)) {
            inetMock.when(() -> InetAddress.getByName(eq("extractor-1")))
                    .thenThrow(new UnknownHostException("unreachable"));

            SourceContentExtractorDiscovery discovery =
                    new SourceContentExtractorDiscovery("extractor", 6565);

            List<SourceContentExtractor> extractors = discovery.findAll();

            assertTrue(extractors.isEmpty(), "Should return empty list when nothing reachable");
        }
    }
}
