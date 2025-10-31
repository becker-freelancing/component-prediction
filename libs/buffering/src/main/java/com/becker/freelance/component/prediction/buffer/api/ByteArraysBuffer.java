package com.becker.freelance.component.prediction.buffer.api;

import java.io.InputStream;

public interface ByteArraysBuffer {
    void buffer(String name, byte[] data);

    InputStream newInputStream(String name);

    byte[] readNBytes(String name, int n);

    void clearBuffer(String name);
}
