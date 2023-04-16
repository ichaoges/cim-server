package com.farsunset.cim.mvc.interceptor;

import com.farsunset.cim.annotation.AccessToken;
import com.farsunset.cim.annotation.UID;
import com.farsunset.cim.service.AccessTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 在此鉴权获得UID
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    private static final String HEADER_TOKEN = "access-token";

    private final AccessTokenService accessTokenService;

    public TokenInterceptor(AccessTokenService accessTokenService) {
        this.accessTokenService = accessTokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {

        String token = request.getHeader(HEADER_TOKEN);

        String uid = accessTokenService.getUid(token);

        /*
         * 直接拒绝无token的接口调用请求或者token没有查询到对应的登录用户
         */
        if (uid == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return false;
        }

        request.setAttribute(UID.class.getName(), uid);
        request.setAttribute(AccessToken.class.getName(), token);

        return true;

    }
}
