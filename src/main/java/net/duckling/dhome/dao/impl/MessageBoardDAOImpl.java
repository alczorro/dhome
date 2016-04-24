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
package net.duckling.dhome.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.repository.BaseDao;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.dao.IMessageBoardDAO;
import net.duckling.dhome.domain.object.Comment;
import net.duckling.dhome.domain.object.CommentReply;
import net.duckling.dhome.domain.object.CommentView;

import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2013-9-5
 */
@Component
public class MessageBoardDAOImpl  extends BaseDao implements IMessageBoardDAO{
	@Override
	public int addComment(Comment commen) {
		return insert(commen);
	}

	@Override
	public void deleteComment(int commentId) {
		Comment comment=new Comment();
		comment.setId(commentId);
		remove(comment);
		
	}

	@Override
	public int addCommentReply(CommentReply comment) {
		return insert(comment);
	}

	@Override
	public void deleteCommentReplyById(int repId) {
		CommentReply comment=new CommentReply();
		comment.setId(repId);
		remove(comment);
	}

	@Override
	public void deleteCommentReplyByCommentId(int commentId) {
		CommentReply reply=new CommentReply();
		reply.setCommentId(commentId);
		remove(reply);
	}

	@Override
	public List<CommentView> selectByHostUid(int uid) {
		Comment comment=new Comment();
		comment.setCommentHostUid(uid);
		List<Comment> comments=findByProperties(comment,"order by `id` desc");
		if(CommonUtils.isNull(comments)){
			return null;
		}
		List<CommentReply> reply=findByProperties(new CommentReply(), generateReplySQL(comments));
		
		return extract(comments,reply);
	}
	private List<CommentView> extract(List<Comment> comments,List<CommentReply> replys){
		List<CommentView> views=new ArrayList<CommentView>();
		Map<Integer,MapList> replysMap=new HashMap<Integer,MapList>();
		if(!CommonUtils.isNull(replys)){
			for(CommentReply reply: replys){
				MapList.add2List(reply, replysMap);
			}
		}
		for(Comment comment:comments){
			CommentView view=new CommentView();
			view.setComment(comment);
			MapList mapList=replysMap.get(comment.getId());
			view.setReplys(mapList==null?null:mapList.list);
			views.add(view);
		}
		return views;
	}
	static class MapList{
		int commentId;
		List<CommentReply> list=new ArrayList<CommentReply>();
		static void add2List(CommentReply reply,Map<Integer,MapList> map){
			MapList mapList=map.get(reply.getCommentId());
			if(mapList==null){
				mapList=new MapList();
				mapList.commentId=reply.getCommentId();
				map.put(mapList.commentId,mapList);
			}
			
			mapList.list.add(reply);
		}
	}
	
	private String generateReplySQL(List<Comment> comments){
		StringBuffer sb=new StringBuffer();
		int index=0;
		sb.append(" and `comment_id` in (");
		for(Comment comment:comments){
			sb.append((index++==0?"":",")).append(comment.getId());
		}
		sb.append(") order by `id`"); 
		return sb.toString();
	}
	@Override
	public Comment getCommentById(int commentId) {
		Comment comment=new Comment();
		comment.setId(commentId);
		return findAndReturnOnly(comment);
	}
	@Override
	public CommentReply getCommentReplyById(int replyId) {
		CommentReply reply=new CommentReply();
		reply.setId(replyId);
		return findAndReturnOnly(reply);
	}
	@Override
	public void updateUserInfo(int uid,int image, String name, String domain) {
		if(uid<=0){
			return;
		}
		Map<String,Object> param=new HashMap<String,Object>();
		param.put("image", image);
		param.put("uid", uid);
		param.put("name", name);
		param.put("domain",domain);
		if(image>0){
			String updateGuest="update `comment` set image=:image where uid=:uid";
			String updateAdmin="update `comment` set comment_host_image=:image where comment_host_uid=:uid";
			String updateReply="update `comment_reply` set `reply_image`=:image where reply_uid=:uid";
			getNamedParameterJdbcTemplate().update(updateGuest,param);
			getNamedParameterJdbcTemplate().update(updateAdmin, param);
			getNamedParameterJdbcTemplate().update(updateReply, param);
		}
		if(!CommonUtils.isNull(name)){
			String updateGuest="update `comment` set `name`=:name where uid=:uid";
			String updateAdmin="update `comment` set `comment_host_user_name`=:name where `comment_host_uid`=:uid";
			String updateReply="update `comment_reply` set `reply_user_name`=:name where reply_uid=:uid";
			getNamedParameterJdbcTemplate().update(updateGuest,param);
			getNamedParameterJdbcTemplate().update(updateAdmin, param);
			getNamedParameterJdbcTemplate().update(updateReply, param);
		}
		if(!CommonUtils.isNull(domain)){
			String updateGuest="update `comment` set `domain`=:domain where uid=:uid";
			String updateAdmin="update `comment` set `comment_host_domain`=:domain where `comment_host_uid`=:uid";
			String updateReply="update `comment_reply` set `reply_domain`=:domain where reply_uid=:uid";
			getNamedParameterJdbcTemplate().update(updateGuest,param);
			getNamedParameterJdbcTemplate().update(updateAdmin, param);
			getNamedParameterJdbcTemplate().update(updateReply, param);
		}
	}
}
