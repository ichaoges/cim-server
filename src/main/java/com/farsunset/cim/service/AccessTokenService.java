package com.farsunset.cim.service;


public interface AccessTokenService {

    String generate(String uid);

    void set(String token, String uid);

    String getUid(String token);

    void delete(String value);
}
