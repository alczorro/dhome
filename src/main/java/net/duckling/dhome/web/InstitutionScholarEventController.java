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
package net.duckling.dhome.web;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.clb.FileNameSafeUtil;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.DateUtils;
import net.duckling.dhome.common.util.ImageUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.domain.institution.ScholarEvent;
import net.duckling.dhome.domain.institution.ScholarEventDetail;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IInstitutionHomeService;
import net.duckling.dhome.service.IInstitutionPeopleService;
import net.duckling.dhome.service.IInstitutionScholarEventService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.ClbFileService;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 学术活动相关页面的Controller
 * @author Yangxp
 * @since 2012-09-21
 */
@Controller
@RequestMapping("/institution/{domain}/scholarevent.html")
public class InstitutionScholarEventController {
	
	private static final String SIZE = "size";
    private static final String OFFSET = "offset";
    private static final String ACTIVITY_ID = "activityId";
	private static final String DOMAIN = "domain";
	
	@Autowired
	private IInstitutionHomeService ihs;
	@Autowired
	private IInstitutionPeopleService peopleService;
	@Autowired
	private IInstitutionScholarEventService ises;
	@Autowired
	private ClbFileService imgService;
	@Autowired
	private IUserService us;
	
	public void setIhs(IInstitutionHomeService ihs) {
		this.ihs = ihs;
	}

	public void setPeopleService(IInstitutionPeopleService peopleService) {
		this.peopleService = peopleService;
	}

	public void setIses(IInstitutionScholarEventService ises) {
		this.ises = ises;
	}

	public void setImgService(ClbFileService imgService) {
		this.imgService = imgService;
	}

	public void setUs(IUserService us) {
		this.us = us;
	}
	/**
	 * 学术活动展示
	 * @param request
	 * @param domain
	 * @return
	 */
	@RequestMapping
	public ModelAndView display(HttpServletRequest request, @PathVariable(DOMAIN) String domain){
		ModelAndView mv = new ModelAndView("institution/instituteActivity");
		InstitutionHome ih = ihs.getInstitutionByDomain(domain);
		if(null != ih){
			int insId = ih.getInstitutionId();
			int uid = SessionUtils.getUserId(request);
			boolean isMember = peopleService.isMember(uid, insId);
			mv.addObject("institution", ih);
			mv.addObject("isMember", isMember);
		}
		mv.addObject("home",ih);
		return mv;
	}
	
	/**
	 * 获取所有的学术活动信息
	 * @param request
	 * @param domain 机构的域名
	 * @param offset 偏移量
	 * @param size 数量
	 * @return
	 */
	@RequestMapping(params="func=eventAll")
	@ResponseBody
	public JSONArray eventAll(HttpServletRequest request, @PathVariable(DOMAIN) String domain,
			@RequestParam(OFFSET) int offset, @RequestParam(SIZE) int size){
		InstitutionHome ih = ihs.getInstitutionByDomain(domain);
		List<ScholarEventDetail> list = ises.getAllScholarEventOfInstitution(ih.getInstitutionId(), offset, size);
		return getJSON(request, list, domain);
	}
	
