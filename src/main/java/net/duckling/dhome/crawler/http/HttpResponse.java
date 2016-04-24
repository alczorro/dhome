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



import java.io.ByteArrayInputStream;
import java.io.InputStream;




import net.duckling.dhome.crawler.io.Writeable;
import net.duckling.dhome.crawler.metadata.Metadata;

import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpMethodBase;


public class HttpResponse {
	
	private byte[] body;
	private String charset;
	private Header[] heads;
	private Cookie[] cookies;
	private Metadata headers = new Metadata();
    int statusCode;
    
	public HttpResponse(Cookie[] cookies,HttpMethodBase method,int statusCode,Writeable write) throws Exception {
		this.statusCode = statusCode;
		charset = method.getResponseCharSet();
		heads = method.getResponseHeaders();
		this.cookies =cookies;
		for (int i = 0; i < heads.length; i++) {
			headers.set(heads[i].getName(), heads[i]
					.getValue());
		}	
		if(write!=null){		
			InputStream in = method.getResponseBodyAsStream();	
			String contentType = headers.get(Metadata.CONTENT_TYPE);
			 boolean isStreamAvailable = false;	
			if(contentType!=null){
				if (contentType.contains("pdf")||contentType.contains("PDF")||contentType.contains("Pdf")){
					isStreamAvailable = true;						
				}
			}		
			 String contentLength= headers.get(Metadata.CONTENT_LENGTH);
			 int size = 0 ;
			 if(contentLength!=null){
				 size=Integer.parseInt(contentLength);
			 }
			 write.write(charset, in,isStreamAvailable,size);	
		    			
		}
		
		
	}
//	public void writeStream(Parser parser) throws Exception{
//		parser.setDocument(getBodyAsInputStream(),charset);		
//	}
//	public void writeStream(OutputStream out) throws IOException {
//		boolean isStreamAvailable = false;
//		String contentType = headers.get(Metadata.CONTENT_TYPE);
//		if (contentType.contains("pdf")||contentType.contains("PDF")||contentType.contains("Pdf")){
//			isStreamAvailable = true;		
//		}
//		if (!isStreamAvailable)
//			return;
//		try {
//			try {
//				byte[] buffer = new byte[4096]; // 4K
//				int length;
//				while ((length = getBodyAsInputStream().read(buffer)) != -1)
//					out.write(buffer, 0, length);
//			} finally {
//				out.close();
//				getBodyAsInputStream().close();
//				isStreamAvailable = false;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			throw new IOException(e.toString());
//
//		}
//	}


	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public Header[] getHeads() {
		return heads;
	}

	public void setHeads(Header[] heads) {
		this.heads = heads;
	}

	public Cookie[] getCookies() {
		return cookies;
	}

	public void setCookies(Cookie[] cookies) {
		this.cookies = cookies;
	}
	public InputStream getBodyAsInputStream() {
		return new ByteArrayInputStream(body);
	}
	public Metadata getHeaders() {
		return headers;
	}

	public void setHeaders(Metadata headers) {
		this.headers = headers;
	}

	public int getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	
}