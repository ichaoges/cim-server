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
