package com.becker.freelance.component.prediction.storage.adapter.qdrant;

import com.becker.freelance.component.prediction.storage.spi.DocumentMetadataRepository;
import com.becker.freelance.component.prediction.storage.spi.EmbeddingRepository;
import io.qdrant.client.QdrantClient;
import io.qdrant.client.QdrantGrpcClient;
import io.qdrant.client.grpc.Collections;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class QdrantConfig {

    private static final Map<String, Collections.Datatype> DATATYPE_MAPPING = Map.of(
            "DEFAULT", Collections.Datatype.Default,
            "FLOAT32", Collections.Datatype.Float32,
            "UINT8", Collections.Datatype.Uint8,
            "FLOAT16", Collections.Datatype.Float16
    );

    private static final Map<String, Collections.Distance> DISTANCE_MAPPING = Map.of(
            "COSINE", Collections.Distance.Cosine,
            "EUCLID", Collections.Distance.Euclid,
            "DOT", Collections.Distance.Dot,
            "MANHATTAN", Collections.Distance.Manhattan
    );


    @Bean
    public QdrantClient qdrantClient(
            @Value("${storage.qdrant.host}") String qdrantHost,
            @Value("${storage.qdrant.port}") int qdrantPort
    ) {
        return new QdrantClient(QdrantGrpcClient.newBuilder(qdrantHost, qdrantPort, false).build());
    }

    @Bean
    public QdrantCollectionEnsurer qdrantCollectionEnsurer(
            QdrantClient qdrantClient,
            @Value("${storage.qdrant.vectorsize}") long vectorSize,
            @Value("${storage.qdrant.ondisk}") boolean onDisk,
            @Value("${storage.qdrant.distance}") String distance,
            @Value("${storage.qdrant.datatype}") String dataType
    ) {
        Collections.Distance parsedDistance = parseDistance(distance);
        Collections.Datatype parsedDataType = parseDataType(dataType);

        return new QdrantCollectionEnsurer(
                qdrantClient,
                vectorSize,
                parsedDistance,
                onDisk,
                parsedDataType
        );
    }

    @Bean
    public EmbeddingRepository embeddingRepository(
            DocumentMetadataRepository metadataRepository,
            QdrantClient qdrantClient,
            QdrantCollectionEnsurer qdrantCollectionEnsurer
    ) {
        return new QdrantEmbeddingRepository(metadataRepository, qdrantClient, qdrantCollectionEnsurer);
    }

    private Collections.Distance parseDistance(String distanceString) {
        Collections.Distance distance = DISTANCE_MAPPING.get(distanceString);
        if (distanceString == null) {
            throw new IllegalArgumentException("No distance found for '" + distance + "'. Allowed distances are '" + DISTANCE_MAPPING.keySet() + "'.");
        }
        return distance;
    }

    private Collections.Datatype parseDataType(String dataTypeString) {
        Collections.Datatype datatype = DATATYPE_MAPPING.get(dataTypeString);
        if (datatype == null) {
            throw new IllegalArgumentException("No datatype found for '" + dataTypeString + "'. Allowed datatypes are '" + DATATYPE_MAPPING.keySet() + "'.");
        }
        return datatype;
    }
}
