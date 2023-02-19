package com.farsunset.cim.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "cim.apns")
public class APNsProperties {

    private final P12 p12 = new P12();
    private boolean debug;
    private String appId;

    public P12 getP12() {
        return p12;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public String getP12File() {
        return p12.file;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getP12Password() {
        return p12.password;
    }

    public static class P12 {

        private String file;
        private String password;

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
