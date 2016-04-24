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
package net.duckling.dhome.web.helper;

import java.awt.Color;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.duckling.common.util.DateUtils;
import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.InstitutionGrants;
import net.duckling.dhome.domain.institution.InstitutionJobApply;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionOption;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.InstitutionPublication;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IInstitutionOptionValService;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;

/**
 * 生成PDF文件模版
 * @author Brett
 *
 */
public class PdfTemplateHelper {
	final static int GRANT_FORM_SIZE = 8;
	
	
public static ByteArrayOutputStream getJobApplys(InstitutionMemberFromVmt  member,SimpleUser user,InstitutionJobApply jobApply,
		List<Work> works,List<Education> edus,List<InstitutionPaper> firstPapers,List<InstitutionPaper> otherPapers,
		Map<Integer,List<InstitutionAuthor>> authorMap,Map<Integer,List<InstitutionAuthor>> otherAuthorMap,
		Map<Integer,InstitutionAuthor> authors,Map<Integer,InstitutionPublication> pubMap,List<InstitutionTopic> topics,
		List<InstitutionPatent> patents,List<InstitutionCopyright> copyrights,List<InstitutionAward> awards,
		List<InstitutionAcademic> academics,List<InstitutionTraining> trainings,Map<Integer,InstitutionOptionVal> awardTypes,
		Map<Integer,InstitutionOptionVal> awardGrades,Map<Integer,InstitutionOptionVal> organizationNames,Map<Integer,InstitutionOptionVal> positions,
		Map<Integer,InstitutionOptionVal> types,Map<Integer,InstitutionOptionVal> degrees,int papersCount,int allSciCount,
		List<InstitutionPaper> allFirstPapers,int allFirstSciCount,int otherSciCount,float allIf,float firstIf,float otherIf,float allFirstIf ){
	
	
	
		ByteArrayOutputStream result = new ByteArrayOutputStream();
//		BufferedWriter result=new BufferedWriter(new OutputStreamWriter(bos));
//		OutputStream os=new OutputStreamWriter(bos);
		
		String prefix = PdfTemplateHelper.class.getResource("/").getPath();
		File file = new File(new File(prefix).getParent(),"/conf/jobApplyTemplate.pdf");
		
		File cacheDir = new File(new File(prefix).getParent(),"cache");
		if(!cacheDir.exists())
			cacheDir.mkdir();
		
		try {
			PdfCopyFields pdf = new PdfCopyFields(result); 
			
//			int size = grantsList.size();
//			int papeSize = (size%8 == 0 ? size/GRANT_FORM_SIZE : size/GRANT_FORM_SIZE+1);
			
//			for(int j = 0; j < papeSize; j++){
				File temp = new File(cacheDir,"temppdf.pdf");
				
				PdfReader reader = new PdfReader(file.getAbsolutePath());
				PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(temp));
				AcroFields form = stamp.getAcroFields();
				
				 BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);  
				  
