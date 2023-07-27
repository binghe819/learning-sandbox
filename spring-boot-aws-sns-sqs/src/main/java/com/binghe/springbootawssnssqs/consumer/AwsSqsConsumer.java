package com.binghe.springbootawssnssqs.consumer;

import com.binghe.springbootawssnssqs.event.EventMessageSample;
import io.awspring.cloud.messaging.listener.Acknowledgment;
import io.awspring.cloud.messaging.listener.SqsMessageDeletionPolicy;
import io.awspring.cloud.messaging.listener.annotation.SqsListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AwsSqsConsumer {

    @SqsListener(value = "${sqs-event.binghe-test-sqs}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
    public void consume(EventMessageSample event) {
        log.info("Consumed Message : {}", event.toString());

//        SqsMessageDeletionPolicy.NEVER 설정시 명시적으로 아래와 같이 메시지를 삭제하도록 ack 응답을 보낼 수 있다.
//        ack.acknowledge();
    }
}
