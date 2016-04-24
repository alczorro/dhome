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
package net.duckling.dhome.common.util;


/**
 * 处理社交媒体URL工具类
 * @author lvly
 * @since 2012-12-21
 */
public final class FavoriteImgUtils {
	private static final String SCHEMA = "://";
	private static final String HTTP="http";
	private static final String FAV_ICON="/favicon.ico";
	private FavoriteImgUtils() {
	}
	/**
	 * 获得图标的url，一般网站的url/favicon.ico
	 * @param url
	 * @return  一个网站图标的网址，绝对地址
	 * */
	public static String getFaviconUrl(String url) {
		return addSchema(getBaseUrl(url))+FAV_ICON;
	}
	/**
	 * 强制带上协议有则默认，无则http
	 * @param url
	 * @return added
	 * */
	public static String addSchema(String url){
		return getSchema(url)+removeSchema(url);
	}
	/**
	 * 获得协议有则默认，无则http
	 * @param url 
	 * @return
	 * */
	public static String getSchema(String url){
		if(CommonUtils.isNull(url)){
			return HTTP+SCHEMA;
		}
		boolean hasSchame=url.contains(SCHEMA);
		if(hasSchame){
			return url.substring(0, url.indexOf(SCHEMA))+SCHEMA;
		}
		return HTTP+SCHEMA;
	}
	/**
	 * 判断是否有协议
	 * @param url
	 * @return hasSchema
	 * */
	public static boolean hasSchema(String url){
		if(CommonUtils.isNull(url)){
			return false;
		}
		return url.contains(SCHEMA);
	}
	/**
	 * 强制删除协议
	 * @param url
	 * @return removed
	 * */
	public static String removeSchema(String url){
		return url.replaceFirst(getSchema(url), "");
	}
	/**
	 * 获得一般网站的BaseUrl，比如www.baidu.com/a/b/c.json 会返回www.baidu.com，如果要求强制带上协议
	 * 请组合用addSchema或者removeSchema
	 * @param url url
	 * */
	public static String getBaseUrl(String url){
		if(CommonUtils.isNull(url)){
			return null;
		}
		String urlWithoutSchema=removeSchema(url);
		if(urlWithoutSchema.contains("/")){
			urlWithoutSchema=urlWithoutSchema.substring(0,urlWithoutSchema.indexOf('/'));
		}
		return (hasSchema(url)?getSchema(url):"")+urlWithoutSchema;
	}
}
