package com.farsunset.cim.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * (User)表实体类
 *
 * @since 2023-02-08 23:23:07
 */
@Data
public class User {

    //ID
    private Integer id;
    //类型：0-普通用户，1-代理
    private Integer type;

    private Integer level;
    //用户名
    private String userName;
    //手机号码
    private String phone;
    //VPN的EMAIL
    private String email;
    //密码
    private String password;
    //昵称
    private String nickName;
    //头像
    private String avatar;
    //余额
    private BigDecimal balance;
    //推荐人ID
    private Integer referUserId;
    //客服ID（顶级推荐人）
    private Integer masterId;
    //IP
    private String ip;
    //状态：1-正常，0禁用, -1已秒杀
    private Integer status;
    //是否认证：0-未认证，1-已认证
    private Integer isAuth;
    //登录token信息，用于加密
    private String token;
    //校验码，用于找回密码
    private String checkCode;
    //创建时间
    private Date createTime;
    //更新时间
    private Date updateTime;
}

