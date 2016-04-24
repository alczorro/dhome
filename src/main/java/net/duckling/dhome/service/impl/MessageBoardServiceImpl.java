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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.email.EmailSendThread;
import net.duckling.dhome.common.email.SimpleEmail;
import net.duckling.dhome.common.util.DefaultValuePageUtils;
import net.duckling.dhome.common.util.MemCacheKeyGenerator;
import net.duckling.dhome.dao.IMessageBoardDAO;
import net.duckling.dhome.domain.object.Comment;
import net.duckling.dhome.domain.object.CommentReply;
import net.duckling.dhome.domain.object.CommentView;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IMessageBoardService;
import net.duckling.dhome.service.IUserService;
import net.duckling.falcon.api.cache.ICacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-9-5
 */
@Service
public class MessageBoardServiceImpl implements IMessageBoardService{
	@Autowired
	private IMessageBoardDAO boardDAO;
	@Autowired
	private IUserService userService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private ICacheService cacheService;
	@Autowired
	private AppConfig appConfig;
	@Override
	public int addComment(Comment commen) {
		commen.setId(boardDAO.addComment(commen));
		sendCreateCommenEmail(commen);
		cacheService.set(MemCacheKeyGenerator.getLastCommentKey(commen.getUid()), System.currentTimeMillis());
		return commen.getId();
	}
	private void sendCreateCommenEmail(Comment comment){
		String title="您有新的留言";
		Map<String, Object> map = new HashMap<String, Object>();
		SimpleUser commentUser=userService.getSimpleUserByUid(comment.getUid());
		SimpleUser titleUser=userService.getSimpleUserByUid(comment.getCommentHostUid());
		String commentDomain=homeService.getDomain(commentUser.getId());
		String titleUserDomain=homeService.getDomain(titleUser.getId());
		map.put("${content}", comment.getContent());
		map.put("${targetUser}", titleUser.getZhName());
		map.put("${currentUser}", commentUser.getZhName());
		map.put("${currentUserUrl}", appConfig.getBaseURL()+"/people/"+commentDomain);
		map.put("${detailUrl}", appConfig.getBaseURL()+"/people/"+titleUserDomain+"/msgboard.dhome?type=history&fromEmail=true&commentId="+comment.getId()+"#comment_"+comment.getId());
		String content = DefaultValuePageUtils.getHTML(map, DefaultValuePageUtils.COMMENT_TEMP);
		EmailSendThread.addEmail(new SimpleEmail(titleUser.getEmail(),content,title));
	}

	@Override
	public void deleteComment(int commentId) {
		boardDAO.deleteComment(commentId);
		boardDAO.deleteCommentReplyByCommentId(commentId);
	}

	@Override
	public int addCommentReply(CommentReply reply) {
		reply.setId(boardDAO.addCommentReply(reply));
		sendReplyEmail(reply);
		return reply.getId();
	}
	private void sendReplyEmail(CommentReply reply){
		Comment comment=getCommentById(reply.getCommentId());
		SimpleUser trigger=null;
		SimpleUser getter=null;
		boolean sendToMe=true;
		//回复者回复，给主页所属者发
		if(reply.getReplyUid()==comment.getUid()){
			trigger=userService.getSimpleUserByUid(comment.getUid());
			getter=userService.getSimpleUserByUid(comment.getCommentHostUid());
			cacheService.set(MemCacheKeyGenerator.getLastCommentKey(comment.getUid()),System.currentTimeMillis());
		}
		//主页所属者回复，给回复者发
		else if(reply.getReplyUid()==comment.getCommentHostUid()){
			trigger=userService.getSimpleUserByUid(comment.getCommentHostUid());
			getter=userService.getSimpleUserByUid(comment.getUid());
			cacheService.set(MemCacheKeyGenerator.getLastAdminReplyKey(comment.getCommentHostUid()),System.currentTimeMillis());
			sendToMe=false;
		}
		String triggerDomain=homeService.getDomain(trigger.getId());
		String getterDomain=homeService.getDomain(getter.getId());
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("${content}", reply.getReplyContent());
		map.put("${targetUser}", getter.getZhName());
		map.put("${currentUser}", trigger.getZhName());
		map.put("${currentUserUrl}", appConfig.getBaseURL()+"/people/"+triggerDomain);
		map.put("${detailUrl}", appConfig.getBaseURL()+"/people/"+(sendToMe?getterDomain:triggerDomain)+"/msgboard.dhome?type=history&fromEmail=true&commentId="+comment.getId()+"#comment_"+comment.getId());
		String content = DefaultValuePageUtils.getHTML(map, DefaultValuePageUtils.COMMENT_REPLY_TEMP);
		EmailSendThread.addEmail(new SimpleEmail(getter.getEmail(),content,"您有新的回复"));
	}

	@Override
	public void deleteCommentReplyById(int commentReplyId) {
		boardDAO.deleteCommentReplyById(commentReplyId);
	}

	@Override
	public List<CommentView> selectByHostUid(int uid) {
		return boardDAO.selectByHostUid(uid);
	}
	
	@Override
	public void updateUserInfo(int uid,int image, String name, String domain) {
		boardDAO.updateUserInfo(uid,image,name,domain);
	}
	
	@Override
	public Comment getCommentById(int commentId) {
		return boardDAO.getCommentById(commentId);
	}
	@Override
	public CommentReply getCommentReplyById(int replyId) {
		return boardDAO.getCommentReplyById(replyId);
	}
	@Override
	public boolean canGuestCommentReply(int guestUid){
		Long lastCommentTime=(Long)cacheService.get(MemCacheKeyGenerator.getLastCommentKey(guestUid));
		if(lastCommentTime==null){
			return true;
		}
		return (System.currentTimeMillis()-lastCommentTime)>2*60*1000l;
	}
	@Override
	public boolean canAdminCommentReply(int adminUid){
		Long lastCommentTime=(Long)cacheService.get(MemCacheKeyGenerator.getLastAdminReplyKey(adminUid));
		if(lastCommentTime==null){
			return true;
		}
		return (System.currentTimeMillis()-lastCommentTime)>20*1000l;
	}
	
	@Override
	public boolean canComment(int uid) {
		Long lastCommentTime=(Long)cacheService.get(MemCacheKeyGenerator.getLastCommentKey(uid));
		if(lastCommentTime==null){
			return true;
		}
		return (System.currentTimeMillis()-lastCommentTime)>2*60*1000l;
	}
	

}
