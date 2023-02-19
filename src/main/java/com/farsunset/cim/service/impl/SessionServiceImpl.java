package com.farsunset.cim.service.impl;

import com.farsunset.cim.component.redis.KeyValueRedisTemplate;
import com.farsunset.cim.entity.Session;
import com.farsunset.cim.repository.SessionRepository;
import com.farsunset.cim.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SessionServiceImpl implements SessionService {

    private final String host;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private KeyValueRedisTemplate keyValueRedisTemplate;

    public SessionServiceImpl() throws UnknownHostException {
        host = InetAddress.getLocalHost().getHostAddress();
    }

    @Override
    public void add(Session session) {
        session.setBindTime(System.currentTimeMillis());
        session.setHost(host);
        sessionRepository.save(session);
    }

    @Override
    public void delete(long id) {
        sessionRepository.removeById(id);
    }

    @Override
    public void deleteLocalhost() {
        sessionRepository.deleteAll(host);
    }

    @Override
    public void updateState(long id, int state) {
        sessionRepository.updateState(id, state);
    }

    @Override
    public void openApns(String uid, String deviceToken) {
        keyValueRedisTemplate.openApns(uid, deviceToken);
        sessionRepository.openApns(uid, Session.CHANNEL_IOS);
    }

    @Override
    public void closeApns(String uid) {
        keyValueRedisTemplate.closeApns(uid);
        sessionRepository.closeApns(uid, Session.CHANNEL_IOS);
    }

    @Override
    public List<Session> findAll() {
        return sessionRepository.list()
                .stream()
                .filter(session -> session.getState() == Session.STATE_ACTIVE || session.getState() == Session.STATE_APNS)
                .collect(Collectors.toList());
    }
}
