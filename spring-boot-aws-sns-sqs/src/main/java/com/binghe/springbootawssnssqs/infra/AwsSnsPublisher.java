package com.binghe.springbootawssnssqs.infra;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.MessageAttributeValue;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsSnsPublisher {

    private final AmazonSNS amazonSNS;
    private final ObjectMapper objectMapper;

    public void publishJson(String topic, Object message) {
        try {
            publishToSns(topic, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            log.error("[TOPIC::{}] Json serializing fail {}", topic, message, e.getMessage(), e);
        }
    }

    private PublishResult publishToSns(String topic, String message) {
        PublishRequest publishRequest = new PublishRequest()
                .withTopicArn(topic)
                .withMessage(message)
                .addMessageAttributesEntry("contentType",
                        new MessageAttributeValue()
                                .withDataType("String")
                                .withStringValue(APPLICATION_JSON_UTF8_VALUE));

        PublishResult result = amazonSNS.publish(publishRequest);
        log.info("[TOPIC::{}] published MessageID : {}", topic, result.getMessageId());
        return result;
    }
}
