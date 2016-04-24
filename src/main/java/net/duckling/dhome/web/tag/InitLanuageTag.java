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
package net.duckling.dhome.web.tag;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import net.duckling.dhome.common.facade.ApplicationFacade;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.impl.HomeService;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

public class InitLanuageTag extends TagSupport {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(InitLanuageTag.class);

    private boolean useBrowserLanguage;

    public boolean isUseBrowserLanguage() {
        return useBrowserLanguage;
    }

    public void setUseBrowserLanguage(boolean useBrowserLanuage) {
        this.useBrowserLanguage = useBrowserLanuage;
    }

    public int doEndTag() throws JspException {
        return EVAL_PAGE;
    }

    public int doStartTag() throws JspException {
        IHomeService homeService = (HomeService) ApplicationFacade.getAnnotationBean(HomeService.class);
       
        if (homeService == null) {
            LOG.error("Can not get menu/home service from application context");
            return 0;
        }
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        String languageSession=SessionUtils.getLanguage(request);
        String targetDomain = (String) request.getAttribute("targetDomain");
        String language = null;
        if (targetDomain != null && !isUseBrowserLanguage()) {
            Home home = homeService.getHomeByDomain(targetDomain);
            language = home.getLanguage();
        } else {
            language = request.getLocale().getLanguage();
            if(!CommonUtils.isNull(languageSession)){
            	language=languageSession;
            }
        }
        changeLocale(language);
        return SKIP_BODY;
    }

    private void changeLocale(String language) {
        HttpServletRequest request = (HttpServletRequest) pageContext.getRequest();
        HttpServletResponse response = (HttpServletResponse) pageContext.getResponse();
        SessionLocaleResolver localeResolver = (SessionLocaleResolver) ApplicationFacade
                .getAnnotationBean(SessionLocaleResolver.class);
        if ("zh_CN".equals(language) || "zh".equals(language)) {
            localeResolver.setLocale(request, response, Locale.CHINA);
        } else if ("en".equals(language) || "en_US".equals(language)) {
            localeResolver.setLocale(request, response, Locale.US);
        } else {
            localeResolver.setLocale(request, response, Locale.SIMPLIFIED_CHINESE);
        }
    }

}
