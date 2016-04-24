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
package net.duckling.dhome.dao.impl;

import java.util.List;

import net.duckling.dhome.dao.IInstitutionDAO;
import net.duckling.dhome.domain.institution.Institution;

/**
 * @author lvly
 * @since 2012-9-21
 */
public class StubInstitutionDAO implements IInstitutionDAO{
	@Override
	public int createInstitution(String name) {
		return 12;
	}
	@Override
	public Institution getInstitutionByName(String name) {
		return null;
	}
	@Override
	public Institution getInstitution(int institutionId) {
		return null;
	}
	@Override
	public List<Institution> getDsnInstitutionsByPrefixName(String prefix) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int searchOfficalInstitution(String insName) {
		// TODO Auto-generated method stub
		return 0;
	}
}