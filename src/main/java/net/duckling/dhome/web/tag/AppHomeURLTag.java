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

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.util.UrlUtil;

import org.apache.log4j.Logger;

public class AppHomeURLTag extends AbstractTag {
	private static final long serialVersionUID = 1L;
	private static final Logger LOG = Logger.getLogger(AppHomeURLTag.class);

	@Override
	public int doVWBStart()  {
		try {
			pageContext.getOut().print(UrlUtil.getRootURL((HttpServletRequest) pageContext.getRequest()));
		} catch (IOException e) {
			LOG.error(e.getMessage(),e);
		}
		return SKIP_BODY;
	}
}