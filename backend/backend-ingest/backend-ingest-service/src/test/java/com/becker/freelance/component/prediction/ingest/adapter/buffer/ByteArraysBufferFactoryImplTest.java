package com.becker.freelance.component.prediction.ingest.adapter.buffer;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.buffer.impl.ByteArraysBufferFactoryImpl;
import com.becker.freelance.component.prediction.buffer.impl.FileByteArrayBuffer;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class ByteArraysBufferFactoryImplTest {

    @Test
    void tempDir_shouldBeCreated_andExist() throws Exception {
        // Zugriff auf das private statische Feld TEMP_DIR
        Field tempDirField = ByteArraysBufferFactoryImpl.class.getDeclaredField("TEMP_DIR");
        tempDirField.setAccessible(true);
        Path tempDir = (Path) tempDirField.get(null);

        assertNotNull(tempDir, "TEMP_DIR should not be null");
        assertTrue(Files.exists(tempDir), "TEMP_DIR should exist");
        assertTrue(Files.isDirectory(tempDir), "TEMP_DIR should be a directory");
    }

    @Test
    void createNew_shouldReturnValidByteArraysBuffer() {
        ByteArraysBufferFactoryImpl factory = new ByteArraysBufferFactoryImpl();

        ByteArraysBuffer buffer = factory.createNew();

        assertNotNull(buffer, "createNew() should not return null");
        assertInstanceOf(FileByteArrayBuffer.class, buffer, "Returned buffer should be instance of FileByteArrayBuffer");
    }

    @Test
    void createNew_shouldUseSameTempDirectory() throws Exception {
        // Zugriff auf TEMP_DIR
        Field tempDirField = ByteArraysBufferFactoryImpl.class.getDeclaredField("TEMP_DIR");
        tempDirField.setAccessible(true);
        Path expectedTempDir = (Path) tempDirField.get(null);

        ByteArraysBufferFactoryImpl factory = new ByteArraysBufferFactoryImpl();
        FileByteArrayBuffer buffer = (FileByteArrayBuffer) factory.createNew();

        // Zugriff auf das interne Path-Feld von FileByteArrayBuffer (angenommen, es heißt 'tempDir')
        Field pathField = FileByteArrayBuffer.class.getDeclaredField("tempDir");
        pathField.setAccessible(true);
        Path actualTempDir = (Path) pathField.get(buffer);

        assertEquals(expectedTempDir, actualTempDir,
                "FileByteArrayBuffer should be created with the same TEMP_DIR");
    }
}
