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
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.duckling.dhome.common.util.CommonUtils;

import org.apache.log4j.Logger;

/**
 * encode工具类
 * @author lvly
 * @since 2012-9-28
 */
public class DecodeTag extends TagSupport{
	private static final long serialVersionUID = 8374387623871L;
	private static final Logger LOG = Logger.getLogger(DecodeTag.class);

	private String value="";

	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			if(CommonUtils.isNull(value)){
				return SKIP_BODY;
			}
			out.print(URLDecoder.decode(URLDecoder.decode(value,"utf8"),"utf8"));
		} catch (UnsupportedEncodingException e) {
			LOG.error(e,e);
		} catch (IOException e) {
			LOG.error(e,e);
		}
		return SKIP_BODY;
	}
	

}
