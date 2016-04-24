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
package net.duckling.dhome.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.duckling.common.util.CommonUtils;
import net.duckling.dhome.dao.IDatabasePaper;
import net.duckling.dhome.dao.IInstitutionBackendKeywordDAO;
import net.duckling.dhome.dao.IInstitutionBackendPaperAuthorDAO;
import net.duckling.dhome.dao.IInstitutionBackendPaperDAO;
import net.duckling.dhome.dao.IInstitutionBackendSponsorDAO;
import net.duckling.dhome.dao.IInstitutionDepartDAO;
import net.duckling.dhome.dao.IInstitutionDisciplineOrientationDAO;
import net.duckling.dhome.dao.IInstitutionPaperSearchDAO;
import net.duckling.dhome.dao.IInstitutionPublicationDAO;
import net.duckling.dhome.domain.institution.DatabasePaper;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionDisciplineOrientation;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPublication;
import net.duckling.dhome.domain.institution.SearchInstitutionPaperCondition;
import net.duckling.dhome.domain.object.PageResult;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.service.IInstitutionBackendPaperService;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InstitutionBackendPaperServiceImpl implements IInstitutionBackendPaperService{
	
	private static final Logger LOG=Logger.getLogger(InstitutionBackendPaperServiceImpl.class);
	@Autowired
	private IInstitutionDisciplineOrientationDAO disDAO;
	
	@Autowired
	private IInstitutionPublicationDAO pubDAO;
	
	@Autowired
	private IInstitutionBackendPaperDAO paperDAO;
	
	@Autowired
	private IInstitutionBackendKeywordDAO keywordDAO;
	
	@Autowired
	private IInstitutionBackendSponsorDAO sponsorDAO;
	
	@Autowired
	private IInstitutionPublicationDAO publicationDAO;
	
	@Autowired
	private IInstitutionBackendPaperAuthorDAO authorDAO;
	
	@Autowired
	private IInstitutionPaperSearchDAO pSearchDAO;
	@Autowired
	private IDatabasePaper dPaper;
	@Autowired
	private IInstitutionDepartDAO deptDAO;
	
	
	@Override
	public List<InstitutionPublication> getAllPubs() {
		return pubDAO.all();
	}
	@Override
	public List<InstitutionPaper> getAllPapers() {
		return paperDAO.getAllPapers();
	}
	@Override
	public List<InstitutionDisciplineOrientation> getAllDiscipline() {
		return disDAO.all();
	}
	private String hash(InstitutionPaper paper,int[] uids,int[] order,
			boolean[] communicationAuthors, boolean[] authorStudents,
			boolean[] authorTeacher){
		StringBuffer sb=new StringBuffer();
		//论文标题
		sb.append(paper.getTitle()).append(",");
		sb.append(paper.getDoi()).append(",");
		sb.append(paper.getDepartId()).append(",");
		//发表年份
		sb.append(paper.getPublicationYear()).append(",");
		//卷号
		sb.append(paper.getVolumeNumber()).append(",");
		//起始页
		sb.append(paper.getStartPage()).append(",");
		//终止页
		sb.append(paper.getEndPage()).append(",");
		//作者信息
		sb.append(Arrays.toString(uids)).append(",");
		sb.append(Arrays.toString(order)).append(",");
		sb.append(Arrays.toString(communicationAuthors)).append(",");
		sb.append(Arrays.toString(authorStudents)).append(",");
		sb.append(Arrays.toString(authorTeacher)).append(",");
		return DigestUtils.md5Hex(sb.toString());
	}
	private String getFirstAuthorName(int[] uids){
		if(uids.length==0){
			return "";
		}
		InstitutionAuthor auth=authorDAO.getById(uids[0]);
		if(auth!=null){
			return auth.getAuthorName();
		}
		return "";
	}
	@Override
	public int create(InstitutionPaper paper, int institutionId, int[] uids,int[] order,
			boolean[] communicationAuthors, boolean[] authorStudents,
			boolean[] authorTeacher) {
		paper.setInstitutionId(institutionId);
		String hash=hash(paper,uids,order,communicationAuthors,authorStudents,authorTeacher);
		paper.setHash(hash);
		int paperId=paperDAO.isHashExitsReturnId(hash);
		
		if(paperId>0){
			paper.setId(paperId);
			LOG.error("find hash:["+hash+"],paper["+paperId+"],create skiped,to update!");
			update(paper, institutionId, uids, order, communicationAuthors, authorStudents, authorTeacher);
			return paper.getId();
		}

		if(paper.getDoi()!=null&&paper.getDoi().trim()!=""){
			int id=paperDAO.getPaperByDoi(paper.getDoi().trim());
			if(id>0){
				paper.setId(id);
				LOG.error("find hash:["+hash+"],paper["+paperId+"],create skiped,to update!");
				update(paper, institutionId, uids, order, communicationAuthors, authorStudents, authorTeacher);
				return paper.getId();
			}
			
		}
		InstitutionPaper p=paperDAO.getPaperByTitle(paper.getTitle().trim());
		if(p!=null&&(paper.getDoi()==null||paper.getDoi()=="")){
			paper.setId(p.getId());
			LOG.error("find hash:["+hash+"],paper["+paperId+"],create skiped,to update!");
			update(paper, institutionId, uids, order, communicationAuthors, authorStudents, authorTeacher);
			return paper.getId();
		}
		
		
		paper.setId(paperDAO.create(paper));
		if(!paperDAO.isExist(institutionId, paper.getId())){
			paperDAO.addRef(institutionId,paper.getId(),getFirstAuthorName(uids));
		}
		keywordDAO.delete(paper.getId());
		sponsorDAO.delete(paper.getId());
		if(!CommonUtils.isNull(paper.getKeywordDisplay())){
			String[] keyword=paper.getKeywordDisplay().split(",");
			keywordDAO.insert(keyword, paper.getId());
		}
		authorDAO.createRef(paper.getId(),uids,order,communicationAuthors,authorStudents,authorTeacher);
		return paper.getId();
	}
	@Override
	public void update(InstitutionPaper paper, int institutionId, int[] uid,
			int[] order, boolean[] communicationAuthors,
			boolean[] authorStudents, boolean[] authorTeacher) {
		paper.setInstitutionId(institutionId);
		if(paper.getId()==0){
			LOG.error("update:paper.id is null");
			return;
		}
//		if(!paperDAO.isExist(institutionId, paper.getId())){
//			LOG.error("update:paper.ref institution["+institutionId+"] is no founded!");
//			return;
//		}
		String hash=hash(paper,uid,order,communicationAuthors,authorStudents,authorTeacher);
		paper.setHash(hash);
		keywordDAO.delete(paper.getId());
		sponsorDAO.delete(paper.getId());
		if(!CommonUtils.isNull(paper.getKeywordDisplay())){
			String[] keyword=paper.getKeywordDisplay().split(",");
			keywordDAO.insert(keyword, paper.getId());
		}
		
		if(!CommonUtils.isNull(paper.getSponsor())){
			String[] sponsor=paper.getSponsor().split(",");
			sponsorDAO.insert(sponsor, paper.getId(),institutionId);
		}
		paperDAO.update(paper);
		paperDAO.updateRef(institutionId, paper.getId(), getFirstAuthorName(uid));
		authorDAO.deleteRef(paper.getId());
		authorDAO.createRef(paper.getId(),uid,order,communicationAuthors,authorStudents,authorTeacher);
		
	}
	
	@Override
	public PageResult<InstitutionPaper> getPapers(int institutionId, int page,SearchInstitutionPaperCondition condition) {
		PageResult<InstitutionPaper> result=new PageResult<InstitutionPaper>();
		result.setAllSize(paperDAO.getPaperCount(institutionId,condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(paperDAO.getPapers(institutionId,condition,(page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public int getPapersCount(int institutionId) {
		return paperDAO.getPaperCount(institutionId,new SearchInstitutionPaperCondition());
	}
	
	@Override
	public PageResult<InstitutionPaper> getPapersByUser(int userId, int page,SearchInstitutionPaperCondition condition) {
		PageResult<InstitutionPaper> result=new PageResult<InstitutionPaper>();
		result.setAllSize(paperDAO.getPaperCountByUser(userId,condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(paperDAO.getPapersByUser(userId,condition,(page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public List<InstitutionPaper> getAllPapersByUser(int userId,SearchInstitutionPaperCondition condition) {
		return paperDAO.getPapersByUser(userId, condition, 0, 9999);
	}
	
	@Override
	public int getPapersCountByUser(int userId) {
		return paperDAO.getPaperCountByUser(userId,new SearchInstitutionPaperCondition());
	}
	
	@Override
	public void delete(int institutionId, int[] paperId) {
		paperDAO.delete(institutionId,paperId);
	}
	
	@Override
	public List<InstitutionPublication> getPubsByIds(List<Integer> pubId) {
		return pubDAO.getPubsByIds(pubId);
	}

	@Override
	public List<String> getAllKeyword(String queryStr) {
		return keywordDAO.search(queryStr);
	}
	
	@Override
	public List<String> getAllSponsor(String queryStr) {
		return sponsorDAO.search(queryStr);
	}
	@Override
	public List<InstitutionPublication> getPubsByKey(String keyword) {
		return publicationDAO.getPubsByKey(keyword);
	}
	@Override
	public List<InstitutionAuthor> searchAuthor(String key) {
		if(CommonUtils.isNull(key)){
			return null;
		}
		return authorDAO.search(key);
	}
	
	@Override
	public void createAuthor(InstitutionAuthor author) {
		if(!authorDAO.isExist(author)){
			authorDAO.createAuthor(author);
		}
	}
	@Override
	public void insertAuthorUser(String cstnetId, int institutionId, String[] authorId,
			int status) {
		authorDAO.insertAuthorUser(cstnetId, institutionId, authorId, status);
	}
	@Override
	public void cancelAuthorUser(int institutionId, String[] authorId) {
		authorDAO.cancelAuthorUser(institutionId, authorId);
	}
	@Override
	public boolean isMyPaper(int institutionId, int paperId) {
		return paperDAO.isExist(institutionId, paperId);
	}
	@Override
	public InstitutionPaper getPaperById(int paperId) {
		return paperDAO.getPaperById(paperId);
	}
	@Override
	public List<InstitutionAuthor> getAuthorsByPaperId(int paperId) {
		return authorDAO.getByPaperId(paperId);
	}
	@Override
	public List<InstitutionAuthor> getAuthorsByCstnetId(String cstnetId) {
		return authorDAO.getByCstnetId(cstnetId);
	}
	@Override
	public Map<Integer,List<InstitutionAuthor>> getListAuthorsMap(List<Integer> paperIds){
		List<InstitutionAuthor> authors=authorDAO.getByPaperIds(paperIds);
		Map<Integer,List<InstitutionAuthor>> map=new HashMap<Integer,List<InstitutionAuthor>>();
		if(CommonUtils.isNull(authors)){
			return map;
		}
		for(InstitutionAuthor author:authors){
			addToMap(map,author.getPaperId(),author);
		}
		caculateSubscript(map);
		return map;
	}
	
	//计算下标
	private void caculateSubscript(Map<Integer,List<InstitutionAuthor>> map){
		Set<Entry<Integer,List<InstitutionAuthor>>> entrys=map.entrySet();
		for(Entry<Integer,List<InstitutionAuthor>> entry:entrys){
			Map<String,Integer> subscriptMap=new HashMap<String,Integer>();
			for(InstitutionAuthor author:entry.getValue()){
				if(subscriptMap.containsKey(author.getAuthorCompany())){
					author.setSubscriptIndex(subscriptMap.get(author.getAuthorCompany()));
				}else{
					int index=subscriptMap.size()+1;
					author.setSubscriptIndex(index);
					subscriptMap.put(author.getAuthorCompany(),index);
				}
			}
		}
	}
	
	//有则追加，无则新建List
	private void addToMap(Map<Integer,List<InstitutionAuthor>> map,int paperId,InstitutionAuthor author){
		if(map.containsKey(paperId)){
			map.get(paperId).add(author);
		}else{
			List<InstitutionAuthor> list=new ArrayList<InstitutionAuthor>();
			list.add(author);
			map.put(paperId, list);
		}
	}
	@Override
	public InstitutionAuthor getAuthorsById(int paperId,int authorId) {
		return authorDAO.getById(paperId,authorId);
	}
	@Override
	public InstitutionAuthor getAuthorById(int authorId) {
		return authorDAO.getById(authorId);
	}
	@Override
	public Map<Integer, Integer> getPublicationYearsMap(int institutionId) {
		return paperDAO.getPublicationYearsMap(institutionId);
	}
	@Override
	public List<InstitutionPaper> getPapersByUID(int uid) {
		return paperDAO.getPapersByUID(uid);
	}
	@Override
	public List<InstitutionPaper> getEnPapersByUID(int uid) {
		return paperDAO.getEnPapersByUID(uid);
	}
	@Override
	public Map<Integer, Integer> getPublicationYearsMapByUser(int userId) {
		return paperDAO.getPublicationYearsMapByUser(userId);
	}
	@Override
	public void deletePaperUser(int[] id) {
		paperDAO.deletePaperUser(id);
	}
	@Override
	public void insertPaperUser(int paperId, int userId) {
		paperDAO.insertPaperUser(paperId, userId);
	}
	@Override
	public List<InstitutionPaper> getPaperByKeyword(int offset, int size,
			String keyword) {
		return pSearchDAO.getPaperByKeyword(offset, size, keyword);
	}
	@Override
	public List<InstitutionPaper> getPaperByInit(int offset, int size,
			String userName) {
		return pSearchDAO.getPaperByInit(offset, size, userName);
	}
	@Override
	public List<InstitutionPaper> getPaperByKeywordInit(int offset, int size,
			List<InstitutionAuthor> authors) {
		return pSearchDAO.getPaperByKeywordInit(offset, size, authors);
	}
	@Override
	public PageResult<InstitutionPaper> getPapersByKey(int institutionId, int page,String userName) {
		PageResult<InstitutionPaper> result=new PageResult<InstitutionPaper>();
		result.setAllSize(pSearchDAO.getPaperCountByInit(institutionId, userName));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(pSearchDAO.getPapersByInit(institutionId,(page-1)*result.getPageSize(),result.getPageSize(),userName));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public List<Integer> getExistPaperIds(int uid) {
		return pSearchDAO.getExistPaperIds(uid);
	}
	@Override
	public void insertPaper(String[] paperId, int userId) {
		pSearchDAO.insertPaper(paperId, userId);
	}
	@Override
	public void deletePaperUser(String[] paperId, int userId) {
		pSearchDAO.deletePaperUser(paperId, userId);
	}
	@Override
	public void deleteUserRef(int uid,int paperId) {
		paperDAO.deleteUserRef(uid, paperId);
	}

	@Override
	public void createUserRef(int uid, int paperId) {
		paperDAO.createUserRef(uid, paperId);
	}
	@Override
	public InstitutionPublication getPubsById(int pubId) {
		return pubDAO.getPubsById(pubId);
	}
	@Override
	public Map<Integer, DatabasePaper> insertPaper(List<DatabasePaper> dbaPaper) {
		return dPaper.insertPaper(dbaPaper);
	}
	@Override
	public List<DatabasePaper> getPaperTemp() {
		return dPaper.getPaperTemp();
	}
	@Override
	public int getPubId(String pubName) {
		return dPaper.getPubId(pubName);
	}
	@Override
	public void insert1(List<DatabasePaper> list) {
		dPaper.insert1(list);
	}
	@Override
	public void insert2(List<DatabasePaper> list) {
		dPaper.insert2(list);
		
	}
	@Override
	public void insert3(List<DatabasePaper> list) {
		dPaper.insert3(list);
		
	}
	@Override
	public void insert4(List<DatabasePaper> list) {
		dPaper.insert4(list);
		
	}
	@Override
	public void insertPaperRef(List<DatabasePaper> papers, int insId) {
		dPaper.insertPaperRef(papers, insId);
		
	}
	@Override
	public void insertAuthor(List<DatabasePaper> dbaPaper) {
		for (DatabasePaper paper : dbaPaper) {
			if(paper.getAuthor1()!=null&&!"".equals(paper.getAuthor1())){
				dPaper.insertAuthor(paper.getAuthor1(), paper.getAuthor1_company()==null?"":paper.getAuthor1_company());
			}
			if(paper.getAuthor2()!=null&&!"".equals(paper.getAuthor2())){
				dPaper.insertAuthor(paper.getAuthor2(), paper.getAuthor2_company()==null?"":paper.getAuthor2_company());
			}
			if(paper.getAuthor3()!=null&&!"".equals(paper.getAuthor3())){
				dPaper.insertAuthor(paper.getAuthor3(), paper.getAuthor3_company()==null?"":paper.getAuthor3_company());
			}
			if(paper.getAuthor4()!=null&&!"".equals(paper.getAuthor4())){
				dPaper.insertAuthor(paper.getAuthor4(), paper.getAuthor4_company()==null?"":paper.getAuthor4_company());
			}
			
		}
		
	}
	

	@Override
	public Map<String, Map<String, Integer>> getPaperStatisticsForDept() {
		return paperDAO.getPaperStatisticsForDept();
	}
	@Override
	public List<InstitutionPaper> getPapers(int insId,SearchInstitutionPaperCondition condition) {
		int paperCount = paperDAO.getPaperCount(insId,condition);
		return paperDAO.getPapers(insId,condition,0,paperCount);
	}
	
	@Override
	public void updatePaper(InstitutionPaper paper) {
		paperDAO.update(paper);
	}
	@Override
	public Map<String, Map<String, Integer>> getPaperStatisticsForYear() {
		return paperDAO.getPaperStatisticsForYear();
	}

	@Override
	public PageResult<InstitutionAuthor> getAuthors(int institutionId, int page,
			SearchInstitutionPaperCondition condition) {
		PageResult<InstitutionAuthor> result=new PageResult<InstitutionAuthor>();
		result.setAllSize(authorDAO.getAuthorsCount(institutionId,condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(authorDAO.getAuthors(institutionId,(page-1)*result.getPageSize(),result.getPageSize(),condition));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public int getAuthorsCount(int institutionId, SearchInstitutionPaperCondition condition) {
		return authorDAO.getAuthorsCount(institutionId, new SearchInstitutionPaperCondition());
	}
	@Override
	public void updateStatus(int paperId,int status) {
		paperDAO.updateStatus(paperId,status);
	}
	@Override
	public int getPapersCite(int insId) {
		return paperDAO.getPapersCite(insId);
	}
	@Override
	public int getPapersCount(int institutionId,
			SearchInstitutionPaperCondition condition) {
		return paperDAO.getPaperCount(institutionId, condition);
	}
	@Override
	public void updateAuthorPropelling(int authorId) {
		authorDAO.updateAuthorPropelling(authorId);
	}
	@Override
	public PageResult<InstitutionPaper> getPapersByPub(int institutionId,
			int page, SearchInstitutionPaperCondition condition) {
		PageResult<InstitutionPaper> result=new PageResult<InstitutionPaper>();
		result.setAllSize(paperDAO.getPaperCountByPub(institutionId, condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(paperDAO.getPapersByPub(institutionId,condition,(page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public PageResult<InstitutionPaper> getPapersByAuthor(int institutionId,
			int page, SearchInstitutionPaperCondition condition) {
		PageResult<InstitutionPaper> result=new PageResult<InstitutionPaper>();
		result.setAllSize(paperDAO.getPaperCountByAuthor(institutionId, condition));
		if(result.getMaxPage()<page){
			page=result.getMaxPage();
		}
		if(page==0){
			return result;
		}
		result.setDatas(paperDAO.getPapersByAuthor(institutionId,condition,(page-1)*result.getPageSize(),result.getPageSize()));
		result.setCurrentPage(page);
		return result;
	}
	@Override
	public List<InstitutionDepartment> getDepartments(int insId) {
		return deptDAO.getDepartsByInsId(insId);
	}
	@Override
	public void movePaper(List<Paper> papers, int uid) {
		InstitutionPaper paper =  new InstitutionPaper();
		
		for (Paper p : papers) {
			paper.setTitle(p.getTitle().trim());
			paper.setVolumeNumber(p.getVolumeIssue());
			paper.setPublicationYear(p.getYear());
			paper.setSummary(p.getSummary());
			paper.setKeywordDisplay(p.getKeywords());
			paper.setPublicationPage(p.getPages());
			//刊物
			if(p.getSource()!=null&&!"".equals(p.getSource())){
				int pubId=publicationDAO.insert(p.getSource().trim());
				if(pubId==0){
					pubId=publicationDAO.getIdByName(p.getSource().trim()).getId();
				}
				paper.setPublicationId(pubId);
			}
			
			//插入到paper表
			int paperId = paperDAO.move(paper);
			
			//作者
			String authors = p.getAuthors();
			if(null != authors && !"".equals(authors)){
				String[] temp = authors.split(",");
				
				if(paperId==0){
					paperId=paperDAO.getPaperByTitle(p.getTitle().trim()).getId();
				}else{
					paperDAO.addRef(1799, paperId, temp[0].trim());
					for (int i=1;i<=temp.length;i++) {
						int authorId = authorDAO.insertAuthor(temp[i-1].trim());
						if(authorId==0){
							authorId=authorDAO.getAuthor(temp[i-1].trim()).getId();
						}
						authorDAO.insertRef(paperId, authorId, i);
					}
				}
				
			}
			
			paperDAO.createUserRef(uid, paperId);
		}
		
	}
	@Override
	public List<InstitutionPaper> getPaperByPub(int institutionId,
			SearchInstitutionPaperCondition condition) {
		int paperCount = paperDAO.getPaperCountByPub(institutionId, condition);
		return paperDAO.getPapersByPub(institutionId,condition,0,paperCount);
	}
	@Override
	public List<InstitutionPaper> getPaperByAuthor(int institutionId,
			SearchInstitutionPaperCondition condition) {
		int paperCount = paperDAO.getPaperCountByAuthor(institutionId,condition);
		return paperDAO.getPapersByAuthor(institutionId,condition,0,paperCount);
	}
	@Override
	public List<InstitutionPaper> getPaperByTitle(String title) {
		return paperDAO.getPapersByTitle(title);
	}
	@Override
	public int getPaperByDoi(String doi) {
		return paperDAO.getPaperByDoi(doi);
	}
	
}
