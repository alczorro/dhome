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

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.domain.people.UrlMapping;

/**
 * 
 * url映射工具类
 * @author lvly
 * @since 2012-9-5
 */
public final class UrlMappingUtil {
	private UrlMappingUtil(){}
	public static final String DEFAULT_URL="www.escience.cn";
	public static final String URL_PEOPLE="/people";
	public static final String URL_SYSTEM="/system";
	public static final String URL_RESOURCES="/resources";
	public static final String URL_INSTITUTION="/institution";
	public static final String SPLIT="/";
	/**
	 * 判断是否需要用urlMapping ，原则上，机构，system，resource等式没有urlMapping的
	 * @param 是否需要转换URL
	 * */
	public static boolean needConvert(String uri) {
		return !uri.startsWith(URL_INSTITUTION)&&!uri.startsWith(UrlMappingUtil.URL_PEOPLE) && !uri.startsWith(UrlMappingUtil.URL_SYSTEM)&&!uri.startsWith(UrlMappingUtil.URL_RESOURCES);

	}
	/**
	 * 拼成一个符合defaultUrl的url
	 * @param uri 请求uri
	 * @param type 机构还是个人,默认是个人
	 * @param domain 域名
	 * @return url /people/{domain}/xxx.html等
	 * */
	public static String getDefaultUrl(String uri, String type, String domain){
		String result="";
		if(UrlMapping.TYPE_INSTITUTION.equals(type)){
			if(!uri.startsWith(URL_INSTITUTION)){
				result+=URL_INSTITUTION;
			}
		}else{
			if(!uri.startsWith(URL_PEOPLE)){
				result+=URL_PEOPLE;
			}
		}
		
		if(!uri.contains(domain)){
			result+=SPLIT+domain;
		}
		return result+uri;
	}
	/**判断是否是学术主页正常url
	 * @param request 用来获取url用
	 * @return boolean
	 * */
	public static boolean isDefaultUrl(HttpServletRequest request){
		return UrlUtil.getAddress(request).equals(DEFAULT_URL);
	}
	/**
	 * 从/people/domain/a/b/c...中提取domain
	 * @param uri
	 * @return domain
	 * */
	public static String getDomain(String uri){
		String domain=uri.replace(URL_PEOPLE+SPLIT, "");
		if(domain.contains(SPLIT)&&domain.indexOf(SPLIT)<domain.length()-1){
			return domain.substring(0,domain.indexOf(SPLIT));
		}
		return domain;
	}
	/**
	 * 重写uri以适合自定义域名命名规则(只适合people类uri,不应包含contextPath)
	 * @return 返回contextPath后面的路径
	 * */
	public static String removePeople(String uri,HttpServletRequest request,UrlMapping mapping){
		String pUri=uri;
		if(pUri.startsWith(request.getScheme())){
			return pUri;
		}
		String contextPath=request.getContextPath();
		pUri=pUri.replace(contextPath, "");
		if(mapping==null){
			return contextPath+(pUri.startsWith(SPLIT)?pUri:SPLIT+pUri);
		}else if(!isDefaultUrl(request)&&pUri.startsWith(URL_PEOPLE)&&isMyself(request,pUri, mapping.getDomain())){
			String result=pUri.replace(URL_PEOPLE,"");
			result=result.substring(1,result.length());
			if(result.contains(SPLIT)){
				result=result.substring(result.indexOf(SPLIT),result.length());
			}else{
				result="/index.html";
			}
			return contextPath+result;
		}else{
			return contextPath+(pUri.startsWith(SPLIT)?pUri:SPLIT+pUri);
		}
		
	}
	/**判断是否是本用户，也就是缩不缩url的问题
	 * @param request 请求，主要获取session用
	 * @param destUri 请求路径
	 * @param url-mapping对应的url
	 * */
	public static boolean isMyself(HttpServletRequest request,String destUri,String mappingDomain){
		String destDomain=UrlMappingUtil.getDomain(destUri);
		String domain=SessionUtils.getDomain(request);
		return destDomain.equals(domain)&&mappingDomain.equals(destDomain);
	}
}
