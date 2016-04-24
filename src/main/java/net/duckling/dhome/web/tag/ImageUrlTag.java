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
 * 建议数据库的值为数字的情况下使用，0代表默认
 * @author lvly
 * @since 2012-9-18
 */
public class ImageUrlTag  extends TagSupport{
	private static final long serialVersionUID = 176532478L;
	private static final Logger LOG = Logger.getLogger(ImageUrlTag.class);
	/**
	 * clbId
	 * */
	private int imgId;
	/**
	 * 默认时候的选择，不如imgId为空或者0
	 * */
	private String  imgName;
	public int getImgId() {
		return imgId;
	}
	public void setImgId(int imgId) {
		this.imgId = imgId;
	}
	public String getImgName() {
		return imgName;
	}
	public void setImgName(String imgName) {
		this.imgName = imgName;
	}
	@Override
	public int doStartTag() throws JspException {
		JspWriter out = this.pageContext.getOut();
		HttpServletRequest request = (HttpServletRequest)pageContext.getRequest();
		String result=request.getContextPath();
		if(imgId!=0){
			result+="/system/img?imgId="+imgId;
		}else{
			result+="/resources/images/";
			if(CommonUtils.isNull(imgName)){
				result+="dhome-head.png";
			}else{
				result+=imgName;
			}
		}
		try {
			out.print(result);
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
		return SKIP_BODY;
	}
	
}
