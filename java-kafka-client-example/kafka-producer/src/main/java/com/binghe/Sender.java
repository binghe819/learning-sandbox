package com.binghe;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class Sender {
    private static final Logger log = LoggerFactory.getLogger(Sender.class);

    private final Producer<String, String> producer;

    public Sender(Properties properties) {
        this.producer = new KafkaProducer<>(properties);
    }

    // 메시지는 보내고 확인하지 않는다.
    public void sendForgot(ProducerRecord<String, String> producerRecord) {
        try {
            producer.send(producerRecord);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 메시지 동기 전송 (send는 Future를 반환하는데, get하기때문에 동기. RecordMetadata를 통해 성공 여부를 확인 가능)
    public void sendSync(ProducerRecord<String, String> producerRecord) {
        try {
            RecordMetadata recordMetadata = producer.send(producerRecord).get();
            log.info("Metadata: {}", recordMetadata);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 메시지 비동기 전송 (callback 필요)
    public void sendAsync(ProducerRecord<String, String> producerRecord, Callback callback) {
        try {
            producer.send(producerRecord, callback);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        producer.close();
    }
}
