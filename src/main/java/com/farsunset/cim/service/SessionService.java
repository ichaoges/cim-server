package com.farsunset.cim.service;


import com.farsunset.cim.entity.Session;

import java.util.List;

/**
 * 存储连接信息，便于查看用户的链接信息
 */
public interface SessionService {

    void add(Session session);

    void delete(long id);

    /**
     * 删除本机的连接记录
     */
    void deleteLocalhost();

    void updateState(long id, int state);

    void openApns(String uid, String deviceToken);

    void closeApns(String uid);

    List<Session> findAll();
}
