package com.binghe.keyvalue;

import com.binghe.proto.KeyValueRequest;
import com.binghe.proto.KeyValueResponse;
import com.binghe.proto.KeyValueServiceGrpc;
import compression.GzipCompression;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import net.devh.boot.grpc.server.service.GrpcService;

@RequiredArgsConstructor
@GrpcService
public class KeyValueGrpcServiceImpl extends KeyValueServiceGrpc.KeyValueServiceImplBase {

    @Override
    public void getValueByKey(KeyValueRequest request, StreamObserver<KeyValueResponse> responseObserver) {
        KeyValueResponse response = KeyValueResponse.newBuilder()
                .setKey(request.getKey())
                .setValue("Hello World binghe")
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
