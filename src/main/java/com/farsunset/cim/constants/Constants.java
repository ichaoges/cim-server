package com.farsunset.cim.constants;

import io.netty.util.AttributeKey;

public interface Constants {

    String PUSH_MESSAGE_INNER_QUEUE = "signal/channel/PUSH_MESSAGE_INNER_QUEUE";

    String BIND_MESSAGE_INNER_QUEUE = "signal/channel/BIND_MESSAGE_INNER_QUEUE";

    String DELIVER_MESSAGE_INNER_QUEUE = "signal/channel/DELIVER_MESSAGE_INNER_QUEUE";

    String TOKEN_REGISTER_INNER_QUEUE = "signal/channel/TOKEN_REGISTER_INNER_QUEUE";

    String APNS_DEVICE_TOKEN = "APNS_OPEN_%s";

    AttributeKey<Long> SESSION_ID = AttributeKey.valueOf("session_id");
}
