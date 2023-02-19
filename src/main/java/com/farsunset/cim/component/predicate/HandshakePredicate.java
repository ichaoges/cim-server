package com.farsunset.cim.component.predicate;

import com.farsunset.cim.handshake.HandshakeEvent;
import com.farsunset.cim.service.AccessTokenService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Predicate;


/**
 * WS链接握手鉴权验证
 */
@Slf4j
@Component
public class HandshakePredicate implements Predicate<HandshakeEvent> {

    @Autowired
    private AccessTokenService accessTokenService;

    /**
     * 验证身份信息，本方法切勿进行耗时操作！！！
     *
     * @param event
     *
     * @return true验证通过 false验证失败
     */
    @Override
    public boolean test(HandshakeEvent event) {

        String token = event.getHeader("x-token");
        if (StringUtils.isBlank(token)) {
            token = event.getParameter("token");
        }

        if (StringUtils.isBlank(token)) {
            log.warn("no token !!!");
            return false;
        }

        String uid = event.getParameter("uid");

        if (StringUtils.isBlank(uid)) {
            log.warn("no uid !!!");
            return false;
        }

        String uidFromToken = accessTokenService.getUid(token);

        if (!Objects.equals(uid, uidFromToken)) {
            log.warn("invalid token. {} - {}", uid, uidFromToken);
            return false;
        }

        return true;
    }
}
