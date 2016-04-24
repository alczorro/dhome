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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.DateUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.ScholarEventDetail;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.feed.AbstractRssFeedView;

import com.sun.syndication.feed.rss.Channel;
import com.sun.syndication.feed.rss.Description;
import com.sun.syndication.feed.rss.Enclosure;
import com.sun.syndication.feed.rss.Item;

/**
 * @author lvly
 * @since 2013-4-3
 */
@Service
public class ScholarRssViewer extends AbstractRssFeedView{
	@Autowired
	private IUserService us;
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
		List<ScholarEventDetail> events=(List<ScholarEventDetail>)model.get("scholarRssInfo");
		InstitutionHome home=(InstitutionHome)model.get("institution");
		List<Item> items=new ArrayList<Item>();
		String url=UrlUtil.getRootURL(request);
		if(!url.endsWith("/")){
			url+="/";
		}
		for(ScholarEventDetail event:events){
			Item item=new Item();
			item.setTitle(event.getTitle());
			Description dis=new Description();
			dis.setValue(getDescripTion(event,url));
			dis.setType("text/html");  
			item.setDescription(dis);
			item.setLink(url+"institution/"+home.getDomain()+"/scholarevent.html?func=view&activityId="+event.getId());
			item.setPubDate(event.getCreateTime());
			item.setEnclosures(getImgUrl(event.getLogoId(), url));
			items.add(item);
		}
		return items;
	}
	private List<Enclosure> getImgUrl(int clbId,String rootUrl){
		List<Enclosure> list=new ArrayList<Enclosure>();
		Enclosure enc=new Enclosure();
		enc.setType("image/jpg");
		if(clbId!=0){
			enc.setUrl(rootUrl+"system/img?imgId="+clbId);
		}else{
			enc.setUrl(rootUrl+"resources/images/dhome-activity.png");
		}
		list.add(enc);
		return list;
	}
	private String getDescripTion(ScholarEventDetail event,String url){
		StringBuffer result=new StringBuffer();
		if(!CommonUtils.isNull(event.getReporter())){
			result.append("主讲:"+event.getReporter());
			result.append("<br/>");
		}
		if(event.getStartTime()!=null&&event.getEndTime()!=null){
			result.append("时间:")
			.append(DateUtils.getDateStr(event.getStartTime()))
			.append("至")
			.append(DateUtils.getDateStr(event.getEndTime()))
			.append("<br/>");
		}
		if(!CommonUtils.isNull(event.getPlace())){
			result.append("地点:")
			.append(event.getPlace())
			.append("<br/>");
		}
		SimpleUser su = us.getSimpleUserByUid(event.getCreator());
		if(su!=null){
			result.append("发布:")
			.append(su.getZhName())
			.append("<br/>");
		}
		return result.toString();
	}
	

}
