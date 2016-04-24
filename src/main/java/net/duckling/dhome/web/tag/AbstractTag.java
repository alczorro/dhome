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

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.TagSupport;
import javax.servlet.jsp.tagext.TryCatchFinally;

import org.apache.log4j.Logger;

public abstract class AbstractTag extends TagSupport implements TryCatchFinally {
	private static final Logger LOG = Logger.getLogger(AbstractTag.class);
	private static final long serialVersionUID = 1L;
	public static final String ATTR_CONTEXT = "duckling.vwbcontext";

	public void setPageContext(PageContext context) {
		super.setPageContext(context);
		initTag();
	}

	public abstract int doVWBStart();

	public void doCatch(Throwable arg0) {

	}

	public void doFinally() {
	}

	public int doEndTag() throws JspException {
		return EVAL_PAGE;
	}

	public int doStartTag() {
		if (pageContext == null) {
			try {
				throw new JspException("Page context may not be NULL - serious internal problem!");
			} catch (JspException e) {
				LOG.error("Tag failed.", e);
			}
		}
		return doVWBStart();
	}

	protected void initTag() {
	}

}
