package com.farsunset.cim.service;

import com.farsunset.cim.model.Message;

public interface APNsService {

    void push(Message message, String deviceToken);
}
