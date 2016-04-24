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
package net.duckling.dhome.domain.institution;

import java.util.List;

import net.duckling.dhome.domain.people.Interest;
import net.duckling.dhome.domain.people.SimpleUser;

/**
 * 
 * 机构主页的研究队伍实体类
 * @author lvly
 * @since 2012-9-19
 */
public class Member extends SimpleUser {
	
	private static final long serialVersionUID = 7544291611957301287L;
	private String domain;
	private List<Interest> interest;
	
	public List<Interest> getInterest() {
		return interest;
	}

	public void setInterest(List<Interest> interest) {
		this.interest=interest;
	}

	/**
	 * @param 用户实体
	 */
	public Member(SimpleUser u) {
		this.setAuditPropose(u.getAuditPropose());
		this.setEmail(u.getEmail());
		this.setEnName(u.getEnName());
		this.setId(u.getId());
		this.setImage(u.getImage());
		this.setIsAdmin(u.getIsAdmin());
		this.setPinyin(u.getPinyin());
		this.setSalutation(u.getSalutation());
		this.setStatus(u.getStatus());
		this.setStep(u.getStep());
		this.setZhName(u.getZhName());
		
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}
	
}
