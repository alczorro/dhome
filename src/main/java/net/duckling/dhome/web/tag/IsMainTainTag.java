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

import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.facade.ApplicationFacade;
import net.duckling.dhome.common.util.CommonUtils;

public class IsMainTainTag extends AbstractTag {
	private static final long serialVersionUID = -2734147503842645727L;
	private AppConfig appConfig;
	private String reverse;
	
	public String getReverse() {
		return reverse;
	}

	public void setReverse(String reverse) {
		this.reverse = reverse;
	}

	public void init(){
		if(appConfig==null){
			appConfig=(AppConfig)ApplicationFacade.getAnnotationBean(AppConfig.class);
		}
	}

	@Override
	public int doVWBStart()  {
		init();
		if(CommonUtils.isNull(reverse)){
			return appConfig.isMaintain()?EVAL_BODY_INCLUDE:SKIP_BODY;
		}else{
			return appConfig.isMaintain()?SKIP_BODY:EVAL_BODY_INCLUDE;
		}
		
	}
}