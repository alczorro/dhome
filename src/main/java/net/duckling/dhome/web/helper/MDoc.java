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

import java.io.BufferedWriter;  
import java.io.ByteArrayOutputStream;
import java.io.File;  
import java.io.FileInputStream;
import java.io.FileNotFoundException;  
import java.io.FileOutputStream;  
import java.io.IOException;  
import java.io.InputStream;
import java.io.OutputStreamWriter;  
import java.io.UnsupportedEncodingException;  
import java.io.Writer;  
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;  
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.common.util.DateUtils;
import net.duckling.dhome.domain.institution.InstitutionAcademic;
import net.duckling.dhome.domain.institution.InstitutionAuthor;
import net.duckling.dhome.domain.institution.InstitutionAward;
import net.duckling.dhome.domain.institution.InstitutionCopyright;
import net.duckling.dhome.domain.institution.InstitutionDepartment;
import net.duckling.dhome.domain.institution.InstitutionJobApply;
import net.duckling.dhome.domain.institution.InstitutionMemberFromVmt;
import net.duckling.dhome.domain.institution.InstitutionOptionVal;
import net.duckling.dhome.domain.institution.InstitutionPaper;
import net.duckling.dhome.domain.institution.InstitutionPatent;
import net.duckling.dhome.domain.institution.InstitutionPublication;
import net.duckling.dhome.domain.institution.InstitutionTopic;
import net.duckling.dhome.domain.institution.InstitutionTraining;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.common.util.BrowseUtils;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfCopyFields;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
  
import freemarker.template.Configuration;  
import freemarker.template.Template;  
import freemarker.template.TemplateException;


/**
 * 生成word文件模版
 * @author wlj
 *
 */
public class MDoc {
	private static Configuration configuration = null;  
	  
    public MDoc() {  
        configuration = new Configuration();  
        configuration.setDefaultEncoding("utf-8");  
    }  
  
