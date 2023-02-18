package com.farsunset.cim.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author ichaoge
 */
@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "cim.server")
public class ServerProperties {

    private boolean deliverMessage;
}
