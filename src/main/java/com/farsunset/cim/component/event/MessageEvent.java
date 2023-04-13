package com.farsunset.cim.component.event;

import com.farsunset.cim.model.Message;
import org.springframework.context.ApplicationEvent;

public class MessageEvent extends ApplicationEvent {

    public MessageEvent(Message message) {
        super(message);
    }

    @Override
    public Message getSource() {
        return (Message) source;
    }
}
