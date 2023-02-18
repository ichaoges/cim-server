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