    public static File createDoc(Map<String,Object> dataMap,String fileName) throws UnsupportedEncodingException {  
        //dataMap 要填入模本的数据文件  
        //设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，  
        //这里我们的模板是放在template包下面  
    	String prefix = MDoc.class.getResource("/").getPath();
		File file = new File(new File(prefix).getParent(),"/conf");
		File cacheDir = new File(new File(prefix).getParent(),"cache");
		if(!cacheDir.exists())
			cacheDir.mkdir();
//        configuration.setClassForTemplateLoading(this.getClass(), "/conf");
        
        Template t=null;  
        try {  
        	configuration.setDirectoryForTemplateLoading(file);
            //test.ftl为要装载的模板  
            t = configuration.getTemplate("wordTempl.ftl");  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        //输出文档路径及名称  
        File outFile = new File(cacheDir,fileName);  
        Writer out = null;  
        FileOutputStream fos=null;  
        try {  
            fos = new FileOutputStream(outFile);  
            OutputStreamWriter oWriter = new OutputStreamWriter(fos,"UTF-8");  
            //这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。  
            //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));  
             out = new BufferedWriter(oWriter);   
        } catch (FileNotFoundException e1) {  
            e1.printStackTrace();  
        }  
           
        try {  
            t.process(dataMap, out);  
            out.close();  
            fos.close();  
        } catch (TemplateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return outFile;
    }
    public static void getJobApplys(String fileName, HttpServletResponse resp,InstitutionMemberFromVmt  member,SimpleUser user,InstitutionJobApply jobApply,
    		List<Work> works,List<Education> edus,List<InstitutionPaper> firstPapers,List<InstitutionPaper> otherPapers,
    		Map<Integer,List<InstitutionAuthor>> authorMap,Map<Integer,List<InstitutionAuthor>> otherAuthorMap,
    		Map<Integer,InstitutionAuthor> authors,Map<Integer,InstitutionPublication> pubMap,List<InstitutionTopic> topics,
    		List<InstitutionPatent> patents,List<InstitutionCopyright> copyrights,List<InstitutionAward> awards,
    		List<InstitutionAcademic> academics,List<InstitutionTraining> trainings,Map<Integer,InstitutionOptionVal> awardTypes,
    		Map<Integer,InstitutionOptionVal> awardGrades,Map<Integer,InstitutionOptionVal> organizationNames,Map<Integer,InstitutionOptionVal> positions,
    		Map<Integer,InstitutionOptionVal> types,Map<Integer,InstitutionOptionVal> degrees,int papersCount,int allSciCount,
    		List<InstitutionPaper> allFirstPapers,int allFirstSciCount,int otherSciCount,Double allIf,Double firstIf,Double otherIf,Double allFirstIf,HttpServletRequest request ){
    	
    	Map<String, Object> dataMap = new HashMap<String, Object>(); 
    	dataMap.put("name", user.getZhName());
    	if(member.getSex()==null||"".equals(member.getSex())){
    		dataMap.put("sex", "");
    	}else if(member.getSex().equals("male")){
	    	 dataMap.put("sex", "男"); 
			}else if(member.getSex().equals("female")){
				dataMap.put("sex", "女"); 
			}else{
				dataMap.put("sex", "");
			}
			Calendar cl = Calendar.getInstance();
		if(member.getBirth()!=null&&"".equals(member.getBirth())){
			cl.setTime(member.getBirth());
			dataMap.put("year", cl.get(Calendar.YEAR));
		}else{
			dataMap.put("year", "");
		}
		Map<Integer,String> positionMap=new HashMap<Integer,String>();
		positionMap.put(1, "研究员");
		positionMap.put(2, "副研究员");
		positionMap.put(3, "正研级高工");
		positionMap.put(4, "高级工程师");
		positionMap.put(5, "编审");
		positionMap.put(6, "副编审");
		positionMap.put(7, "高级实验师");
		dataMap.put("position", positionMap.get(jobApply.getJobId()));
			
		//教育工作经历
        List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();//题目  
//        List<Map<String, Object>> list11 = new ArrayList<Map<String, Object>>();//答案  
        
        String str="";
//		 Calendar cl = Calendar.getInstance();
        if(works!=null){
        	for (int i = 0; i < works.size(); i++) { 
              	 str+=works.get(i).getAliasInstitutionName()+"   "+works.get(i).getDepartment();
       				if(works.get(i).getDepartment()!=null&&!"".equals(works.get(i).getDepartment())){
       					str+=","+works.get(i).getPosition()+"|";
//       					Calendar cl = Calendar.getInstance();
       					if(works.get(i).getBeginTime()!=null){
       						cl.setTime(works.get(i).getBeginTime());
       						str+=cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH);
       					}
       					if(works.get(i).getEndTime()!=null){
       						cl.setTime(works.get(i).getEndTime());
       						if(cl.get(Calendar.YEAR)== 3000){
       							str+="-"+"至今"+"\r\n";
       						}else{
       							str+="-"+cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH)+"\r\n";
       						}
       					}
       				}
         
                   Map<String, Object> map = new HashMap<String, Object>();  
                   map.put("work", str); 
                   str="";
                  
                   list1.add(map);  
         
               }  
        }
        if(edus!=null){
    	 for (int i = 0; i < edus.size(); i++) { 
           	 str+=edus.get(i).getAliasInstitutionName()+"   "+edus.get(i).getDepartment()+","+edus.get(i).getDegree()+"|";
//        			Calendar cl = Calendar.getInstance(); 
    			cl.setTime(edus.get(i).getBeginTime());
    			str+=cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH)+"-";
    			
    			cl.setTime(edus.get(i).getEndTime());
    			if(cl.get(Calendar.YEAR)== 3000){
    				str+="至今"+"\r\n";
    			}else{
    				str+=cl.get(Calendar.YEAR)+"."+cl.get(Calendar.MONTH)+"."+cl.get(Calendar.DAY_OF_MONTH)+"\r\n";
    			}
      
                Map<String, Object> map = new HashMap<String, Object>();  
                map.put("work", str);  
               
                list1.add(map);  
                str="";
            }  
        }
       
        dataMap.put("table1", list1); 
//        dataMap.put("table11", list11);
      //工作业绩
        dataMap.put("performance",jobApply.getJobPerformance());
     // 论文  
        List<Map<String, Object>> list2 = new ArrayList<Map<String, Object>>();  
//        List<Map<String, Object>> list12 = new ArrayList<Map<String, Object>>();
        String firstPaper="";
		 String otherPaper="";
			int j=1;
//			Map<Integer,InstitutionPublication> pubMap = extractPub(paperService.getAllPubs());
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
				 Map<String, Object> map = new HashMap<String, Object>();
				 if(firstPaper!=""){
					 map.put("firstPaper", firstPaper);
					 list2.add(map); 
				 }
	             
