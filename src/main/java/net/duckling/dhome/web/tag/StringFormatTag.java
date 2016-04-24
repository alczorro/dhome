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

import java.io.IOException;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import net.duckling.dhome.common.util.CommonUtils;

import org.apache.log4j.Logger;

/**
 * 左菜单项动态加载
 * @author zhaojuan
 * 
 */
public class StringFormatTag extends TagSupport{
	private static final Logger LOG = Logger.getLogger(StringFormatTag.class);
	private static final long serialVersionUID = 11231231232L;

	private String value ;
	private String split ;
	
	public String getValue() {
		return CommonUtils.trim(value);
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getSplit() {
		return CommonUtils.trim(split);
	}
	public void setSplit(String split) {
		this.split = split;
	}
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		try {
			if(CommonUtils.isNull(value)){
				return SKIP_BODY;
			}
			if(CommonUtils.isNull(split)){
				split="，";
			}
			String[] strs=value.split(split);
			StringBuffer sb=new StringBuffer();
			for(String str:strs){
				if(!CommonUtils.isNull(str)){
					sb.append(str).append(split);
				}
			}
			out.println(CommonUtils.format(sb.toString(), split));
		} catch (IOException e) {
			LOG.error(e);
		}
		return SKIP_BODY;
	}
}
