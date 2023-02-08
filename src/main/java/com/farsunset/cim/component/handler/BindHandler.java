/*
 * Copyright 2013-2022 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.cim.component.handler;

import com.farsunset.cim.component.handler.annotation.CIMHandler;
import com.farsunset.cim.component.redis.SignalRedisTemplate;
import com.farsunset.cim.constant.ChannelAttr;
import com.farsunset.cim.constants.Constants;
import com.farsunset.cim.entity.Session;
import com.farsunset.cim.group.SessionGroup;
import com.farsunset.cim.handler.CIMRequestHandler;
import com.farsunset.cim.model.ReplyBody;
import com.farsunset.cim.model.SentBody;
import com.farsunset.cim.service.AccessTokenService;
import com.farsunset.cim.service.SessionService;
import io.netty.channel.Channel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * 客户长连接 账户绑定实现
 */
@CIMHandler(key = "client_bind")
public class BindHandler implements CIMRequestHandler {

	@Resource
	private SessionService sessionService;

	@Resource
	private SessionGroup sessionGroup;

	@Resource
	private SignalRedisTemplate signalRedisTemplate;

	@Autowired
	private AccessTokenService accessTokenService;

	@Override
	public void process(Channel channel, SentBody body) {
		if (sessionGroup.isManaged(channel)) {
			return;
		}


		ReplyBody reply = new ReplyBody();
		reply.setKey(body.getKey());
		reply.setCode(HttpStatus.OK.value());
		reply.setTimestamp(System.currentTimeMillis());

		String token = body.get("token");
		if (StringUtils.isBlank(token)) {
			reply.setCode(HttpStatus.UNAUTHORIZED.value());
			reply.setMessage("no token");
			channel.writeAndFlush(reply);
			return;
		}

		String uidFromToken = accessTokenService.getUid(token);
		String uid = body.get("uid");

		if (StringUtils.isBlank(uid)) {
			reply.setCode(HttpStatus.UNAUTHORIZED.value());
			reply.setMessage("no uid");
			channel.writeAndFlush(reply);
			return;
		}

		if (!Objects.equals(uid, uidFromToken)) {
			reply.setCode(HttpStatus.UNAUTHORIZED.value());
			reply.setMessage("invalid token");
			channel.writeAndFlush(reply);
			return;
		}

		Session session = new Session();
		session.setUid(uid);
		session.setNid(channel.attr(ChannelAttr.ID).get());
		session.setDeviceId(body.get("deviceId"));
		session.setChannel(body.get("channel"));
		session.setDeviceName(body.get("deviceName"));
		session.setAppVersion(body.get("appVersion"));
		session.setOsVersion(body.get("osVersion"));
		session.setLanguage(body.get("language"));

		channel.attr(ChannelAttr.UID).set(uid);
		channel.attr(ChannelAttr.CHANNEL).set(session.getChannel());
		channel.attr(ChannelAttr.DEVICE_ID).set(session.getDeviceId());
		channel.attr(ChannelAttr.LANGUAGE).set(session.getLanguage());

		/*
		 *存储到数据库
		 */
		sessionService.add(session);

		channel.attr(Constants.SESSION_ID).set(session.getId());

		/*
		 * 添加到内存管理
		 */
		sessionGroup.add(channel);

		/*
		 *向客户端发送bind响应
		 */
		channel.writeAndFlush(reply);

		/*
		 * 发送上线事件到集群中的其他实例，控制其他设备下线
		 */
		signalRedisTemplate.bind(session);
	}
}