	             firstPaper="";
			}
		 dataMap.put("count2",String.valueOf(j-1+otherSciCount));
		 dataMap.put("count4",String.valueOf(j-1));
          
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
			 Map<String, Object> map = new HashMap<String, Object>();  
			 if(firstPaper!=""){
				 map.put("firstPaper", firstPaper);
				 list2.add(map);
			 }
            firstPaper="";
		}
        dataMap.put("table2", list2); 
        List<Map<String, Object>> list3 = new ArrayList<Map<String, Object>>();
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
				 Map<String, Object> map = new HashMap<String, Object>();
				 if(otherPaper!=""){
					 map.put("otherPaper", otherPaper);
					 list3.add(map);
				 }
	             
	             otherPaper="";
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
				 Map<String, Object> map = new HashMap<String, Object>();  
				 if(otherPaper!=""){
					 map.put("otherPaper", otherPaper);
					 list3.add(map);
				 }
	             
	             otherPaper="";
			}
		 dataMap.put("table3", list3);
		//论文数量
		 dataMap.put("allcount1",papersCount);
		 dataMap.put("allcount2",allSciCount);
		 dataMap.put("allcount3",allFirstPapers.size());
		 dataMap.put("allcount4",allFirstSciCount);
		
		 dataMap.put("count1",firstPapers.size()+otherPapers.size());
		
		 dataMap.put("count3",firstPapers.size());
		
		 dataMap.put("allIf",allIf);
		 dataMap.put("allfirstIf",allFirstIf);
		 BigDecimal bd1 = new BigDecimal(Double.toString(otherIf));
	     BigDecimal bd2 = new BigDecimal(Double.toString(firstIf));
		 dataMap.put("ifs",bd1.add(bd2).doubleValue());
		 dataMap.put("firstIf",firstIf);
		//课题
		 List<Map<String, Object>> list4 = new ArrayList<Map<String, Object>>();
		for(int i = 1; i <= topics.size(); i++){
			InstitutionTopic topic = topics.get(i-1);
			 Map<String, Object> map = new HashMap<String, Object>();  
            map.put("topic_name", topic.getName());
            map.put("topic_type", types.get(topic.getType()).getVal());
            map.put("topic_cost", topic.getProject_cost());
            if(topic.getRole().equals("admin")){
           	map.put("topic_role", "负责人");
			}
			if(topic.getRole().equals("member")){
				map.put("topic_role", "参与人");
			}
			map.put("topic_time", topic.getStart_year()+"."+topic.getStart_month() + " - " + 
					topic.getEnd_year()+"."+topic.getEnd_month());
			list4.add(map);
		}
		dataMap.put("table4", list4);
		//专利与成果
		 List<Map<String, Object>> list5 = new ArrayList<Map<String, Object>>();
		for(int i = 1; i <= patents.size(); i++){
			InstitutionPatent patent = patents.get(i-1);
			Map<String, Object> map = new HashMap<String, Object>();  
           map.put("patent_name", patent.getName());
			list5.add(map);
		}
		for(int i = 1; i <= copyrights.size(); i++){
			InstitutionCopyright copyright = copyrights.get(i-1);
			Map<String, Object> map = new HashMap<String, Object>();  
           map.put("patent_name", copyright.getName());
			list5.add(map);
		}
		dataMap.put("table5", list5);
		//奖励
		 List<Map<String, Object>> list6 = new ArrayList<Map<String, Object>>();
		for(int i = 1; i <= awards.size(); i++){
			InstitutionAward award = awards.get(i-1);
			Map<String, Object> map = new HashMap<String, Object>();  
           map.put("award_year", award.getYear());
           map.put("award_type", awardTypes.get(award.getType()).getVal());
           map.put("award_name", award.getName());
           map.put("award_grade", awardGrades.get(award.getGrade()).getVal());
           map.put("award_order", award.getAuthorOrder());
           list6.add(map);
		}
		dataMap.put("table6", list6);
		//学术任职
		 List<Map<String, Object>> list7 = new ArrayList<Map<String, Object>>();
		for(int i = 1; i <= academics.size(); i++){
			InstitutionAcademic aca = academics.get(i-1);
			Map<String, Object> map = new HashMap<String, Object>();  
           map.put("academic_name", organizationNames.get(aca.getName()).getVal());
           map.put("academic_position", positions.get(aca.getPosition()).getVal());
           map.put("academic_time", aca.getStartYear()+"."+aca.getStartMonth() + " - " + 
					aca.getEndYear()+"."+aca.getEndMonth());
			list7.add(map);
		}
		dataMap.put("table7", list7);
		
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
		dataMap.put("dcount",count1==0?"0":String.valueOf(count1));
		dataMap.put("dnames",docNames);
		dataMap.put("mcount",count2==0?"0":String.valueOf(count2));
		dataMap.put("mnames",masterNames);
		
		dataMap.put("remark",jobApply.getRemark());
		 
        MDoc doc= new MDoc();
        File file = null;  
        InputStream fin = null;  
        ServletOutputStream out = null; 
        try{
       	 	file=createDoc(dataMap, fileName);
            fin = new FileInputStream(file);  
            
            resp.setCharacterEncoding("utf-8");  
            resp.setContentType("application/msword"); 
            // 设置浏览器以下载的方式处理该文件默认名为resume.doc  
//            resp.addHeader("Content-Disposition", "attachment;filename="+new String( fileName.getBytes("utf-8"), "ISO8859-1" )+".doc");  
            resp.setHeader("Content-Disposition", BrowseUtils.encodeFileName(request.getHeader("User-Agent"),fileName+".doc"));
            out = resp.getOutputStream();  
            byte[] buffer = new byte[512];  // 缓冲区  
            int bytesToRead = -1;  
            // 通过循环将读入的Word文件的内容输出到浏览器中  
            while((bytesToRead = fin.read(buffer)) != -1) {  
                out.write(buffer, 0, bytesToRead);  
            }  
        } catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {  
            if(fin != null)
				try {
					fin.close();
					if(out != null) out.close();  
		            if(file != null) file.delete(); // 删除临时文件  
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
        }  
    		
    	}
    public static File paperDoc(Map<String,Object> dataMap,String fileName) throws UnsupportedEncodingException {  
        //dataMap 要填入模本的数据文件  
        //设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，  
        //这里我们的模板是放在template包下面  
    	String prefix = MDoc.class.getResource("/").getPath();
		File file = new File(new File(prefix).getParent(),"/conf");
		File cacheDir = new File(new File(prefix).getParent(),"cache");
		if(!cacheDir.exists())
			cacheDir.mkdir();
//        configuration.setClassForTemplateLoading(this.getClass(), "/conf");
        
        Template t=null;  
        try {  
        	configuration.setDirectoryForTemplateLoading(file);
            //test.ftl为要装载的模板  
            t = configuration.getTemplate("papersTempl.ftl");  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        //输出文档路径及名称  
        File outFile = new File(cacheDir,fileName);  
        Writer out = null;  
        FileOutputStream fos=null;  
        try {  
            fos = new FileOutputStream(outFile);  
            OutputStreamWriter oWriter = new OutputStreamWriter(fos,"UTF-8");  
            //这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。  
            //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));  
             out = new BufferedWriter(oWriter);   
        } catch (FileNotFoundException e1) {  
            e1.printStackTrace();  
        }  
           
        try {  
            t.process(dataMap, out);  
            out.close();  
            fos.close();  
        } catch (TemplateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return outFile;
    }
    public static void getPapers(String fileName,HttpServletResponse resp,List<InstitutionPaper> papers,
    		Map<Integer,List<InstitutionAuthor>> authorMap,Map<Integer, InstitutionPublication> pubMap,HttpServletRequest request){
    	Map<String, Object> dataMap = new HashMap<String, Object>(); 
    	//论文
    	int index = 1;
    	List<Map<String, Object>> list1 = new ArrayList<Map<String, Object>>();
    	
    	for (InstitutionPaper paper : papers) {
    		StringBuffer auth=new StringBuffer();
    		Map<String, Object> map = new HashMap<String, Object>();
    		auth.append(index+". "+paper.getTitle()+".");
//    		map.put("seq", index);
//    		map.put("title", paper.getTitle());
    		if(authorMap.get(paper.getId())!=null){
    			for (InstitutionAuthor author : authorMap.get(paper.getId())) {
        			if(author.isCommunicationAuthor()){
        				auth.append(author.getAuthorName()+"(*),");
        			}else{
        				auth.append(author.getAuthorName()+",");
        			}
    			}
//        		auth.deleteCharAt(auth.length()-1);
    		}
//    		map.put("author", auth);
    		if(pubMap!=null){
    			auth.append(" "+(pubMap.get(paper.getPublicationId())==null?"":pubMap.get(paper.getPublicationId()).getPubName())+".");
    		}
    		
    		if(paper.getSummary()!=null&&!"".equals(paper.getSummary())){
    			auth.append("\r\n");
    			auth.append("  摘要："+paper.getSummary());
    		}
//    		map.put("pub", pubMap.get(paper.getPublicationId())==null?"":pubMap.get(paper.getPublicationId()).getPubName());
//    		map.put("summary", paper.getSummary()==null?"":paper.getSummary());
    		replaceAll(auth,"&", "&amp;");
    		replaceAll(auth,"<", "&lt;");
    		replaceAll(auth,">", "&gt;");
    		
    		map.put("paper", auth);
    		list1.add(map);
    		index++;
		}
    	dataMap.put("table1", list1);
    	
    	MDoc doc= new MDoc();
    	 File file = null;  
         InputStream fin = null;  
         ServletOutputStream out = null; 
         try{
        	 	file=paperDoc(dataMap, fileName);
             fin = new FileInputStream(file);  
             
             resp.setCharacterEncoding("utf-8");  
             resp.setContentType("application/msword"); 
             // 设置浏览器以下载的方式处理该文件默认名为resume.doc  
//             resp.addHeader("Content-Disposition", "attachment;filename="+BrowseUtils.encodeFileName(request.getHeader("User-Agent"),fileName+".doc"));  
             resp.setHeader("Content-Disposition", BrowseUtils.encodeFileName(request.getHeader("User-Agent"),fileName+".doc")); 
             out = resp.getOutputStream();  
             byte[] buffer = new byte[512];  // 缓冲区  
             int bytesToRead = -1;  
             // 通过循环将读入的Word文件的内容输出到浏览器中  
             while((bytesToRead = fin.read(buffer)) != -1) {  
                 out.write(buffer, 0, bytesToRead);  
             }  
         } catch (UnsupportedEncodingException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (FileNotFoundException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} finally {  
             if(fin != null)
 				try {
 					fin.close();
 					if(out != null) out.close();  
 		            if(file != null) file.delete(); // 删除临时文件  
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}  
         }  
    }
    public static StringBuffer replaceAll(StringBuffer sb, String oldStr, String newStr) {
        int i = sb.indexOf(oldStr);
        int oldLen = oldStr.length();
        int newLen = newStr.length();
        while (i > -1) {
            sb.delete(i, i + oldLen);
            sb.insert(i, newStr);
            i = sb.indexOf(oldStr, i + newLen);
        }
        return sb;
    }
    public static File achievementsDoc(Map<String,Object> dataMap,String fileName) throws UnsupportedEncodingException {  
        //dataMap 要填入模本的数据文件  
        //设置模本装置方法和路径,FreeMarker支持多种模板装载方法。可以重servlet，classpath，数据库装载，  
        //这里我们的模板是放在template包下面  
    	String prefix = MDoc.class.getResource("/").getPath();
		File file = new File(new File(prefix).getParent(),"/conf");
		File cacheDir = new File(new File(prefix).getParent(),"cache");
		if(!cacheDir.exists())
			cacheDir.mkdir();
//        configuration.setClassForTemplateLoading(this.getClass(), "/conf");
        
        Template t=null;  
        try {  
        	configuration.setDirectoryForTemplateLoading(file);
            //test.ftl为要装载的模板  
            t = configuration.getTemplate("achievementsTempl.ftl");  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        //输出文档路径及名称  
        File outFile = new File(cacheDir,fileName);  
        Writer out = null;  
        FileOutputStream fos=null;  
        try {  
            fos = new FileOutputStream(outFile);  
            OutputStreamWriter oWriter = new OutputStreamWriter(fos,"UTF-8");  
            //这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。  
            //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));  
             out = new BufferedWriter(oWriter);   
        } catch (FileNotFoundException e1) {  
            e1.printStackTrace();  
        }  
           
        try {  
            t.process(dataMap, out);  
            out.close();  
            fos.close();  
        } catch (TemplateException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return outFile;
    }
    public static void getAchievements(String fileName,HttpServletResponse resp,List<InstitutionPaper> papers,
    		Map<Integer,List<InstitutionAuthor>> authorMap,Map<Integer, InstitutionPublication> pubMap,InstitutionMemberFromVmt mem,
    		Map<Integer, InstitutionDepartment> deptMap,List<InstitutionAuthor> authors,HttpServletRequest request){
    	Map<String, Object> dataMap = new HashMap<String, Object>();
    	dataMap.put("name",mem.getTrueName());
    	dataMap.put("dept", deptMap.get(mem.getDepartId())==null?"":deptMap.get(mem.getDepartId()).getShortName());
   
    	//发表期刊的十个条件
    	List<InstitutionPaper> list1 = new ArrayList<InstitutionPaper>();
    	List<InstitutionPaper> list2 = new ArrayList<InstitutionPaper>();
    	List<InstitutionPaper> list3 = new ArrayList<InstitutionPaper>();
    	List<InstitutionPaper> list4 = new ArrayList<InstitutionPaper>();
    	List<InstitutionPaper> list5 = new ArrayList<InstitutionPaper>();
    	List<InstitutionPaper> list6 = new ArrayList<InstitutionPaper>();
    	List<InstitutionPaper> list7 = new ArrayList<InstitutionPaper>();
    	List<InstitutionPaper> list8 = new ArrayList<InstitutionPaper>();
    	List<InstitutionPaper> list9 = new ArrayList<InstitutionPaper>();
    	
    	Map<Integer,List<InstitutionPaper>> paperMap = new HashMap<Integer,List<InstitutionPaper>>();
    	for (InstitutionPaper paper : papers) {
    		StringBuffer auth=new StringBuffer();
    		Map<String, Object> map = new HashMap<String, Object>();
    		if(pubMap.get(paper.getPublicationId())!=null&&pubMap.get(paper.getPublicationId()).getPubName().equals("NATURE")){
    			list1.add(paper);
    		}
    		else if(pubMap.get(paper.getPublicationId())!=null&&pubMap.get(paper.getPublicationId()).getPubName().equals("SCIENCE")){
    			list1.add(paper);
    			paperMap.put(1, list1);
    		}
    		else if(pubMap.get(paper.getPublicationId())!=null&&Double.parseDouble(pubMap.get(paper.getPublicationId()).getIfs())>5){
    			list2.add(paper);
    			paperMap.put(2, list2);
    		}
    		else if(pubMap.get(paper.getPublicationId())!=null&&Double.parseDouble(pubMap.get(paper.getPublicationId()).getIfs())>3&&Double.parseDouble(pubMap.get(paper.getPublicationId()).getIfs())<=5){
    			list3.add(paper);
    			paperMap.put(3, list3);
    		}
    		else if(pubMap.get(paper.getPublicationId())!=null&&(Double.parseDouble(pubMap.get(paper.getPublicationId()).getIfs())>1&&Double.parseDouble(pubMap.get(paper.getPublicationId()).getIfs())<=3
    				||pubMap.get(paper.getPublicationId()).getPubName().equals("大气科学进展"))){
    			list4.add(paper);
    			paperMap.put(4, list4);
    		}
    		else if(pubMap.get(paper.getPublicationId())!=null&&pubMap.get(paper.getPublicationId()).getPubName().equals("大气和海洋科学快报")){
    			list5.add(paper);
    			paperMap.put(5, list5);
    		}
    		else if(pubMap.get(paper.getPublicationId())!=null&&Double.parseDouble(pubMap.get(paper.getPublicationId()).getIfs())<=1){
    			list6.add(paper);
    			paperMap.put(6, list6);
    		}
    		else if(pubMap.get(paper.getPublicationId())!=null&&(!isContainChinese(pubMap.get(paper.getPublicationId()).getPubName())&&
    				!pubMap.get(paper.getPublicationId()).getPublicationType().equals("SCI"))||pubMap.get(paper.getPublicationId()).getPublicationType().equals("SCIE")
    				||pubMap.get(paper.getPublicationId()).getPubName().equals("大气科学")||pubMap.get(paper.getPublicationId()).getPubName().equals("气候与环境研究")){
    			list7.add(paper);
    			paperMap.put(7, list7);
    		}
    		else if(pubMap.get(paper.getPublicationId())!=null&&(!isContainChinese(pubMap.get(paper.getPublicationId()).getPubName())&&
    				!pubMap.get(paper.getPublicationId()).getPublicationType().equals("SCI"))){
    			list8.add(paper);
    			paperMap.put(8, list8);
    		}
    		
		}
    	

    	Map<Integer,Integer> scoreMap = new HashMap<Integer,Integer>();
    	scoreMap.put(1, 120);
    	scoreMap.put(2, 70);
    	scoreMap.put(3, 50);
    	scoreMap.put(4, 40);
    	scoreMap.put(5, 30);
    	scoreMap.put(6, 20);
    	scoreMap.put(7, 15);
    	scoreMap.put(8, 8);
		String value="";
		int order=0;
		int sum=0;
	 	//论文
    	int index = 1;
    	List<Map<String, Object>> list11 = new ArrayList<Map<String, Object>>();
		for(int i=1;i<=8;i++){
			for(int k=1;k<=6;k++){
				dataMap.put("no"+i+k, "");
			}
			dataMap.put("N"+i, paperMap.get(i)==null?0:paperMap.get(i).size());
			if(paperMap.get(i)!=null){
				for (int j=1;j<=paperMap.get(i).size()&&j<=6;j++) {
					//附件论文
					StringBuffer sb = new StringBuffer();
					InstitutionPaper p=paperMap.get(i).get(j-1);
					sb.append(index+". "+p.getTitle()+".");
					for (InstitutionAuthor author : authorMap.get(p.getId())) {
						for (InstitutionAuthor a : authors) {
							if(a.getId()==author.getId()){
								order=author.getOrder();
							}
						}
						
						sb.append(author.getAuthorName()+",");
					}
					sb.append(p.getPublicationYear()+".");
					if(pubMap.get(p.getPublicationId())!=null){
						sb.append(pubMap.get(p.getPublicationId()).getPubName()+","+pubMap.get(p.getPublicationId()).getIfs());
					}
					index++;
					Map<String, Object> map = new HashMap<String, Object>();
			    	map.put("paper", sb);
			    	list11.add(map);
					
					value=order+"/"+p.getAuthorAmount();
					dataMap.put("no"+i+j, value);
					
					
					
				}
				sum+=paperMap.get(i).size()*scoreMap.get(i);
			}
			dataMap.put("sum"+i, paperMap.get(i)==null?"":paperMap.get(i).size()*scoreMap.get(i));
		}
		
    	dataMap.put("sum", sum);
    	dataMap.put("score", sum);
    	dataMap.put("table1", list11);
    	
    	
    	MDoc doc= new MDoc();
    	 File file = null;  
         InputStream fin = null;  
         ServletOutputStream out = null; 
         try{
        	 	file=achievementsDoc(dataMap, fileName);
             fin = new FileInputStream(file);  
             
             resp.setCharacterEncoding("utf-8");  
             resp.setContentType("application/msword"); 
             // 设置浏览器以下载的方式处理该文件默认名为resume.doc  
//             resp.addHeader("Content-Disposition", "attachment;filename="+new String( fileName.getBytes("utf-8"), "ISO8859-1" )+".doc");  
             resp.setHeader("Content-Disposition", BrowseUtils.encodeFileName(request.getHeader("User-Agent"),fileName+".doc"));
             out = resp.getOutputStream();  
             byte[] buffer = new byte[512];  // 缓冲区  
             int bytesToRead = -1;  
             // 通过循环将读入的Word文件的内容输出到浏览器中  
             while((bytesToRead = fin.read(buffer)) != -1) {  
                 out.write(buffer, 0, bytesToRead);  
             }  
         } catch (UnsupportedEncodingException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (FileNotFoundException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} catch (IOException e) {
 			// TODO Auto-generated catch block
 			e.printStackTrace();
 		} finally {  
             if(fin != null)
 				try {
 					fin.close();
 					if(out != null) out.close();  
 		            if(file != null) file.delete(); // 删除临时文件  
 				} catch (IOException e) {
 					// TODO Auto-generated catch block
 					e.printStackTrace();
 				}  
         }  
    }
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\u4e00-\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }
}
