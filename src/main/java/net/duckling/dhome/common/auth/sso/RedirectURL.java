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
package net.duckling.dhome.common.auth.sso;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
/**
 * 重定向URL
 * @author Yangxp
 *
 */
public class RedirectURL {
	private boolean redirect;
	private String url;
	/**
	 * 构造函数
	 * @param redirect
	 * @param url
	 */
	public RedirectURL(boolean redirect, String url) {
		this.redirect = redirect;
		this.url = url;
	}
	/**
	 * 返回URL
	 * @return
	 */
	public String getUrl() {
		return this.url;
	}
	/**
	 * 返回是否重定向标志
	 * @return
	 */
	public boolean isRedirect() {
		return this.redirect;
	}
	/**
	 * 跳转到重定向的URL
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	public void forward(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		if (isRedirect()) {
			response.sendRedirect(url);
		} else {
			RequestDispatcher dispatcher = request.getRequestDispatcher(url);
			dispatcher.forward(request, response);
		}
	}
}
