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
package net.duckling.dhome.dao;

import java.util.List;

import net.duckling.dhome.domain.people.Discipline;

/**
 * @author lvly
 * @since 2012-8-21
 */
public interface IDisciplineDAO {

	/**
	 * 获得一级学科队列
	 * @return 返回一级学科
	 */
	List<Discipline> getRoot();

	/**
	 * 
	 * 获得子级学科内容
	 * @param id 父id
	 * @return 所有子项
	 */
	List<Discipline> getChild(int id);
	/**
	 * 通过学科id，获得名称
	 * @param id disciplineID
	 * @return 学科名字
	 * */
	String getName(int id);

}
