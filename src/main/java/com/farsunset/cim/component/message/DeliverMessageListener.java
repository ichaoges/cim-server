package com.farsunset.cim.component.message;

import com.farsunset.cim.component.redis.SignalRedisTemplate;
import com.farsunset.cim.config.properties.ServerProperties;
import com.farsunset.cim.model.Message;
import com.farsunset.cim.util.JSONUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author ichaoge
 */
@Slf4j
@Component
public class DeliverMessageListener implements MessageListener {

    private final ServerProperties serverProperties;

    private final SignalRedisTemplate signalRedisTemplate;

    public DeliverMessageListener(ServerProperties serverProperties, SignalRedisTemplate signalRedisTemplate) {
        this.serverProperties = serverProperties;
        this.signalRedisTemplate = signalRedisTemplate;
    }

    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        DeliverMessage deliverMessage = JSONUtils.fromJson(message.getBody(), DeliverMessage.class);
        this.handle(deliverMessage);
    }

    private void handle(DeliverMessage message) {
        if (!serverProperties.isDeliverMessage()) {
            return;
        }

        log.info("收到投递消息：{}", message);

        if (message.receivers == null || message.receivers.isEmpty()) {
            return;
        }

        for (String receiver : message.receivers) {
            Message msg = new Message();
            msg.setId(message.id);
            msg.setAction(message.action);
            msg.setReceiver(receiver);
            msg.setContent(message.content);
            msg.setSender(message.sender);
            msg.setTimestamp(message.timestamp);
            signalRedisTemplate.push(msg);
        }
    }

    @Data
    public static class DeliverMessage {

        private long id;

        private String action;

        private String content;

        private String sender;

        private List<String> receivers;

        private long timestamp;

    }
}
