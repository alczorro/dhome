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
package net.duckling.dhome.web.helper;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.people.Paper;

import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Item;

/**
 * @author lvly
 * @since 2013-4-3
 */
public class PaperRssViewer extends AbstractRssFeedView{
	@Override
	protected void writeToResponse(HttpServletResponse response,
			ByteArrayOutputStream baos) throws IOException {
	}
	@Override
	protected void buildFeedMetadata(Map<String, Object> model, Channel feed, HttpServletRequest request) {
		InstitutionHome home=(InstitutionHome)model.get("institution");
		feed.setTitle(home.getName());
		feed.setDescription(CommonUtils.killNull(home.getIntroduction()));
		String url=UrlUtil.getRootURL(request);
		if(!url.endsWith("/")){
			url+="/";
		}
		feed.setLink(url+"institution/"+home.getDomain()+"/index.html");
		super.buildFeedMetadata(model, feed, request);
	}
	
	@Override
	protected List<Item> buildFeedItems(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List<Paper> papers=(List<Paper>)model.get("paperRssInfo");
		List<Item> items=new ArrayList<Item>();
		int count=papers.size();
		int index=1;
		for(Paper paper:papers){
			Item item=new Item();
			item.setTitle(paper.getTitle());
			Description dis=new Description();
			dis.setValue(getDescripTion(paper));
			dis.setType("text/html"); 
			item.setDescription(dis);
			item.setLink(paper.getPaperURL());
			items.add(item);
			index++;
		}
		return items;
	}
	private String getDescripTion(Paper paper){
		StringBuffer result=new StringBuffer();
		if(!CommonUtils.isNull(paper.getAuthors())){
			result.append(paper.getAuthors()).append("<br/>");
		}
		if(!CommonUtils.isNull(paper.getSource())){
			result.append(paper.getSource());
			if(!CommonUtils.isNull(paper.getVolumeIssue())){
				result.append(":").append(paper.getVolumeIssue());
			}
			result.append("<br/>");
		}
		if(!CommonUtils.isNull(paper.getSummary())){
			result.append("摘要:").append(paper.getSummary());
			result.append("<br/>");
		}
		StringBuffer finalStr=new StringBuffer();
		
		for(int i=0;i<result.length();i++){
			char c=result.charAt(i);
			if(c<5){
				continue;
			}
			finalStr.append(result.charAt(i));
		}  

		return finalStr.toString();
	}
}
