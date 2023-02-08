package com.farsunset.cim.service.impl;

import com.farsunset.cim.entity.User;
import com.farsunset.cim.service.UserService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ichaoge
 */
@Service
public class UserServiceImpl implements UserService {


    private static final Map<String, User> users = new HashMap<>();

    static {
        users.put("+8613977777777", User.builder().id(10461L).name("张三").telephone("+8613977777777").build());
        users.put("+8613988888888", User.builder().id(10462L).name("李思").telephone("+8613988888888").build());
        users.put("+8613999999999", User.builder().id(10463L).name("王五").telephone("+8613999999999").build());
    }

    @Override
    public User getUserByPhone(String phone) {
        return users.get(phone);
    }
}
