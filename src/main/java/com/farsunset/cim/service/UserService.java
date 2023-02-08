package com.farsunset.cim.service;

import com.farsunset.cim.entity.User;

/**
 * @author ichaoge
 */
public interface UserService {

    User getUserByPhone(String phone);

}