				form.addSubstitutionFont(bfChinese);
				//基本信息
				Map<Integer,String> map=new HashMap<Integer,String>();
				map.put(1, "研究员");
				map.put(2, "副研究员");
				map.put(3, "正研级高工");
				map.put(4, "高级工程师");
				map.put(5, "编审");
				map.put(6, "副编审");
				map.put(7, "高级实验师");
				form.setField("name", user.getZhName());
				if(member.getSex().equals("male")){
					form.setField("sex","男");
				}
				if(member.getSex().equals("female")){
					form.setField("sex","女");
				}
				Calendar cl = Calendar.getInstance();
				cl.setTime(member.getBirth());
				form.setField("birthYear",String.valueOf(cl.get(Calendar.YEAR)));
				form.setField("position", map.get(jobApply.getJobId()));
				//教育工作经历
				String str="";
				for (Work work : works) {
					str+=work.getAliasInstitutionName()+"   "+work.getDepartment();
					if(work.getDepartment()!=null&&!"".equals(work.getDepartment())){
						str+=","+work.getPosition()+"|";
//						Calendar cl = Calendar.getInstance();
						if(work.getBeginTime()!=null){
							cl.setTime(work.getBeginTime());
							str+=cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH);
						}
						if(work.getEndTime()!=null){
							cl.setTime(work.getEndTime());
							if(cl.get(Calendar.YEAR)== 3000){
								str+="-"+"至今"+"\r\n";
							}else{
								str+="-"+cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH)+"\r\n";
							}
						}
					}
				}
				for (Education education : edus) {
					str+=education.getAliasInstitutionName()+"   "+education.getDepartment()+","+education.getDegree()+"|";
//					Calendar cl = Calendar.getInstance(); 
					cl.setTime(education.getBeginTime());
					str+=cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH)+"-";
					
					cl.setTime(education.getEndTime());
					if(cl.get(Calendar.YEAR)== 3000){
						str+="至今"+"\r\n";
					}else{
						str+=cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH)+"\r\n";
					}
				}
				form.setField("education",str);
				//工作业绩
				form.setField("performance",jobApply.getJobPerformance());
				//论文
				String firstPaper="";
				String otherPaper="";
				int j=1;
				for (int i=1;i<=firstPapers.size();i++) {
					if(pubMap.get(firstPapers.get(i-1).getPublicationId()).getPublicationType().equals("SCI")){
						firstPaper+="("+j+") ";
						j++;
						for (InstitutionAuthor au : authorMap.get(firstPapers.get(i-1).getId())) {
							firstPaper+=au.getAuthorName()+",";
						}
						firstPaper+=firstPapers.get(i-1).getPublicationYear()+":"+firstPapers.get(i-1).getTitle()+"."+
						pubMap.get(firstPapers.get(i-1).getPublicationId()).getPubName()+","+firstPapers.get(i-1).getVolumeNumber()+"("+
						firstPapers.get(i-1).getSeries()+"),"+firstPapers.get(i-1).getPublicationPage();
						if(firstPapers.get(i-1).getDoi()!=null){
							firstPaper+=",doi:"+firstPapers.get(i-1).getDoi();
						}
						firstPaper+=". 【SCI】";
						for (InstitutionAuthor au : authorMap.get(firstPapers.get(i-1).getId())) {
							if(authors.get(au.getId())!=null){
								if(au.isCommunicationAuthor()==true){
									firstPaper+="【通讯作者】";
								}
								if(au.getOrder()==2){
									if((au.isAuthorStudent()==true)||(au.isAuthorTeacher()==true)){
										firstPaper+="【学生在读】";
									}
								}
							}
							
						}
						firstPaper+="\r\n";
					}
					
				}
				form.setField("count2",String.valueOf(j-1+otherSciCount));
				form.setField("count4",String.valueOf(j-1));
				for (int i=1;i<=firstPapers.size();i++) {
					if(!pubMap.get(firstPapers.get(i-1).getPublicationId()).getPublicationType().equals("SCI")){
						firstPaper+="("+j+") ";
						j++;
						for (InstitutionAuthor au : authorMap.get(firstPapers.get(i-1).getId())) {
							firstPaper+=au.getAuthorName()+",";
						}
						firstPaper+=firstPapers.get(i-1).getPublicationYear()+":"+firstPapers.get(i-1).getTitle()+"."+
						pubMap.get(firstPapers.get(i-1).getPublicationId()).getPubName()+","+firstPapers.get(i-1).getVolumeNumber()+"("+
						firstPapers.get(i-1).getSeries()+"),"+firstPapers.get(i-1).getPublicationPage();
						if(firstPapers.get(i-1).getDoi()!=null){
							firstPaper+=",doi:"+firstPapers.get(i-1).getDoi();
						}
						firstPaper+=". ";
						for (InstitutionAuthor au : authorMap.get(firstPapers.get(i-1).getId())) {
							if(authors.get(au.getId())!=null){
								if(au.isCommunicationAuthor()==true){
									firstPaper+="【通讯作者】";
								}
								if(au.getOrder()==2){
									if((au.isAuthorStudent()==true)||(au.isAuthorTeacher()==true)){
										firstPaper+="【学生在读】";
									}
								}
							}
							
						}
						firstPaper+="\r\n";
					}
					
				}
				form.setField("firstPaper",firstPaper);
				
				int k=1;
				for (int i=1;i<=otherPapers.size();i++) {
					if(pubMap.get(otherPapers.get(i-1).getPublicationId()).getPublicationType().equals("SCI")){
						otherPaper+="("+k+") ";
						k++;
						for (InstitutionAuthor au : otherAuthorMap.get(otherPapers.get(i-1).getId())) {
							otherPaper+=au.getAuthorName()+",";
						}
						otherPaper+=otherPapers.get(i-1).getPublicationYear()+":"+otherPapers.get(i-1).getTitle()+"."+
						pubMap.get(otherPapers.get(i-1).getPublicationId()).getPubName()+","+otherPapers.get(i-1).getVolumeNumber()+"("+
						otherPapers.get(i-1).getSeries()+"),"+otherPapers.get(i-1).getPublicationPage();
						if(otherPapers.get(i-1).getDoi()!=null){
							otherPaper+=",doi:"+otherPapers.get(i-1).getDoi();
						}
						otherPaper+=". 【SCI】";
						for (InstitutionAuthor au : otherAuthorMap.get(otherPapers.get(i-1).getId())) {
							if(authors.get(au.getId())!=null){
								if(au.isCommunicationAuthor()==true){
									otherPaper+="【通讯作者】";
								}
								if(au.getOrder()==2){
									if((au.isAuthorStudent()==true)||(au.isAuthorTeacher()==true)){
										otherPaper+="【学生在读】";
									}
								}
							}
							
						}
						otherPaper+="\r\n";
					}
					
				}
				for (int i=1;i<=otherPapers.size();i++) {
					if(!pubMap.get(otherPapers.get(i-1).getPublicationId()).getPublicationType().equals("SCI")){
						otherPaper+="("+k+") ";
						k++;
						for (InstitutionAuthor au : otherAuthorMap.get(otherPapers.get(i-1).getId())) {
							otherPaper+=au.getAuthorName()+",";
						}
						otherPaper+=otherPapers.get(i-1).getPublicationYear()+":"+otherPapers.get(i-1).getTitle()+"."+
						pubMap.get(otherPapers.get(i-1).getPublicationId()).getPubName()+","+otherPapers.get(i-1).getVolumeNumber()+"("+
						otherPapers.get(i-1).getSeries()+"),"+otherPapers.get(i-1).getPublicationPage();
						if(otherPapers.get(i-1).getDoi()!=null){
							otherPaper+=",doi:"+otherPapers.get(i-1).getDoi();
						}
						otherPaper+=". ";
						for (InstitutionAuthor au : otherAuthorMap.get(otherPapers.get(i-1).getId())) {
							if(authors.get(au.getId())!=null){
								if(au.isCommunicationAuthor()==true){
									otherPaper+="【通讯作者】";
								}
								if(au.getOrder()==2){
									if((au.isAuthorStudent()==true)||(au.isAuthorTeacher()==true)){
										otherPaper+="【学生在读】";
									}
								}
							}
							
						}
						otherPaper+="\r\n";
					}
					
				}
				form.setField("otherPaper",otherPaper);
				//论文数量
				form.setField("allcount1",String.valueOf(papersCount));
				form.setField("allcount2",String.valueOf(allSciCount));
				form.setField("allcount3",String.valueOf(allFirstPapers.size()));
				form.setField("allcount4",String.valueOf(allFirstSciCount));
				
				form.setField("count1",String.valueOf(firstPapers.size()+otherPapers.size()));
				
				form.setField("count3",String.valueOf(firstPapers.size()));
				
				form.setField("allIf",String.valueOf(allIf));
				form.setField("allFirstIf",String.valueOf(allFirstIf));
				form.setField("ifs",String.valueOf(firstIf+otherIf));
				form.setField("firstIf",String.valueOf(firstIf));
				
				
				
				//课题
				for(int i = 1; i <= topics.size(); i++){
					int index = i;
					InstitutionTopic topic = topics.get(i-1);
					form.setField("topic_name_"+index, topic.getName());
					form.setField("topic_type_"+index, types.get(topic.getType()).getVal());
					form.setField("topic_cost_"+index,String.valueOf(topic.getProject_cost()));
					if(topic.getRole().equals("admin")){
						form.setField("topic_role_"+index, "负责人");
					}
					if(topic.getRole().equals("member")){
						form.setField("topic_role_"+index, "参与人");
					}
					form.setField("topic_time_"+index,topic.getStart_year()+"."+topic.getStart_month() + " - " + 
							topic.getEnd_year()+"."+topic.getEnd_month());
				}
				
				//专利与成果
				for(int i = 1; i <= patents.size(); i++){
					int index = i;
					InstitutionPatent patent = patents.get(i-1);
					form.setField("patent_name_"+index, patent.getName());
					
				}
				for(int i = 1; i <= copyrights.size(); i++){
					int index = i+patents.size();
					InstitutionCopyright copyright = copyrights.get(i-1);
					form.setField("patent_name_"+index, copyright.getName());
					
				}
				
				//奖励
				for(int i = 1; i <= awards.size(); i++){
					int index = i;
					InstitutionAward award = awards.get(i-1);
					form.setField("award_year_"+index, String.valueOf(award.getYear()));
					form.setField("award_type_"+index, awardTypes.get(award.getType()).getVal());
					form.setField("award_name_"+index,award.getName());
					form.setField("award_grade_"+index, awardGrades.get(award.getGrade()).getVal());
					form.setField("award_order_"+index,String.valueOf(award.getAuthorOrder()));
				}
				
				//学术任职
				for(int i = 1; i <= academics.size(); i++){
					int index = i;
					InstitutionAcademic aca = academics.get(i-1);
					form.setField("academic_name_"+index, organizationNames.get(aca.getName()).getVal());
					form.setField("academic_position_"+index, positions.get(aca.getPosition()).getVal());
					form.setField("academic_time_"+index,aca.getStartYear()+"."+aca.getStartMonth() + " - " + 
							aca.getEndYear()+"."+aca.getEndMonth());
				}
				
				int count1=0;
				int count2=0;
				String docNames="";
				String masterNames="";
				for (InstitutionTraining training : trainings) {
					if(degrees.get(training.getDegree()).getVal().equals("博士")){
						count1++;
						docNames+=training.getStudentName()+"   ";
					}
					if(degrees.get(training.getDegree()).getVal().equals("硕士")){
						count2++;
						masterNames+=training.getStudentName()+"   ";
					}
				}
				form.setField("doc_count",count1==0?"0":String.valueOf(count1));
				form.setField("doc_names",docNames);
				form.setField("master_count",count2==0?"0":String.valueOf(count2));
				form.setField("master_name",masterNames);
				
				form.setField("remark",jobApply.getRemark());
				
				stamp.setFormFlattening(true);
				stamp.close();
				
				PdfReader tempReader = new PdfReader(temp.getAbsolutePath());
				pdf.addDocument(tempReader); 
				
