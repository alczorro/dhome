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
package net.duckling.dhome.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.dsn.DsnClient;
import net.duckling.dhome.common.util.PaperRender;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.dao.ISimpleUserDAO;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IDsnSearchService;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
/**
 * 功能：链接DSN服务进行论文搜索
 * @author Yangxp
 * @date 2012-08-01
 */
@Service
public class DsnSearchService implements IDsnSearchService {

	private static final Logger LOG = Logger.getLogger(DsnSearchService.class);
	@Autowired
	private AppConfig config;
	@Autowired
	private ISimpleUserDAO simpleUserDAO;
	private DsnClient client;

	public void setDsnClient(DsnClient c){
		client = c;
	}
	public void setSimpleUserDAO(ISimpleUserDAO suDAO){
		this.simpleUserDAO = suDAO;
	}
	/**
	 * 初始化DSN客户端
	 */
	@PostConstruct
	public void init() {
		client = new DsnClient(config.getDsnServerURL());
	}
	/**
	 * 销毁DSN客户端
	 */
	@PreDestroy
	public void destroy() {
		client = null;
	}

	@Override
	public JSONArray initQuery(int uid, int offset, int size, List<Long> existIds) {
		String query = getQueryString(uid);
		JSONArray array = new JSONArray();
		try {
			JSONArray dsnResult = client.getDsnPapers(query, offset, size, true);
			array = filterExist(dsnResult, existIds);
			array = PaperRender.format(array, getUserNameString(uid));
		} catch (Exception e) {
			String errorInfo = "Dsn Search Error, query = " + query + " !";
			LOG.error(errorInfo, e);
		}
		return array;
	}

	@Override
	public JSONArray query(int uid, String keyword, int offset, int size, List<Long> existIds) {
		JSONArray array = new JSONArray();
		try {
			JSONArray dsnResult = client.getDsnPapers(keyword, offset, size, false);
			array = filterExist(dsnResult, existIds);
			array = PaperRender.format(array, getUserNameString(uid));
		} catch (Exception e) {
			String errorInfo = "Dsn Search Error, query = " + keyword + " !";
			LOG.error(errorInfo, e);
		}
		return array;
	}

	@Override
	public JSONArray getDsnPapers(String paperIds) {
		return client.getDsnPapersDetail(paperIds);
	}

	private String getQueryString(int uid) {
		SimpleUser su = simpleUserDAO.getUser(uid);
		Set<String> names = new HashSet<String>();
		StringBuilder sb = new StringBuilder("@(authors) ");
		if (null != su.getZhName()) {
			sb.append("(" + su.getZhName() + ")");
			names.add(su.getZhName());
		}
		String pinyin = su.getPinyin();
		if(pinyin.indexOf(';')>0){
			pinyin = pinyin.substring(0, pinyin.indexOf(';'));
		}	
		addString(su.getEnName(), sb, names);
		addString(pinyin, sb, names);
		addString(PinyinUtil.getPinyinMingXing(su.getZhName()), sb, names);
		addString(PinyinUtil.getPinyinXingMing(su.getZhName()), sb, names);
		return sb.toString();
	}
	
	@Override
	public String getUserNameString(int uid){
		String source = getQueryString(uid);
		String temp = source.replace("@(authors)", "");
		temp = temp.replace("(", "");
		temp = temp.replace(")", "");
		temp = temp.replace("|", ",");
		return temp;
	}
	
	private void addString(String qStr, StringBuilder sb, Set<String> names){
		if(null == qStr || "".equals(qStr.trim())){
			return;
		}
		if(!names.contains(qStr)){
			sb.append("|(" + qStr + ")");
			names.add(qStr);
		}
	}

	private JSONArray filterExist(JSONArray result, List<Long> existIds) {
		if (null == result || result.size() <= 0 || null == existIds || existIds.isEmpty()) {
			return result;
		}
		int size = result.size();
		JSONArray array = new JSONArray();
		for (int i = 0; i < size; i++) {
			JSONObject obj = (JSONObject) result.get(i);
			long dsnId = (Long) obj.get(DsnClient.DSN_PAPER_ID);
			if (!existIds.contains(dsnId)) {
				array.add(obj);
			}
		}
		return array;
	}
}
