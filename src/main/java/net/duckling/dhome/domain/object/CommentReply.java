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
package net.duckling.dhome.domain.object;

import java.util.Date;

/**
 * 留言回复
 * @author lvly
 * @since 2013-9-5
 */
public class CommentReply {
	private int id;
	private int commentId;
	private int replyUid;
	private String replyUserName;
	private String replyContent;
	private Date replyTime;
	private int replyImage;
	private String replyDomain;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCommentId() {
		return commentId;
	}
	public void setCommentId(int commentId) {
		this.commentId = commentId;
	}
	public int getReplyUid() {
		return replyUid;
	}
	public int getReplyImage() {
		return replyImage;
	}
	public void setReplyImage(int image) {
		this.replyImage = image;
	}
	public String getReplyDomain() {
		return replyDomain;
	}
	public void setReplyDomain(String domain) {
		this.replyDomain = domain;
	}
	public void setReplyUid(int replyUid) {
		this.replyUid = replyUid;
	}
	public String getReplyUserName() {
		return replyUserName;
	}
	public void setReplyUserName(String replyUserName) {
		this.replyUserName = replyUserName;
	}
	public String getReplyContent() {
		return replyContent;
	}
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}
	public Date getReplyTime() {
		return replyTime;
	}
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
	
	

}
