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
package net.duckling.dhome.web;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.object.Comment;
import net.duckling.dhome.domain.object.CommentReply;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.UserSetting;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IMenuItemService;
import net.duckling.dhome.service.IMessageBoardService;
import net.duckling.dhome.service.IUserSettingService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.web.helper.MessageBoardHelper;
import net.duckling.dhome.web.helper.ThemeHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;


/**
 * @author lvly
 * @since 2013-9-6
 */
@Controller
@RequestMapping("/people/{domain}")
public class MessageBoardGuestController {
	@Autowired
	private ThemeHelper themeHelper;
	@Autowired
	private IMessageBoardService msgBoardService;
	@Autowired
	private IUserSettingService settingService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IMenuItemService menuItemService;
	@Autowired
	private MessageBoardHelper msgBoardHelper;
	@Autowired
	private IWorkService workService;
	@Autowired
	private IEducationService eduService;
	/**
	 * @return
	 */
	@RequestMapping(value="msgboard.dhome",params="fromEmail=true")
	public ModelAndView display(@PathVariable("domain")String domain,HttpServletRequest request,@RequestParam("commentId")int commentId){
		if(themeHelper.isOnePageTheme(domain)){
			return new ModelAndView(new RedirectView(request.getContextPath()+"/people/"+domain+"/index.html?type=history&commentId="+commentId+"#comment_"+commentId));
		}
		return display(domain,request);
	}
	@RequestMapping("msgboard.dhome")
	public ModelAndView display(@PathVariable("domain")String domain,HttpServletRequest request){
		String type=request.getParameter("type");
		String viewName=themeHelper.getTargetView(domain, "browseMsgBoard");
		ModelAndView mv=new ModelAndView(viewName);
		SimpleUser targetUser=homeService.getSimpleUserByDomain(domain);
		SimpleUser currUser=SessionUtils.getUser(request);
		if(CommonUtils.isNull(type)){
			type="comment";
		}
		boolean isSelf=SessionUtils.isSameUser(request, targetUser);
		if(isSelf){
			type="history";
		}
			
		UserSetting userSetting=settingService.getSetting(targetUser.getId(), UserSetting.KEY_MSG_BOARD_KEY);
		mv.addObject("msgBoardSetting",userSetting);
		mv.addObject("titleUser",targetUser);
		mv.addObject("name", targetUser.getZhName());
		//单位
		List<Work> works = workService.getWorksByUID(targetUser.getId());
		List<Education> edus = eduService.getEducationsByUid(targetUser.getId());
		if(!CommonUtils.isNull(works)){
			mv.addObject("nearestInstitution",CommonUtils.first(works));
		}else if(!CommonUtils.isNull(edus)){
			mv.addObject("nearestInstitution",CommonUtils.first(edus));
		}
		
		
		if("history".equals(type)){
			mv.addObject("msgs",msgBoardHelper.removeOthers(targetUser, currUser, userSetting));
		}
		mv.addObject("active",UrlUtil.getMsgBoardURL(domain));
		mv.addObject("type",type);
		mv.addObject("isSelf",isSelf);
		
		return mv;
	}
	
	@RequestMapping(value="comment",method=RequestMethod.POST)
	@ResponseBody
	public String comment(@PathVariable("domain")String domain,@RequestParam("content")String content,HttpServletRequest request){
		if(CommonUtils.isNull(content)){
			return "null";
		}
		SimpleUser targetUser=homeService.getSimpleUserByDomain(domain);
		SimpleUser currUser=SessionUtils.getUser(request);
		String currDomain=SessionUtils.getDomain(request);
		MenuItem menuItem=menuItemService.getMenuItemByUrl(UrlUtil.getMsgBoardURL(domain));
		if(menuItem==null||menuItem.getStatus()==MenuItem.MENU_ITEM_STATUS_HIDING){
			return "hide";
		}
		if(targetUser.getId()==currUser.getId()){
			return "self";
		}
		if(!msgBoardService.canComment(currUser.getId())){
			return "frequent";
		}
		
		Comment comment=new Comment();
		comment.setCommentHostUid(targetUser.getId());
		comment.setCommentHostUserName(targetUser.getZhName());
		comment.setName(currUser.getZhName());
		comment.setUid(currUser.getId());
		comment.setContent(content.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		comment.setCommentTime(new Date());
		comment.setCommentHostImage(targetUser.getImage());
		comment.setCommentHostDomain(domain);
		comment.setDomain(currDomain);
		comment.setImage(currUser.getImage());
		msgBoardService.addComment(comment);
		return "success";
	}
	
	@RequestMapping(value="/commentReply",method=RequestMethod.POST)
	@ResponseBody
	public CommentReply createCommentReply(
			@RequestParam("hostUid")int hostUid,
			@RequestParam("content")String content,
			@RequestParam("commentId")int commentId,
			HttpServletRequest request){
		SimpleUser user=SessionUtils.getUser(request);
		String domain=SessionUtils.getDomain(request);
		Comment comment=msgBoardService.getCommentById(commentId);
		if(comment.getUid()!=user.getId()||CommonUtils.isNull(content)){
			return null;
		}
		
		CommentReply reply=new CommentReply();
		if(!msgBoardService.canGuestCommentReply(user.getId())){
			return reply;
		}
		reply.setCommentId(comment.getId());
		reply.setReplyContent(content.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		reply.setReplyTime(new Date());
		reply.setReplyUid(user.getId());
		reply.setReplyUserName(user.getZhName());
		reply.setReplyDomain(domain);
		reply.setReplyImage(user.getImage());
		reply.setId(msgBoardService.addCommentReply(reply));
		
		return reply;
	}
	
	@RequestMapping(value="/deleteReply",method=RequestMethod.POST)
	@ResponseBody
	public boolean deleteReply(@RequestParam("replyId")int replyId,@RequestParam("commentId")int commentId,HttpServletRequest request){
		CommentReply reply=msgBoardService.getCommentReplyById(replyId);
		SimpleUser user=SessionUtils.getUser(request);
		if(reply.getReplyUid()!=user.getId()){
			return false;
		}
		msgBoardService.deleteCommentReplyById(replyId);
		return true;
	}
	@RequestMapping(value="/deleteComment",method=RequestMethod.POST)
	@ResponseBody
	public boolean deleteComment(@RequestParam("commentId")int commentId,HttpServletRequest request){
		Comment comment=msgBoardService.getCommentById(commentId);
		SimpleUser user=SessionUtils.getUser(request);
		if(comment.getUid()!=user.getId()){
			return false;
		}
		msgBoardService.deleteComment(commentId);
		return true;
	}
}
