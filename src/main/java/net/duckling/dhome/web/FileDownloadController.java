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
package net.duckling.dhome.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.service.impl.ClbFileService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * 文件下载
 * @author Yangxp
 * @date 2012-08-30
 */
@Controller
@RequestMapping("/system/download/{clbId}")
public class FileDownloadController {
	
	@Autowired
	private ClbFileService fileService;
	/**
	 * 下载指定clbId的文件
	 * @param request
	 * @param response
	 * @param clbId
	 */
	@RequestMapping
	public void download(HttpServletRequest request, HttpServletResponse response, @PathVariable("clbId") int clbId){
		fileService.fetchContent(clbId, request, response);
	}
}
