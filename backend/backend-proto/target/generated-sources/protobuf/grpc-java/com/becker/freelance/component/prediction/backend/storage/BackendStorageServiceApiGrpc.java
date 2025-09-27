package com.becker.freelance.component.prediction.backend.storage;

import static io.grpc.MethodDescriptor.generateFullMethodName;

/**
 */
@javax.annotation.Generated(
    value = "by gRPC proto compiler (version 1.54.0)",
    comments = "Source: com/becker/freelance/component/prediction/backend/storage/storage.proto")
@io.grpc.stub.annotations.GrpcGenerated
public final class BackendStorageServiceApiGrpc {

  private BackendStorageServiceApiGrpc() {}

  public static final String SERVICE_NAME = "com.becker.freelance.component.prediction.backend.storage.BackendStorageServiceApi";

  // Static method descriptors that strictly reflect the proto.
  private static volatile io.grpc.MethodDescriptor<com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest,
      com.becker.freelance.component.prediction.backend.storage.DocumentsResponse> getFindAllMethod;

  @io.grpc.stub.annotations.RpcMethod(
      fullMethodName = SERVICE_NAME + '/' + "FindAll",
      requestType = com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest.class,
      responseType = com.becker.freelance.component.prediction.backend.storage.DocumentsResponse.class,
      methodType = io.grpc.MethodDescriptor.MethodType.UNARY)
  public static io.grpc.MethodDescriptor<com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest,
      com.becker.freelance.component.prediction.backend.storage.DocumentsResponse> getFindAllMethod() {
    io.grpc.MethodDescriptor<com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest, com.becker.freelance.component.prediction.backend.storage.DocumentsResponse> getFindAllMethod;
    if ((getFindAllMethod = BackendStorageServiceApiGrpc.getFindAllMethod) == null) {
      synchronized (BackendStorageServiceApiGrpc.class) {
        if ((getFindAllMethod = BackendStorageServiceApiGrpc.getFindAllMethod) == null) {
          BackendStorageServiceApiGrpc.getFindAllMethod = getFindAllMethod =
              io.grpc.MethodDescriptor.<com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest, com.becker.freelance.component.prediction.backend.storage.DocumentsResponse>newBuilder()
              .setType(io.grpc.MethodDescriptor.MethodType.UNARY)
              .setFullMethodName(generateFullMethodName(SERVICE_NAME, "FindAll"))
              .setSampledToLocalTracing(true)
              .setRequestMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest.getDefaultInstance()))
              .setResponseMarshaller(io.grpc.protobuf.ProtoUtils.marshaller(
                  com.becker.freelance.component.prediction.backend.storage.DocumentsResponse.getDefaultInstance()))
              .setSchemaDescriptor(new BackendStorageServiceApiMethodDescriptorSupplier("FindAll"))
              .build();
        }
      }
    }
    return getFindAllMethod;
  }

  /**
   * Creates a new async stub that supports all call types for the service
   */
  public static BackendStorageServiceApiStub newStub(io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BackendStorageServiceApiStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BackendStorageServiceApiStub>() {
        @java.lang.Override
        public BackendStorageServiceApiStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BackendStorageServiceApiStub(channel, callOptions);
        }
      };
    return BackendStorageServiceApiStub.newStub(factory, channel);
  }

  /**
   * Creates a new blocking-style stub that supports unary and streaming output calls on the service
   */
  public static BackendStorageServiceApiBlockingStub newBlockingStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BackendStorageServiceApiBlockingStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BackendStorageServiceApiBlockingStub>() {
        @java.lang.Override
        public BackendStorageServiceApiBlockingStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BackendStorageServiceApiBlockingStub(channel, callOptions);
        }
      };
    return BackendStorageServiceApiBlockingStub.newStub(factory, channel);
  }

  /**
   * Creates a new ListenableFuture-style stub that supports unary calls on the service
   */
  public static BackendStorageServiceApiFutureStub newFutureStub(
      io.grpc.Channel channel) {
    io.grpc.stub.AbstractStub.StubFactory<BackendStorageServiceApiFutureStub> factory =
      new io.grpc.stub.AbstractStub.StubFactory<BackendStorageServiceApiFutureStub>() {
        @java.lang.Override
        public BackendStorageServiceApiFutureStub newStub(io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
          return new BackendStorageServiceApiFutureStub(channel, callOptions);
        }
      };
    return BackendStorageServiceApiFutureStub.newStub(factory, channel);
  }

  /**
   */
  public interface AsyncService {

    /**
     */
    default void findAll(com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest request,
        io.grpc.stub.StreamObserver<com.becker.freelance.component.prediction.backend.storage.DocumentsResponse> responseObserver) {
      io.grpc.stub.ServerCalls.asyncUnimplementedUnaryCall(getFindAllMethod(), responseObserver);
    }
  }

  /**
   * Base class for the server implementation of the service BackendStorageServiceApi.
   */
  public static abstract class BackendStorageServiceApiImplBase
      implements io.grpc.BindableService, AsyncService {

    @java.lang.Override public final io.grpc.ServerServiceDefinition bindService() {
      return BackendStorageServiceApiGrpc.bindService(this);
    }
  }

  /**
   * A stub to allow clients to do asynchronous rpc calls to service BackendStorageServiceApi.
   */
  public static final class BackendStorageServiceApiStub
      extends io.grpc.stub.AbstractAsyncStub<BackendStorageServiceApiStub> {
    private BackendStorageServiceApiStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BackendStorageServiceApiStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BackendStorageServiceApiStub(channel, callOptions);
    }

    /**
     */
    public void findAll(com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest request,
        io.grpc.stub.StreamObserver<com.becker.freelance.component.prediction.backend.storage.DocumentsResponse> responseObserver) {
      io.grpc.stub.ClientCalls.asyncUnaryCall(
          getChannel().newCall(getFindAllMethod(), getCallOptions()), request, responseObserver);
    }
  }

  /**
   * A stub to allow clients to do synchronous rpc calls to service BackendStorageServiceApi.
   */
  public static final class BackendStorageServiceApiBlockingStub
      extends io.grpc.stub.AbstractBlockingStub<BackendStorageServiceApiBlockingStub> {
    private BackendStorageServiceApiBlockingStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BackendStorageServiceApiBlockingStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BackendStorageServiceApiBlockingStub(channel, callOptions);
    }

    /**
     */
    public com.becker.freelance.component.prediction.backend.storage.DocumentsResponse findAll(com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest request) {
      return io.grpc.stub.ClientCalls.blockingUnaryCall(
          getChannel(), getFindAllMethod(), getCallOptions(), request);
    }
  }

  /**
   * A stub to allow clients to do ListenableFuture-style rpc calls to service BackendStorageServiceApi.
   */
  public static final class BackendStorageServiceApiFutureStub
      extends io.grpc.stub.AbstractFutureStub<BackendStorageServiceApiFutureStub> {
    private BackendStorageServiceApiFutureStub(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      super(channel, callOptions);
    }

    @java.lang.Override
    protected BackendStorageServiceApiFutureStub build(
        io.grpc.Channel channel, io.grpc.CallOptions callOptions) {
      return new BackendStorageServiceApiFutureStub(channel, callOptions);
    }

    /**
     */
    public com.google.common.util.concurrent.ListenableFuture<com.becker.freelance.component.prediction.backend.storage.DocumentsResponse> findAll(
        com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest request) {
      return io.grpc.stub.ClientCalls.futureUnaryCall(
          getChannel().newCall(getFindAllMethod(), getCallOptions()), request);
    }
  }

  private static final int METHODID_FIND_ALL = 0;

  private static final class MethodHandlers<Req, Resp> implements
      io.grpc.stub.ServerCalls.UnaryMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ServerStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.ClientStreamingMethod<Req, Resp>,
      io.grpc.stub.ServerCalls.BidiStreamingMethod<Req, Resp> {
    private final AsyncService serviceImpl;
    private final int methodId;

    MethodHandlers(AsyncService serviceImpl, int methodId) {
      this.serviceImpl = serviceImpl;
      this.methodId = methodId;
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public void invoke(Req request, io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        case METHODID_FIND_ALL:
          serviceImpl.findAll((com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest) request,
              (io.grpc.stub.StreamObserver<com.becker.freelance.component.prediction.backend.storage.DocumentsResponse>) responseObserver);
          break;
        default:
          throw new AssertionError();
      }
    }

    @java.lang.Override
    @java.lang.SuppressWarnings("unchecked")
    public io.grpc.stub.StreamObserver<Req> invoke(
        io.grpc.stub.StreamObserver<Resp> responseObserver) {
      switch (methodId) {
        default:
          throw new AssertionError();
      }
    }
  }

  public static final io.grpc.ServerServiceDefinition bindService(AsyncService service) {
    return io.grpc.ServerServiceDefinition.builder(getServiceDescriptor())
        .addMethod(
          getFindAllMethod(),
          io.grpc.stub.ServerCalls.asyncUnaryCall(
            new MethodHandlers<
              com.becker.freelance.component.prediction.backend.storage.FindAllDocumentsRequest,
              com.becker.freelance.component.prediction.backend.storage.DocumentsResponse>(
                service, METHODID_FIND_ALL)))
        .build();
  }

  private static abstract class BackendStorageServiceApiBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoFileDescriptorSupplier, io.grpc.protobuf.ProtoServiceDescriptorSupplier {
    BackendStorageServiceApiBaseDescriptorSupplier() {}

    @java.lang.Override
    public com.google.protobuf.Descriptors.FileDescriptor getFileDescriptor() {
      return com.becker.freelance.component.prediction.backend.storage.Storage.getDescriptor();
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.ServiceDescriptor getServiceDescriptor() {
      return getFileDescriptor().findServiceByName("BackendStorageServiceApi");
    }
  }

  private static final class BackendStorageServiceApiFileDescriptorSupplier
      extends BackendStorageServiceApiBaseDescriptorSupplier {
    BackendStorageServiceApiFileDescriptorSupplier() {}
  }

  private static final class BackendStorageServiceApiMethodDescriptorSupplier
      extends BackendStorageServiceApiBaseDescriptorSupplier
      implements io.grpc.protobuf.ProtoMethodDescriptorSupplier {
    private final String methodName;

    BackendStorageServiceApiMethodDescriptorSupplier(String methodName) {
      this.methodName = methodName;
    }

    @java.lang.Override
    public com.google.protobuf.Descriptors.MethodDescriptor getMethodDescriptor() {
      return getServiceDescriptor().findMethodByName(methodName);
    }
  }

  private static volatile io.grpc.ServiceDescriptor serviceDescriptor;

  public static io.grpc.ServiceDescriptor getServiceDescriptor() {
    io.grpc.ServiceDescriptor result = serviceDescriptor;
    if (result == null) {
      synchronized (BackendStorageServiceApiGrpc.class) {
        result = serviceDescriptor;
        if (result == null) {
          serviceDescriptor = result = io.grpc.ServiceDescriptor.newBuilder(SERVICE_NAME)
              .setSchemaDescriptor(new BackendStorageServiceApiFileDescriptorSupplier())
              .addMethod(getFindAllMethod())
              .build();
        }
      }
    }
    return result;
  }
}
