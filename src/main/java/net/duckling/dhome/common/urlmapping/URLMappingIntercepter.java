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
package net.duckling.dhome.common.urlmapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.util.UrlMappingUtil;
import net.duckling.dhome.common.util.UrlUtil;
import net.duckling.dhome.service.IUrlMappingCacheService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.view.RedirectView;

public class URLMappingIntercepter extends HandlerInterceptorAdapter {

    @Autowired
    private IUrlMappingCacheService urlCacheService;
    @Autowired
    private AppConfig appConfig;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView mv) {
        if (mv != null && mv.getView() instanceof RedirectView) {
            String modelUrl = ((RedirectView) mv.getView()).getUrl();
            String rootUrl = UrlUtil.getRootURL(request).replace(request.getContextPath(), "");
            if (!isServerAddress(modelUrl)) {
                String result = "";
                if (modelUrl.contains(rootUrl)) {
                    result = rootUrl;
                }
                modelUrl = modelUrl.replace(rootUrl, "");
                result += UrlMappingUtil.removePeople(modelUrl, request,
                        urlCacheService.getUrlMapping(UrlUtil.getAddress(request)));
                mv.setView(new RedirectView(result));
            }
        }
    }

    private boolean isServerAddress(String str) {
        return str.startsWith(appConfig.getUmtBaseURL());
    }

}
