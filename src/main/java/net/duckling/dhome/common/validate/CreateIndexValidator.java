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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.validate.domain.ValidateResult;
import net.duckling.dhome.service.IRegistService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * @author lvly
 * @since 2012-9-28
 */
@Component
public class CreateIndexValidator implements Validator {
	@Autowired
	private IRegistService registService;
	
	private Pattern emailPattern = Pattern.compile("^((([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+(\\.([a-z]|\\d|[!#\\$%&'\\*\\+\\-\\/=\\?\\^_`{\\|}~]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])+)*)|((\\x22)((((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(([\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x7f]|\\x21|[\\x23-\\x5b]|[\\x5d-\\x7e]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(\\\\([\\x01-\\x09\\x0b\\x0c\\x0d-\\x7f]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF]))))*(((\\x20|\\x09)*(\\x0d\\x0a))?(\\x20|\\x09)+)?(\\x22)))@((([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|\\d|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.)+(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])|(([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])([a-z]|\\d|-|\\.|_|~|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])*([a-z]|[\\u00A0-\\uD7FF\\uF900-\\uFDCF\\uFDF0-\\uFFEF])))\\.?$",Pattern.CASE_INSENSITIVE);
	private Pattern domainPattern=Pattern.compile("^[a-zA-Z0-9\\-]+$");
	private static final int PASSWORD_LENGTH=6;
	@Override
	public ValidateResult validate(HttpServletRequest request) {
		ValidateResult result = new ValidateResult();
		// 验证邮箱
		String email = CommonUtils.trim(request.getParameter("emailToRegist"));
		if (CommonUtils.isNull(email)) {
			result.setKey("createIndex.check.required.email");
		}
		Matcher matcher = emailPattern.matcher(CommonUtils.killNull(email));
		if (!matcher.find()) {
			result.setKey("createIndex.check.email");
		}
//		if (registService.isEmailUsed(email)) {
//			result.setKey("createIndex.check.emailInUse");
//		}
		//password
		validatePassword(request,result);
		// domain
		String domain = CommonUtils.trim(request.getParameter("domain"));
		if (CommonUtils.isNull(domain)) {
			result.setKey("createIndex.check.required.domain");
		}
		domain = CommonUtils.killNull(domain);
		Matcher domainMatcher = domainPattern.matcher(CommonUtils.killNull(domain));
		if(!domainMatcher.find()){
			result.setKey("createIndex.check.onlyLetterNum");
		}
		if (registService.isDomainUsed(domain)) {
			result.setKey("createIndex.check.domainInUse");
		}
		//read
		String read=request.getParameter("read");
		if(CommonUtils.isNull(read)){
			result.setKey("createIndex.check.userRead.checked");
		}
		return result;
	}

	private ValidateResult validatePassword(HttpServletRequest request, ValidateResult result) {
		// umtExist
		String umtExist = CommonUtils.trim(request.getParameter("umtExist"));
		if (CommonUtils.isNull(umtExist)) {
			String password = CommonUtils.trim(request.getParameter("password"));
			String confirmPassword =CommonUtils.trim( request.getParameter("repeatPassword"));
			if (CommonUtils.isNull(password)) {
				result.setKey("createIndex.check.required.password");
			}
			if (CommonUtils.isNull(confirmPassword)) {
				result.setKey("createIndex.check.required.repeatPassword");
			}
			password = CommonUtils.killNull(password);
			confirmPassword = CommonUtils.killNull(confirmPassword);
			if (!password.equals(confirmPassword)) {
				result.setKey("createIndex.check.repeatPassword");
			}
			
			if (password.length() < PASSWORD_LENGTH) {
				result.setKey("createIndex.check.password.minLength");
			}
		}
		return result;
	}
}
