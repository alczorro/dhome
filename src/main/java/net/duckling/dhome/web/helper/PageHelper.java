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
package net.duckling.dhome.web.helper;

import net.duckling.dhome.domain.people.SimpleUser;

/**
 * controller太臃肿，一些逻辑挪到help来执行
 * 
 * @author lvly
 * @since 2012-9-11
 */
public final class PageHelper {
	private PageHelper() {

	}

	/**
	 * 自己可阅读
	 * @param user
	 * @return
	 */
	public static boolean adminCanRead(SimpleUser user) {
		if (SimpleUser.STATUS_AUDIT_DELETE.equals(user.getStatus())) {
			return false;
		}
		return true;
	}

	/**
	 * 游客可阅读
	 * @param user 游客实体
	 * @return
	 */
	public static boolean guestCanRead(SimpleUser user) {
		if(user==null){
			return false;
		}
		if (SimpleUser.STATUS_AUDIT_DELETE.equals(user.getStatus())
				|| SimpleUser.STATUS_AUDIT_NOT.equals(user.getStatus())
				|| SimpleUser.STATUS_AUDIT_ING.equals(user.getStatus())) {
			return false;
		}
		return true;
	}

}
