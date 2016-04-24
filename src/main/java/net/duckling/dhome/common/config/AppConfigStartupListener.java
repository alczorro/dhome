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
package net.duckling.dhome.common.config;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import net.duckling.dhome.common.email.EmailSendThread;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.log4j.Logger;

/**
 * @title: AppConfigStartupListener.java
 * @package net.duckling.dhome.common.config
 * @description: 系统启动时配置初始化类
 * @author clive
 * @date 2012-9-27 下午3:43:56
 */
public class AppConfigStartupListener implements ServletContextListener {

    private static final Logger LOG = Logger.getLogger(AppConfigStartupListener.class);
    private static final String REMOTE_FILE_URL = "http://10.10.1.104/falcon/download?fileName=appconfig.properties";
    private static final int BUFFER = 1024;
    
    /**邮件发送队列*/
    private EmailSendThread emailSendThread;

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    	if(emailSendThread!=null){
    		emailSendThread.shutdown();
    	}
    }

    @Override
    public void contextInitialized(ServletContextEvent event) {
        String realPath = event.getServletContext().getRealPath("/");
        String configPath = realPath + "WEB-INF/classes/conf/appconfig.properties";
        File f = new File(configPath);
        if (!f.exists()) {
            updateProperties(configPath);
        }
        sendEmailThreadStart();
    }
    /**
     * 邮件发送队列启动
     * */
    private void sendEmailThreadStart(){
    	 emailSendThread=new EmailSendThread();
         emailSendThread.start();
    }

    private void updateProperties(String configFilePath) {
        HttpClient client = new HttpClient();
        GetMethod httpGet = new GetMethod(REMOTE_FILE_URL);
        InputStream in = null;
        FileOutputStream out = null;
        try {
            client.executeMethod(httpGet);
            in = httpGet.getResponseBodyAsStream();
            out = new FileOutputStream(new File(configFilePath));
            byte[] b = new byte[BUFFER];
            int len = 0;
            while ((len = in.read(b)) != -1) {
                out.write(b, 0, len);
            }
        } catch (HttpException e) {
            LOG.error(e.getMessage(), e);
            httpGet.releaseConnection();
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        } finally {
            closeInputStream(in);
            closeOutputStream(out);
            httpGet.releaseConnection();
        }
    }
    
    private void closeInputStream(InputStream in){
        try {
            if (in != null) {
                in.close();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
    
    private void closeOutputStream(OutputStream out){
        try {
            if (out != null) {
                out.close();
            }
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
