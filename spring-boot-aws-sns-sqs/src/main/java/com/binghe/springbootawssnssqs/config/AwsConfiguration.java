package com.binghe.springbootawssnssqs.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClient;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.awspring.cloud.messaging.config.QueueMessageHandlerFactory;
import io.awspring.cloud.messaging.config.SimpleMessageListenerContainerFactory;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import java.util.Collections;

@Getter
@Configuration
public class AwsConfiguration {
    @Value("${cloud.aws.credentials.access-key}")
    private String awsAccessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String awsSecretKey;

    /**
     * SNS 설정
     */
    @Bean
    public AmazonSNS amazonSNS() {
        return AmazonSNSClient.builder()
//                .withCredentials(getAwsCredentialsProvider())
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }

    /**
     * AWS Credential 설정
     */
    public AWSCredentials getAwsCredentials() {
        return new BasicAWSCredentials(awsAccessKey, awsSecretKey);
    }

    public AWSCredentialsProvider getAwsCredentialsProvider() {
        return new AWSStaticCredentialsProvider(getAwsCredentials());
    }

    /**
     * SQS 설정
     */
    @Bean
    public AmazonSQSAsync amazonSqs() {
        return AmazonSQSAsyncClientBuilder
                .standard()
//                .withCredentials(new AWSStaticCredentialsProvider(getAwsCredentials()))
                .withRegion(Regions.AP_NORTHEAST_2)
                .build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return Jackson2ObjectMapperBuilder.json()
                .modules(new JavaTimeModule())
                .build();
    }

    @Bean
    public MappingJackson2MessageConverter mappingJackson2MessageConverter() {
        MappingJackson2MessageConverter mappingJackson2MessageConverter = new MappingJackson2MessageConverter();
        mappingJackson2MessageConverter.setStrictContentTypeMatch(false);
        return mappingJackson2MessageConverter;
    }

//    @Bean
//    public QueueMessageHandlerFactory queueMessageHandlerFactory(AmazonSQSAsync amazonSqs) {
//        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
//        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
//        messageConverter.setStrictContentTypeMatch(false);
//        messageConverter.setObjectMapper(objectMapper());
//        factory.setAmazonSqs(amazonSqs);
//        factory.setArgumentResolvers(Collections.singletonList(new PayloadMethodArgumentResolver(messageConverter)));
//        return factory;
//    }

    /**
     * SQS는 @SqsListener 이용하면 쉽게 Consume 할 수 있다.
     *
     * 다만, 디폴트로는 Short Polling한다. 그러므로 Long Polling 이용하려면 아래와 같이 설정해줘야한다.
     *
     * 그외에도 Visibility 설정등 SQS의 메시지를 Consume 할 때의 다양한 설정을 해줄 수 있다.
     */
    @Bean
    public SimpleMessageListenerContainerFactory simpleMessageListenerContainerFactory(AmazonSQSAsync amazonSqs,
                                                                                       SimpleAsyncTaskExecutor simpleAsyncTaskExecutor) {
        SimpleMessageListenerContainerFactory factory = new SimpleMessageListenerContainerFactory();
        factory.setAmazonSqs(amazonSqs);
        factory.setWaitTimeOut(10); // polling 설정
        factory.setVisibilityTimeout(30);
        factory.setTaskExecutor(simpleAsyncTaskExecutor);
        return factory;
    }

    @Bean
    public SimpleAsyncTaskExecutor simpleAsyncTaskExecutor() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(50);
        return simpleAsyncTaskExecutor;
    }
}
