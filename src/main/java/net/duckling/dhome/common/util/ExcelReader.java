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
package net.duckling.dhome.common.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import jxl.*;
import jxl.read.biff.BiffException;

import net.duckling.dhome.common.exception.BibResolveFailedException;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPublication;
import net.duckling.dhome.domain.institution.InstitutionTreatise;
import net.duckling.dhome.domain.people.Paper;

import org.apache.log4j.Logger;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrError;
import org.jbibtex.Value;
/**
 * 解析Bib文件的类，并将解析后的论文以集合形式返回
 * @author Yangxp
 * @date 2012-08-15
 */
public class ExcelReader {

	public static final String BIB_ENCODE = "GBK";

	private static final Logger LOG = Logger.getLogger(ExcelReader.class);

//	private InputStream in;
//	/**
//	 * 构造函数
//	 * @param in bib文件的输入流
//	 * @param uid 用户ID，主要用于生成Paper对象
//	 */
//	public ExcelReader(InputStream in) {
//		this.in = new BufferedInputStream(in);
////		this.uid = uid;

//	public List<InstitutionTreatise> analyzeCopyright(InputStream is) {
//		try{
//			Workbook book = Workbook.getWorkbook(is);
//		
//			Sheet sheet = book.getSheet(0);
//			int rowNum = sheet.getRows();
//			List<InstitutionCopyright> result = new ArrayList<InstitutionCopyright>();
//			for (int row = 1; row < rowNum; row++) {
//				InstitutionCopyright cr = new InstitutionCopyright();
//				String c="";
//				c = getContent(sheet, 0, row);
//				t.setDepartShortName(c.trim());
//				c = getContent(sheet, 1, row);
//				t.setName(c.trim());
//				c = getContent(sheet, 2, row);
//				t.setPublisherName(c.trim());
//				c = getContent(sheet, 3, row);
//				if(!"".equals(c.trim())){
//					t.setYear(Integer.parseInt(c));
//				}
//				//作者
//				c = getContent(sheet, 4, row);
//				t.setAuthor(c.trim());
//				t.setAuthorOrder(1);
//				c = getContent(sheet, 5, row);
//				t.setAuthorCompany(c.trim());
//				c = getContent(sheet, 6, row);
//				t.setAuthor2(c.trim());
//				c = getContent(sheet, 7, row);
//				t.setAuthor2Company(c.trim());
//				c = getContent(sheet, 8, row);
//				t.setAuthor3(c.trim());
//				c = getContent(sheet, 9, row);
//				t.setAuthor3Company(c.trim());
//				c = getContent(sheet, 10, row);
//				t.setLanguage(c.trim());
//				c = getContent(sheet, 11, row);
//				if(!"".equals(c.trim())){
//					t.setCompanyOrder(Integer.parseInt(c));
//				}
//				
//				result.add(t);
//			}
//			return result;
//			
//		}catch (IndexOutOfBoundsException e) {
//			LOG.error(e);
//			} catch (IOException e) {
//			LOG.error(e);
//			} catch (BiffException e) {
//				e.printStackTrace();
//			}
//			return null;
//
//	}
	public List<InstitutionTreatise> analyzeTreatise(InputStream is) {
		try{
			Workbook book = Workbook.getWorkbook(is);
		
			Sheet sheet = book.getSheet(0);
			int rowNum = sheet.getRows();
			List<InstitutionTreatise> result = new ArrayList<InstitutionTreatise>();
			for (int row = 1; row < rowNum; row++) {
				InstitutionTreatise t = new InstitutionTreatise();
				String c="";
				c = getContent(sheet, 0, row);
				t.setDepartShortName(c.trim());
				c = getContent(sheet, 1, row);
				t.setName(c.trim());
				c = getContent(sheet, 2, row);
				t.setPublisherName(c.trim());
				c = getContent(sheet, 3, row);
				if(!"".equals(c.trim())){
					t.setYear(Integer.parseInt(c));
				}
				//作者
				c = getContent(sheet, 4, row);
				t.setAuthor(c.trim());
				t.setAuthorOrder(1);
				c = getContent(sheet, 5, row);
				t.setAuthorCompany(c.trim());
				c = getContent(sheet, 6, row);
				t.setAuthor2(c.trim());
				c = getContent(sheet, 7, row);
				t.setAuthor2Company(c.trim());
				c = getContent(sheet, 8, row);
				t.setAuthor3(c.trim());
				c = getContent(sheet, 9, row);
				t.setAuthor3Company(c.trim());
				c = getContent(sheet, 10, row);
				t.setLanguage(c.trim());
				c = getContent(sheet, 11, row);
				if(!"".equals(c.trim())){
					t.setCompanyOrder(Integer.parseInt(c));
				}
				
				result.add(t);
			}
			return result;
			
		}catch (IndexOutOfBoundsException e) {
			LOG.error(e);
			} catch (IOException e) {
			LOG.error(e);
			} catch (BiffException e) {
				e.printStackTrace();
			}
			return null;

	}
	
