package com.xcodeassociated.service.eventbus;

import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

@Getter
@ToString
public class SampleEvent extends ApplicationEvent {
    private String message;

    public SampleEvent(Object source, String message) {
        super(source);
        this.message = message;
    }

}