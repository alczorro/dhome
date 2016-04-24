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
package net.duckling.dhome.domain.people;

import java.util.List;

/**
 * 相关人员
 * @author lvly
 * @since 2012-10-11
 */
public class Friend {
	private SimpleUser simpleUser;
	private DetailedUser detailedUser;
	private List<Interest> interests;
	private String domain;

	public SimpleUser getSimpleUser() {
		return simpleUser;
	}

	public void setSimpleUser(SimpleUser simpleUser) {
		this.simpleUser = simpleUser;
	}

	public DetailedUser getDetailedUser() {
		return detailedUser;
	}

	public void setDetailedUser(DetailedUser detailedUser) {
		this.detailedUser = detailedUser;
	}

	public List<Interest> getInterests() {
		return interests;
	}

	public void setInterests(List<Interest> interests) {
		this.interests = interests;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

}
