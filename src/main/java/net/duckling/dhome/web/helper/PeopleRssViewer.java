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

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.Member;
import net.duckling.dhome.domain.people.Interest;

import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Enclosure;
import com.sun.syndication.feed.rss.Item;

/**
 * @author lvly
 * @since 2013-4-3
 */
public class PeopleRssViewer extends AbstractRssFeedView{
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
		List<Member> userRssInfos=(List<Member>)model.get("peopleRssInfo");
		List<Item> items=new ArrayList<Item>();
		String url=UrlUtil.getRootURL(request);
		if(!url.endsWith("/")){
			url+="/";
		}
		for(Member mem:userRssInfos){
			Item item=new Item();
			item.setTitle(mem.getZhName());
			Description dis=new Description();
			dis.setValue(getDescripTion(mem,url));
			dis.setType("text/html");  
			item.setDescription(dis);
			item.setLink(url+"people/"+mem.getDomain());
			item.setPubDate(mem.getLastEditTime());
			item.setEnclosures(getImgUrl(mem.getImage(), url));
			items.add(item);
		}
		return items;
	}
	private List<Enclosure> getImgUrl(int clbId,String rootUrl)throws Exception{
		List<Enclosure> list=new ArrayList<Enclosure>();
		Enclosure enc=new Enclosure();
		enc.setType("image/jpg");
		if(clbId!=0){
			enc.setUrl(rootUrl+"system/img?imgId="+clbId);
		}else{
			enc.setUrl(rootUrl+"resources/images/dhome-head.png");
		}
		list.add(enc);
		return list;
	}
	private String getDescripTion(Member mem,String rootUrl)throws Exception{
		StringBuffer result=new StringBuffer();
		if(!CommonUtils.isNull(mem.getSalutation())){
			result.append(mem.getSalutation());
			result.append("<br/>");
		}
		if(!CommonUtils.isNull(mem.getInterest())){
			StringBuffer sb=new StringBuffer();
			sb.append("研究兴趣:");
			for(Interest intr:mem.getInterest()){
				sb.append(URLDecoder.decode(URLDecoder.decode(intr.getKeyword(), "utf8"), "utf8")).append(",");
			}
			result.append(CommonUtils.format(sb.toString()));
			result.append("<br/>");
		}
		return result.toString();
	}
	

}
