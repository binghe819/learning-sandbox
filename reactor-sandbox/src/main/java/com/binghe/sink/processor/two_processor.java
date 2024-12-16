package com.binghe.sink.processor;

import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.ArrayList;
import java.util.List;

/**
 * Processor 사용하여 데이터 변화 감지
 *
 * 구독 행위가 등록되고 난 이후에 해당 이벤트가 발생하면 구독하는 대상에게 데이터를 동기적으로 전달한다.
 *
 * Flux의 Processor에는 FluxProcessor, EmitterProcessor, ReplayProcessor 등 많은 프로세서들이 존재하는데, 그중 EmitterProcessor는 여러 구독자(subscriber)가 사용할 수 있는 구독과 발행이 동시해 일어나는 프로세서이다.
 * 여기선 EmitterProcessor을 사용한다.
 *
 * Processor는 기존에 있거나 새롭게 등장한 구독자(subscriber)에게 데이터(old data)를 전달한다. -> 구독자 중심
 * 발행하는 기관(processor)을 건설하고 구독자를 모집(subscribe)한 뒤에 계속해서 발행(sink.next)하는 형태이다. 구독 이후에 데이터가 변경되어 발행되었다면 추가 구독없이 변경된 데이터를 받아볼 수 있다.
 * (구독자 추가이후 subscribe를 하지 않아도 데이터를 emit하면 구독자에게 전달된다)
 * 작업 할 내용이 데이터가 중심이면 일반 구독형태를 사용하고, 구독자가 중심이면 프로세스를 사용하면 될 것 같다.
 *
 */
public class two_processor {

    public static void main(String[] args) {
        List<String> source = new ArrayList<>();

        source.addAll(List.of("1", "2", "3", "4", "5"));

        // 프로세서 시작 구간
        EmitterProcessor<List<String>> data = EmitterProcessor.create();
        data.subscribe(it -> System.out.println("1번 구독자: " + it)); // 구독 -> 구독자 추가
        FluxSink<List<String>> sink = data.sink(); // 데이터 전송해주는 배달부
        sink.next(source); // 전송 (publisher 입장)

        // 새로운 구독자
        data.subscribe(it -> System.out.println("2번 구독자: " + it)); // 구독자 추가
        sink.next(source); // 전송 (publisher 입장)

        sink.complete();

        // 데이터 소스 추가
        source.addAll(List.of("6", "7", "8", "9"));
        sink.next(source); // 전송 (publisher 입장)
    }
}
