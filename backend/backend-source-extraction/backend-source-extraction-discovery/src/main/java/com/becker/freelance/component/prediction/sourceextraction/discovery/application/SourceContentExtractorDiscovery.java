package com.becker.freelance.component.prediction.sourceextraction.discovery.application;

import com.becker.freelance.component.prediction.backend.SourceContentExtraction.ApiSourceContentExtractionServiceGrpc;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBufferFactory;
import com.becker.freelance.component.prediction.sourceextraction.discovery.api.SourceContentExtractor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class SourceContentExtractorDiscovery {

    private static final Logger logger = LoggerFactory.getLogger(SourceContentExtractorDiscovery.class);

    private final ByteArraysBufferFactory byteArraysBufferFactory;
    private final String contentExtractorServicePrefix;
    private final int contentExtractorServiceGrpcPort;

    public SourceContentExtractorDiscovery(String contentExtractorServicePrefix, int contentExtractorServiceGrpcPort) {
        this.byteArraysBufferFactory = ByteArraysBufferFactory.getInstance();
        this.contentExtractorServicePrefix = contentExtractorServicePrefix;
        this.contentExtractorServiceGrpcPort = contentExtractorServiceGrpcPort;
    }

    public List<SourceContentExtractor> findAll() {
        List<ManagedChannel> channels = new ArrayList<>();
        List<InetAddress> hosts = new ArrayList<>();
        int i = 1;
        while (true){
            String host = contentExtractorServicePrefix + "-" + i;
            try {
                InetAddress address = InetAddress.getByName(host);
                if (address.isReachable(1000)){
                    ManagedChannel channel = ManagedChannelBuilder
                            .forAddress(host, contentExtractorServiceGrpcPort)
                            .usePlaintext()
                            .build();
                    channels.add(channel);
                    hosts.add(address);
                }
            } catch (IOException e) {
                break;
            }
            i++;
        }

        logger.info("Found {} content extractor services with hosts: {}", channels.size(), hosts);

        return channels.stream()
                .map(ApiSourceContentExtractionServiceGrpc::newStub)
                .map(stub -> new SourceContentExtractorImpl(stub, byteArraysBufferFactory))
                .map(e -> (SourceContentExtractor) e)
                .toList();
    }
}
