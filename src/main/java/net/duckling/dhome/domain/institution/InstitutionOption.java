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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 机构选项
 * @author Brett
 *
 */
public class InstitutionOption {
	
	
	private static Map<Integer, List<InstitutionOption>> map = new HashMap<Integer, List<InstitutionOption>>();
	
	public final static int PAPER_DISCIPLINE_ORIENTATION = 11; //论文:学科方向
	public final static int TREATISE_PUBLISHER = 31; //论著：出版社
	public final static int AWARD_NAME = 41; //奖励:名称
	public final static int AWARD_TYPE = 42; //奖励:类别
	public final static int AWARD_GRADE = 43; //奖励:等级
	public final static int COPYRIGHT_TYPE = 51; //软件著作权:类别
	public final static int COPYRIGHT_GRADE = 52; //软件著作权:等级
	public final static int PATENT_TYPE = 61; //专利:类别
	public final static int PATENT_GRADE = 62; //专利:等级
	public final static int PATENT_STATUS = 63; //专利:等级
	public final static int TOPIC_TYPE = 71; //课题:类别
	public final static int TOPIC_FUNDS_FROM = 72; //课题:资金来源
	public final static int ACADEMIC_ASSIGNMENT_ORGANIZATION_NAME = 81;  //学术任职:组织名称
	public final static int ACADEMIC_ASSIGNMENT_POSITION = 82;  //学术任职:职位
	public final static int PERIODICAL_ASSIGNMENT_PERIODICAL_NAME = 91; //期刊任职:期刊名称
	public final static int PERIODICAL_ASSIGNMENT_POSITION = 92; //期刊任职:职位
	public final static int TRAINING_DEGREE = 101; //人才培养:学位
	
	static{
		List<InstitutionOption> paperList = new ArrayList<InstitutionOption>();
		List<InstitutionOption> userList = new ArrayList<InstitutionOption>();
		List<InstitutionOption> treatiseList = new ArrayList<InstitutionOption>();
		List<InstitutionOption> awardList = new ArrayList<InstitutionOption>();
		List<InstitutionOption> copyrightList = new ArrayList<InstitutionOption>();
		List<InstitutionOption> patentList = new ArrayList<InstitutionOption>();
		List<InstitutionOption> topicList = new ArrayList<InstitutionOption>();
		List<InstitutionOption> academicAssignmentList = new ArrayList<InstitutionOption>();
		List<InstitutionOption> periodicalAssignmentList = new ArrayList<InstitutionOption>();
		List<InstitutionOption> traingList = new ArrayList<InstitutionOption>();
		
		paperList.add(new InstitutionOption(PAPER_DISCIPLINE_ORIENTATION,InstitutionSetting.PAPER,"学科方向",1));
		
		treatiseList.add(new InstitutionOption(TREATISE_PUBLISHER,InstitutionSetting.TREATISE,"出版社",1));
		
		awardList.add(new InstitutionOption(AWARD_NAME,InstitutionSetting.AWARD,"获奖名称",1));
		awardList.add(new InstitutionOption(AWARD_TYPE,InstitutionSetting.AWARD,"类别",2));
		awardList.add(new InstitutionOption(AWARD_GRADE,InstitutionSetting.AWARD,"等级",3));
		
		copyrightList.add(new InstitutionOption(COPYRIGHT_TYPE,InstitutionSetting.COPYRIGHT,"类别",1));
		copyrightList.add(new InstitutionOption(COPYRIGHT_GRADE,InstitutionSetting.COPYRIGHT,"等级",2));
		
		patentList.add(new InstitutionOption(PATENT_TYPE,InstitutionSetting.PATENT,"类别",1));
		patentList.add(new InstitutionOption(PATENT_GRADE,InstitutionSetting.PATENT,"等级",2));
		patentList.add(new InstitutionOption(PATENT_STATUS,InstitutionSetting.PATENT,"状态",3));
		
		topicList.add(new InstitutionOption(TOPIC_TYPE,InstitutionSetting.TOPIC,"类别",1));
		topicList.add(new InstitutionOption(TOPIC_FUNDS_FROM,InstitutionSetting.TOPIC,"资金来源",2));
		
		academicAssignmentList.add(new InstitutionOption(ACADEMIC_ASSIGNMENT_ORGANIZATION_NAME,InstitutionSetting.ACADEMIC_ASSIGNMENT,"组织名称",1));
		academicAssignmentList.add(new InstitutionOption(ACADEMIC_ASSIGNMENT_POSITION,InstitutionSetting.ACADEMIC_ASSIGNMENT,"职位",2));
		
		periodicalAssignmentList.add(new InstitutionOption(PERIODICAL_ASSIGNMENT_PERIODICAL_NAME,InstitutionSetting.PERIODICAL_ASSIGNMENT,"期刊名称",1));
		periodicalAssignmentList.add(new InstitutionOption(PERIODICAL_ASSIGNMENT_POSITION,InstitutionSetting.PERIODICAL_ASSIGNMENT,"职位",2));
		
		traingList.add(new InstitutionOption(TRAINING_DEGREE,InstitutionSetting.TRAINING,"学位",1));
		
		map.put(InstitutionSetting.PAPER, paperList);
		map.put(InstitutionSetting.USER, userList);
		map.put(InstitutionSetting.TREATISE, treatiseList);
		map.put(InstitutionSetting.AWARD, awardList);
		map.put(InstitutionSetting.COPYRIGHT, copyrightList);
		map.put(InstitutionSetting.PATENT, patentList);
		map.put(InstitutionSetting.TOPIC, topicList);
		map.put(InstitutionSetting.ACADEMIC_ASSIGNMENT, academicAssignmentList);
		map.put(InstitutionSetting.PERIODICAL_ASSIGNMENT, periodicalAssignmentList);
		map.put(InstitutionSetting.TRAINING, traingList);
	} 

	private int id;
	private int settingId;
	private String title;
	private int rank;
	private List<InstitutionOptionVal> valList = new ArrayList<InstitutionOptionVal>();
	
	public InstitutionOption(){
		
	}
	public InstitutionOption(int id, int settingId, String title, int rank){
		this.id = id;
		this.settingId = settingId;
		this.title = title;
		this.rank = rank;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	public int getSettingId() {
		return settingId;
	}
	public void setSettingId(int settingId) {
		this.settingId = settingId;
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
	public List<InstitutionOptionVal> getValList() {
		return valList;
	}
	public void setValList(List<InstitutionOptionVal> valList) {
		this.valList = valList;
	}
	
	public static List<InstitutionOption> getList(int settingId){
		return map.get(settingId);
	}
	
	
}
