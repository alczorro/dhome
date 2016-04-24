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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.auth.sso.Constants;
import net.duckling.dhome.common.auth.sso.IAuthenticationService;
import net.duckling.dhome.common.dsn.PaperJSONHelper;
import net.duckling.dhome.common.exception.EmailExistedException;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.ImageUtils;
import net.duckling.dhome.common.util.JSONHelper;
import net.duckling.dhome.common.util.PinyinUtil;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.common.validate.Validator;
import net.duckling.dhome.common.validate.annotation.DhomeValidate;
import net.duckling.dhome.common.validate.domain.ValidateResult;
import net.duckling.dhome.domain.institution.Institution;
import net.duckling.dhome.domain.people.DetailedUser;
import net.duckling.dhome.domain.people.Discipline;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.Paper;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.domain.people.Theme;
import net.duckling.dhome.service.IDsnSearchService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IInterestService;
import net.duckling.dhome.service.IPaperService;
import net.duckling.dhome.service.IRegistService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.IWorkService;
import net.duckling.dhome.web.helper.InterestSplitHelper;

import org.json.simple.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 
 * 注册controller，包括后续步骤
 * 
 * @author lvly
 * @since 2012-08-08
 * 
 */
@Controller
@RequestMapping("/system/regist")
public class QuickRegistController {
	private static final String DOMAIN = "domain";
	private static final String EMAIL_TO_REGIST = "emailToRegist";

	@Autowired
	private IRegistService registService;
	@Autowired
	private IAuthenticationService authenticationService;
	@Autowired
	private IDsnSearchService dsnSearchService;
	@Autowired
	private IPaperService paperService;
	@Autowired
	private IWorkService workService;
	@Autowired
	private IInterestService interestService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IInstitutionVmtService institutionVmtService;
	@Autowired
	private IInstitutionBackendPaperService BackendPaperService;

	
	@RequestMapping(params = "func=isEmailUsed")
	@ResponseBody
	/** 从主页点击创建，验证邮箱是否可用,返回的结果是"可用吗？" */
	public  boolean isEmailUsed(HttpServletRequest request) {
		String email = CommonUtils.killNull(request.getParameter("email"));
		boolean flag = registService.isEmailUsed(email.toLowerCase())||userService.isUmtUser(email.toLowerCase());
		if(checkUMTExist(request)){
			return true;
		}
		return !flag;
	}
	/**用户须知页面*/
	@RequestMapping(value="/protocal.html")
	public String userRead(){
		return "agreement";
	}

	/** 点击创建主页的时候，验证域名是否重复 */
	@RequestMapping(params = "func=isDomainUsed")
	@ResponseBody
	public  boolean isDomainUsed(HttpServletRequest request) {
		return !registService.isDomainUsed(CommonUtils.trim(request.getParameter(DOMAIN)));
	}

	

	/** 第一步，验证邮箱可用已经完成 */
	@RequestMapping(params = "func=stepOne")
	public ModelAndView stepOne(HttpServletRequest request) {
		String email = CommonUtils.trim(request.getParameter(EMAIL_TO_REGIST));
		String zhName =CommonUtils.trim(request.getParameter("zhName"));

		ModelAndView mv = new ModelAndView();
		mv.addObject(EMAIL_TO_REGIST, email);
		mv.addObject("zhName", zhName);
		// TEST
		mv.addObject("pageName", "个人研究主页创建");
		// 默认值，默认是拼音,用户
		if (!CommonUtils.isNull(zhName)) {
			String enName = PinyinUtil.getPinyinMingXing(zhName);
			enName = (null==enName || "".equals(enName.trim()))?zhName:enName;
			mv.addObject("enName", upperFirstChar(enName));
			mv.addObject("defaultDomain", PinyinUtil.getPinyinOnly(zhName).replaceAll(" ", ""));
		}
		mv.addObject("rootDisciplines",registService.getRootDiscipline());
		mv.setViewName("createIndex");
		return mv;
	}
	
