package com.farsunset.cim.mvc.controller.api;

import com.farsunset.cim.service.SessionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@RestController
//@RequestMapping("/apns")
@Api(produces = "application/json", tags = "APNs推送相关")
public class APNsController {

    @Autowired
    private SessionService sessionService;

    @ApiOperation(httpMethod = "POST", value = "开启apns")


    @ApiImplicitParams({
            @ApiImplicitParam(name = "deviceToken", value = "APNs的deviceToken", paramType = "query", dataTypeClass = String.class, required = true, example = ""),
            @ApiImplicitParam(name = "uid", value = "用户ID", paramType = "query", dataTypeClass = String.class, example = "0")
    })
    @PostMapping(value = "/open")
    public ResponseEntity<Void> open(@RequestParam String uid, @RequestParam String deviceToken) {

        sessionService.openApns(uid, deviceToken);

        return ResponseEntity.ok().build();
    }

    @ApiOperation(httpMethod = "POST", value = "关闭apns")
    @ApiImplicitParam(name = "uid", value = "用户ID", paramType = "query", dataTypeClass = String.class, example = "0")
    @PostMapping(value = "/close")
    public ResponseEntity<Void> close(@RequestParam String uid) {

        sessionService.closeApns(uid);

        return ResponseEntity.ok().build();
    }
}
