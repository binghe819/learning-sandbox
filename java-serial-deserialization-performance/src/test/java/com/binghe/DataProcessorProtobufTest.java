package com.binghe;

import com.binghe.example.DataSetFactory;
import com.binghe.proto.LargeObject;
import com.binghe.proto.SmallObject;
import com.google.protobuf.InvalidProtocolBufferException;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

public class DataProcessorProtobufTest {

    @Test
    void serialization_performance_test_small() {
        SmallObject smallObjectProto = DataSetFactory.createSmallObjectProto();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 100_000_000; i++) {
            byte[] byteArray = smallObjectProto.toByteArray();
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void serialization_performance_test_large() {
        LargeObject largeObjectProto = DataSetFactory.createLargeObjectProto();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 1_000_000; i++) {
            byte[] byteArray = largeObjectProto.toByteArray();
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void deserialization_performance_test_small() throws InvalidProtocolBufferException {
        SmallObject smallObjectProto = DataSetFactory.createSmallObjectProto();

        byte[] serializedData = smallObjectProto.toByteArray();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

//        byte[] bytes = dataProcessor.getBytes(smallObject);
        for (int i = 0; i < 100_000_000; i++) {
            SmallObject smallObject = SmallObject.parseFrom(serializedData);
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void deserialization_performance_test_large() throws InvalidProtocolBufferException {
        LargeObject largeObjectProto = DataSetFactory.createLargeObjectProto();

        byte[] serializedData = largeObjectProto.toByteArray();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

//        byte[] bytes = dataProcessor.getBytes(smallObject);
        for (int i = 0; i < 1_000_000; i++) {
            LargeObject largeObject = LargeObject.parseFrom(serializedData);
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void serialization_data_size() {
        SmallObject smallObjectProto = DataSetFactory.createSmallObjectProto();
        LargeObject largeObjectProto = DataSetFactory.createLargeObjectProto();

//        byte[] bytes = smallObjectProto.toByteArray();
        byte[] bytes = largeObjectProto.toByteArray();

        System.out.println("byte size : " + bytes.length);
        System.out.println("byte: " + bytes);
        System.out.println("문자열: " + new String(bytes));
    }
}