	public List<InstitutionMemberFromVmt> analyzeMember(InputStream is) {
		try{
			Workbook book = Workbook.getWorkbook(is);
		
			Sheet sheet = book.getSheet(0);
			int rowNum = sheet.getRows();
			
			List<InstitutionMemberFromVmt> result = new ArrayList<InstitutionMemberFromVmt>();
			for (int row = 1; row < rowNum; row++) {
				InstitutionMemberFromVmt m = new InstitutionMemberFromVmt();
//				InstitutionPublication publication = new InstitutionPublication();
//				InstitutionAuthor author = new InstitutionAuthor();
//				List<InstitutionAuthor> authors = new ArrayList<InstitutionAuthor>();
				String c = "";
				c = getContent(sheet, 0, row);   //编号
				m.setSn(c.trim());
				c = getContent(sheet, 1, row);
				m.setTrueName(c.trim());
				c = getContent(sheet, 3, row);
				if(!"".equals(c.trim())){
					m.setBirth(getDate(c,"mm/dd/yy"));
				}
				c = getContent(sheet, 4, row);
				m.setBirthPlace(c.trim());
				c = getContent(sheet, 5, row);
				if(c.trim().equals("男")){
					c="male";
				}
				if(c.trim().equals("女")){
					c="female";
				}
				m.setSex(c);
				c = getContent(sheet, 6, row);
				if(!"".equals(c.trim())){
					m.setAge(Integer.parseInt(c));
				}
				c = getContent(sheet, 7, row);
				if(!"".equals(c)){
					m.setJobAge(Integer.parseInt(c));
				}
				c = getContent(sheet,8, row);
				m.setOfficeTelephone(c);
				c = getContent(sheet,9, row);
				m.setMobilePhone(c);
				c = getContent(sheet,10, row);
				m.setCstnetId(c);
				c = getContent(sheet,11, row);
				m.setOfficeAddress(c);
				c = getContent(sheet,12, row);
				m.setDepartment(c.trim());
				c = getContent(sheet,13, row);
				m.setJobType(c);
				c = getContent(sheet,14, row);
				m.setLevelCode(c);
				c = getContent(sheet,15, row);
				m.setJobLevel(c);
				c = getContent(sheet,16, row);
				m.setSalaryLevel(c);
				c = getContent(sheet,17, row);
				m.setHomeAddress(c);
				c = getContent(sheet, 18, row);
				if(!"".equals(c)){
					m.setJobDate(getDate(c,"mm/dd/yy"));
				}
				c = getContent(sheet, 19, row);
				m.setPoliticalStatus(c);
				c = getContent(sheet,20, row);
				m.setDutyType(c);
				c = getContent(sheet,21, row);
				m.setJobStatus(c);
				c = getContent(sheet,22, row);
				m.setDutyGrade(c);
				c = getContent(sheet,23, row);
				m.setSalaryBase(c);
				c = getContent(sheet,24, row);
				m.setSalaryJob(c);
				c = getContent(sheet,25, row);
				if(!"".equals(c)){
					m.setRetireDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,26, row);
				if(!"".equals(c)){
					m.setLeaveDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,27, row);
				if(!"".equals(c)){
					m.setPartyDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet, 28, row);
				m.setGraduateSchool(c);
				c = getContent(sheet,29, row);
				if(!"".equals(c)){
					m.setGraduateDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,30, row);
				m.setMajor(c);
				c = getContent(sheet,31, row);
				m.setHighestGraduateCode(c);
				c = getContent(sheet,32, row);
				m.setHighestGraduate(c);
				c = getContent(sheet,33, row);
				m.setHighestDegreeCode(c);
				c = getContent(sheet,34, row);
				m.setHighestDegree(c);
				c = getContent(sheet,35, row);
				if(!"".equals(c)){
					m.setDegreeDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,36, row);
				m.setDegreeCompany(c);
				c = getContent(sheet,37, row);
				if(!"".equals(c)){
					m.setCompanyJoinDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,38, row);
				m.setCompanyJoinWay(c);
				c = getContent(sheet,39, row);
				if(!"".equals(c)){
					m.setIndustryJoinDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,40, row);
				if(!"".equals(c)){
					m.setDistrictJoinDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,41, row);
				m.setCompanyJoinStatus(c);
				c = getContent(sheet,42, row);
				m.setCompanyJoinType(c);
				c = getContent(sheet,43, row);
				m.setCompanyBefore(c);
				c = getContent(sheet,44, row);
				m.setCompanyBeforeRelation(c);
				c = getContent(sheet,45, row);
				m.setCompanyBeforeType(c);
				c = getContent(sheet,46, row);
				m.setDistrictBefore(c);
				c = getContent(sheet,47, row);
				m.setAdministrationDuty(c);
				c = getContent(sheet,48, row);
				m.setAdministrationDutyDesc(c);
				c = getContent(sheet,49, row);
				m.setAdministrationStatus(c);
				c = getContent(sheet,50, row);
				m.setAdministrationInstitution(c);
				c = getContent(sheet,51, row);
				m.setAdministrationAprovedBy(c);
				c = getContent(sheet,52, row);
				if(!"".equals(c)){
					m.setAdministrationAprovedDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,53, row);
				m.setAdministrationAprovedFile(c);
				c = getContent(sheet,54, row);
				if(!"".equals(c)){
					m.setAdministrationRetainDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,55, row);
				m.setStaffLevel(c);
				c = getContent(sheet,56, row);
				if(!"".equals(c)){
					m.setStaffLevedAprovedDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,57, row);
				m.setStaffLevedAprovedFile(c);
				c = getContent(sheet,58, row);
				m.setStaffLevedAprovedCompany(c);
				c = getContent(sheet,59, row);
				m.setTechnicalDuty(c);
				c = getContent(sheet,60, row);
				m.setTechnicalTitle(c);
				c = getContent(sheet,61, row);
				m.setTechnicalDutyCompany(c);
				c = getContent(sheet,62, row);
				if(!"".equals(c)){
					m.setTechnicalDutyEvaluateDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,63, row);
				if(!"".equals(c)){
					m.setTechnicalDutyEndDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,64, row);
				m.setTechnicalDutyLevel(c);
				c = getContent(sheet,65, row);
				if(!"".equals(c)){
					m.setTechnicalDutyBeginDate(getDate(c,"yyyy-mm-dd"));
				}
				c = getContent(sheet,66, row);
				m.setTechnicalTitleMajor(c);
				c = getContent(sheet,67, row);
				m.setDutyCompany(c);
				c = getContent(sheet,68, row);
				m.setDutyChangeType(c);
				c = getContent(sheet,69, row);
				m.setDutyChangeFile(c);
				c = getContent(sheet,70, row);
				m.setHasKnowledgeInnovation(c);
				c = getContent(sheet,71, row);
				m.setHasArchive(c);
				c = getContent(sheet,72, row);
				m.setHasContract(c);
				c = getContent(sheet,73, row);
				m.setDutyName(c);
				c = getContent(sheet,74, row);
				m.setDutyLevel(c);
				c = getContent(sheet,75, row);
				if(!"".equals(c)){
					m.setDutyDate(getDate(c,"yyyy-mm-dd"));
				}
			
			result.add(m);
			}
			return result;
		}catch (IndexOutOfBoundsException e) {
		LOG.error(e);
		} catch (IOException e) {
		LOG.error(e);
		} catch (BiffException e) {
			e.printStackTrace();
		}
		return null;

	}
	public Date getDate(String c,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format); 
		try {
			return (Date) sdf.parse(c);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 分析Bib文件，将其中的字段填入Paper对象。最终返回解析出的Paper集合。
	 * 若遇到解析出的title字段为空的情况，则视bib文件中 的该项无效，不会返回相应的Paper对象。
	 * 
	 * @return
	 * @throws BibResolveFailedException 
	 */
	public List<InstitutionPaper> analyzePaper(InputStream is) {
		try{
			Workbook book = Workbook.getWorkbook(is);
		
			Sheet sheet = book.getSheet(0);
			int rowNum = sheet.getRows();
			
			List<InstitutionPaper> result = new ArrayList<InstitutionPaper>();
			for (int row = 2; row < rowNum; row++) {
				InstitutionPaper p = new InstitutionPaper();
				InstitutionPublication publication = new InstitutionPublication();
				InstitutionAuthor author = new InstitutionAuthor();
				List<InstitutionAuthor> authors = new ArrayList<InstitutionAuthor>();
			String c = "";
			c = getContent(sheet, 1, row);   //部门名
			p.setDepartName(c);
			c = getContent(sheet, 2, row);	 //标题
			p.setTitle(c);
			c = getContent(sheet, 3, row);	 //刊物名称
			publication.setPubName(c);
			p.setPublication(publication);
			c = getContent(sheet, 5, row);	 //发表日期
			if(!c.trim().equals("")){
				p.setPublicationYear(Integer.parseInt(c));
			}
			c = getContent(sheet, 6, row);	 //卷号
			p.setVolumeNumber(c);
			c = getContent(sheet, 7, row);	 //期号
			p.setSeries(c);
			c = getContent(sheet, 8, row);	  //页号
			if(!c.trim().equals("")){
				String[] page = c.trim().split("-");
				p.setStartPage(Integer.parseInt(page[0].trim()));
				p.setEndPage(Integer.parseInt(page[1]));
			}
			//作者
			StringBuffer sb = new StringBuffer();
			c = getContent(sheet, 9, row);
			author.setAuthorName(c);
			sb.append(c.trim());
			sb.append(",");
			if(c.equals(getContent(sheet, 15, row))){
				author.setCommunicationAuthor(true);
			}else{
				author.setCommunicationAuthor(false);
			}
			c = getContent(sheet, 10, row);
			author.setAuthorCompany(c);
			author.setOrder(1);
			authors.add(author);
			
			c = getContent(sheet, 11, row);
			author.setAuthorName(c);
			sb.append(c.trim());
			sb.append(",");
			if(c.equals(getContent(sheet, 15, row))){
				author.setCommunicationAuthor(true);
			}else{
				author.setCommunicationAuthor(false);
			}
			c = getContent(sheet, 12, row);
			author.setAuthorCompany(c);
			author.setOrder(2);
			authors.add(author);
			
			c = getContent(sheet, 13, row);
			author.setAuthorName(c);
			sb.append(c.trim());
			if(c.equals(getContent(sheet, 15, row))){
				author.setCommunicationAuthor(true);
			}else{
				author.setCommunicationAuthor(false);
			}
			c = getContent(sheet, 14, row);
			author.setAuthorCompany(c);
			author.setOrder(3);
			authors.add(author);
			p.setAuthors(authors);
			p.setAuthor(sb.toString());
			
			result.add(p);
			}
			return result;
		}catch (IndexOutOfBoundsException e) {
		LOG.error(e);
		} catch (IOException e) {
		LOG.error(e);
		} catch (BiffException e) {
			e.printStackTrace();
		}
		return null;

	}
	private String getContent(Sheet sheet, int col, int row) {
		String content = "";
		Cell cell = null;
		cell = sheet.getCell(col, row);
		if (cell != null) {
		content = cell.getContents();
		}
		return content;
		}

}



