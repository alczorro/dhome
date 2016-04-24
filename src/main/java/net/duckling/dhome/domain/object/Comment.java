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
 * 留言板
 * @author lvly
 * @since 2013-9-5
 */
public class Comment {
	/**
	 * primary key
	 */
	private int id;
	/**
	 * comment content
	 */
	private String content;
	/**
	 * who comment this
	 */
	private int uid;
	/**
	 * commenter Name
	 */
	private String name;
	
	private String domain;
	
	private int image;
	/**
	 * when comment
	 */
	private Date commentTime;
	/**
	 * comment on who
	 */
	private int commentHostUid;
	
	private String commentHostUserName;
	
	private String commentHostDomain;
	
	private int commentHostImage;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getImage() {
		return image;
	}
	public void setImage(int image) {
		this.image = image;
	}
	public String getCommentHostDomain() {
		return commentHostDomain;
	}
	public void setCommentHostDomain(String commentHostDomain) {
		this.commentHostDomain = commentHostDomain;
	}
	public int getCommentHostImage() {
		return commentHostImage;
	}
	public void setCommentHostImage(int commentHostImage) {
		this.commentHostImage = commentHostImage;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCommentHostUserName() {
		return commentHostUserName;
	}
	public void setCommentHostUserName(String commentHostUserName) {
		this.commentHostUserName = commentHostUserName;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public Date getCommentTime() {
		return commentTime;
	}
	public void setCommentTime(Date commentTime) {
		this.commentTime = commentTime;
	}
	public int getCommentHostUid() {
		return commentHostUid;
	}
	public void setCommentHostUid(int commentHostUid) {
		this.commentHostUid = commentHostUid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}
