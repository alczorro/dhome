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
package net.duckling.dhome.web;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.validate.domain.ValidateResult;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author lvly
 * @since 2012-9-28
 */
@Controller
@RequestMapping("/system/error")
public class ErrorController {
	@RequestMapping(value="/validate")
	public ModelAndView gotoValidateError(HttpServletRequest request){
		ValidateResult result=(ValidateResult)request.getAttribute("result");
		ModelAndView mv=new ModelAndView("validateError");
		if(result!=null){
			mv.addObject("result",result);
			return mv;
		}
		return mv;
	}
	@RequestMapping(value="/institution")
	public String gotoInstitutionError(){
		return "institution/instituteHomeNotFound";
	}
}
