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
package net.duckling.dhome.domain.institution;

import java.util.ArrayList;
import java.util.List;

/**
 * 机构设置
 * @author Brett
 *
 */
public class InstitutionSetting {
	
	public final static int PAPER = 1;
	public final static int USER = 2; //员工
	public final static int TREATISE = 3;
	public final static int AWARD = 4;
	public final static int COPYRIGHT = 5;
	public final static int PATENT = 6;
	public final static int TOPIC = 7;
	public final static int ACADEMIC_ASSIGNMENT = 8;
	public final static int PERIODICAL_ASSIGNMENT = 9;
	public final static int TRAINING = 10;
	

	private int id;
	private String title;
	private int rank;
	public InstitutionSetting(){
		
	}
	public InstitutionSetting(int id, String title, int rank){
		this.id = id;
		this.title = title;
		this.rank = rank;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public int getRank() {
		return rank;
	}
	public void setRank(int rank) {
		this.rank = rank;
	}
	
	static List<InstitutionSetting> optionList = new ArrayList<InstitutionSetting>();
	public static List<InstitutionSetting> getList(){
		return optionList;
	}
	
	static{
		optionList.add(new InstitutionSetting(PAPER, "论文", 1));
		//optionList.add(new InstitutionSetting(USER, "员工", 2));
		optionList.add(new InstitutionSetting(TREATISE, "论著", 3));
		optionList.add(new InstitutionSetting(AWARD, "奖励", 4));
		optionList.add(new InstitutionSetting(COPYRIGHT, "软件著作权", 5));
		optionList.add(new InstitutionSetting(PATENT, "专利", 6));
		optionList.add(new InstitutionSetting(TOPIC, "课题", 7));
		optionList.add(new InstitutionSetting(ACADEMIC_ASSIGNMENT, "学术任职", 8));
		optionList.add(new InstitutionSetting(PERIODICAL_ASSIGNMENT, "期刊任职", 9));
		optionList.add(new InstitutionSetting(TRAINING, "人才培养", 10));
	}
}
