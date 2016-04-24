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

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.object.Comment;
import net.duckling.dhome.domain.object.CommentReply;
import net.duckling.dhome.domain.people.CustomPage;
import net.duckling.dhome.domain.people.MenuItem;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.UserSetting;
import net.duckling.dhome.service.IMenuItemService;
import net.duckling.dhome.service.IMessageBoardService;
import net.duckling.dhome.service.IPageService;
import net.duckling.dhome.service.IUserSettingService;

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
 * @since 2013-9-5
 */
@Controller
@RequestMapping("/people/{domain}/admin/msgboard")
public class MessageBoardAdminController {
	private static final String DEFAULT_BOARD_NAME="留言板";
	@Autowired
	private IMessageBoardService msgBoardService;
	@Autowired
	private IMenuItemService menuItemService;
	@Autowired
	private IUserSettingService userSettingService;
	@Autowired
	private IPageService pageService;
	
	@RequestMapping("/createMsgBoard")
	public ModelAndView createMsgBoard(HttpServletRequest request,@PathVariable("domain")String domain){
		String url=UrlUtil.getMsgBoardURL(domain);
		MenuItem item=menuItemService.getMenuItemByUrl(url);
		SimpleUser su=SessionUtils.getUser(request);
		if(item==null){
			item = new MenuItem();
			item.setStatus(MenuItem.MENU_ITEM_STATUS_SHOWING);
			item.setTitle(DEFAULT_BOARD_NAME);
			item.setUid(SessionUtils.getUserId(request));
			item.setUrl(UrlUtil.getMsgBoardURL(domain));
			item.setId(menuItemService.addMenuItem(item));
			CustomPage page=new CustomPage();
			page.setKeyWord("msgboard.dhome");
			page.setTitle("留言板");
			page.setCreateTime(new Date());
			page.setUid(su.getId());
			page.setUrl(UrlUtil.getMsgBoardURL(domain));
			page.setLastEditTime(new Date());
			pageService.createPage(page);
			
		}
		return display(request,domain);
	}
	public String getGuestPageUrl(String str) {
		String temp = str.replace("/admin", "");
		if(temp.endsWith("paper")||temp.endsWith("msgboard")){
			temp+=".dhome";
		}else if (!temp.contains(".html")) {
			temp += ".html";
		}
		
		return temp;
	}

	@RequestMapping(value="/deleteReply",method=RequestMethod.POST)
	@ResponseBody
	public boolean deleteReply(@RequestParam("replyId")int replyId,@RequestParam("commentId")int commentId,HttpServletRequest request){
		Comment comment=msgBoardService.getCommentById(commentId);
		CommentReply reply=msgBoardService.getCommentReplyById(replyId);
		SimpleUser user=SessionUtils.getUser(request);
		if(comment.getCommentHostUid()!=user.getId()||reply.getReplyUid()!=user.getId()){
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
		if(comment.getCommentHostUid()!=user.getId()){
			return false;
		}
		msgBoardService.deleteComment(commentId);
		return true;
	}
	@RequestMapping("changeOpenOrClose")
	@ResponseBody
	public boolean changeOpenOrClose(@RequestParam("flag")boolean isOpen,HttpServletRequest request){
		SimpleUser u=SessionUtils.getUser(request);
		userSettingService.updateSetting(u.getId(), UserSetting.KEY_MSG_BOARD_KEY,isOpen?UserSetting.VALUE_MSG_BOARD_VALUE_OPEN:UserSetting.VALUE_MSG_BOARD_VALUE_CLOSE);
		return true;
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
		if((comment.getUid()!=user.getId()&&comment.getCommentHostUid()!=user.getId())||CommonUtils.isNull(content)){
			return null;
		}
		CommentReply reply=new CommentReply();
		if(!msgBoardService.canAdminCommentReply(user.getId())){
			return reply;
		}
		reply.setCommentId(comment.getId());
		reply.setReplyContent(content.replaceAll("<", "&lt;").replaceAll(">", "&gt;"));
		reply.setReplyTime(new Date());
		reply.setReplyUid(user.getId());
		reply.setReplyUserName(user.getZhName());
		reply.setReplyImage(user.getImage());
		reply.setReplyDomain(domain);
		reply.setId(msgBoardService.addCommentReply(reply));
		
		return reply;
	}
	
	@RequestMapping("/changeStatus")
	public ModelAndView changeStatus(HttpServletRequest request) {
		String url = request.getRequestURI().replace(request.getContextPath(), "");
		url = url.substring(0, url.lastIndexOf('/'));
		MenuItem item = menuItemService.getMenuItemByUrl(getGuestPageUrl(url));
		if (MenuItem.MENU_ITEM_STATUS_HIDING == item.getStatus()) {
			item.setStatus(MenuItem.MENU_ITEM_STATUS_SHOWING);
		} else if (MenuItem.MENU_ITEM_STATUS_SHOWING == item.getStatus()) {
			item.setStatus(MenuItem.MENU_ITEM_STATUS_HIDING);
		}
		menuItemService.updateMenuItem(item);
		return new ModelAndView(new RedirectView(url));
	}
	@RequestMapping
	public ModelAndView display(HttpServletRequest request,@PathVariable("domain")String domain){
		ModelAndView mv=new ModelAndView("adminMessageBoard");
		SimpleUser curUser = SessionUtils.getUser(request);
		String url=UrlUtil.getMsgBoardURL(domain);
		MenuItem item=menuItemService.getMenuItemByUrl(url);
		String status="";
		if (item != null) {
			switch (item.getStatus()) {
			case MenuItem.MENU_ITEM_STATUS_HIDING:
				status = "hide";
				break;
			case MenuItem.MENU_ITEM_STATUS_SHOWING:
				status = "show";
				break;
			case MenuItem.MENU_ITEM_STATUS_REMOVED:
				status = "delete";
				break;
			default:
				break;
			}
		}
		mv.addObject("domain", domain);
		mv.addObject("name", curUser.getZhName());
		mv.addObject("titleUser",curUser);
		mv.addObject("msgs", msgBoardService.selectByHostUid(curUser.getId()));
		mv.addObject("banner","1");
		mv.addObject("status",status);
		CustomPage page=new CustomPage();
		page.setKeyWord("msgboard");
		mv.addObject("page",page);
		mv.addObject("boardSetting",userSettingService.getSetting(curUser.getId(), UserSetting.KEY_MSG_BOARD_KEY));
		mv.addObject("active",request.getRequestURI().replace(request.getContextPath(), ""));
		return mv;
	}
}
