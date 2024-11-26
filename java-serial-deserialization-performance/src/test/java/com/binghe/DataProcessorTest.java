package com.binghe;

import com.binghe.example.DataSetFactory;
import com.binghe.example.LargeObject;
import com.binghe.example.SmallObject;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class DataProcessorTest {

    @Test
    void serialization_performance_test_small() {
        SmallObject smallObject = DataSetFactory.createSmallObject();

        DataProcessor dataProcessor = new DataProcessorImplProtostuff();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

//        byte[] bytes = dataProcessor.getBytes(smallObject);
        for (int i = 0; i < 100_000_000; i++) {
            byte[] bytes = dataProcessor.getBytes(smallObject);
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void serialization_performance_test_large() {
        LargeObject largeObject = DataSetFactory.createLargeObject();

        DataProcessor dataProcessor = new DataProcessorImplMsgPack();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        for (int i = 0; i < 100_000; i++) {
            byte[] bytes = dataProcessor.getBytes(largeObject);
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void deserialization_performance_test_small() {
        SmallObject smallObject = DataSetFactory.createSmallObject();

        DataProcessor dataProcessor = new DataProcessorImplProtostuff();

        byte[] serializedData = dataProcessor.getBytes(smallObject);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

//        byte[] bytes = dataProcessor.getBytes(smallObject);
        for (int i = 0; i < 100_000_000; i++) {
            SmallObject object = dataProcessor.getObject(serializedData, SmallObject.class);
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void deserialization_performance_test_large() {
        LargeObject largeObject = DataSetFactory.createLargeObject();

        DataProcessor dataProcessor = new DataProcessorImplProtostuff();

        byte[] serializedData = dataProcessor.getBytes(largeObject);

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

//        byte[] bytes = dataProcessor.getBytes(smallObject);
        for (int i = 0; i < 1_000_000; i++) {
            LargeObject object = dataProcessor.getObject(serializedData, LargeObject.class);
        }

        stopWatch.stop();
        System.out.println(stopWatch.toString());
    }

    @Test
    void serialization_data_size() {
        SmallObject smallObject = DataSetFactory.createSmallObject();
        LargeObject largeObject = DataSetFactory.createLargeObject();

        DataProcessor dataProcessor = new DataProcessorImplProtostuff();

        byte[] bytes = dataProcessor.getBytes(largeObject);

        System.out.println("byte size : " + bytes.length);
        System.out.println("byte: " + bytes);
        System.out.println("문자열: " + new String(bytes));
    }
}