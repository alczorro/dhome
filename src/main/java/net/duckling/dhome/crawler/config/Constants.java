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
package net.duckling.dhome.crawler.config;




public class Constants {

public final static String HOST_URL = "http://apps.isiknowledge.com";
public final static String ISI_ADVANCED_URL =  "http://apps.isiknowledge.com/WOS_AdvancedSearch.do";

public final static String PAGE_URL_SUFFIX = "&formValue(summary_mode)=AdvancedSearch&page=";


public final static String CCR_NAME = "Current Chemical Reactions (CCR-EXPANDED)";
public final static String IC_NAME= "Index Chemicus (IC)";
public final static String ISTP_NAME = "Conference Proceedings Citation Index - Science (CPCI-S)";
public final static String SCI_NAME = "Science Citation Index Expanded (SCI-EXPANDED)";
public final static String ACTION = "search";
public final static String EDITIONS1 = "SCI";
public final static String EDITIONS2 = "ISCP";
public final static String EDITIONS3 = "IC";
public final static String EDITIONS4 = "CCR";

public final static String ENDYEAR = "2012";
public final static String INPUT_INVALID_NOTICE= "wrong";
public final static String INPUT_INVALID_NOTICE_LIMITS = "notice";
public final static String LIMIT_STATUS =  "expanded";
public final static String PERIOD = "Year Range";
public final static String PRODUCT = "WOS";
public final static String RNAGE = "ALL";
public final static String SEARCH_MODE = "AdvancedSearch";
public final static String START_YEAR = "1900";
public final static String LIMIT_COUNT = "13";
public final static String SELECT2 = "LA";
public final static String SELECT3 = "DT";
public final static String X = "29";
public final static String Y ="9";

/** Parsing succeeded. */
public static final String SUCCESS         = "success";
/** General failure. There may be a more specific error message in arguments. */
public static final String FAILED          = "failed";

}