	@RequestMapping(params = "func=stepTwoLoadInstituion")
	@ResponseBody
	/**第二步,数据准备*/
	public  List<Institution> stepTwoLoadInstituion(@RequestParam(value = "prefixName") String prefixName,
			HttpServletRequest request) {
		if(CommonUtils.isNull(prefixName)){
			return new ArrayList<Institution>();
		}
		return workService.getDsnInstitutionsByPrefixName(prefixName.trim());
	}

	
	@RequestMapping(params = "func=stepTwo")
	@DhomeValidate(validator=Validator.CREATE_INDEX)
	/** 第二步，创建用户和主页 */
	public ModelAndView stepTwo(HttpServletRequest request) {
		if (!checkUMTExist(request) && !isSamePassword(request) ) {
			return passwordError();
		}
		// create user
		SimpleUser user = null;
		try {
			user = createUserAndHome(request);
			if(user==null){
				return new ModelAndView(new RedirectView(UrlUtil.getRootURL(request)+"/system/login?umt=true&email="+CommonUtils.trim(request.getParameter(EMAIL_TO_REGIST))));
			}
		} catch (EmailExistedException e) {
			ValidateResult result = new ValidateResult();
			result.setKey(e.getMessage());
			ModelAndView errMv = new ModelAndView("/system/error/validate");
			errMv.addObject("result", result);
			return errMv;
		}
		
		createInterest(user.getId(), request);
		createUserGuide(user.getId());
		authenticationService.login(user.getEmail(), CommonUtils.trim(request.getParameter("password")));
		SessionUtils.updateUser(request, user);
		String domain = CommonUtils.trim(request.getParameter(DOMAIN));
		SessionUtils.setDomain(request, domain);
		return registSuccess(user, domain);
	}

	/** 第三步准备，研究单位页面 */
	@RequestMapping(params = "func=stepThreeWork")
	@NPermission(authenticated = true)
	public String stepThreeWork() {
		return "configCareer";
	}

	/** 第三步准备，我是学生页面 */
	@NPermission(authenticated = true)
	@RequestMapping(params = "func=stepThreeEdu")
	public String stepThreeEdu() {
		return "configEducation";
	}

	/** 第三步，存[我说学生/研究单位]数据，并跳到第四步 **/
	@NPermission(authenticated = true)// /system/regist?func=xxx
	@RequestMapping(params = "func=stepThree")
	public ModelAndView stepThree(HttpServletRequest request) {
		String preOper = CommonUtils.trim(request.getParameter("preOper"));
		String institution = CommonUtils.trim(request.getParameter("institution"));
		String department = CommonUtils.trim(request.getParameter("department"));
		if(!CommonUtils.isNull(institution)){
			institution=institution.trim();
		}
		SimpleUser user = SessionUtils.getUser(request);
		if(user==null){
			return new ModelAndView(new RedirectView("system/login"));
		}
		if ("education".equals(preOper)) {
			String degree = CommonUtils.trim(request.getParameter("degree"));
			registService.createEducation(user.getId(), degree, department, institution);
		} else if ("work".equals(preOper)) {
			String position = CommonUtils.trim(request.getParameter("position"));
			user.setSalutation(position);
			registService.createWork(user.getId(), position, department, institution);
		}
		user.setStep(SimpleUser.STEP_CONFIG_PAPER);
		updateUser(request,user);
		if(institutionVmtService.isMember(user.getEmail())){
			return new ModelAndView("configIAPPaper");
		}else{
			return new ModelAndView("configPaper");
		}
//		return new ModelAndView("configPaper");
	}

