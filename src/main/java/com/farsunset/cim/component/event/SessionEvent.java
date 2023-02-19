package com.farsunset.cim.component.event;

import com.farsunset.cim.entity.Session;
import org.springframework.context.ApplicationEvent;

public class SessionEvent extends ApplicationEvent {

    public SessionEvent(Session session) {
        super(session);
    }

    @Override
    public Session getSource() {
        return (Session) source;
    }

}
