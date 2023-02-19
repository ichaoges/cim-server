package com.farsunset.cim.component.push;

import com.farsunset.cim.component.redis.KeyValueRedisTemplate;
import com.farsunset.cim.component.redis.SignalRedisTemplate;
import com.farsunset.cim.model.Message;
import com.farsunset.cim.service.APNsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 消息发送实现类
 */
@Slf4j
@Component
public class DefaultMessagePusher implements CIMMessagePusher {

    @Autowired
    private APNsService apnsService;

    @Autowired
    private SignalRedisTemplate signalRedisTemplate;

    @Autowired
    private KeyValueRedisTemplate keyValueRedisTemplate;

    /**
     * 向用户发送消息
     *
     * @param message
     */
    public final void push(Message message) {

        String uid = message.getReceiver();

        log.info("推送消息: {}", message);

        /*
         * 说明iOS客户端开启了apns
         */
        String deviceToken = keyValueRedisTemplate.getDeviceToken(uid);
        if (deviceToken != null) {
            apnsService.push(message, deviceToken);
            return;
        }

        /*
         * 通过发送redis广播，到集群中的每台实例，获得当前UID绑定了连接并推送
         * @see com.farsunset.hoxin.component.message.PushMessageListener
         */
        signalRedisTemplate.push(message);

    }

}
