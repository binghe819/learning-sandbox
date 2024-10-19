package com.binghe;

import com.binghe.example.DataSetFactory;
import com.binghe.thrift.LargeObject;
import com.binghe.thrift.SmallObject;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

public class DataProcessorTProtocolTest {
    @Test
    void serialization_performance_test_small() {
        SmallObject smallObject = DataSetFactory.createSmallObjectThrift();

        DataProcessorImplTProtocol dataProcessor = new DataProcessorImplTProtocol();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

//        byte[] bytes = dataProcessor.getBytes(smallObject);
        for (int i = 0; i < 100_000_000; i++) {
            byte[] bytes = dataProcessor.encodeSmallObject(smallObject);
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void serialization_performance_test_large() {
        LargeObject largeObject = DataSetFactory.createLargeObjectThrift();

        DataProcessorImplTProtocol dataProcessor = new DataProcessorImplTProtocol();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 1_000_000; i++) {
            byte[] bytes = dataProcessor.encodeLargeObject(largeObject);
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void deserialization_performance_test_small() {
        SmallObject smallObjectThrift = DataSetFactory.createSmallObjectThrift();

        DataProcessorImplTProtocol dataProcessor = new DataProcessorImplTProtocol();

        byte[] serializedData = dataProcessor.encodeSmallObject(smallObjectThrift);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

//        byte[] bytes = dataProcessor.getBytes(smallObject);
        for (int i = 0; i < 100_000_000; i++) {
            SmallObject smallObject = dataProcessor.decodeSmallObject(serializedData);
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void deserialization_performance_test_large() {
        LargeObject largeObjectThrift = DataSetFactory.createLargeObjectThrift();

        DataProcessorImplTProtocol dataProcessor = new DataProcessorImplTProtocol();

        byte[] serializedData = dataProcessor.encodeLargeObject(largeObjectThrift);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

//        byte[] bytes = dataProcessor.getBytes(smallObject);
        for (int i = 0; i < 1_000_000; i++) {
            LargeObject largeObject = dataProcessor.decodeLargeObject(serializedData);
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void serialization_data_size() {
        LargeObject largeObjectThrift = DataSetFactory.createLargeObjectThrift();

        DataProcessorImplTProtocol dataProcessor = new DataProcessorImplTProtocol();

        byte[] bytes = dataProcessor.encodeLargeObject(largeObjectThrift);

        System.out.println("byte size : " + bytes.length);
        System.out.println("byte: " + bytes);
        System.out.println("문자열: " + new String(bytes));
    }
}
