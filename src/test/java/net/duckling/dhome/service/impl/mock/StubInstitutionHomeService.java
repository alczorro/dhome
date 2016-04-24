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
package net.duckling.dhome.service.impl.mock;

import java.util.List;

import net.duckling.dhome.domain.institution.Institution;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.InstitutionHomeDiscover;
import net.duckling.dhome.service.IInstitutionHomeService;

public class StubInstitutionHomeService implements IInstitutionHomeService{
	@Override
	public boolean isValidHome(String domain) {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public int getInstitutionsByKeywordCount(String keyword) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<InstitutionHomeDiscover> getInstitutionsByLastest(int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public InstitutionHome getInstitutionByInstitutionId(int institutionId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void updateInstitutionHome(InstitutionHome home) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int getInstitutionIdByDomain(String domain) {
		return 12;
	}

	@Override
	public int createInstitutionHome(int userId, Institution institution) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public List<InstitutionHomeDiscover> getInstitutionsByKeyword(String keyword, int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public InstitutionHome getInstitutionByDomain(String domain) {
		InstitutionHome home=new InstitutionHome();
		if("testJSON".equals(domain)){
			home.setId(11);
			home.setDomain("testJSON");
			home.setName("js");
			home.setInstitutionId(11);
		}else{
			home.setId(12);
			home.setDomain("test");
			home.setName("测试用机构");
		}
		return home;
	}
	@Override
	public List<InstitutionHomeDiscover> getInstitutionsByMemberCount(int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<InstitutionHomeDiscover> getInstitutionsByPaperCount(int offset, int size) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public List<Institution> searchForInstitutionBySimilarName(String name) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Institution getInstitutionById(int insId) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int createAliasInstitutionName(String name, int insId, boolean isFull) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void deleteAliasInstitutionName(String name, int insId) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public int createInstitutionHomeForAdmin(int adminUserId,
			Institution institution) {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void updateInstitutionHomeForZeroFieldById(InstitutionHome home) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public boolean checkTypeOfInstitutionName(String name, int insId,
			boolean officalOrCustom) {
		// TODO Auto-generated method stub
		return false;
	}
	

}