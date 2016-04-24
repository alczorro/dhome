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

/**
 * 邮件日志记录服务层
 * @author lvly
 * @since 2012-10-30
 */
public interface IEmailSendLogService {
	/**
	 * 发过此类型的邮件吗
	 * @param Email 邮箱
	 * @param type 应为EmailSendLog.TYPE_XX中的一个
	 * @return boolean 
	 * */
	boolean isSended(String email,String type);
	
	/**
	 * 记录发邮件的日志
	 * @param email 邮箱
	 * @param uid userId
	 * @param type 应为EmailSendLog.TYPE_XX中的一个
	 * */
	void writeLog(String email,String type,int uid);

}
