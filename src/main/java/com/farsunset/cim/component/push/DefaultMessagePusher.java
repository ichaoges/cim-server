/*
 * Copyright 2013-2022 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
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

        log.debug("推送消息: {}", message);

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
