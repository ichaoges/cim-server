package com.farsunset.cim.config;

import com.farsunset.cim.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;

import java.util.Objects;


@Configuration
@AutoConfigureBefore(RedisAutoConfiguration.class)
public class RedisConfig {

    @Autowired
    public RedisConfig(LettuceConnectionFactory connectionFactory, @Value("${spring.profiles.active}") String profile) {
        if (Objects.equals("local", profile)) {
            connectionFactory.setValidateConnection(true);
        }
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(RedisConnectionFactory connectionFactory,
                                                                       MessageListener pushMessageListener,
                                                                       MessageListener deliverMessageListener,
                                                                       MessageListener tokenRegisterListener,
                                                                       MessageListener bindMessageListener) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(pushMessageListener, new ChannelTopic(Constants.PUSH_MESSAGE_INNER_QUEUE));
        container.addMessageListener(bindMessageListener, new ChannelTopic(Constants.BIND_MESSAGE_INNER_QUEUE));
        container.addMessageListener(deliverMessageListener, new ChannelTopic(Constants.DELIVER_MESSAGE_INNER_QUEUE));
        container.addMessageListener(tokenRegisterListener, new ChannelTopic(Constants.TOKEN_REGISTER_INNER_QUEUE));
        return container;
    }

}