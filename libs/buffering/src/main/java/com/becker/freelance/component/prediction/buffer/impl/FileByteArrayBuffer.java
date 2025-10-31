package com.becker.freelance.component.prediction.buffer.impl;


import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class FileByteArrayBuffer implements ByteArraysBuffer {

    private final Path tempDir;

    public FileByteArrayBuffer(Path tempDir) {
        this.tempDir = tempDir;
    }

    @Override
    public void buffer(String name, byte[] data) {
        try (FileOutputStream fos = new FileOutputStream(resolve(name), false)){
            fos.write(data);
        } catch (IOException e) {
            throw new IllegalStateException("Could not buffer", e);
        }
    }

    @Override
    public InputStream newInputStream(String name) {
        try {
            return new FileInputStream(resolve(name));
        } catch (IOException e) {
            throw new IllegalStateException("Could not read", e);
        }
    }

    @Override
    public byte[] readNBytes(String name, int n) {
        byte[] buffer = new byte[n];
        try (InputStream is = newInputStream(name)){
            int len = is.read(buffer);
            if (len == -1){
                return new byte[0];
            }
            return Arrays.copyOf(buffer, len);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read " + n + " bytes", e);
        }
    }

    @Override
    public void clearBuffer(String name) {
        try {
            Path path = Path.of(resolve(name).toURI());
            if (Files.exists(path)){
                Files.delete(path);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Could not delete Buffer", e);
        }
    }

    private File resolve(String name) throws IOException {
        Path path = tempDir.resolve(name + ".bin");
        if (!Files.exists(path)){
            Files.createDirectories(path.getParent());
            Files.createFile(path);
        }
        return path.toFile();
    }

}
