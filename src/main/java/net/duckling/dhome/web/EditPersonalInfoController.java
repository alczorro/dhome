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

import java.net.URLDecoder;
import java.sql.Date;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.DateUtils;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.domain.object.FavoriteUrl;
import net.duckling.dhome.domain.object.InstitutionName;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Education;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Work;
import net.duckling.dhome.service.IEducationService;
import net.duckling.dhome.service.IFavoriteUrlService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IMessageBoardService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.web.helper.InterestSplitHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 编辑个人资料
 * @author Yangxp
 * @date 2012-08-01
 */
@Controller
@RequestMapping("/people/{domain}/admin/personal/edit")
public class EditPersonalInfoController {
	public static void main(String[] args) throws Exception {
		System.out.println(URLDecoder.decode("%E9%83%91%E4%BE%9D%E5%8D%8E","UTF-8"));
	}
	@Autowired
	private IUserService userService;
	@Autowired
	private IWorkService workService;
	@Autowired
	private IEducationService eduService;
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IInterestService interestService;
	@Autowired
	private IFavoriteUrlService favoriteService;
	@Autowired
	private IMessageBoardService msgBoard;
	@Autowired
	IInstitutionVmtService institutionVmtService;
	
	private static final String FUNC_SAVE="func=save";
	private static final String FUNC_DELETE="func=delete";
	
