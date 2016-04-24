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

import net.duckling.dhome.domain.people.ComposedUser;
import net.duckling.dhome.domain.people.DisciplineStructrue;
/**
 * 发现对象的服务
 * @author zhaojuan
 *
 */
public interface IComposedUserService {
	/**
	 * 得到所有的发现对象
	 * @param offset 偏移量
	 * @param size 需要获取的对象数目
	 * @return
	 */
     List<ComposedUser> getAllComposedUsers(int offset, int size);
     /**
      * 得到最近的发现对象
      * @param offset 偏移量
      * @param size 需要获取的对象数目
      * @return
      */
     List<ComposedUser> getLatestComposedUsers(int offset, int size);

     
     /**获得所有学科目录树
      * 
      * @author lvly
      * @return 获得一个学科的完整树
      * */
     DisciplineStructrue getDiscipline();
     
 	/**
 	 * 根据所选学科返回用户
 	 * @param first 一级学科id
 	 * @param  second 二级学科id
 	 * @param offset 偏移量
 	 * @param size 一次取的数量
 	 * @return 
 	 */
      List<ComposedUser> getComposedUsersByDiscipline(int first,int second,int offset,int size);

     /**
      * 搜索指定关键词keyword的用户，搜索字段为zh_name, pinyin
      * @param keyword 关键词
      * @param offset 偏移量
      * @param size 需要获取的对象数目
      * @return
      */
     List<ComposedUser> searchComposedUsers(String keyword, int offset, int size);
	/**
	 * 获得关键字搜索的返回记录数量
	 * @param keyword
	 * @return
	 */
	int getSearchComposedUsersCount(String keyword);
	
	/**
	 * 根据研究兴趣发现用户
	 * @param keyword
	 * @param offset
	 * @param size
	 * @return
	 */
	List<ComposedUser> getComposedUsersByInterest(String keyword, int offset, int size);

}