	/**
	 * 获取即将开始的学术活动信息
	 * @param request
	 * @param domain 机构的域名
	 * @param offset 偏移量
	 * @param size 数量
	 * @return
	 */
	@RequestMapping(params="func=eventUpcoming")
	@ResponseBody
	public JSONArray eventUpcoming(HttpServletRequest request, @PathVariable(DOMAIN) String domain,
			@RequestParam(OFFSET) int offset, @RequestParam(SIZE) int size){
		InstitutionHome ih = ihs.getInstitutionByDomain(domain);
		List<ScholarEventDetail> list = ises.getUpcomingScholarEventOfInstitution(ih.getInstitutionId(), offset, size);
		return getJSON(request, list, domain);
	}
	/**
	 * 获取正在进行的学术活动
	 * @param request
	 * @param domain 机构的域名
	 * @param offset 偏移量
	 * @param size 数量
	 * @return
	 */
	@RequestMapping(params="func=eventOngoing")
	@ResponseBody
	public JSONArray eventOngoing(HttpServletRequest request, @PathVariable(DOMAIN) String domain,
			@RequestParam(OFFSET) int offset, @RequestParam(SIZE) int size){
		InstitutionHome ih = ihs.getInstitutionByDomain(domain);
		List<ScholarEventDetail> list = ises.getOngoingScholarEventOfInstitution(ih.getInstitutionId(), offset, size);
		return getJSON(request, list, domain);
	}
	/**
	 * 获取已结束的学术活动信息
	 * @param request
	 * @param domain 机构的域名
	 * @param offset 偏移量
	 * @param size 数量
	 * @return
	 */
	@RequestMapping(params="func=eventExpired")
	@ResponseBody
	public JSONArray eventExpired(HttpServletRequest request, @PathVariable(DOMAIN) String domain,
			@RequestParam(OFFSET) int offset, @RequestParam(SIZE) int size){
		InstitutionHome ih = ihs.getInstitutionByDomain(domain);
		List<ScholarEventDetail> list = ises.getExpiredScholarEventOfInstitution(ih.getInstitutionId(), offset, size);
		return getJSON(request, list, domain);
	}
	/**
	 * 跳转到添加学术活动页面
	 * @param request
	 * @param domain
	 * @return
	 */
	@RequestMapping(params="func=add")
	public ModelAndView addScholarEvent(HttpServletRequest request, @PathVariable(DOMAIN) String domain){
		InstitutionHome ih = ihs.getInstitutionByDomain(domain);
		ModelAndView mv = new ModelAndView("institution/instituteAddAct");
		mv.addObject("home",ih);
		return mv;
	}
	/**
	 * 浏览学术活动的详细信息
	 * @param request
	 * @param domain 机构域名
	 * @param activityId 学术活动的ID
	 * @return
	 */
	@RequestMapping(params = "func=view")
	public ModelAndView viewScholarEvent(HttpServletRequest request, @PathVariable(DOMAIN) String domain,
			@RequestParam(ACTIVITY_ID) int activityId){
		ModelAndView mv = new ModelAndView("institution/instituteActivityDetail");
		InstitutionHome ih = ihs.getInstitutionByDomain(domain);
		ScholarEvent se = ises.getScholarEventByID(activityId);
		SimpleUser su = us.getSimpleUserByUid(se.getCreator());
		mv.addObject("institution", ih);
		mv.addObject("event", se);
		mv.addObject("creatorName", su.getZhName());
		mv.addObject("isMember", peopleService.isMember(SessionUtils.getUserId(request), ih.getInstitutionId()));
		return mv;
	}
	/**
	 * 删除学术活动
	 * @param request
	 * @param domain 机构域名
	 * @param activityId 学术活动的ID
	 * @return
	 */
	@RequestMapping(params="func=delete")
	@ResponseBody
	public JSONObject deleteScholarEvent(HttpServletRequest request, @PathVariable(DOMAIN) String domain,
			@RequestParam(ACTIVITY_ID) int activityId){
		ises.remove(activityId);
		JSONObject obj = new JSONObject();
		obj.put("status", "true");
		obj.put(ACTIVITY_ID, activityId);
		return obj;
	}
	
