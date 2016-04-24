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
package net.duckling.dhome.web.helper;

import net.duckling.dhome.common.util.CommonUtils;

/**
 * 处理字符串的工具类，针对Page，
 * @author lvly
 * @since 2012-12-3
 */
public final class PageCharHelper {
	private PageCharHelper(){}
	public static final String ENTER_HTML="<p><br /></p>";
	public static final String ENTER_HTML_IN_EDIT_HTML="<p>\r\n&nbsp;&nbsp;&nbsp;&nbsp;<br />\r\n</p>";
	public static final String IE_ENTER_HTML="<p> </p>";
	public static final String IE_ENTER_HTML_IN_EDIT_HTML="<p>\r\n&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;\r\n</p>";
	
	/**
	 * 删除前后空行
	 * @param str 在IE或者火狐上Enter的标示方法不同，立志全部删掉
	 * @return 删掉前后空行以后的字符
	 * */
	public static String deleteEnter(String str){
		String result=PageCharHelper.deleteHtmlEnter(str, ENTER_HTML);
		result=PageCharHelper.deleteHtmlEnter(result, ENTER_HTML_IN_EDIT_HTML);
		result=PageCharHelper.deleteHtmlEnter(result, IE_ENTER_HTML);
		result=PageCharHelper.deleteHtmlEnter(result, IE_ENTER_HTML_IN_EDIT_HTML);
		return result;
		
	}
	/**
	 * 判断字符串是否在这个char的范围里
	 * @param num char的number
	 * @param gt GreaterThan
	 * @param lt LetterThan
	 * @return flag
	 * */
	private static boolean isGtAndLt(int num,int gt,int lt){
		return num>=gt&&num<=lt;
	}
	/**
	 * 只留下英文和数字
	 * @param str @#$%^&abc*&^%123
	 * @return abc123
	 * */
	public static String getEnOnly(String str){
		if(CommonUtils.isNull(str)){
			return "";
		}
		StringBuilder sb=new StringBuilder();
		for(int i=0;i<str.length();i++){
			char ls=str.charAt(i);
			if(isGtAndLt(ls,'a','z')||isGtAndLt(ls,'A','Z')||isGtAndLt(ls,'0','9')){
				sb.append(ls);
			}
		}
		return sb.toString();
	}
	/**
	 * 从content删除指定的字符串，掐头去尾，碰到不匹配就中断
	 * @param 目标字符串 aabaacdaaada
	 * @param enterHtml a
	 * @return 结果 baacdaaad
	 */
	public static String deleteHtmlEnter(String content,String enterHtml) {
		StringBuilder sb=new StringBuilder(content.trim());
		while(sb.indexOf(enterHtml)==0){
			sb.delete(0, enterHtml.length());
			trim(sb);
		}
		while(sb.lastIndexOf(enterHtml)>0&&sb.lastIndexOf(enterHtml)==(sb.length()-enterHtml.length())){
			sb.delete(sb.length()-enterHtml.length(),sb.length());
			trim(sb);
		}
		return sb.toString();
		
	}
	/**
	 * 对stringBuffer做trim操作
	 * @param sb StringBuilder
	 * @return 返回.trim();
	 * */
	public static void trim(StringBuilder sb){
		while(sb.charAt(0)<=' '){
			sb.delete(0, 1);
		}
		while(sb.charAt(sb.length()-1)<=' '){
			sb.delete(sb.length()-1, sb.length());
		}
	}
}
