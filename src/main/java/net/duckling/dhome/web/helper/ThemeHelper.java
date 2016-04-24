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

import net.duckling.dhome.domain.people.Home;
import net.duckling.dhome.domain.people.Theme;
import net.duckling.dhome.service.IHomeService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2013-9-6
 */
@Service
public class ThemeHelper {
	@Autowired
	private IHomeService homeService;
	
	public String getTargetView(String domain,String jspName) {
		Home home = homeService.getHomeByDomain(domain);
		int themeid = home.getThemeid();
		switch(themeid){
			case Theme.THEME_M_TRADITION:return "theme_m_tradition_index/"+jspName;
			case Theme.THEME_M_NAMECRARD:return "theme_m_namecard_index/"+jspName;
			case Theme.THEME_M_TREND:return "theme_m_trend_index/"+jspName;
			case Theme.THEME_M_STEADY:return "theme_m_steady_index/"+jspName;
			case Theme.THEME_S_FASHION:return "theme_s_fashion_index/browseIndex";
			case Theme.THEME_S_SIMPLE:return "theme_s_simple_index/browseIndex";
			case Theme.THEME_M_IAP:return "theme_m_iap_index/"+jspName;
			case Theme.THEME_M_IAP_SCIENCE:return "theme_m_iap_science_index/"+jspName;
			default:return "theme_m_namecard_index/"+jspName;
		}
	}
	
	public boolean isOnePageTheme(String domain){
		Home home = homeService.getHomeByDomain(domain);
		int themeId = home.getThemeid();
		return (themeId==Theme.THEME_S_FASHION||themeId==Theme.THEME_S_SIMPLE);
	}
}
