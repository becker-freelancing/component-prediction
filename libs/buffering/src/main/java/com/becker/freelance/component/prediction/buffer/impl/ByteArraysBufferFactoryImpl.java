package com.becker.freelance.component.prediction.buffer.impl;

import com.becker.freelance.component.prediction.buffer.api.ByteArraysBuffer;
import com.becker.freelance.component.prediction.buffer.api.ByteArraysBufferFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

public class ByteArraysBufferFactoryImpl implements ByteArraysBufferFactory {

    private static final Path TEMP_DIR;

    static {
        try {
            TEMP_DIR = Files.createTempDirectory("component-prediction." + UUID.randomUUID());
        } catch (IOException e) {
            throw new IllegalStateException("Could not create Temp Dir", e);
        }
    }

    @Override
    public ByteArraysBuffer createNew() {
        return new FileByteArrayBuffer(TEMP_DIR);
    }
}
