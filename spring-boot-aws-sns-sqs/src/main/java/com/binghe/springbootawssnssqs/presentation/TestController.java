package com.binghe.springbootawssnssqs.presentation;

import com.binghe.springbootawssnssqs.event.EventMessageSample;
import com.binghe.springbootawssnssqs.infra.AwsSnsPublisher;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    private final AwsSnsPublisher publisher;
    private final String topic;

    public TestController(AwsSnsPublisher publisher,
                          @Value("${sns-topic.binghe-test-sns}") String topic) {
        this.publisher = publisher;
        this.topic = topic;
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody EventMessageSample eventMessageSample) {
        publisher.publishJson(topic, eventMessageSample);
        return ResponseEntity.ok("ok");
    }
}
