package com.farsunset.cim.service.impl;

import com.farsunset.cim.component.redis.TokenRedisTemplate;
import com.farsunset.cim.service.AccessTokenService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class AccessTokenServiceImpl implements AccessTokenService {

    @Resource
    private TokenRedisTemplate tokenRedisTemplate;

    @Override
    public String getUid(String token) {
        if (StringUtils.isBlank(token)) {
            return null;
        }
        return tokenRedisTemplate.get(token);
    }

    @Override
    public void delete(String token) {
        if (StringUtils.isBlank(token)) {
            return;
        }
        tokenRedisTemplate.remove(token);
    }

    @Override
    public String generate(String uid) {
        String newToken = UUID.randomUUID().toString().replace("-", "");
        set(newToken, uid);
        return newToken;
    }

    @Override
    public void set(String token, String uid) {
        tokenRedisTemplate.save(token, uid, 7, TimeUnit.DAYS);
    }
}
