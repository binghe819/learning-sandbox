package com.binghe;

import org.apache.kafka.clients.producer.ProducerRecord;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

public class Main {
    public static void main(String[] args) {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", "bh-kafka01.binghe.com:9092,bh-kafka02.binghe.com:9092,bh-kafka03.binghe.com:9092");
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        Sender sender = new Sender(properties);

        try {
            for (int i = 0; i < 5; i++) {
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>("Football-No" + i, "A team Player .... in " + LocalDateTime.now().format(DateTimeFormatter.BASIC_ISO_DATE));
                sender.sendSync(producerRecord);
            }
        } finally {
            sender.close();
        }
    }
}