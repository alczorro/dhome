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
package net.duckling.dhome.common.dsn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 * 请求Dsn的Rest Service
 * 
 * @author zhaojuan
 * 
 */
public class DsnClient {
    private static final Logger LOG = Logger.getLogger(DsnClient.class);

    private String serverURL;
    private static final String DEFAULT_CHARSET = "utf-8";
    private static final String SEARCH_PAPER = "searchPaper.json";
    private static final String SEARCH_PAPERS = "searchPaperList.json";
    private static final String SEARCH_PAPERS_DETAIL = "searchPapersDetail.json";

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    /**
     * 构造函数
     * 
     * @param serverURL
     */
    public DsnClient(String serverURL) {
        this.serverURL = getAbsoluteURL(serverURL);
    }

    /**
     * 根据查询词得到简单的论文列表
     * 
     * @param query
     *            查询词
     * @param offest
     * @param pagesize
     * @param isBoolean
     *            是否是布尔查询
     * @return
     */
    public JSONArray getDsnPapers(String query, int offest, int pagesize, boolean isBoolQuery) {
        JSONParser parser = new JSONParser();
        String isBoolQueryParm = "false";
        JSONArray array = new JSONArray();
        if (isBoolQuery) {
            isBoolQueryParm = "true";
        }
        String responseBody = connectService(
                formParams(query, String.valueOf(offest), String.valueOf(pagesize), isBoolQueryParm),
                getServiceURL(SEARCH_PAPERS));
        if (responseBody != null && !responseBody.equals("")) {
            try {
                JSONObject obj = (JSONObject) parser.parse(responseBody);
                array = (JSONArray) obj.get("result");
            } catch (ParseException e) {
                LOG.error(e);
            }
        }

        return array;
    }

    /**
     * 根据许多论文的dsnPaperId"1,2,3,4"得到这些论文的详细信息
     * 
     * @param query
     *            查询词
     * @param offest
     * @param pagesize
     * @param isBoolean
     *            是否是布尔查询
     * @return
     */
    public JSONArray getDsnPapersDetail(String dsnPaerIds) {
        JSONParser parser = new JSONParser();
        String responseBody = connectService(formSimpleParams(dsnPaerIds), getServiceURL(SEARCH_PAPERS_DETAIL));
        JSONArray result = new JSONArray();
        if (responseBody != null && !responseBody.equals("")) {
            try {

                JSONObject obj = (JSONObject) parser.parse(responseBody);
                result = (JSONArray) obj.get("result");

            } catch (ParseException e) {
                LOG.error(e);
            }
        }
        return result;
    }

    /**
     * 根据dsnPaperId从DSN服务器获取Paper的详细信息
     * 
     * @param dsnPaperId
     * @return
     */
    public JSONObject getDsnPaper(int dsnPaperId) {
        JSONParser parser = new JSONParser();
        String responseBody = connectService(formSimpleParams(String.valueOf(dsnPaperId)), getServiceURL(SEARCH_PAPER));
        JSONObject result = new JSONObject();
        if (responseBody != null && !responseBody.equals("")) {
            try {
                JSONObject obj = (JSONObject) parser.parse(responseBody);
                result = (JSONObject) obj.get("result");

            } catch (ParseException e) {
                LOG.error(e);
            }
        }
        return result;
    }

    private String connectService(NameValuePair[] params, String serviceURL) {
        HttpMethodBase method = null;
        StringBuffer sbuffer = new StringBuffer();
        BufferedReader reader = null;
        InputStreamReader in = null;
        try {
            HttpClient client = new HttpClient();
            client.getParams().setCookiePolicy(CookiePolicy.BROWSER_COMPATIBILITY);
            client.getParams().setParameter(HttpMethodParams.SINGLE_COOKIE_HEADER, true);
            method = createPostMethod(serviceURL, params);
            int satuscode = client.executeMethod(method);
            if (satuscode != HttpStatus.SC_OK) {
                LOG.error("Unable to fetch page, http status code: " + satuscode);
            } else {
            	in = new InputStreamReader(method.getResponseBodyAsStream(),
                        method.getResponseCharSet()); 
                reader = new BufferedReader(in);
                for (String s = reader.readLine(); s != null; s = reader.readLine()) {
                    sbuffer.append(s);
                }
            }
        } catch (IOException e) {
            LOG.error(e);
        } finally {
        	closeStream(in, reader, method);
        }
        return sbuffer.toString();
    }
    
    private void closeStream(InputStreamReader in, BufferedReader reader, HttpMethodBase method){
    	if(in!=null){
	   		try {
	   			in.close();
	        } catch (IOException e) {
	            LOG.error(e);
	        }
    	}
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
                LOG.error(e);
            }
        }
        if (method != null) {
            method.releaseConnection();
        }
    }

    private PostMethod createPostMethod(String url, NameValuePair[] params) {
        PostMethod method = new PostMethod(url);
        method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, DEFAULT_CHARSET);
        if (params != null) {
            method.setRequestBody(params);
        }

        return method;
    }

    private NameValuePair[] formParams(String query, String offest, String pagesize, String isBoolQuery) {
        NameValuePair np1 = new NameValuePair("keyword", query);
        NameValuePair np2 = new NameValuePair("offset", offest);
        NameValuePair np3 = new NameValuePair("pagesize", pagesize);
        NameValuePair np4 = new NameValuePair("isBoolQuery", isBoolQuery);
        return new NameValuePair[] { np1, np2, np3, np4 };
    }

    private NameValuePair[] formSimpleParams(String dsnPaperId) {
        NameValuePair np1 = new NameValuePair("paperId", dsnPaperId);
        return new NameValuePair[] { np1 };
    }

    private String getAbsoluteURL(String serverURL) {
        if (!serverURL.startsWith("http://")) {
            return "http://" + serverURL;
        }
        return serverURL;

    }

    private String getServiceURL(String serviceName) {
        return serverURL + "/" + serviceName;
    }

    public static final String DSN_PAPER_ID = "dsnPaperId";
    public static final String TITLE = "title";
    public static final String AUTHORS = "authors";
    public static final String SOURCE = "source";
    public static final String PUBLISHEDTIME = "publishedTime";
    public static final String KEYWORDS = "keywords";

    public static final String PAPERURL = "paperURL";
    public static final String VOLUMEISSUE = "volumeIssue";
    public static final String LANGUAGE = "language";
    public static final String SUMMARY = "summary";
    public static final String TIMECITED = "timeCited";
    public static final String PAGES = "pages";

}
