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
package net.duckling.dhome.web.tag;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.duckling.dhome.common.facade.ApplicationFacade;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.UrlMappingUtil;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.people.UrlMapping;
import net.duckling.dhome.service.IUrlMappingCacheService;
import net.duckling.dhome.service.impl.UrlMappingCacheService;
import net.duckling.falcon.api.mstatic.VersionAttributeHelper;

import org.apache.log4j.Logger;

/**
 * 重写c:url,并动态指定url
 * @author lvly
 * @since 2012-9-5
 */
public class UrlFormatTag extends TagSupport{
	private static final Logger LOG = Logger.getLogger(UrlFormatTag.class);
	private static final long serialVersionUID = 1123331231232L;

	private String value ;
	
	public String getValue() {
		return CommonUtils.trim(value);
	}
	public void setValue(String value) {
		this.value = value;
	}


	public int doStartTag() throws JspException {
		IUrlMappingCacheService cacheService = (IUrlMappingCacheService) ApplicationFacade.getAnnotationBean(UrlMappingCacheService.class);

		JspWriter out = this.pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		String result="";
		//表明没有使用自定义域名
		try{
			String rootUrl=UrlUtil.getAddress(request);
			UrlMapping mapping=null;
			if(!UrlMappingUtil.isDefaultUrl(request)){
				mapping=cacheService.getUrlMapping(rootUrl);
				if(mapping==null){
					result=request.getContextPath()+value;
				}else{
					String rewriteUrl="";
					if(needConvert(value)){
						rewriteUrl=UrlMappingUtil.removePeople(value, request,mapping);
					}else{
						rewriteUrl=request.getContextPath()+value;
					}
					if(!UrlMappingUtil.isMyself(request,value, mapping.getDomain())){
						rewriteUrl=request.getContextPath()+value;
					}
					result=rewriteUrl;
				}
			}else{
			result=request.getContextPath()+value;
			}
			if(result.startsWith(UrlMappingUtil.URL_RESOURCES)){
				result=UrlUtil.addParam(result, "v", VersionAttributeHelper.readVersion(pageContext.getServletContext()).replaceAll("\\.", "_"));
			}
			out.print(result);
		}catch(IOException e){
			LOG.error(e);
		}
		
		return SKIP_BODY;
	}
	private boolean needConvert(String uri){
		return !uri.startsWith(UrlMappingUtil.URL_RESOURCES)&&!uri.startsWith(UrlMappingUtil.URL_INSTITUTION)&&!uri.startsWith(UrlMappingUtil.URL_SYSTEM );
	}
}
