package com.farsunset.cim.component.message;

import com.farsunset.cim.service.AccessTokenService;
import com.farsunset.cim.util.JSONUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

/**
 * @author ichaoge
 */
@Slf4j
@Component
public class TokenRegisterListener implements MessageListener {

    private final AccessTokenService accessTokenService;

    public TokenRegisterListener(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Override
    public void onMessage(org.springframework.data.redis.connection.Message message, byte[] pattern) {
        TokenData tokenData = JSONUtils.fromJson(message.getBody(), TokenData.class);
        this.handle(tokenData);
    }

    private void handle(TokenData message) {
        log.info("收到Token注册：{}", message);
        accessTokenService.set(message.token, message.uid);
    }

    @Data
    public static class TokenData {

        private String token;

        private String uid;

    }
}