	/** 第四步，设置头像 */
	@NPermission(authenticated = true)
	@RequestMapping(params = "func=stepFour")
	public String stepFour(HttpServletRequest request) {
		SimpleUser su=SessionUtils.getUser(request);
		String paids = CommonUtils.trim(request.getParameter("paperIds"));
		if(institutionVmtService.isMember(su.getEmail())){
			String existCommonPapers = CommonUtils.trim(request.getParameter("commonPaperIds"));
			if(null != existCommonPapers && !"".equals(existCommonPapers)){
				JSONArray array = dsnSearchService.getDsnPapers(existCommonPapers);
				List<Paper> papers = PaperJSONHelper.jsonArray2PaperList(SessionUtils.getUserId(request), array);
//				paperService.batchCreate(papers);
				BackendPaperService.movePaper(papers, su.getId());
			}
			if(null != paids && !"".equals(paids)){
				String[] paperId = paids.split(",");
//				String[] authorId = existAuthors.split(",");
//				List<InstitutionPaper> list = paperService.getPaperByInit(offset, size, CommonUtils.trim(user.getZhName()));
//				paperService.insertAuthorUser(user.getId(), user.getInstitutionId(), authorId, -1);
				BackendPaperService.insertPaper(paperId, su.getId());
			}
			su.setIsMove(1);
		}else{
			if(null != paids && !"".equals(paids)){
				JSONArray array = dsnSearchService.getDsnPapers(paids);
				List<Paper> papers = PaperJSONHelper.jsonArray2PaperList(SessionUtils.getUserId(request), array);
				paperService.batchCreate(papers);
			}
			
		}
		su.setStep(SimpleUser.STEP_CONFIG_PHOTO);
		updateUser(request, su);
		return "configPhoto";
	}

	/** 裁剪图片提交 */
	@RequestMapping(params = "func=cutImg")
	public void cutImg(HttpServletRequest request, HttpServletResponse response) {
		String x = CommonUtils.trim(request.getParameter("x"));
		String y = CommonUtils.trim(request.getParameter("y"));
		String height = CommonUtils.trim(request.getParameter("height"));
		String width =CommonUtils.trim( request.getParameter("width"));
		String fileName =CommonUtils.trim( request.getParameter("fileName"));
		String path = request.getSession().getServletContext().getRealPath("");
		float scale = Float.valueOf(CommonUtils.trim(request.getParameter("scale")));
		path += ImageUtils.PATH;
		path += fileName;
		File file = ImageUtils.cutting(new File(path), CommonUtils.toInt(x), CommonUtils.toInt(y), CommonUtils.toInt(width),
				CommonUtils.toInt(height), scale);
		JSONHelper.writeJSONObject(response,
				request.getContextPath() + ImageUtils.PATH.replaceAll("\\\\", "/") + file.getName());
	}

	
	
	/**  如果umtExist为true，则该用户是UMT存在的，检查邮箱是否使用时需返回true **/
	private boolean checkUMTExist(HttpServletRequest request){
		String umtExist = CommonUtils.trim(request.getParameter("umtExist"));
		if(null != umtExist && "true".equals(umtExist)){
			return true;
		}
		return false;
	}

	/** 密码是否相同 */
	private boolean isSamePassword(HttpServletRequest request) {
		String password = CommonUtils.trim(request.getParameter("password"));
		String confirmPassword = CommonUtils.trim(request.getParameter("repeatPassword"));
		return password != null && password.equals(confirmPassword);
	}

	/** 用户创建成功 */
	private ModelAndView registSuccess(SimpleUser user, String domain) {
		ModelAndView mv = new ModelAndView();
		mv.addObject("uid", user.getId());
		mv.addObject("message", "账户创建成功");
		mv.addObject("pageName", "个人研究主页创建");
		mv.addObject(DOMAIN, domain);
		mv.setViewName("configCareer");
		return mv;
	}

	/** 两次密码输入错误 */
	private ModelAndView passwordError() {
		ModelAndView mv = new ModelAndView();
		mv.addObject("passWordMessage", "两次密码输入不一致");
		mv.setViewName("createIndex");
		return mv;
	}

