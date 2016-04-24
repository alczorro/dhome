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
package net.duckling.dhome.domain.people;

/**
 * @group: net.duckling
 * @title: Publication.java
 * @description: 发表物与用户的关联类
 * @author clive
 * @date 2012-8-4 下午5:58:48
 */
public class Publication {
	/**
	 * @Fields uid : 用户唯一数字标示
	 */
	private int uid;
	/**
	 * @Fields paperId : 论文数字标示 TODO 书籍怎么办
	 */
	private int paperId;
	/**
	 * @Fields sequence : 发表物的排序号
	 */
	private int sequence;

	public int getUid() {
		return uid;
	}

	public void setUid(int uid) {
		this.uid = uid;
	}

	public int getPaperId() {
		return paperId;
	}

	public void setPaperId(int paperId) {
		this.paperId = paperId;
	}

	public int getSequence() {
		return sequence;
	}

	public void setSequence(int sequence) {
		this.sequence = sequence;
	}

}
