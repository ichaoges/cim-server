package com.farsunset.cim.component.redis;

import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class TokenRedisTemplate extends StringRedisTemplate {

    private static final String TOKEN_CACHE_PREFIX = "CIM_TOKEN_%s";


    public TokenRedisTemplate(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public void save(String token, String uid) {

        String key = String.format(TOKEN_CACHE_PREFIX, token);

        super.boundValueOps(key).set(uid);
    }

    public void save(String token, String uid, long timeout, TimeUnit unit) {

        String key = String.format(TOKEN_CACHE_PREFIX, token);

        super.boundValueOps(key).set(uid, timeout, unit);
    }

    public String get(String token) {

        String key = String.format(TOKEN_CACHE_PREFIX, token);

        return super.boundValueOps(key).get();
    }

    public void remove(String token) {

        String key = String.format(TOKEN_CACHE_PREFIX, token);

        super.delete(key);
    }

}
