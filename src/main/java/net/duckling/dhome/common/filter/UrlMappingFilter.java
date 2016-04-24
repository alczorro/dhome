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
package net.duckling.dhome.common.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.facade.ApplicationFacade;
import net.duckling.dhome.common.util.UrlMappingUtil;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.people.UrlMapping;
import net.duckling.dhome.service.IUrlMappingCacheService;
import net.duckling.dhome.service.impl.UrlMappingCacheService;
/**
 * url映射过滤器，用于个人域名设置，
 * @author lvly
 * @since 2012-12-31
 * */	
public class UrlMappingFilter implements Filter {
	private IUrlMappingCacheService cacheService;
	private String encoding;
	@Override
	public void destroy() {
		cacheService=null;
	}
	
	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		init(null);
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;
		
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		
		String rootUrl = UrlUtil.getAddress(request);
		String rootUri = UrlUtil.getURI(request);
		UrlMapping mapping = null;
		//如果不是默认地址就得匹配 urlMapping并转发了
		if (!UrlMappingUtil.isDefaultUrl(request)&&UrlMappingUtil.needConvert(rootUri)) {
			mapping = cacheService.getUrlMapping(rootUrl);
			if (mapping == null) {
				filterChain.doFilter(request, response);
			}else{
				String rewriteUrl  = UrlMappingUtil.getDefaultUrl(rootUri, mapping.getType(), mapping.getDomain());
				request.getRequestDispatcher(rewriteUrl).forward(request, response);
			}
			return;
		}
		filterChain.doFilter(request, response);
		
	}
	@Override
	public void init(FilterConfig arg0) throws ServletException {
		if(cacheService==null){
			cacheService = (IUrlMappingCacheService) ApplicationFacade.getAnnotationBean(UrlMappingCacheService.class);
		}
		encoding="utf-8";
	}
}
