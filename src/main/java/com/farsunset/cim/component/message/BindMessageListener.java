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
package com.farsunset.cim.component.message;

import com.farsunset.cim.component.event.SessionEvent;
import com.farsunset.cim.constant.ChannelAttr;
import com.farsunset.cim.entity.Session;
import com.farsunset.cim.group.SessionGroup;
import com.farsunset.cim.model.Message;
import com.farsunset.cim.util.JSONUtils;
import io.netty.channel.Channel;
import org.springframework.context.event.EventListener;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 集群环境下，监控多设备登录情况，控制是否其余终端下线的逻辑
 */
@Component
public class BindMessageListener implements MessageListener {

    private static final String FORCE_OFFLINE_ACTION = "999";

    private static final String SYSTEM_ID = "0";

    /*
     一个账号只能在同一个类型的终端登录
     如: 多个android或ios不能同时在线
         一个android或ios可以和web，桌面同时在线
     */
    private final Map<String, String[]> conflictMap = new HashMap<>();

    @Resource
    private SessionGroup sessionGroup;

    public BindMessageListener() {
        conflictMap.put(Session.CHANNEL_ANDROID, new String[]{Session.CHANNEL_ANDROID, Session.CHANNEL_IOS});
        conflictMap.put(Session.CHANNEL_IOS, new String[]{Session.CHANNEL_ANDROID, Session.CHANNEL_IOS});
        conflictMap.put(Session.CHANNEL_WINDOWS, new String[]{Session.CHANNEL_WINDOWS, Session.CHANNEL_WEB, Session.CHANNEL_MAC});
        conflictMap.put(Session.CHANNEL_WEB, new String[]{Session.CHANNEL_WINDOWS, Session.CHANNEL_WEB, Session.CHANNEL_MAC});
        conflictMap.put(Session.CHANNEL_MAC, new String[]{Session.CHANNEL_WINDOWS, Session.CHANNEL_WEB, Session.CHANNEL_MAC});
    }

    @EventListener
    public void onMessage(SessionEvent event) {
        this.handle(event.getSource());
    }

    @Override
    public void onMessage(org.springframework.data.redis.connection.Message redisMessage, byte[] bytes) {

        Session session = JSONUtils.fromJson(redisMessage.getBody(), Session.class);

        this.handle(session);
    }

    private void handle(Session session) {

        String uid = session.getUid();
        String[] conflictChannels = conflictMap.get(session.getChannel());

        Collection<Channel> channelList = sessionGroup.find(uid, conflictChannels);

        channelList.removeIf(channel -> session.getNid().equals(channel.attr(ChannelAttr.ID).get()));

        /*
         * 获取到其他在线的终端连接，提示账号再其他终端登录
         */
        channelList.forEach(channel -> {

            if (Objects.equals(session.getDeviceId(), channel.attr(ChannelAttr.DEVICE_ID).get())) {
                channel.close();
                return;
            }

            Message message = new Message();
            message.setAction(FORCE_OFFLINE_ACTION);
            message.setReceiver(uid);
            message.setSender(SYSTEM_ID);
            message.setContent(session.getDeviceName());
            channel.writeAndFlush(message);
            channel.close();
        });


    }
}
