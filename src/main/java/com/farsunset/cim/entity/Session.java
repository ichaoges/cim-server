package com.farsunset.cim.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("cim_session")
public class Session {

    public static final int STATE_ACTIVE = 0;
    public static final int STATE_APNS = 1;
    public static final int STATE_INACTIVE = 2;

    public static final String CHANNEL_IOS = "ios";
    public static final String CHANNEL_ANDROID = "android";
    public static final String CHANNEL_WINDOWS = "windows";
    public static final String CHANNEL_MAC = "mac";
    public static final String CHANNEL_WEB = "web";
    public static final String CHANNEL_DOCKER = "docker";

    /**
     * 数据库主键ID
     */
    @TableId
    private Long id;

    /**
     * session绑定的用户账号
     */
    private String uid;

    /**
     * session在本台服务器上的ID
     */
    private String nid;

    /**
     * 客户端ID (设备号码+应用包名),ios为deviceToken
     */

    private String deviceId;

    /**
     * 终端设备型号
     */
    private String deviceName;

    /**
     * session绑定的服务器IP
     */
    private String host;

    /**
     * 终端设备类型
     */
    private String channel;

    /**
     * 终端应用版本
     */
    private String appVersion;

    /**
     * 终端系统版本
     */
    private String osVersion;

    /**
     * 终端语言
     */
    private String language;

    /**
     * 登录时间
     */
    private Long bindTime;

    /**
     * 经度
     */
    private Double longitude;

    /**
     * 维度
     */
    private Double latitude;

    /**
     * 位置
     */
    private String location;

    /**
     * 状态
     */
    private int state;
}
