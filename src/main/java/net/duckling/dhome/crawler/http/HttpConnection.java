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
package net.duckling.dhome.crawler.http;



import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;



import net.duckling.dhome.crawler.io.Writeable;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethodBase;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;



public class HttpConnection {
	public static final String DEFAULT_USER_AGENT = "Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US; rv:1.8.0.1) Gecko/20060111 Firefox/1.5.0.1";
	public static final String GET = "get";
	public static final String POST = "post";
	public static final String DEFAULT_CHARSET = "utf-8";
	public static final String FollowRedirect = "true";
	public static final String NotFollowRedirect = "false";
	public static final String ReferFollowRedirect = "refer";

	public HttpResponse executeGetWithoutContent(String url,
			NameValuePair[] params,String cookie,String refer, boolean isFollowRedirects) throws Exception {
		String isFollowRedirectsStr = isFollowRedirects==true?FollowRedirect:NotFollowRedirect;
		return this.execute(url, params, GET, cookie,"", isFollowRedirectsStr, null);
	}

	public HttpResponse executeGet(String url, NameValuePair[] params,String cookie,String refer,
			boolean isFollowRedirects, Writeable write) throws Exception {
		String isFollowRedirectsStr = isFollowRedirects==true?ReferFollowRedirect:NotFollowRedirect;
		return this.execute(url, params, GET, cookie,refer, isFollowRedirectsStr, write);
	}
	public HttpResponse executeGet(String url, NameValuePair[] params,String cookie,
			boolean isFollowRedirects, Writeable write,String referUrl) throws Exception {
		String isFollowRedirectsStr = isFollowRedirects==true?ReferFollowRedirect:NotFollowRedirect;
		return this.execute(url, params, GET,cookie,referUrl,  isFollowRedirectsStr, write);
	}
	public HttpResponse executeGet(String url, NameValuePair[] params,String cookie,
			String isFollowRedirects, Writeable write) throws Exception {		
		return this.execute(url, params, GET, cookie,"",  isFollowRedirects, write);
	}

	public HttpResponse executePost(String url, NameValuePair[] params,String cookie,
			Writeable write) throws Exception {
		return this.execute(url, params,POST,cookie,"",  FollowRedirect, write);
	}

	public HttpResponse executePostWithoutContent(String url,
			NameValuePair[] params,String cookie) throws Exception {
		return this.execute(url, params,POST,cookie,"",  FollowRedirect, null);
	}
	
    public HttpResponse executePostISI(String url, NameValuePair[] params,Writeable writeable,String referURL,String cookies)throws Exception {
    	HttpMethodBase method = null;
		try {
			HttpClient client = Http.getClient();
			method = createPostMethod(url, params);						
			method.setRequestHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
			method.setRequestHeader("Connection", "keep-alive");
			method.setRequestHeader("Host","apps.webofknowledge.com");
			method.setRequestHeader("Origin","http://apps.webofknowledge.com");
			method.setRequestHeader("Referer", referURL);
			method.setRequestHeader("Host","apps.webofknowledge.com");
			method.setRequestHeader("Accept-Encoding","gzip,deflate,sdch");
			method.setRequestHeader("Content-Type","application/x-www-form-urlencoded;charset=UTF-8");
			method.setRequestHeader("Cookie",cookies);
			method.setRequestHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.15) Gecko/2009101601 Firefox/3.0.15 (.NET CLR 3.5.30729");
			int statusCode = client.executeMethod(method);
			return new HttpResponse(client.getState().getCookies(), method,
					statusCode, writeable);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			method.releaseConnection();
		}
    }
	private HttpResponse execute(String url, NameValuePair[] params,
			String methodType,String cookie,String referURL,String isFollowRedirects, Writeable writeable)
			throws Exception {
		HttpMethodBase method = null;
		try {
			HttpClient client = Http.getClient();
//			client.getParams().setContentCharset("UTF-8");
//			client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			
			method = generateMethod(url, methodType, params, isFollowRedirects,"");
			method.setRequestHeader("Accept-Language", "zh-CN,zh;q=0.8,en;q=0.6");
//			method.setRequestHeader("Charset", "utf-8");
			method.setRequestHeader("Cookie", cookie);
//			method.setRequestHeader("Referer", referURL);
			method.setRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
			method.setRequestHeader("Connection", "keep-alive");
//			method.setRequestHeader("Host","apps.webofknowledge.com");
//			method.setRequestHeader("Origin","http://apps.webofknowledge.com");
			method.setRequestHeader("Host","apps.webofknowledge.com");
			method.setRequestHeader("Accept-Encoding","gzip,deflate");
			method.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
			method.setRequestHeader(
					"User-Agent",
					"Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.0.15) Gecko/2009101601 Firefox/3.0.15 (.NET CLR 3.5.30729");
			
			int statusCode = client.executeMethod(method);
			
//			byte [] responseBody = method.getResponseBody(); // 读取为字节数组   
//            String response =  new  String(responseBody,"utf-8");   
//            System.out.println( statusCode + "----------response:" +response);
            
			return new HttpResponse(client.getState().getCookies(), method,
					statusCode, writeable);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			method.releaseConnection();
		}

	}
	