//			}
			pdf.close(); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	public static ByteArrayOutputStream getGrants(List<InstitutionGrants> grantsList){
		
//		for(int i = 0; i< 20; i++){
//			InstitutionGrants grant = new InstitutionGrants();
//			grant.setTopicNo("topic_no_"+i);
//			grant.setStudentName("学生 "+i);
//			grant.setClassName("班级 "+i);
//			grant.setDegree(3);
//			grant.setScholarship1(new BigDecimal(120+i));
//			grant.setScholarship2(new BigDecimal(220+i));
//			grant.setAssistantFee(new BigDecimal(10+i));
//			grant.setStartTime(new Date());
//			grant.setEndTime(new Date());
//			grantsList.add(grant);
//		}
		
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		
		String prefix = PdfTemplateHelper.class.getResource("/").getPath();
		File file = new File(new File(prefix).getParent(),"/conf/grantstemplate.pdf");
		
		File cacheDir = new File(new File(prefix).getParent(),"cache");
		if(!cacheDir.exists())
			cacheDir.mkdir();
		
		try {
			PdfCopyFields pdf = new PdfCopyFields(result);  
			int size = grantsList.size();
			int papeSize = (size%8 == 0 ? size/GRANT_FORM_SIZE : size/GRANT_FORM_SIZE+1);
			
			for(int j = 0; j < papeSize; j++){
				File temp = new File(cacheDir,"temppdf"+j+".pdf");
				
				PdfReader reader = new PdfReader(file.getAbsolutePath());
				PdfStamper stamp = new PdfStamper(reader, new FileOutputStream(temp));
				AcroFields form = stamp.getAcroFields();

				 BaseFont bfChinese = BaseFont.createFont("STSongStd-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);  
				  
				form.addSubstitutionFont(bfChinese);
				
				for(int i = j*GRANT_FORM_SIZE; i < grantsList.size(); i++){
					int index = i - j*GRANT_FORM_SIZE;
					InstitutionGrants grant = grantsList.get(i);
					form.setField("topic_"+index, grant.getTopicNo());
					form.setField("name_"+index, grant.getStudentName());
					form.setField("class_"+index, grant.getClassName());
					form.setField("degree_"+index, grant.getDegreeName());
					form.setField("scholarship1_"+index,String.valueOf(grant.getScholarship1()) );
					form.setField("scholarship2_"+index,String.valueOf(grant.getScholarship2()) );
					form.setField("assistant_"+index,String.valueOf(grant.getAssistantFee()) );
					form.setField("total_"+index, String.valueOf(grant.getSumFee()));
					form.setField("time_"+index,DateUtils.getDateStr(grant.getStartTime(), "yyyy.MM.dd") + " - " + 
							DateUtils.getDateStr(grant.getEndTime(), "yyyy.MM.dd"));
				}
				stamp.setFormFlattening(true);
				stamp.close();
				
				PdfReader tempReader = new PdfReader(temp.getAbsolutePath());
				pdf.addDocument(tempReader); 
				
			}
			pdf.close(); 
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		 
//		Document document = new Document(PageSize.A4.rotate()); 
//		ByteArrayOutputStream result = new ByteArrayOutputStream();
//		try {
//		  PdfWriter.getInstance( document, result);
//		  document.open();
//
//		   BaseFont bfChi = BaseFont.createFont("STSong-Light",
//					     "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
//		   
//		   Font fontChi = new Font(bfChi, 12, Font.UNDERLINE);
//			   // c)添加一个块
//		   Paragraph pTitle = new Paragraph("研究生助学金发放表", fontChi);
//		   pTitle.setAlignment(1);
//		   document.add(pTitle);
//		   
//		   
//		   Paragraph pTitle1 = new Paragraph("", fontChi);
//		   document.add(pTitle1);
//		   
//		   
//			PdfPTable datatable = new PdfPTable(10);
//			int headerwidths[] = { 5, 10, 10, 10, 10, 10, 10, 12, 8, 15 };
//			datatable.setWidths(headerwidths);
//			datatable.setWidthPercentage(100);
//			datatable.getDefaultCell().setPadding(5);
//			datatable.getDefaultCell().setHorizontalAlignment(
//					Element.ALIGN_CENTER);
//			
//			datatable.addCell(createPhraseZh("序号"));
//			datatable.addCell(createPhraseZh("课题号"));
//			datatable.addCell(createPhraseZh("研究生姓名"));
//			datatable.addCell(createPhraseZh("班级"));
//			datatable.addCell(createPhraseZh("硕士/博士"));
//			
//			PdfPTable table1 = new PdfPTable(2);
//			table1.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//			table1.getDefaultCell().setColspan(2);
//			table1.addCell(createPhraseZh("研究所奖学金(元/月)"));
//			table1.getDefaultCell().setColspan(1);
//			table1.addCell(createPhraseZh("研究所支付"));
//			table1.addCell(createPhraseZh("课题支付"));
//			PdfPCell cell1 = new PdfPCell(table1);
//			cell1.setColspan(2);
//			datatable.addCell(cell1);
//			
//			PdfPTable table2 = new PdfPTable(1);
//			table2.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//			table2.addCell(createPhraseZh("助研酬金(元/月)"));
//			table2.addCell(createPhraseZh("课题支付"));
//			PdfPCell cell2 = new PdfPCell(table2);
//			cell2.setColspan(1);
//			datatable.addCell(cell2);
//			
//			datatable.addCell(createPhraseZh("金额(元/月)"));
//			datatable.addCell(createPhraseZh("发放起止时间"));
//			
//			datatable.getDefaultCell().setBorderWidth(1);
//			datatable.setHeaderRows(1);
//			for (int i = 0; i < grantsList.size(); i++) {
//				InstitutionGrants item = grantsList.get(i);
//				datatable.getDefaultCell().setHorizontalAlignment(Element.ALIGN_CENTER);
//				datatable.addCell(String.valueOf(i+1));
//				datatable.addCell(createPhraseZh(item.getTopicNo()));
//				datatable.addCell(createPhraseZh(item.getStudentName()));
//				datatable.addCell(createPhraseZh(item.getClassName()));
//				datatable.addCell(createPhraseZh(String.valueOf(item.getDegree())));
//				datatable.addCell(String.valueOf(item.getScholarship1()));
//				datatable.addCell(String.valueOf(item.getScholarship2()));
//				datatable.addCell(String.valueOf(item.getAssistantFee()));
//				datatable.addCell(String.valueOf(item.getSumFee()));
//				datatable.addCell(DateUtils.getDateStr(item.getStartTime(), "yyyy.MM.dd") + " - " + 
//						DateUtils.getDateStr(item.getEndTime(), "yyyy.MM.dd"));
//			}
//			datatable.setSplitLate(false);
//			document.add(datatable);
//			
//			
//			document.add(new Paragraph("研究生部签字:         导师签字:      ", fontChi));
//		} catch (DocumentException de) {
//		} catch (IOException e) {
//		}
//		document.close();
//		
		return result;
	}
	
	private static Phrase createPhraseZh(String str) throws DocumentException, IOException{
		return new Phrase(str, createFontZh(10));
	}
	
	private static Font createFontZh(int fontSize) throws DocumentException, IOException{
		BaseFont bf = BaseFont.createFont("STSong-Light",  "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
		Font font = new Font(bf,fontSize);
		return font;
	}
}
