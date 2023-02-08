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
package com.farsunset.cim.repository;

import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.farsunset.cim.entity.Session;
import com.farsunset.cim.mapper.SessionMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

@Repository
public class SessionRepository extends ServiceImpl<SessionMapper, Session> {

    public void deleteAll(String host) {
        if (StringUtils.isBlank(host)) {
            return;
        }

        new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Session::getHost, host)
                .remove();
    }

    public void updateState(long id, int state) {
        new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Session::getId, id)
                .set(Session::getState, state)
                .update();
    }

    public void openApns(String uid, String channel) {
        new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Session::getUid, uid)
                .eq(Session::getChannel, channel)
                .set(Session::getState, Session.STATE_APNS)
                .update();
    }

    public void closeApns(String uid, String channel) {
        new LambdaUpdateChainWrapper<>(baseMapper)
                .eq(Session::getUid, uid)
                .eq(Session::getChannel, channel)
                .set(Session::getState, Session.STATE_ACTIVE)
                .update();
    }
}
