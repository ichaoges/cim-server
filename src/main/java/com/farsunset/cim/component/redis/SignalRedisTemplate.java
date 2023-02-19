package com.farsunset.cim.component.redis;

import com.farsunset.cim.component.event.MessageEvent;
import com.farsunset.cim.component.event.SessionEvent;
import com.farsunset.cim.constants.Constants;
import com.farsunset.cim.entity.Session;
import com.farsunset.cim.model.Message;
import com.farsunset.cim.util.JSONUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Objects;

@Component
public class SignalRedisTemplate extends StringRedisTemplate {

    @Value("${spring.profiles.active}")
    private String env;

    @Resource
    private ApplicationContext applicationContext;


    public SignalRedisTemplate(LettuceConnectionFactory connectionFactory) {
        super(connectionFactory);
        connectionFactory.setValidateConnection(true);
    }

    /**
     * 消息发送到 集群中的每个实例，获取对应长连接进行消息写入
     *
     * @param message
     */
    public void push(Message message) {
        if (isLocal()) {
            applicationContext.publishEvent(new MessageEvent(message));
            return;
        }
        super.convertAndSend(Constants.PUSH_MESSAGE_INNER_QUEUE, JSONUtils.toJSONString(message));
    }

    /**
     * 消息发送到 集群中的每个实例，解决多终端在线冲突问题
     *
     * @param session
     */
    public void bind(Session session) {
        if (isLocal()) {
            applicationContext.publishEvent(new SessionEvent(session));
            return;
        }
        super.convertAndSend(Constants.BIND_MESSAGE_INNER_QUEUE, JSONUtils.toJSONString(session));
    }

    /**
     * 本地调试环境下不走redis，避免lettuce 经常command timeout。
     *
     * @return
     */
    private boolean isLocal() {
        return Objects.equals(env, "local");
    }

}
