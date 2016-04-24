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

import java.util.Iterator;
import java.util.List;

import net.duckling.dhome.domain.object.CommentView;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.UserSetting;
import net.duckling.dhome.service.IMessageBoardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-9-10
 */
@Service
public class MessageBoardHelper {
	@Autowired
	private IMessageBoardService msgBoardService;
	public List<CommentView> removeOthers(SimpleUser targetUser, SimpleUser currUser, UserSetting userSetting){
		if(targetUser==null||currUser==null){
			return null;
		}
		List<CommentView> views = msgBoardService.selectByHostUid(targetUser.getId());
		if(targetUser.getId()==currUser.getId()){
			return views;
		}
		if (views == null) {
			return null;
		}
		for (Iterator<CommentView> it = views.iterator(); it.hasNext();) {
			CommentView view = it.next();
			if (!userSetting.isMsgBoardOpen() && view.getComment().getUid() != currUser.getId()) {
				it.remove();
			}
		}
		return views;
	}
}
