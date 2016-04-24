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
package net.duckling.dhome.dao;

import java.util.List;

import net.duckling.dhome.domain.object.Comment;
import net.duckling.dhome.domain.object.CommentReply;
import net.duckling.dhome.domain.object.CommentView;

/**
 * @author lvly
 * @since 2013-9-5
 */
public interface IMessageBoardDAO {

	/**
	 * 增加留言
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
	int addCommentReply(CommentReply reply);
	
	/**
	 * 删除一个留言回复
	 * @param commentId
	 */
	void deleteCommentReplyById(int commentReplyId);
	
	/**
	 * 删除留言回复
	 * */
	void deleteCommentReplyByCommentId(int commentId);
	
	/**
	 * 获得某个个人主页所属的留言
	 * @param uid
	 * @return
	 */
	List<CommentView> selectByHostUid(int uid);

	/**
	 * 留言
	 * @param commentId
	 * @return
	 */
	Comment getCommentById(int commentId);

	/**
	 * 
	 * @param replyId
	 * @return
	 */
	CommentReply getCommentReplyById(int replyId);

	/**
	 * @param image
	 * @param name
	 * @param domain
	 */
	void updateUserInfo(int uid,int image, String name, String domain);
}
