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
package net.duckling.dhome.web.tag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.duckling.dhome.common.facade.ApplicationFacade;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.institution.InstitutionHome;
import net.duckling.dhome.service.IInstitutionBackendAcademicService;
import net.duckling.dhome.service.IInstitutionBackendAwardService;
import net.duckling.dhome.service.IInstitutionBackendCopyrightService;
import net.duckling.dhome.service.IInstitutionBackendPaperService;
import net.duckling.dhome.service.IInstitutionBackendPatentService;
import net.duckling.dhome.service.IInstitutionBackendPeriodicalService;
import net.duckling.dhome.service.IInstitutionBackendService;
import net.duckling.dhome.service.IInstitutionBackendTrainingService;
import net.duckling.dhome.service.IInstitutionBackendTreatiseService;
import net.duckling.dhome.service.IInstitutionBackendTopicService;
import net.duckling.dhome.service.IInstitutionHomeService;

import org.apache.log4j.Logger;

/**
 * encode工具类
 * @author lvly
 * @since 2012-9-28
 */
public class InstitutionLeftMenuCountTag extends TagSupport{
	private static final long serialVersionUID = 837324487623871L;
	private static final Logger LOG = Logger.getLogger(InstitutionLeftMenuCountTag.class);
	private String domain;
	
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	@Override
	public int doStartTag() throws JspException {
		init();
		InstitutionHome home=homeService.getInstitutionByDomain(domain);
		HttpServletRequest req=(HttpServletRequest)pageContext.getRequest();
		if(home==null){
			LOG.error("can't find the domain["+domain+"]");
			return SKIP_BODY;
		}
//		if(!backEndService.isAdmin(home.getInstitutionId(),SessionUtils.getUser(req).getEmail())){
//			LOG.error("is not admin!");
//			return SKIP_BODY;
//		}
		//成员数量
		pageContext.setAttribute("memberCount", backEndService.getVmtMemberCount(home.getInstitutionId()));
		//论文数量
		pageContext.setAttribute("paperCount",paperService.getPapersCount(home.getInstitutionId()));
		//课题数量
		pageContext.setAttribute("topicCount", topicService.getTopicCount(home.getInstitutionId()));
		//期刊任职数量
		pageContext.setAttribute("periodicalCount",periodicalService.getPeriodicalCount(home.getInstitutionId()));
		//学术任职数量
		pageContext.setAttribute("academicCount",academicService.getAcademicCount(home.getInstitutionId()));
		//论著数量
		pageContext.setAttribute("treatiseCount", treatiseService.getTreatiseCount(home.getInstitutionId()));
		//奖励数量
		pageContext.setAttribute("awardCount", awardService.getAwardCount(home.getInstitutionId()));
		//软件著作权数量
		pageContext.setAttribute("copyrightCount", copyrightService.getCopyrightCount(home.getInstitutionId()));
		//专利数量
		pageContext.setAttribute("patentCount", patentService.getPatentCount(home.getInstitutionId()));
		//人才管理数量
		pageContext.setAttribute("trainingCount", trainingService.getTrainingCount(home.getInstitutionId()));
		return SKIP_BODY;
	}
	
	private IInstitutionBackendTreatiseService treatiseService;
	
	private IInstitutionBackendService backEndService;
	
	private IInstitutionHomeService homeService;
	
	private IInstitutionBackendPaperService paperService;
	
	private IInstitutionBackendTopicService topicService;
	
	private IInstitutionBackendAwardService awardService;
	
	private IInstitutionBackendPeriodicalService periodicalService;
	
	private IInstitutionBackendAcademicService academicService;
	
	private IInstitutionBackendCopyrightService copyrightService;
	
	private IInstitutionBackendPatentService patentService;
	
	private IInstitutionBackendTrainingService trainingService;
	
	
	public void init(){
		if(backEndService==null){
			backEndService=(IInstitutionBackendService)ApplicationFacade.getAnnotationBean(IInstitutionBackendService.class);
		}
		if(homeService==null){
			homeService=(IInstitutionHomeService)ApplicationFacade.getAnnotationBean(IInstitutionHomeService.class);
		}
		if(paperService==null){
			paperService=(IInstitutionBackendPaperService)ApplicationFacade.getAnnotationBean(IInstitutionBackendPaperService.class);
		}
		if(topicService==null){
			topicService=(IInstitutionBackendTopicService)ApplicationFacade.getAnnotationBean(IInstitutionBackendTopicService.class);
		}
		
		if(treatiseService == null){
			treatiseService = (IInstitutionBackendTreatiseService) ApplicationFacade.getAnnotationBean(IInstitutionBackendTreatiseService.class);
		}
		
		if(periodicalService == null){
			periodicalService = (IInstitutionBackendPeriodicalService) ApplicationFacade.getAnnotationBean(IInstitutionBackendPeriodicalService.class);
		}
		
		if(awardService == null){
			awardService = (IInstitutionBackendAwardService) ApplicationFacade.getAnnotationBean(IInstitutionBackendAwardService.class);
		}
		
		if(academicService == null){
			academicService = (IInstitutionBackendAcademicService) ApplicationFacade.getAnnotationBean(IInstitutionBackendAcademicService.class);
		}
		if(copyrightService == null){
			copyrightService = (IInstitutionBackendCopyrightService) ApplicationFacade.getAnnotationBean(IInstitutionBackendCopyrightService.class);
		}
		if(patentService == null){
			patentService = (IInstitutionBackendPatentService) ApplicationFacade.getAnnotationBean(IInstitutionBackendPatentService.class);
		}
		if(trainingService==null){
			trainingService=(IInstitutionBackendTrainingService)ApplicationFacade.getAnnotationBean(IInstitutionBackendTrainingService.class);
		}
	}

}
