package com.becker.freelance.component.prediction.buffer.api;

import com.becker.freelance.component.prediction.buffer.impl.ByteArraysBufferFactoryImpl;

public interface ByteArraysBufferFactory {

    public static ByteArraysBufferFactory getInstance(){
        return new ByteArraysBufferFactoryImpl();
    }

    public ByteArraysBuffer createNew();
}
