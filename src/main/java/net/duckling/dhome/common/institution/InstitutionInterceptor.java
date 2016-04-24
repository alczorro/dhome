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
package net.duckling.dhome.common.institution;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.service.IInstitutionHomeService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 
 * 拦截机构主页，去掉无效主页（包括屏蔽，无用）
 * @author lvly
 * @since 2012-10-9
 */
public class InstitutionInterceptor extends HandlerInterceptorAdapter {
	private static final Logger LOG=Logger.getLogger(InstitutionInterceptor.class);
	private static final String PRE_URI="/institution";
	private static final Pattern PATTERN = Pattern.compile("^\\/[a-zA-Z0-9\\-]+\\/");
	@Autowired
	private IInstitutionHomeService homeService;
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException, IOException {
		String uri=request.getRequestURI();
		uri=uri.replace(request.getContextPath(), "").replace(PRE_URI, "");
		String domain="";
		Matcher matcher = PATTERN.matcher(uri);
		if(matcher.find()){
			domain=matcher.group().replaceAll("/", "");
		}else{
			LOG.error("the uri is strenge! can't find ${domain} in["+uri+"]");
			return gotoError(request,response);
		}
		boolean flag=homeService.isValidHome(domain);
		if(flag){
			return true;
		}else{
			LOG.error("the institutionHome is invalid what domain is ["+domain+"]");
			return gotoError(request, response);
		}
		
	}
	/**跳转到错误页面
	 * @throws IOException 
	 * @throws ServletException */
	private boolean gotoError(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		request.getRequestDispatcher("/system/error/institution").forward(request, response);
		return false;
	}
}
