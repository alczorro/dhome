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
package net.duckling.dhome.common.validate;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.validate.annotation.DhomeValidate;
import net.duckling.dhome.common.validate.domain.ValidateResult;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 * 验证拦截器
 * @author lvly
 * @since 2012-9-28
 */
public class DhomeValidateInterceptor extends HandlerInterceptorAdapter{
	private static final Logger LOG=Logger.getLogger(DhomeValidateInterceptor.class);
	@Autowired
	private CreateIndexValidator createIndexValidator;
	private Map<String,Validator> validators;
	private void init(){
		if(validators==null){
			validators=new HashMap<String,Validator>();
			validators.put(Validator.CREATE_INDEX, createIndexValidator);
		}
	}
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		init();
		if(handler==null){
			return true;
		}
		HandlerMethod method = (HandlerMethod) handler;
		Method destMethod= method.getMethod();
        if(destMethod==null){
        	return true;
        }
        DhomeValidate validate=destMethod.getAnnotation(DhomeValidate.class);
        if(validate!=null){
        	String validatorStr=validate.validator();
        	ValidateResult result=null;
        	//start
        	Validator validator=validators.get(validatorStr);
        	if(validator!=null){
        		result=validator.validate(request);
        	}else{
        		LOG.error(" the validator named ["+validatorStr+"] is not found");
        		return false;
        	}
        	//end
        	if(result!=null&& result.isPass()){
        		return true;
        	}
        	request.setAttribute("result", result);
        	try {
				request.getRequestDispatcher("/system/error/validate").forward(request, response);
			} catch (ServletException e) {
				LOG.error(e.getMessage(),e);
			} catch (IOException e) {
				LOG.error(e.getMessage(),e);
			}
        	return false;
        }
		return true;
	}
	

}
