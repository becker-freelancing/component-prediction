package com.becker.freelance.component.prediction.gateway.application;

import com.becker.freelance.component.prediction.gateway.api.SourceUploadHandler;
import com.becker.freelance.component.prediction.gateway.api.dto.SourceMetadataDto;
import com.becker.freelance.component.prediction.gateway.spi.SourceInputStreamHandler;
import com.becker.freelance.component.prediction.gateway.spi.SourceStorageService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class SourceUploadInteractor implements SourceUploadHandler {

    private final Map<UUID, SourceMetadataDto> preparedUploads;
    private final SourceStorageService sourceStorageService;

    public SourceUploadInteractor(SourceStorageService sourceStorageService) {
        this.sourceStorageService = sourceStorageService;
        this.preparedUploads = new ConcurrentHashMap<>();
    }

    @Override
    public UUID prepareSave(SourceMetadataDto sourceMetadataDto) {
        UUID uploadId = UUID.randomUUID();
        preparedUploads.put(uploadId, sourceMetadataDto);
        return uploadId;
    }

    @Override
    public SourceMetadataDto handleSave(UUID uploadId, InputStream inputStream) throws IOException, ExecutionException, InterruptedException {
        SourceMetadataDto sourceMetadataDto = preparedUploads.remove(uploadId);
        if (sourceMetadataDto == null){
            throw new IllegalStateException("No upload prepared with id " + uploadId);
        }

        SourceInputStreamHandler sourceInputStreamHandler = sourceStorageService.save(sourceMetadataDto);

        byte[] buffer = new byte[1024];

        try (ZipInputStream zis = new ZipInputStream(inputStream)){
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (entry.isDirectory()){
                    zis.closeEntry();
                    continue;
                }

                int len;
                while ((len = zis.read(buffer)) > 0){
                    sourceInputStreamHandler.nextPart(entry.getName(), buffer, len);
                }
            }
        }

        return sourceInputStreamHandler.completed();
    }
}
