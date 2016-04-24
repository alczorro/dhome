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

import org.json.simple.JSONArray;
/**
 * DSN搜索服务接口
 * @author Yangxp
 *
 */
public interface IDsnSearchService {
	/**
	 * 系统初始查询，根据用户的英文名，中文名，中文名拼音查询用户的论文。
	 * 并将existIds中的论文过滤掉后返回查询结果。
	 * @param uid TODO
	 * @param offset 查询偏移量
	 * @param size 查询论文条数
	 * @param existIds 需要过滤到的论文ID
	 * @return 论文信息的JSON数组
	 */
	JSONArray initQuery(int uid, int offset, int size, List<Long> existIds);
	/**
	 * 根据用户输入的关键词进行论文搜索，并将existIds中的论文过滤掉后返回查询结果。
	 * @param uid TODO
	 * @param keyword 关键词
	 * @param offset 查询偏移量
	 * @param size 查询论文条数
	 * @param existIds 需要过滤到的论文ID
	 * @return 论文信息的JSON数组
	 */
	JSONArray query(int uid, String keyword, int offset, int size, List<Long> existIds);
	/**
	 * 通过Paper ID集合组合成的字符串从DSN获取Paper信息组成的JSON数组
	 * @param paperIds
	 * @return
	 */
	JSONArray getDsnPapers(String paperIds);
	/**
	 * 获取默认搜索的关键词组，即用户中文名，英文名，中文拼音等。
	 * @param uid 用户ID
	 * @return
	 */
	String getUserNameString(int uid);
}
