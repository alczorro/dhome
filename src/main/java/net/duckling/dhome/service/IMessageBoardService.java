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
package net.duckling.dhome.service;

import java.util.List;

import net.duckling.dhome.domain.object.Comment;
import net.duckling.dhome.domain.object.CommentReply;
import net.duckling.dhome.domain.object.CommentView;

/**
 * 留言板
 * @author lvly
 * @since 2013-9-5
 */
public interface IMessageBoardService {

	/**
	 * 留言
	 * @param commen
	 * @return
	 */
	int addComment(Comment commen);
	
	/**
	 * 删除留言
	 * @param commentId
	 */
	void deleteComment(int commentId);
	
	/**
	 * 增加一个留言回复
	 * @param commentId
	 * @param comment
	 */
	int addCommentReply(CommentReply comment);
	
	/**
	 * 删除一个留言回复
	 * @param commentId
	 */
	void deleteCommentReplyById(int commentReplyId);
	
	/**
	 * 获得某个个人主页所属的留言
	 * @param uid
	 * @return
	 */
	List<CommentView> selectByHostUid(int uid);

	/**
	 * 根据留言id获得留言
	 * @param commentId
	 * @return
	 */
	Comment getCommentById(int commentId);

	/**
	 * 根据回复id 获得回复
	 * @param replyId
	 * @return
	 */
	CommentReply getCommentReplyById(int replyId);

	/**
	 * 更新用户信息，如果无更新，请用0或者null
	 * @param id
	 */
	void updateUserInfo(int uid,int image,String name,String domain);
	/**
	 * 
	 * 判断发言频繁度，不允许低于2分钟
	 * */
	boolean canComment(int id);

	boolean canGuestCommentReply(int guestUid);

	boolean canAdminCommentReply(int adminUid);
}
