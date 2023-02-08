package com.farsunset.cim.service;

import com.farsunset.cim.entity.TempUser;
import com.farsunset.cim.entity.User;

/**
 * @author ichaoge
 */
public interface UserService {

    TempUser getUserByPhone(String phone);

    User getUserById(String uid);

}
