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
package net.duckling.dhome.common.dsn;

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.people.Paper;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 * Paper对象和JSON对象相互转换的工具类
 * @author Yangxp
 *
 */
public final class PaperJSONHelper {
	
	public static final String URL_NOT_USED = "http://apps.webofknowledge.com"; 
	
	private PaperJSONHelper() {
	}
	/**
	 * 将JSONArray转换成List<Paper>
	 * @param uid 用户ID
	 * @param array 待转换的JSONArray对象
	 * @return
	 */
	public static List<Paper> jsonArray2PaperList(int uid, JSONArray array) {
		List<Paper> papers = new ArrayList<Paper>();
		if (null != array && array.size() > 0) {
			for (int i = 0; i < array.size(); i++) {
				JSONObject obj = (JSONObject) array.get(i);
				Paper paper = jsonObject2Paper(uid, obj);
				papers.add(paper);
			}
		}
		return papers;
	}
	/**
	 * 将JSONObject对象转换成Paper对象
	 * @param uid 用户ID
	 * @param obj 待转换的JSONObject对象
	 * @return
	 */
	public static Paper jsonObject2Paper(int uid, JSONObject obj) {
		/** 从Bib解析后的JSON会带上UID, 从DSN带来的JSON不会带上UID **/
		String uidStr = (String) obj.get("uid");
		int realUid = (uid < 0 && null != uidStr) ? Integer.parseInt(uidStr) : uid;

		String localFulltextURL = (String) obj.get("localFulltextURL");
		String dsnPaperIdStr = String.valueOf(obj.get(DsnClient.DSN_PAPER_ID));
		long dsnPaperId = (!StringUtils.isBlank(dsnPaperIdStr) && !"null".equals(dsnPaperIdStr)) ? Long
				.valueOf(dsnPaperIdStr) : 0;
		Object timeCited = obj.get(DsnClient.TIMECITED);
		String volumeIssue = StringUtils.trim((String) obj.get(DsnClient.VOLUMEISSUE));
		return Paper.build(realUid, (String) obj.get(DsnClient.TITLE), (String) obj.get(DsnClient.AUTHORS),
				(String) obj.get(DsnClient.SOURCE), volumeIssue,
				(String) obj.get(DsnClient.PUBLISHEDTIME),(null==timeCited)?"":String.valueOf(timeCited),
				(String) obj.get(DsnClient.SUMMARY), (String) obj.get(DsnClient.LANGUAGE),
				(String) obj.get(DsnClient.KEYWORDS), localFulltextURL, (String) obj.get(DsnClient.PAPERURL), 0, 0,
				dsnPaperId, (String)obj.get(DsnClient.PAGES));
	}
	/**
	 * 将Paper转换成JSONObject对象
	 * @param paper 待转换的Paper对象
	 * @return
	 */
	public static JSONObject paper2JSONObject(Paper paper){
		JSONObject obj = new JSONObject();
		obj.put("id", paper.getId());
		obj.put("uid", paper.getUid());
		obj.put("title", formatString(paper.getTitle()));
		obj.put("authors", paper.getAuthors());
		obj.put("source", formatString(paper.getSource()));
		String volumeIssue = formatString(paper.getVolumeIssue());
		obj.put("volumeIssue", eraseBracket(volumeIssue));
		obj.put("pages", paper.getPages());
		obj.put("publishedTime", paper.getPublishedTime());
		obj.put("paperURL", formatString(filterURL(paper.getPaperURL())));
		obj.put("localFulltextURL", formatString(paper.getLocalFulltextURL()));
		obj.put("clbId", paper.getClbId());
		obj.put("summary", formatString(paper.getSummary()));
		return obj;
	}
	
	public static String eraseBracket(String volumeIssue){
		String result = volumeIssue;
		if(!StringUtils.isBlank(volumeIssue)){
			boolean start = volumeIssue.startsWith("(") || volumeIssue.startsWith("（");
			boolean end = volumeIssue.endsWith(")") || volumeIssue.endsWith("）");
			if(start && end){
				result = result.substring(1, volumeIssue.length()-1);
			}
		}
		return result;
	}
	
	private static String formatString(String source){
		if(null == source){
			return null;
		}
		String result = source.replace("\\", "");
		result = result.replace("\"", "");
		result = result.replace("\n", " ");
		result = result.replace("'", "");//此处有待改进
		result = result.replace("&", "&amp;");
		result = result.replace(">", "&gt;");
		return result.replace("<", "&lt;");
	}
	
	public static String filterURL(String url){
		if(null != url && !"".equals(url.trim()) && !url.contains(URL_NOT_USED)){
			return url;
		}
		return "";
	}
	/**
	 * 将InstitutionPaper转换成JSONObject对象
	 * @param InstitutionPaper 待转换的InstitutionPaper对象
	 * @return
	 */
	public static JSONObject institutionPaper2JSONObject(InstitutionPaper paper){
		JSONObject obj = new JSONObject();
		obj.put("id", paper.getId());
		obj.put("publicationYear", paper.getPublicationYear());
		obj.put("title", formatString(paper.getTitle()));
		obj.put("authors", paper.getAuthors());
		obj.put("startPage", paper.getStartPage());
//		String volumeIssue = formatString(paper.getVolumeIssue());
		obj.put("volumeNumber", paper.getVolumeNumber());
		obj.put("endPage", paper.getEndPage());
		obj.put("series", paper.getSeries());
		obj.put("publication", paper.getPublication());
		obj.put("citation", paper.getCitation());
		obj.put("clbId", paper.getClbId());
		obj.put("summary", formatString(paper.getSummary()));
		obj.put("departName", paper.getDepartName());
		return obj;
	}
	
}
