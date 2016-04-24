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
/**
 * 
 */
package net.duckling.dhome.service.impl;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.email.EmailSendThread;
import net.duckling.dhome.common.email.SimpleEmail;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.DefaultValuePageUtils;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IMailService;
import net.duckling.dhome.service.IUserService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 邮件服务具体实现事宜
 * 
 * @author lvly
 * @since 2012-8-30
 */
@Service
public class MailService implements IMailService {
	private static final Logger LOG = Logger.getLogger(MailService.class);
	public static final String EMAIL_DISPLAY="科研在线";
	@Autowired
	private AppConfig appConfig;
	@Autowired
	private IUserService userService;
	
	private static Properties pro;
	/**
	 * 获得发送邮件的基本参数，单例
	 * @return pro Properties
	 * */
	private synchronized Properties getEmailParam(){
		if(pro==null){
			pro=new Properties();
			pro.setProperty("mail.smtp.host", appConfig.getStmpHost());
			pro.setProperty("mail.smtp.auth", appConfig.getStmpAuth());
			pro.setProperty("mail.pop3.host", appConfig.getPop3Auth());
		}
		return pro;
	}

	/**发送邮件
	 * @param title 邮件标题
	 * @param content 邮件内容
	 * @param address 邮件地址
	 * @throws MessagingException 
	 * */
	@Override
	public void sendEmail(String title,String content,String[] address) {
		LOG.debug("sendEmail() to: " +Arrays.toString(address));
		if(CommonUtils.isNull(address)){
			LOG.error("the address is empty!");
			return ;
		}
		try {
			Address[] addressArray=new Address[address.length];
			int index=0;
			for(String str:address){
				addressArray[index++]=new InternetAddress(str);
			}
			MimeMessage msg=getMessage(addressArray,content, title);
			

			if ((appConfig.getEmailFromAddress() != null)&& (appConfig.getEmailFromAddress().indexOf("@") != -1)) {
				cheat(msg, appConfig.getEmailFromAddress().substring(appConfig.getEmailFromAddress().indexOf("@")));
			}
			Transport.send(msg);
			LOG.info("Successfully send the mail to " + Arrays.toString(address));

		} catch (MessagingException e) {
			LOG.error("Exception occured while trying to send notification to: " + Arrays.toString(address));
			LOG.error(e);
			LOG.debug("Details:", e);
		}
	}
	private MimeMessage getMessage(Address[] addressArray,String content,String title) throws  MessagingException{
		Session session = Session.getInstance(getEmailParam(),
				new EmailAuthenticator(appConfig.getEmailUserName(), appConfig.getEmailPassword()));
		session.setDebug(false);
		MimeMessage msg = new MimeMessage(session);
		try {
			msg.setFrom(new InternetAddress(appConfig.getEmailFromAddress(),EMAIL_DISPLAY,"UTF-8"));
		} catch (UnsupportedEncodingException e) {
			LOG.error(e.getMessage(), e);
			msg.setFrom(new InternetAddress(appConfig.getEmailFromAddress()));
		}
		msg.setRecipients(Message.RecipientType.TO, addressArray);
		msg.setSubject(title);
		msg.setSentDate(new Date());
		Multipart mp = new MimeMultipart();

		MimeBodyPart txtmbp = new MimeBodyPart();
		String contentType = "text/html;charset=UTF-8";
		txtmbp.setContent(content, contentType);
		mp.addBodyPart(txtmbp);
		msg.setContent(mp);
		return msg;
	}
	@Override
	public void sendRegistMail(String tmpFile, SimpleUser user, String domain) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("${userName}", user.getZhName());
			map.put("${domain}", domain);
			map.put("${email}", user.getEmail());
			String content = DefaultValuePageUtils.getHTML(map, tmpFile);
			EmailSendThread.addEmail(new SimpleEmail(user.getEmail(),content,"学术主页创建成功！"));
	}
	@Override
	public void sendRegistMailToAdmin(String tmpType, SimpleUser user, String domain) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("${userName}", user.getZhName());
		map.put("${domain}", domain);
		DetailedUser dUser=userService.getDetailedUser(user.getId());
		map.put("${introduction}",dUser==null?"":dUser.getIntroduction());
		map.put("${email}", user.getEmail());
		String content = DefaultValuePageUtils.getHTML(map, tmpType);
		EmailSendThread.addEmail(new SimpleEmail(appConfig.getEmailAdminList().split(","),content,user.getZhName()+"创建了学术主页！"));
	}
	@Override
	public void sendCheckHomePageMail(String tmpType, SimpleUser user, String domain) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("${userName}", user.getZhName());
		map.put("${domain}", domain);
		map.put("${status}", getStatusDesc(user.getStatus()));
		map.put("${reason}", user.getAuditPropose());
		map.put("${email}", user.getEmail());
		String content = DefaultValuePageUtils.getHTML(map, tmpType);
		EmailSendThread.addEmail(new SimpleEmail(user.getEmail(),content,"学术主页审核结果通知！"));
	}
	/**e
	 * account for sendMailAccount
	 * @author lvly
	 * */
	public static class EmailAuthenticator extends Authenticator {
		private final PasswordAuthentication passwordAuthentication;
		/**
		 * constructor
		 * @param userid userId
		 * @param password passWord
		 * */
		public EmailAuthenticator(String userid, String password) {
			passwordAuthentication = new PasswordAuthentication(userid, password);
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return passwordAuthentication;
		}
	}

	private void cheat(MimeMessage mimeMessage, String serverDomain) throws MessagingException {
		mimeMessage.saveChanges();
		mimeMessage.setHeader("User-Agent", "Thunderbird 2.0.0.16 (Windows/20080708)");
		String messageid = mimeMessage.getHeader("Message-ID", null);
		messageid = messageid.replaceAll("\\.JavaMail.*", "@" + serverDomain + ">");
		mimeMessage.setHeader("Message-ID", messageid);
	}
	
	private String getStatusDesc(String status){
		String desc = "";
		if(SimpleUser.STATUS_AUDIT_OK.equals(status)){
			desc = "审核通过";
		}else if(SimpleUser.STATUS_AUDIT_NOT.equals(status)){
			desc = "审核未通过";
		}else if(SimpleUser.STATUS_AUDIT_DELETE.equals(status)){
			desc = "已删除";
		}else{
			desc = "未知状态";
		}
		return desc;
	}

}
