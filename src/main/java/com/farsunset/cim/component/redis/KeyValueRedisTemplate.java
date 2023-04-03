package com.farsunset.cim.component.redis;

import com.farsunset.cim.constants.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class KeyValueRedisTemplate extends StringRedisTemplate {

    public KeyValueRedisTemplate(RedisConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    public void set(String key, String value) {
        super.boundValueOps(key).set(value);
    }

    public String get(String key) {
        return super.boundValueOps(key).get();
    }

    public String getDeviceToken(String uid) {
        return super.boundValueOps(String.format(Constants.APNS_DEVICE_TOKEN, uid)).get();
    }

    public void openApns(String uid, String deviceToken) {
        super.boundValueOps(String.format(Constants.APNS_DEVICE_TOKEN, uid)).set(deviceToken);
    }

    public void closeApns(String uid) {
        super.delete(String.format(Constants.APNS_DEVICE_TOKEN, uid));
    }


}
