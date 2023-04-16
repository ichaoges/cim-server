package com.farsunset.cim.component.handler;

import com.farsunset.cim.component.handler.annotation.CIMHandler;
import com.farsunset.cim.component.redis.SignalRedisTemplate;
import com.farsunset.cim.constant.ChannelAttr;
import com.farsunset.cim.constants.Constants;
import com.farsunset.cim.entity.Session;
import com.farsunset.cim.group.SessionGroup;
import com.farsunset.cim.handler.CIMRequestHandler;
import com.farsunset.cim.model.ReplyBody;
import com.farsunset.cim.model.SentBody;
import com.farsunset.cim.service.SessionService;
import io.netty.channel.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

/**
 * 客户长连接 账户绑定实现
 */
@Slf4j
@CIMHandler(key = "client_bind")
public class BindHandler implements CIMRequestHandler {

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionGroup sessionGroup;

    @Autowired
    private SignalRedisTemplate signalRedisTemplate;

    @Override
    public void process(Channel channel, SentBody body) {
        if (sessionGroup.isManaged(channel)) {
            return;
        }

        log.info("Received bind package: {}", body);

        ReplyBody reply = new ReplyBody();
        reply.setKey(body.getKey());
        reply.setCode(HttpStatus.OK.value());
        reply.setTimestamp(System.currentTimeMillis());

        Session session = new Session();
        session.setUid(body.get("uid"));
        session.setNid(channel.attr(ChannelAttr.ID).get());
        session.setDeviceId(body.get("deviceId"));
        session.setChannel(body.get("channel"));
        session.setDeviceName(body.get("deviceName"));
        session.setAppVersion(body.get("appVersion"));
        session.setOsVersion(body.get("osVersion"));
        session.setLanguage(body.get("language"));

        channel.attr(ChannelAttr.UID).set(body.get("uid"));
        channel.attr(ChannelAttr.CHANNEL).set(session.getChannel());
        channel.attr(ChannelAttr.DEVICE_ID).set(session.getDeviceId());
        channel.attr(ChannelAttr.LANGUAGE).set(session.getLanguage());

        /*
         *存储到数据库
         */
        sessionService.add(session);

        channel.attr(Constants.SESSION_ID).set(session.getId());

        /*
         * 添加到内存管理
         */
        sessionGroup.add(channel);

        /*
         *向客户端发送bind响应
         */
        channel.writeAndFlush(reply);

        /*
         * 发送上线事件到集群中的其他实例，控制其他设备下线
         */
        signalRedisTemplate.bind(session);
    }
}
