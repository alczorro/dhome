/*
 * Copyright (c) 2008-2016 Computer Network Information Center (CNIC), Chinese Academy of Sciences.
 * 
 * This file is part of Duckling project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 *
 */
package net.duckling.dhome.service.impl;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.falcon.api.mq.DFMQFactory;
import net.duckling.falcon.api.mq.DFMQMode;
import net.duckling.falcon.api.mq.IDFMessageHandler;
import net.duckling.falcon.api.mq.IDFSubscriber;
import net.duckling.falcon.api.mq.NotFoundHandlerException;
import net.duckling.vmt.api.domain.message.MQBaseMessage;
import net.duckling.vmt.api.domain.message.MQCreateDepartMessage;
import net.duckling.vmt.api.domain.message.MQDeleteDepartMessage;
import net.duckling.vmt.api.domain.message.MQLinkUserMessage;
import net.duckling.vmt.api.domain.message.MQMoveDepartMessage;
import net.duckling.vmt.api.domain.message.MQMoveUserMessage;
import net.duckling.vmt.api.domain.message.MQUnlinkUserMessage;
import net.duckling.vmt.api.domain.message.MQUpdateDepartMessage;
import net.duckling.vmt.api.domain.message.MQUpdateGroupMessage;
import net.duckling.vmt.api.domain.message.MQUpdateOrgMessage;
import net.duckling.vmt.api.domain.message.MQUpdateUserMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VMTMessageHandlerImpl {
	private static final Logger LOG = Logger.getLogger(VMTMessageHandlerImpl.class);

	private class ReceiveThread implements Runnable {
		IDFSubscriber subscriber;

		public ReceiveThread(IDFSubscriber subscriber) {
			this.subscriber = subscriber;
		}

		@Override
		public void run() {
			try {
				subscriber.receive();
			} catch (NotFoundHandlerException e) {
				LOG.error("", e);
			}
		}

	}

	abstract class VmtMessageHandle implements IDFMessageHandler {

		/**
		 * 处理消息
		 * 
		 * @param message
		 */
		public abstract void dealMessage(MQBaseMessage message);

		@Override
		public void handle(Object message, String routingKey) {
			try {
				if (message instanceof MQBaseMessage) {
					dealMessage((MQBaseMessage) message);
					LOG.info(((MQBaseMessage) message).toJsonString());
				} else {
					LOG.error("MESSAGE type error :" + message);
				}
			} catch (Exception e) {
				LOG.error("", e);
			}
		}
	}

	class VmtTeamMessageHandle extends VmtMessageHandle {

		@Override
		public void dealMessage(MQBaseMessage message) {
			//更新群组名称或者管理员
			if (message instanceof MQUpdateGroupMessage) {
				vmtService.updateVmtGroup((MQUpdateGroupMessage)message);
			} 
			//更新组织机构名称或者管理员
			else if(message instanceof MQUpdateOrgMessage){
				vmtService.updateVmtOrg((MQUpdateOrgMessage)message);
			}
		}

	}
	class VmtDepartMessageHandle extends VmtMessageHandle{

		@Override
		public void dealMessage(MQBaseMessage message) {
			if(message instanceof MQUpdateDepartMessage){
				vmtService.updateVmtDepart((MQUpdateDepartMessage)message);
			}else if(message instanceof MQMoveDepartMessage){
				vmtService.moveVmtDepart((MQMoveDepartMessage)message);
			}else if(message instanceof MQDeleteDepartMessage){
				vmtService.removeVmtDepart((MQDeleteDepartMessage)message);
			}else if(message instanceof MQCreateDepartMessage){
				vmtService.createVmtDepart((MQCreateDepartMessage)message);
			}
		}
	}

	class VmtUserMessageHandle extends VmtMessageHandle {

		@Override
		public void dealMessage(MQBaseMessage message) {
			//添加新用户
			if (message instanceof MQLinkUserMessage) {
				vmtService.addVmtUser((MQLinkUserMessage)message);
			} 
			//移除用户
			else if (message instanceof MQUnlinkUserMessage) {
				vmtService.removeVmtUser((MQUnlinkUserMessage)message);
			} 
			//移动用户
			else if(message instanceof MQMoveUserMessage){
				vmtService.moveVmtUser((MQMoveUserMessage)message);
			}
			//更新用户
			else if(message instanceof MQUpdateUserMessage){
				vmtService.updateVmtUser((MQUpdateUserMessage)message);
			}
			else {
				LOG.error("unkown message type["+message.getType()+","+message+"]");
			}
		}
	}

	private IDFSubscriber subscriber;

	@PreDestroy
	public void destroy() {
		if (subscriber != null) {
			subscriber.close();
		}
	}

	@Value("${duckling.mq.host}")
	private String mqHost;
	@Value("${duckling.mq.queuename}")
	private String mqQueueName;
	@Value("${duckling.mq.exchange}")
	private String mqExchange;
	@Value("${duckling.mq.userName}")
	private String mqUserName;
	@Value("${duckling.mq.password}")
	private String mqPassword;
	
	@Autowired
	private IInstitutionVmtService vmtService;

	@PostConstruct
	public void init() {
		if (subscriber == null) {
			subscriber = DFMQFactory.buildSubscriber(mqUserName, mqPassword,
					mqHost, mqExchange, mqQueueName + "-group", DFMQMode.TOPIC);
			subscriber.registHandler("#.group", new VmtTeamMessageHandle());
			subscriber.registHandler("#.org", new VmtTeamMessageHandle());
			subscriber.registHandler("#.user", new VmtUserMessageHandle());
			subscriber.registHandler("#.dept", new VmtDepartMessageHandle());
			Runnable groupThream = new ReceiveThread(subscriber);
			Thread t = new Thread(groupThream);
			t.setDaemon(true);
			t.setName("messageThread");
			t.start();
		}
	}

}
