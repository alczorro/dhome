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
package net.duckling.dhome.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.common.util.DefaultValuePageUtils;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.Work;

import org.junit.Test;

/**
 * @author lvly
 * @since 2012-8-24
 */
public class DefaultValuePageUtilsTestCase {
	@Test
	public void test_getHTML_Index(){
		List<Paper> papers=new ArrayList<Paper>();
		Paper p1=new Paper();
		p1.setAuthors("测试君1");
		p1.setPublishedTime("2012-02-12");
		p1.setTitle("标题1");
		p1.setSource("喵喵期刊");
		Paper p2=new Paper();
		p2.setAuthors("测试君1");
		p2.setPublishedTime("2012-02-12");
		p2.setTitle("标题1");
		p2.setSource("喵喵期刊");
		papers.add(p1);
		papers.add(p2);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("${papers}", papers);
		String result=DefaultValuePageUtils.getHTML(map, "src/test/resources/net/duckling/dhome/template/index.tmp").replaceAll("\\s", "");
		Assert.assertEquals("<br><h1>论文列表</h1><ul><li><span><br><span>作者：测试君1</span><br><span>标题：标题1</span><br><span>期刊：喵喵期刊</span><br><span>时间：2012-02-12</span></span><divclass=\"clear\"></div></li></ul><ul><li><span><br><span>作者：测试君1</span><br><span>标题：标题1</span><br><span>期刊：喵喵期刊</span><br><span>时间：2012-02-12</span></span><divclass=\"clear\"></div></li></ul>", result);
	}
	@Test
	public void test_getHTML_profile(){
		List<Work> works=new ArrayList<Work>();
		Work work=new Work();
		work.setBeginTime(new Date());
		work.setEndTime(new Date());
		work.setAliasInstitutionName("xx大学");
		work.setDepartment("计算机学院");
		work.setPosition("教授");
		works.add(work);
		List<Education> edus=new ArrayList<Education>();
		Education edu=new Education();
		edu.setAliasInstitutionName("xx大学");
		edu.setDegree("学士");
		edu.setDepartment("计算机本科");
		edus.add(edu);
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("${works}", works);
		map.put("${edus}", edus);
		String result=DefaultValuePageUtils.getHTML(map, "src/test/resources/net/duckling/dhome/template/profile.tmp").replaceAll("\\s", "");
		Assert.assertEquals("<p><fontcolor=\"red\">个人经历:</font></p><br><h1>教育经历：</h1><p>xx大学，学士，计算机本科</p><b><h1>工作经历：</h1></b><p>xx大学，计算机学院，教授</p><br>", result);
	}
	@Test
	public void test_getHTML_contact(){
		Map<String,Object> map=new HashMap<String,Object>();
		map.put("${test}", "44444444444");
		String result=DefaultValuePageUtils.getHTML(map, "src/test/resources/net/duckling/dhome/template/contact.tmp").replaceAll("\\s", "");
		Assert.assertEquals("<p>联系地址:<p><p>联系电话:<p>QQ：44444444444<br>", result);
	}

}
