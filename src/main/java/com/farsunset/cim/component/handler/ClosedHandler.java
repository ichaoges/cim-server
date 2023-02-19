package com.farsunset.cim.component.handler;

import com.farsunset.cim.component.handler.annotation.CIMHandler;
import com.farsunset.cim.constant.ChannelAttr;
import com.farsunset.cim.constants.Constants;
import com.farsunset.cim.entity.Session;
import com.farsunset.cim.handler.CIMRequestHandler;
import com.farsunset.cim.model.SentBody;
import com.farsunset.cim.service.SessionService;
import io.netty.channel.Channel;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 连接断开时，更新用户相关状态
 */
@CIMHandler(key = "client_closed")
public class ClosedHandler implements CIMRequestHandler {

    @Resource
    private SessionService sessionService;

    @Override
    public void process(Channel channel, SentBody message) {

        Long sessionId = channel.attr(Constants.SESSION_ID).get();

        if (sessionId == null) {
            return;
        }

        /*
         * ios开启了apns也需要显示在线，因此不删记录
         */
        if (Objects.equals(channel.attr(ChannelAttr.CHANNEL).get(), Session.CHANNEL_IOS)) {
            sessionService.updateState(sessionId, Session.STATE_INACTIVE);
            return;
        }

        sessionService.delete(sessionId);

    }

}
