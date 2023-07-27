package com.binghe.springbootawssnssqs.event;


import lombok.Getter;
import lombok.ToString;

@ToString
@Getter
public class EventMessageSample {

    private String id;
    private String message;

    private EventMessageSample() {
    }

    public EventMessageSample(String id, String message) {
        this.id = id;
        this.message = message;
    }
}