	private String upperFirstChar(String enName){
		String[] temp = enName.split(" ");
		int size = temp.length;
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<size; i++){
			if(!"".equals(temp[i])){
				String subStr = temp[i].substring(0,1).toUpperCase()+temp[i].substring(1,temp[i].length());
				sb.append(subStr+" ");
			}
		}
		return sb.toString().trim();
	}

	

	/**
	 * 根据请求带来的数据，去持久化SimpleUser和DetailUser,home
	 * @throws EmailExistedException 
	 * */
	private SimpleUser createUserAndHome(HttpServletRequest request) throws EmailExistedException {
		String email = CommonUtils.trim(request.getParameter(EMAIL_TO_REGIST)).toLowerCase();
		String password = CommonUtils.trim(request.getParameter("password"));
		String zhName = CommonUtils.trim(request.getParameter("zhName"));
		String enName =CommonUtils.trim( request.getParameter("enName"));
		String umtExistStr =CommonUtils.trim( request.getParameter("umtExist"));
		
		boolean umtExist = (null == umtExistStr || "".equals(umtExistStr))?false:Boolean.valueOf(umtExistStr);
		
		SimpleUser user = userService.getSimpleUser(email);
		if(user==null){
			user = new SimpleUser();
			user.setEmail(email);
			user.setPinyin(PinyinUtil.getPinyin(zhName));
			user.setEnName(enName);
			user.setZhName(zhName);
			user.setStep(SimpleUser.STEP_CONFIG_CAREER);
			user.setId(registService.createSimpleUser(user, password, umtExist));
		}else { 
			//是否是初始化步骤
			if(user.getStep()==null){
				user.setStep(SimpleUser.STEP_CONFIG_CAREER);
				userService.updateSimpleUserByUid(user);
			}else{
				throw new EmailExistedException();
			}
		}
		
		if (user.getId() > 0) {
			SessionUtils.updateUser(request, user);
			// create Detail User
			createDetailedUser(user,request);
			
			//关联组织
			if(institutionVmtService.isMember(email)){
				institutionVmtService.updateUserId(email, user.getId());
			}
		}else{
			return null;
		}
		
		return user;
	}
	private void createDetailedUser(SimpleUser user,HttpServletRequest request){
		String firstClass = CommonUtils.trim(request.getParameter("firstClassDiscipline"));
		String secondClass = CommonUtils.trim(request.getParameter("secondClassDiscipline"));
		String introduction = CommonUtils.trim(request.getParameter("introduction"));
		DetailedUser dUser = new DetailedUser();
		String domain =CommonUtils.trim( request.getParameter(DOMAIN));
		if(!CommonUtils.isNull(firstClass)){
			dUser.setFirstClassDiscipline(Integer.valueOf(firstClass));
		}
		if(!CommonUtils.isNull(secondClass)){
			dUser.setSecondClassDiscipline(Integer.valueOf(secondClass));
		}
		dUser.setIntroduction(introduction);
		dUser.setUid(user.getId());
		registService.createDetailedUser(dUser);
		request.getSession().setAttribute(Constants.CURRENT_USER_DOMAIN, domain);
		Home home = new Home();
		home.setUrl(domain);
		home.setUid(user.getId());
		int defaultThemeId = Theme.DEFAULT_THEME;
		home.setThemeid(defaultThemeId);
		registService.createHome(home);
		SessionUtils.setDomain(request, domain);
	}
	
	
	private void createInterest(int uid, HttpServletRequest request){
		String interests = request.getParameter("interests");
		if(!CommonUtils.isNull(interests)){
			String[] interestArray = InterestSplitHelper.getInterestSplit(interests);
			interestService.batchCreate(uid, Arrays.asList(interestArray));
		}
	}
	
	private void createUserGuide(int uid){
		userService.create(uid, "adminIndex", 1);
	}
	private void updateUser(HttpServletRequest request,SimpleUser user){
		registService.updateSimpleUser(user);
		SessionUtils.updateUser(request, user);
	}
	/**加载二级级学科*/
	@RequestMapping(params = "func=getDisciplineChild")
	@ResponseBody
	public  List<Discipline> getSecondDiscipline(HttpServletRequest request,@RequestParam("parentId") int parentId){
		return registService.getChildDiscipline(parentId);
	}
}
