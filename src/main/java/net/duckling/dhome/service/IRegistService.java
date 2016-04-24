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
package net.duckling.dhome.service;

import java.util.List;

import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Discipline;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.SimpleUser;


/**
 * 注册模块服务层
 * @author lvly
 * @since 2012-08-06
 * */
public interface IRegistService {
	
	/**创建一个账号
	 * @param user 需要新建的账户
	 * @param password 密码
	 * @Param umtExist UMT存在用户的标志，此字段用于用户能登录UMT但没有创建主页
	 * @return 如果失败则会返回负数
	 * */
	int createSimpleUser(SimpleUser user,String password, boolean umtExist);
	
	/**创建一个用户的更详细信息
	 * @param user 需要新建的详细信息
	 * */
	int createDetailedUser(DetailedUser user);
	
	/**验证一个邮箱时候已经注册过
	 * @param email 邮箱地址
	 * @return boolean 是否已经被注册
	 * */
	boolean isEmailUsed(String email) ;
	
	/**判断是否已经创建个人主页
	 * @param email 邮箱地址
	 **/
	boolean hasCreateHomePage(String email);
	
	/**判断域名可以使用
	 * @param  domain 域名
	 * @return  返回"域名使用了没？"
	 * */
	boolean isDomainUsed(String domain);
	
	/**创建一个教育背景实例
	 * @param uid 当前登录用户ID
	 * @param degree 学历
	 * @param department 院校
	 * @param institution 大学id或者名称
	 * @return 返回新生成的ID
	 * */
	int createEducation(int uid, String degree, String department, String institution);
	
	/**创建一个工作经历实例
	 * @param uid 当前登录用户ID
	 * @param position 职称
	 * @param department 部门
	 * @param institution 研究机构Id或者名称
	 * @return 返回新生成的ID
	 * */
	int createWork(int id, String position, String department, String institution);
	
	/**更新用户信息，给出的user实例，应指出id
	 * @param user 用户实例
	 * */
	void updateSimpleUser(SimpleUser user);
	
	/**创建个人域名
	 * @param home 域名信息
	 * @return 
	 * */
	int createHome(Home home);

	/**
	 * 获得一级学科内容
	 * @return
	 */
	List<Discipline> getRootDiscipline();

	/**
	 * 
	 * 获得子级学科内容
	 * @param id 父id
	 * @return 所有子项
	 */
	List<Discipline> getChildDiscipline(int id);

	
	
}