	private HttpResponse execute(String url, NameValuePair[] params,
			String methodType, String isFollowRedirects, String refer,Writeable writeable)
			throws Exception {
		HttpMethodBase method = null;
		try {
			HttpClient client = Http.getClient();
			method = generateMethod(url, methodType, params, isFollowRedirects,refer);
			int statusCode = client.executeMethod(method);
			return new HttpResponse(client.getState().getCookies(), method,
					statusCode, writeable);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			method.releaseConnection();
		}

	}
	public void setHttpProxyCredentials(String username, String password) {
		HttpClient client = Http.getClient();
		client.getState().setProxyCredentials(
				new AuthScope(AuthScope.ANY_HOST, AuthScope.ANY_PORT),
				new UsernamePasswordCredentials(username, password));
	}

	private HttpMethodBase generateMethod(String url, String methodType,
			NameValuePair[] params, String isFollowRedirects,String referUrl) throws Exception {
		HttpMethodBase method;

		if (POST.equalsIgnoreCase(methodType)) {
			method = createPostMethod(url, params);
		} else {
			method = createGetMethod(url, params, DEFAULT_CHARSET,
					isFollowRedirects,referUrl);
		}
		method
				.setRequestHeader(
						"User-Agent",
						"Mozilla/5.0 (Windows; U; Windows NT 5.1; en-US) AppleWebKit/534.13 (KHTML, like Gecko) Chrome/9.0.597.84 Safari/534.13");
		return method;
	}

	private PostMethod createPostMethod(String url, NameValuePair[] params) {
		PostMethod method = new UTF8PostMethod(url);
		method.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET,"utf-8");
		if (params != null) {
			method.setRequestBody(params);
			method.setFollowRedirects(false);
		}

		return method;
	}
	
	public static class UTF8PostMethod extends PostMethod{     
	       public UTF8PostMethod(String url){     
	           super(url);     
	       }     
	       @Override    
	       public String getRequestCharSet() {     
	           return "utf-8";     
	       }     
	  }       

	private GetMethod createGetMethod(String url, NameValuePair[] params,
			String charset, String isFollowRedirects,String referer) throws Exception {
		if (params != null) {
			String urlParams = "";
			for (NameValuePair pair : params) {
				try {
					urlParams += pair.getName() + "="
							+ URLEncoder.encode(pair.getValue(), charset) + "&";
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					throw e;
				}
			}
			if (url.indexOf("?") < 0) {
				url += "?" + urlParams;
			} else if (url.endsWith("&")) {
				url += urlParams;
			} else {
				url += "&" + urlParams;
			}
		}

		GetMethod method = null;
		try {
			method = new GetMethod(url);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(isFollowRedirects!=null){
			if(isFollowRedirects.equals(FollowRedirect)){
				method.setFollowRedirects(true);
			}
			else{
				method.setFollowRedirects(false);
				if (isFollowRedirects.equals(ReferFollowRedirect)) {
					if(referer!=null&&!referer.equals("")){
						method.setRequestHeader("Referer", referer);
					}
					else{
						method.setRequestHeader("Referer", url);
					}
					
				}
			}
		}
		
		
		
		return method;
	}

}
