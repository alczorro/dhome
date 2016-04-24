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
package net.duckling.dhome.domain.people;


/**
 * @author lvly
 * @since 2013-9-5
 */
public class UserSetting {
	private int id;
	private int uid;
	private String key;
	private String value;
	
	public static final String KEY_MSG_BOARD_KEY="msgBoard";
	public static final String VALUE_MSG_BOARD_VALUE_OPEN="open";
	public static final String VALUE_MSG_BOARD_VALUE_CLOSE="close";
	
	public boolean isNull(){  
		return id==0&&uid==0&&key==null&&value==null;
	}
	public boolean isMsgBoardOpen(){
		return isNull()||(KEY_MSG_BOARD_KEY.equals(key)&&VALUE_MSG_BOARD_VALUE_OPEN.equals(value));
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	
	
}
