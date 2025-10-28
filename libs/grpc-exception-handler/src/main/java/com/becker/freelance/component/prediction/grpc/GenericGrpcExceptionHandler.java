package com.becker.freelance.component.prediction.grpc;

import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GenericGrpcExceptionHandler {

    @GrpcExceptionHandler(Exception.class)
    public StatusRuntimeException genericHandler(Exception e) {
        return Status.INTERNAL
                .withDescription(e.getMessage() + " (Original Exception Class: " + e.getClass() + ").")
                .withCause(e)
                .asRuntimeException();
    }
}