	/**
	 * 跳转到编辑学术活动的页面
	 * @param request
	 * @param eventId 学术活动的ID
	 * @param domain 机构域名
	 * @return
	 */
	@RequestMapping(params="func=edit")
	public ModelAndView editScholarEvent(HttpServletRequest request, @RequestParam(ACTIVITY_ID) int eventId,@PathVariable(DOMAIN) String domain){
		ModelAndView mv = new ModelAndView("institution/instituteAddAct");
		ScholarEvent event=ises.getScholarEventByID(eventId);
		mv.addObject("event",event);
		mv.addObject("home",ihs.getInstitutionByDomain(domain));
		String[] start=DateUtils.getDateStr(event.getStartTime()).split(" ");
		mv.addObject("startDate",start[0]);
		mv.addObject("startTime",start[1]);
		String[] end=DateUtils.getDateStr(event.getEndTime()).split(" ");
		mv.addObject("endDate",end[0]);
		mv.addObject("endTime",end[1]);
		return mv;
	}
	/**
	 * 更新学术活动
	 * @param request
	 * @param domain 机构域名
	 * @return
	 */
	@RequestMapping(params="func=submit")
	public ModelAndView submitScholarEvent(HttpServletRequest request, @PathVariable(DOMAIN) String domain){
		String eventId=CommonUtils.trim(request.getParameter("eventId"));
		//edit
		if(!CommonUtils.isNull(eventId)){
			ScholarEvent event=ises.getScholarEventByID(Integer.valueOf(eventId));
			setValue(event, request);
			ises.updateByID(event);
		}
		//add
		else{
			ScholarEvent event=new ScholarEvent();
			setValue(event, request);
			ises.create(event);
		}
		
		return new ModelAndView(new RedirectView(getPATH(domain)));
	}
	private ScholarEvent setValue(ScholarEvent event,HttpServletRequest request){
		event.setCreator(SessionUtils.getUserId(request));
		event.setStartTime(DateUtils.getDate(CommonUtils.trim(request.getParameter("startTime")+" "+request.getParameter("startCurrentTime"))));
		event.setEndTime(DateUtils.getDate(CommonUtils.trim(request.getParameter("endTime")+" "+request.getParameter("endCurrentTime"))));
		event.setReporter(CommonUtils.trim(request.getParameter("reporter")));
		event.setIntroduction(CommonUtils.trim(request.getParameter("introduction")));
		if(!"true".equals(CommonUtils.trim(request.getParameter("isDefault")))){
			event.setLogoId(storeImage(request));
		}
		event.setTitle(CommonUtils.trim(request.getParameter("title")));
		event.setPlace(CommonUtils.trim(request.getParameter("place")));
		event.setInstitutionId(Integer.valueOf(request.getParameter("institutionId")));
		return event;
		
		
	}
	private int storeImage(HttpServletRequest request) {
		String fileName = CommonUtils.trim(request.getParameter("fileName"));
		fileName=FileNameSafeUtil.makeSafe(fileName);
		String path = request.getSession().getServletContext().getRealPath("");
		path += ImageUtils.PATH;
		path += fileName;
		File file = new File(("true".equals(CommonUtils.trim(request.getParameter("isCut")))) ? ImageUtils.getCutPath(path) : path);
		file = ImageUtils.scale(file.getAbsolutePath());
		int id = imgService.createFile(file);
		ImageUtils.delete(file);
		return id;
	}
	
	private JSONArray getJSON(HttpServletRequest request,List<ScholarEventDetail> list, String domain){
		JSONArray array = new JSONArray();
		if(null != list && !list.isEmpty()){
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			for(ScholarEventDetail sed : list){
				JSONObject obj = new JSONObject();
				obj.put("id", sed.getId());
				obj.put("title", sed.getTitle());
				obj.put("reporter", sed.getReporter());
				obj.put("startTime", format.format(sed.getStartTime()));
				obj.put("endTime", format.format(sed.getEndTime()));
				obj.put("creator", sed.getCreator());
				obj.put("creatorName", sed.getCreatorName());
				obj.put("createTime", format.format(sed.getCreateTime()));
				obj.put("introduction", sed.getIntroduction());
				obj.put("place", sed.getPlace());
				obj.put("imgurl", getImageURL(request, sed.getLogoId()));
				obj.put("editurl", getEditEventURL(request, sed.getId(), domain));
				obj.put("deleteurl", getDeleteEventURL(request, sed.getId(), domain));
				obj.put("eventurl", getEventViewURL(request, sed.getId(), domain));
				array.add(obj);
			}
		}
		return array;
	}
	
	private String getImageURL(HttpServletRequest request,int logoId){
		String path = (logoId ==0)?"/resources/images/dhome-activity.png?":"/system/img?imgId="+logoId;
		return UrlUtil.getRootURL(request)+path;
	}
	
	private String getPATH(String domain){
		return "/institution/"+domain+"/scholarevent.html";
	}
	
	private String getEditEventURL(HttpServletRequest request,int id, String domain){
		return UrlUtil.getRootURL(request)+getPATH(domain)+
				"?func=edit&activityId="+id;
	}
	
	private String getDeleteEventURL(HttpServletRequest request,int id, String domain){
		return UrlUtil.getRootURL(request)+getPATH(domain)+
				"?func=delete&activityId="+id;
	}
	
	private String getEventViewURL(HttpServletRequest request,int id, String domain){
		return UrlUtil.getRootURL(request)+getPATH(domain)+
				"?func=view&activityId="+id;
	}
}
