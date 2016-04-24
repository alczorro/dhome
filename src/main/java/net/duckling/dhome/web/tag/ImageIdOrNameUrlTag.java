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

import net.duckling.dhome.common.util.CommonUtils;

import org.apache.log4j.Logger;

/**
 * 建议数据库值不是数字，而是string的情况下使用，也就是说
 * 默认是有，但是很多静态资源被引用的时候使用
 * @author lvly
 * @since 2012-9-18
 */
public class ImageIdOrNameUrlTag  extends TagSupport{
	/**
	 * 
	 */
	private static final long serialVersionUID = 176532478L;
	private static final Logger LOG = Logger.getLogger(ImageUrlTag.class);
	/**
	 * 图片信息，有可能是clbId，有可能是静态资源
	 * */
	private String img;
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		String result=request.getContextPath();
		//img为0或者空，或者直接是一个字符串（本地静态资源）
		if(CommonUtils.isNumber(img)){
			result+="/system/img?imgId=";
		}else{
			result+="/resources/images/";
		}
		try {
			out.print(result+img);
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
		return SKIP_BODY;
	}
	
}
