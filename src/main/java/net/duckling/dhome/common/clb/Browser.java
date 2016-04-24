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

package net.duckling.dhome.common.clb;

import java.io.UnsupportedEncodingException;

import javax.mail.internet.MimeUtility;

import org.apache.commons.lang.StringUtils;

/**
 * @group: net.duckling
 * @title: Browser.java
 * @description: TODO
 * @author clive
 * @date 2012-8-5 下午8:53:49
 */
public final class Browser {
	private static final String CHAR_SET_UTF_8 = "UTF-8";
	private static final int CODE_FILE_LEN = 150;

	public static enum BrowserType {
		Chrome, Firefox, MsIE, Safari, Unknown
	};
	
	private Browser(){}
	/**
	 * 将文件名编码成指定浏览器类型下的文件名
	 * @param agent 浏览器类型
	 * @param filename 原始文件名
	 * @return
	 * @throws UnsupportedEncodingException
	 */
	public static String encodeFileName(String agent, String filename) throws UnsupportedEncodingException {
		BrowserType type = recognizeBrowser(agent);
		String result;
		switch (type) {
		case MsIE:
		case Chrome:
			result =  getFileName(encodeWithURLEncoder(filename));
			break;
		case Firefox:
			result = getFileName(encodeWithBase64(filename));
			break;
		case Safari:
			result = getFileName(encodeWithISO(filename));
			break;
		default:
			result = getFileName(filename);
			break;
		}
		return result;
	}
	
	private static String getFileName(String filename){
		return "attachment;filename=\"" + filename + "\"";
	}

	private static String encodeWithBase64(String filename) throws UnsupportedEncodingException {
		return MimeUtility.encodeText(filename, CHAR_SET_UTF_8, "B");
	}

	private static String encodeWithISO(String filename) throws UnsupportedEncodingException {
		return new String(filename.getBytes(CHAR_SET_UTF_8), "ISO8859-1");
	}

	private static String encodeWithURLEncoder(String filename) throws UnsupportedEncodingException {
		String codedfilename = java.net.URLEncoder.encode(filename, CHAR_SET_UTF_8);
		codedfilename = StringUtils.replace(codedfilename, "+", "%20").replaceAll("%28", "(").replaceAll("%29", ")");
		if (codedfilename.length() > CODE_FILE_LEN) {
			codedfilename = new String(filename.getBytes(CHAR_SET_UTF_8), "ISO8859-1");
			codedfilename = StringUtils.replace(codedfilename, " ", "%20");
		}
		return codedfilename;
	}
	/**
	 * 识别浏览器类型
	 * @param agent
	 * @return
	 */
	public static BrowserType recognizeBrowser(String agent) {
		BrowserType type = BrowserType.Unknown;
		if (agent != null) {
			String temp = agent.toLowerCase();
			if (-1 != temp.indexOf("msie")) {
				type = BrowserType.MsIE;
			} else if (-1 != temp.indexOf("chrome")) {
				type = BrowserType.Chrome;
			} else if (-1 != temp.indexOf("safari")) {
				type = BrowserType.Safari;
			} else if (-1 != temp.indexOf("firefox")) {
				type = BrowserType.Firefox;
			}
		}
		return type;
	}
}
