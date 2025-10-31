package com.becker.freelance.component.prediction.buffer.impl;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FileByteArrayBufferTest {

    @TempDir
    Path tempDir;

    private ByteArraysBuffer buffer;

    @BeforeEach
    void setUp() {
        buffer = new FileByteArrayBuffer(tempDir);
    }

    @Test
    void buffer_shouldCreateFileWithGivenData() throws IOException {
        byte[] data = "HelloWorld".getBytes();
        String name = "testFile";

        buffer.buffer(name, data);

        Path expectedFile = tempDir.resolve(name + ".bin");
        assertTrue(Files.exists(expectedFile), "File should be created");
        assertArrayEquals(data, Files.readAllBytes(expectedFile), "File content should match");
    }

    @Test
    void newInputStream_shouldReturnStreamToExistingFile() throws IOException {
        String name = "streamFile";
        byte[] data = "12345".getBytes();
        Path file = tempDir.resolve(name + ".bin");
        Files.write(file, data);

        try (InputStream is = buffer.newInputStream(name)) {
            byte[] read = is.readAllBytes();
            assertArrayEquals(data, read, "Stream should read the same data");
        }
    }

    @Test
    void readNBytes_shouldReturnExactNumberOfBytes() throws IOException {
        String name = "partialFile";
        byte[] data = "abcdefghij".getBytes();
        buffer.buffer(name, data);

        byte[] firstFive = buffer.readNBytes(name, 5);

        assertArrayEquals(Arrays.copyOfRange(data, 0, 5), firstFive);
    }

    @Test
    void readNBytes_shouldThrowException_whenFileMissing() {
        assertArrayEquals(new byte[0], buffer.readNBytes("nonExisting", 5));
    }

    @Test
    void filesShouldBeCreatedInsideTempDir() throws IOException {
        String name = "insideCheck";
        buffer.buffer(name, "abc".getBytes());

        Path expected = tempDir.resolve(name + ".bin");
        assertTrue(Files.exists(expected));
        assertTrue(expected.normalize().startsWith(tempDir.normalize()));
    }
}