	/**
	 * 默认跳转到显示个人资料的页面
	 * @param request
	 * @return
	 */
	@RequestMapping
	public ModelAndView display(HttpServletRequest request){
		SimpleUser su = (SimpleUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		String domain = homeService.getDomain(su.getId());
		String url = UrlUtil.getRootURL(request)+"/people/"+domain+"/admin/personal/baseinfo";
		ModelAndView mv = new ModelAndView(new RedirectView(url));
		mv.addObject("titleUser", su.getZhName());
		return mv;
	}
	/**
	 * 保存个人基本信息
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/baseinfo", params=FUNC_SAVE)
	public ModelAndView saveBaseInfo(HttpServletRequest request){
		SimpleUser su = getUpdateSimpleUser(request);
		userService.updateSimpleUserByUid(su);
		updateInterest(su.getId(), request);
		userService.updateDetailedUserByUid(getUpdateDetailedUser(request));
		msgBoard.updateUserInfo(su.getId(), 0, su.getZhName(), null);
		//设置用户所属组织ID
		su.setInstitutionId(institutionVmtService.getInstituionId(su.getId()));
		SessionUtils.updateUser(request, su);
		return redirect2ShowPersonalPage(request, "baseinfo");
	}
	/**
	 * 保存工作经历
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/work", params=FUNC_SAVE)
	public ModelAndView saveWork(HttpServletRequest request){
		Work work = getWorkFromRequest(request);
		if(work.getId()>0){
			workService.updateWorkById(work);
		}else{
			workService.createWork(work);
		}
		checkAndUpdateSalutation(request);
		return redirect2ShowPersonalPage(request, "work");
	}
	/**
	 * 保存社交网络
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/favorite", params=FUNC_SAVE)
	public ModelAndView saveFavorite(
			HttpServletRequest request,
			@RequestParam("oper")String oper,
			@RequestParam("title")String title,
			@RequestParam("url")String url,
			@RequestParam("selectMedia")String selectMedia
			){
		FavoriteUrl fav=buildFavoriteUrl(title,url,selectMedia);
		fav.setUid(SessionUtils.getUserId(request));
		if("add".equals(oper)){
			favoriteService.addFavorite(fav);
		}else if("edit".equals(oper)){
			fav.setId(Integer.valueOf(CommonUtils.trim(request.getParameter("favId"))));
			favoriteService.updateFavorite(fav);
		}
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		return redirect2ShowPersonalPage(request, "favorite");
	}
	private FavoriteUrl buildFavoriteUrl(String title,String url,String selectMedia){
		FavoriteUrl fav=new FavoriteUrl();
		fav.setTitle(title.trim());
		fav.setUrl(url.trim());
		fav.setSelectMedia(selectMedia);
		return fav;
	}
	/**
	 * 删除社交网络
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/favorite", params=FUNC_DELETE)
	public ModelAndView deleteFavorite(HttpServletRequest request,@RequestParam("delFavId") int id){
		favoriteService.deleteFavorite(id);
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		return redirect2ShowPersonalPage(request, "favorite");
	}
	/**
	 * 删除工作经历
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/work", params=FUNC_DELETE)
	public ModelAndView deleteWork(HttpServletRequest request){
		String idStr = CommonUtils.trim(request.getParameter("id"));
		if(!CommonUtils.isNull(idStr)){
			int id = Integer.valueOf(idStr);
			workService.delteWork(id);
		}
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		return redirect2ShowPersonalPage(request, "work");
	}
	/**
	 * 保存教育背景
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/education", params=FUNC_SAVE)
	public ModelAndView saveEducation(HttpServletRequest request){
		Education edu = getEducationFromRequest(request);
		if(edu.getId()>0){
			eduService.updateEducationById(edu);
		}else{
			eduService.createEducation(edu);
		}
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		return redirect2ShowPersonalPage(request, "education");
	}
	/**
	 * 删除教育背景
	 * @param request
	 * @return
	 */
	@RequestMapping(value="/education", params=FUNC_DELETE)
	public ModelAndView deleteEducation(HttpServletRequest request){
		String idStr =CommonUtils.trim( request.getParameter("id"));
		if(!CommonUtils.isNull(idStr)){
			int id = Integer.valueOf(idStr);
			eduService.deleteEducation(id);
		}
		userService.updateSimpleUserLastEditTimeByUid(SessionUtils.getUserId(request));
		return redirect2ShowPersonalPage(request, "education");
	}
	
	private ModelAndView redirect2ShowPersonalPage(HttpServletRequest request, String target){
		SimpleUser su = SessionUtils.getUser(request);
		String domain = homeService.getDomain(su.getId());
		String url = "/people/"+domain+"/admin/personal/"+target;
		ModelAndView mv = new ModelAndView(new RedirectView(url));
		mv.addObject("titleUser", su.getZhName());
		return mv;
	}
	
	private SimpleUser getUpdateSimpleUser(HttpServletRequest request){
		SimpleUser su = userService.getSimpleUserByUid(SessionUtils.getUserId(request));
		String zhName =CommonUtils.trim( request.getParameter("zhName"));
		String enName =CommonUtils.trim( request.getParameter("enName"));
		String salutation=CommonUtils.trim(request.getParameter("salutation"));
		su.setZhName(zhName);
		su.setEnName(enName);
		su.setSalutation(salutation);
		su.setPinyin(PinyinUtil.getPinyin(zhName));
		return su;
	}
	
	private void updateInterest(int uid, HttpServletRequest request){
		String interests = CommonUtils.trim(request.getParameter("interests"));
		interestService.deleteByUid(uid);
		if(!CommonUtils.isNull(interests)){
			String[] interestArray = InterestSplitHelper.getInterestSplit(interests);
			interestService.batchCreate(uid, Arrays.asList(interestArray));
		}
	}
	
	private DetailedUser getUpdateDetailedUser(HttpServletRequest request){
		SimpleUser su = (SimpleUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		String firstClassDiscipline = CommonUtils.trim(request.getParameter("firstClassDiscipline"));
		String secondClassDiscipline =CommonUtils.trim( request.getParameter("secondClassDiscipline"));
		String introduction = CommonUtils.trim(request.getParameter("introduction"));
		DetailedUser du = userService.getDetailedUser(su.getId());
		if(!CommonUtils.isNull(firstClassDiscipline)){
			du.setFirstClassDiscipline(Integer.valueOf(firstClassDiscipline));
		}
		if(!CommonUtils.isNull(secondClassDiscipline)){
			du.setSecondClassDiscipline(Integer.valueOf(secondClassDiscipline));
		}
		du.setIntroduction(introduction);
		return du;
	}
	
	private Work getWorkFromRequest(HttpServletRequest request){
		int id=0;
		String idStr = CommonUtils.trim(request.getParameter("id"));
		if(null != idStr && !"".equals(idStr)){
			id = Integer.valueOf(idStr);
		}
		String insZhName =CommonUtils.trim( request.getParameter("institutionZhName"));
		int insId = checkInstitutionId(id, insZhName, InstitutionName.WORK);
		String department = CommonUtils.trim(request.getParameter("department"));
		String position =CommonUtils.trim( request.getParameter("position"));
		String beginTime =CommonUtils.trim( request.getParameter("beginTime"));
		String endTimeStr = CommonUtils.trim(request.getParameter("endTime"));
		java.util.Date endTime = (CommonUtils.isNull(endTimeStr))?DateUtils.getMaxDate():Date.valueOf(endTimeStr);
		SimpleUser su = (SimpleUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		
		Work work = new Work();
		work.setId(id);
		work.setInstitutionId(insId);
		work.setDepartment(department);
		work.setPosition(position);
		work.setBeginTime(Date.valueOf(beginTime));
		work.setEndTime(endTime);
		work.setUid(su.getId());
		work.setAliasInstitutionName(insZhName);
		return work;
	}
	
	private void checkAndUpdateSalutation(HttpServletRequest request){
		String isSalutation = CommonUtils.trim(request.getParameter("salutation"));
		String salutation =CommonUtils.trim( request.getParameter("position"));
		SimpleUser su = SessionUtils.getUser(request);
		SimpleUser dbUser = userService.getSimpleUserByUid(su.getId());
		if(null!=isSalutation){
			dbUser.setSalutation(salutation);
		}else{
			String curSal = su.getSalutation();
			if(null!=curSal && curSal.equals(salutation)){
				dbUser.setSalutation("");
			}
		}
		userService.updateSimpleUserByUid(dbUser);
		//设置用户所属组织ID
		dbUser.setInstitutionId(institutionVmtService.getInstituionId(dbUser.getId()));
		SessionUtils.updateUser(request, dbUser);
	}
	
	private Education getEducationFromRequest(HttpServletRequest request){
		int id = 0;
		String idStr = CommonUtils.trim(request.getParameter("id"));
		if(null != idStr && !"".equals(idStr)){
			id = Integer.valueOf(idStr);
		}
		String degree = CommonUtils.trim(request.getParameter("degree"));
		String department = CommonUtils.trim(request.getParameter("department"));
		String insZhName = CommonUtils.trim(request.getParameter("institutionZhName").trim());
		int insId = checkInstitutionId(id, insZhName, InstitutionName.EDUCATION);
		String beginTime = CommonUtils.trim(request.getParameter("beginTime"));
		String endTimeStr = CommonUtils.trim(request.getParameter("endTime"));
		java.util.Date endTime = (CommonUtils.isNull(endTimeStr))?DateUtils.getMaxDate():Date.valueOf(endTimeStr);
		SimpleUser su = (SimpleUser)request.getSession().getAttribute(Constants.CURRENT_USER);
		
		Education edu = new Education();
		edu.setId(id);
		edu.setDegree(degree);
		edu.setInsitutionId(insId);
		edu.setDepartment(department);
		edu.setBeginTime(Date.valueOf(beginTime));
		edu.setEndTime(endTime);
		edu.setUid(su.getId());
		edu.setAliasInstitutionName(insZhName);
		return edu;
	}
	
	private int checkInstitutionId(int id, String insZhName, String type){
		int insId = 0;
		if(id>0){//检查已存在的记录
			if(InstitutionName.EDUCATION.equals(type)){
				Education edu = eduService.getEducation(id);
				insId = (insZhName.equals(edu.getAliasInstitutionName()))?
						edu.getInsitutionId():0;
			}else{
				Work work = workService.getWork(id);
				insId = (insZhName.equals(work.getAliasInstitutionName()))?
						work.getInstitutionId():0;
			}
		}
		if(insId==0){//没有关联官方机构，则自动搜索可能的官方机构
			insId = workService.searchOfficalInstitution(insZhName);
		}
		return insId;
	}
}
