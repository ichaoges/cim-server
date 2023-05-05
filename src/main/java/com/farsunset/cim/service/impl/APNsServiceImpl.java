package com.farsunset.cim.service.impl;

import com.eatthepath.pushy.apns.ApnsClient;
import com.eatthepath.pushy.apns.ApnsClientBuilder;
import com.eatthepath.pushy.apns.ApnsPushNotification;
import com.eatthepath.pushy.apns.util.ApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPayloadBuilder;
import com.eatthepath.pushy.apns.util.SimpleApnsPushNotification;
import com.eatthepath.pushy.apns.util.TokenUtil;
import com.farsunset.cim.config.properties.APNsProperties;
import com.farsunset.cim.model.Message;
import com.farsunset.cim.service.APNsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Primary
@Service
public class APNsServiceImpl implements APNsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(APNsServiceImpl.class);

    private final ApnsClient apnsClient;

    private final APNsProperties properties;

    @Autowired
    public APNsServiceImpl(APNsProperties properties) throws IOException {
        this.properties = properties;

        InputStream stream = getClass().getResourceAsStream(properties.getP12File());

        apnsClient = new ApnsClientBuilder()
                .setApnsServer(properties.isDebug() ? ApnsClientBuilder.DEVELOPMENT_APNS_HOST
                        : ApnsClientBuilder.PRODUCTION_APNS_HOST)
                .setClientCredentials(stream, properties.getP12Password())
                .build();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void push(Message message, String deviceToken) {

        if (StringUtils.isBlank(deviceToken)) {
            return;
        }

        ApnsPayloadBuilder payloadBuilder = new SimpleApnsPayloadBuilder();

        payloadBuilder.setAlertTitle("you have a new message.");

        payloadBuilder.setSound("default");
        payloadBuilder.setBadgeNumber(1);
        payloadBuilder.addCustomProperty("id", message.getId());
        payloadBuilder.addCustomProperty("action", message.getAction());
        payloadBuilder.addCustomProperty("content", message.getContent());
        payloadBuilder.addCustomProperty("sender", message.getSender());
        payloadBuilder.addCustomProperty("receiver", message.getReceiver());
        payloadBuilder.addCustomProperty("format", message.getFormat());
        payloadBuilder.addCustomProperty("extra", message.getExtra());
        payloadBuilder.addCustomProperty("timestamp", message.getTimestamp());

        String token = TokenUtil.sanitizeTokenString(deviceToken);

        String payload = payloadBuilder.build();

        ApnsPushNotification notification = new SimpleApnsPushNotification(token, properties.getAppId(), payload);

        apnsClient.sendNotification(notification).whenComplete((response, cause) -> {
            if (response != null) {
                LOGGER.info("APNs push done.\ndeviceToken : {} \napnsPayload : {}", deviceToken, payload);
            } else {
                LOGGER.error("APNs push failed", cause);
            }
        });
    }
}
