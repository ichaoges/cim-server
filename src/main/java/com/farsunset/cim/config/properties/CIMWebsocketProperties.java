package com.farsunset.cim.config.properties;

import com.farsunset.cim.constant.WebsocketProtocol;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;


@ConfigurationProperties(prefix = "cim.websocket")
public class CIMWebsocketProperties {

    private boolean enable;

    private Integer port;
    private String path;
    private WebsocketProtocol protocol;

    /**
     * 长链接写空闲时间触发时间(s)
     * 心跳发送定时，每当x秒无数据下发写入，触发 服务端-->客户端 心跳事件
     */
    private Duration writeIdle = Duration.ofSeconds(45);

    /**
     * 长链接读空闲时间触发时间(s)
     * 心跳响应定时，每当readIdle - writeIdle秒无数据接收，触发心跳超时计数
     */
    private Duration readIdle = Duration.ofSeconds(60);


    /**
     * 长链接最大允许心跳响应超时次数
     * 达到该次数则 服务端断开链接
     */
    private int maxPongTimeout = 1;

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public WebsocketProtocol getProtocol() {
        return protocol;
    }

    public void setProtocol(WebsocketProtocol protocol) {
        this.protocol = protocol;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public Duration getWriteIdle() {
        return writeIdle;
    }

    public void setWriteIdle(Duration writeIdle) {
        if (writeIdle == null || writeIdle.getSeconds() <= 0) {
            return;
        }
        this.writeIdle = writeIdle;
    }

    public Duration getReadIdle() {
        return readIdle;
    }

    public void setReadIdle(Duration readIdle) {
        if (readIdle == null || readIdle.getSeconds() <= 0) {
            return;
        }
        this.readIdle = readIdle;
    }

    public int getMaxPongTimeout() {
        return maxPongTimeout;
    }

    public void setMaxPongTimeout(int maxPongTimeout) {
        if (maxPongTimeout <= 0) {
            return;
        }
        this.maxPongTimeout = maxPongTimeout;
    }
}
