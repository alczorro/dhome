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
package net.duckling.dhome.web.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.log4j.Logger;

import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.domain.people.SimpleUser;

/**
 * 顶菜单加载
 * 
 * @author zhaojuan
 */
public class TopBannerLoaderTag extends TagSupport  implements TryCatchFinally{
	private static final long serialVersionUID = 2346287364871L;
	private static final Logger LOG = Logger.getLogger(TopBannerLoaderTag.class);
	@Override
	public int doStartTag() throws JspException {
		HttpSession session = pageContext.getSession();
		SimpleUser su = (SimpleUser) session.getAttribute(Constants.CURRENT_USER);
		String domain = (String)session.getAttribute(Constants.CURRENT_USER_DOMAIN);
		pageContext.setAttribute("tag_currentUser", su);
		pageContext.setAttribute("tag_myDomain", domain);
		pageContext.setAttribute("tag_isDiscoverOrIndex", isDiscoverOrIndex());
		pageContext.setAttribute("tag_isMyProfileSetting", isMyProfileSetting());
		return SKIP_BODY;

	}
  
	/**判断请求的是否是发现页面还是首页 */
	private String isDiscoverOrIndex(){
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String requestURI = request.getRequestURI();
		if(requestURI!=null){
			if(requestURI.contains("views/discover.jsp")||requestURI.contains("views/institution/instituteDiscover.jsp")){
				return "discover";
			}
			else if(requestURI.endsWith("/views/index.jsp")){
				return "index";
			}else if(requestURI.endsWith("/views/dhomeHelp.jsp")){
				return "help";
			}
		}
		return "";
	}
	/**判断请求的是否是个人信息设置 */
	private boolean isMyProfileSetting(){
		HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
		String requestURI = request.getRequestURI();
		if(requestURI==null){
			return false;
		}
		if(requestURI.contains("views/personalBaseInfo.jsp")
					||requestURI.contains("views/personalEducationInfo.jsp")
					||requestURI.contains("views/personalPhotoInfo.jsp")||requestURI.contains("views/personalWorkInfo.jsp")){
				return true;
					
		}
		return false;
	}
	@Override
	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}
	@Override
	public void doCatch(Throwable t){
		LOG.error(t);
	}

	@Override
	public void doFinally() {
		
	}

}
