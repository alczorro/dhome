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
package net.duckling.dhome.service;

import javax.mail.MessagingException;

import net.duckling.dhome.domain.people.SimpleUser;


/**
 * 发送邮件接口
 * @author lvly
 * @since 2012-8-30
 */
public interface IMailService {
	/**
	 * 发送注册邮件
	 * @param 模版类型，具体看DefaultValuePageUtil.TYPE_XX
	 * @param user SimpleUser,注册邮件至少得有这玩意替换内容吧
	 * @param domain 用户的域名
	 */
	void sendRegistMail(String tmpType,SimpleUser user,String domain);
	/**
	 * 发送注册邮件给管理员
	 * @param 模版类型，具体看DefaultValuePageUtil.TYPE_XX
	 * @param user SimpleUser,注册邮件至少得有这玩意替换内容吧
	 * @param domain 用户的域名
	 */
	void sendRegistMailToAdmin(String tmpType,SimpleUser user,String domain);
	/**
	 * 发送审核邮件给用户
	 * @param tmpType 模版类型，具体看DefaultValuePageUtil.TYPE_XX
	 * @param user user SimpleUser,注册邮件至少得有这玩意替换内容吧
	 * @param domain 用户的域名
	 */
	void sendCheckHomePageMail(String tmpType,SimpleUser user, String domain);
	
	/**发送邮件
	 * @param title 邮件标题
	 * @param content 邮件内容
	 * @param address 邮件地址
	 * @throws MessagingException 
	 * */
	void sendEmail(String title,String content,String[] address) ;
}
