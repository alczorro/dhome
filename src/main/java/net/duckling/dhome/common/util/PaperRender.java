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
package net.duckling.dhome.common.util;

import java.util.List;

import net.duckling.dhome.common.dsn.DsnClient;
import net.duckling.dhome.common.dsn.PaperJSONHelper;
import net.duckling.dhome.domain.people.Paper;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
/**
 * 处理论文输出格式的类
 * 处理内容：1)过长的作者列表，2)自己的加重显示(待完善)
 * @author Yangxp
 * @date 2012-08-20
 */
public final class PaperRender {
	private PaperRender(){}
	private static final String AUTHOR_EMPHISIS = "strong";
	private static final String ELLIPSIS = "...";
	private static final int AUTHOR50 = 50;
	private static final int AUTHOR49 =49;
	/**
	 * 格式化List<Paper>，userStr为以逗号分隔的用户中英文名称。格式化内容如下：
	 * 1) 若Authors中包含userStr中的内容，则用构造函数中的authorEmphisis标签强调显示；
	 *    另外，作者个数最多只显示3个（包含自身）
	 * @param papers
	 * @param userStr
	 * @return
	 */
	public static JSONArray format(List<Paper> papers, String userStr){
		JSONArray result = new JSONArray();
		if(null != papers && ! papers.isEmpty()){
			for(Paper paper: papers){
				String tempAuthors = paper.getAuthors();
				String authors = (null != tempAuthors)?formatAuthors(tempAuthors, userStr):"";
				JSONObject obj = PaperJSONHelper.paper2JSONObject(paper);
				obj.put("short"+DsnClient.AUTHORS, authors);
				result.add(obj);
			}
		}
		return result;
	}
	
	/**
	 * 格式化JSONArray（包含Paper集合的JSON对象），userStr为以逗号分隔的用户中英文名称。格式化内容如下：
	 * 1) 若Authors中包含userStr中的内容，则用构造函数中的authorEmphisis标签强调显示；
	 *    另外，作者个数最多只显示3个（包含自身）
	 * @param papers
	 * @param userStr
	 * @return
	 */
	public static JSONArray format(JSONArray array, String userStr){
		JSONArray result = new JSONArray();
		if(null != array && !array.isEmpty()){
			int size = array.size();
			for(int i=0; i<size; i++){
				JSONObject obj = (JSONObject)array.get(i);
				String tempAuthors = (String)obj.get(DsnClient.AUTHORS);
				String authors = (null != tempAuthors)?formatAuthors(tempAuthors, userStr):"";
				obj.put("short"+DsnClient.AUTHORS, authors);
				result.add(obj);
			}
		}
		return result;
	}
	
	private static String formatAuthors(String authors, String userStr){
		String[] userNames = (null==userStr)?new String[0]:userStr.split(",");
		int authors50Len = getNAuthorsLength(authors, AUTHOR50);
		int authors49Len = getNAuthorsLength(authors, AUTHOR49);
		int authorsLen = authors.length();
		String finalAuthors = "";
		for(String un : userNames){
			int index = authors.indexOf(un);
			if(index >= 0){
				String emUn = "<"+AUTHOR_EMPHISIS+">"+un+"</"+AUTHOR_EMPHISIS+">";
				if(index > authors50Len){
					finalAuthors = authors.substring(0, authors49Len)+ELLIPSIS+emUn+ELLIPSIS;
				}else{
					finalAuthors = authors.substring(0, index);
					finalAuthors += emUn;					
					if(authorsLen > authors50Len){
						finalAuthors += authors.substring(index+un.length(), authors50Len);
						finalAuthors += ELLIPSIS;
					}else{
						finalAuthors += authors.substring(index+un.length(), authorsLen);
					}
				}
				break;
			}
		}
		return generateFinalAuthors(finalAuthors, authors, authors50Len);
	}
	
	private static String generateFinalAuthors(String fa, String authors, int tal){
		if(!"".equals(fa)){
			return fa;
		}
		int len = authors.length();
		if(tal>=len){
			return authors;
		}else{
			return authors.substring(0, tal)+ELLIPSIS;
		}
	}
	
	private static int getNAuthorsLength(String authors, int number){
		String curDelimiter = getDelimiter(authors);
		String[] subStrs = authors.split(curDelimiter);
		int size = subStrs.length;
		int result = 0;
		for(int i=0; i<number && i<size; i++){
			result += subStrs[i].length();
		}
		return result+(number-1)*curDelimiter.length();
	}
	
	private static String getDelimiter(String authors){
		String[] delimiters = getDelimiters();
		String curDelimiter = ";";
		if(null != authors){
			for(String temp: delimiters){
				if(authors.contains(temp)){
					curDelimiter = temp;
					break;
				}
			}
		}
		return curDelimiter;
	}
	
	private static String[] getDelimiters(){
		return new String[]{";", "；", ",","，"," "};
	}
}
