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
package net.duckling.dhome.common.log;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.util.SessionUtils;

import cn.cnic.cerc.dlog.client.WebLogResolver;

/**
 * 记日志，具体以来Dlog
 * @author lvly
 * @since 2012-10-31
 */
public class DLogResolverImpl extends WebLogResolver{
	@Override
	public Map<String,String> buildFixedParameters(HttpServletRequest request){
		Map<String,String> map = new HashMap<String,String>();
		map.put("client", getIpAddress(request));
		map.put("hosts",request.getRemoteAddr());
		map.put("referer", getReferer(request));
		//如果未登录，就会是-1
		map.put("currentUser", SessionUtils.getUserId(request)+"");
		//目标用户暂时记的是domain，
		return map;
	}
}
