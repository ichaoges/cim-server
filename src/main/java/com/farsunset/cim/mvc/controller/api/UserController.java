package com.farsunset.cim.mvc.controller.api;


import com.farsunset.cim.annotation.AccessToken;
import com.farsunset.cim.entity.TempUser;
import com.farsunset.cim.mvc.response.ResponseEntity;
import com.farsunset.cim.service.AccessTokenService;
import com.farsunset.cim.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(produces = "application/json", tags = "用户登录接口")
@Validated
public class UserController {

    @Autowired
    private AccessTokenService accessTokenService;

    @Autowired
    private UserService userService;

    @ApiOperation(httpMethod = "POST", value = "模拟登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "telephone", value = "手机号码", paramType = "query", dataTypeClass = String.class),
            @ApiImplicitParam(name = "password", value = "密码", paramType = "query", dataTypeClass = String.class),
    })
    //@PostMapping(value = "/login")
    public ResponseEntity<?> login(@RequestParam String telephone) {
        TempUser user = userService.getUserByPhone(telephone);
        if (user == null) {
            return ResponseEntity.make(HttpStatus.UNAUTHORIZED);
        }

        Map<String, Object> body = new HashMap<>();
        body.put("id", user.getId());
        body.put("name", user.getName());
        body.put("telephone", telephone);

        ResponseEntity<Map<String, Object>> result = new ResponseEntity<>();

        result.setData(body);

        result.setToken(accessTokenService.generate(String.valueOf(user.getId())));
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @Deprecated
    @ApiOperation(httpMethod = "POST", value = "新登录")
    @PostMapping(value = "/signIn")
    public ResponseEntity<?> signIn(@NotEmpty @RequestParam String token) {
        ResponseEntity<Map<String, Object>> result = new ResponseEntity<>();
        result.setToken(token);
        result.setTimestamp(System.currentTimeMillis());
        return result;
    }

    @ApiOperation(httpMethod = "GET", value = "退出登录")
    @GetMapping(value = "/logout")
    public ResponseEntity<Void> logout(@ApiParam(hidden = true) @AccessToken String token) {
        accessTokenService.delete(token);
        return ResponseEntity.make();
    }

}
