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

import java.util.Properties;

import javax.annotation.PostConstruct;

import net.duckling.falcon.api.mq.DFMQFactory;
import net.duckling.falcon.api.mq.DFMQMode;
import net.duckling.falcon.api.mq.IDFSubscriber;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
/**
 * 应用程序配置类
 * @author Yangxp
 *
 */
@Configuration
@PropertySource("classpath:conf/appconfig.properties")
public class AppConfig {
	@Value("${duckling.umt.login}")
	private String umtLoginURL;
	@Value("${duckling.umt.logout}")
	private String umtLogoutURL;
	@Value("${duckling.umt.theme}")
	private String theme;
	@Value("${duckling.umt.site}")
	private String umtBaseURL;
	@Value("${duckling.umt.service.url}")
	private String umtServerURL;
	
	@Value("${app.baseURL}")
	private String baseURL;
	@Value("${app.name}")
	private String appName;
	@Value("${c3p0.username}")
	private String dbUser;
	@Value("${c3p0.password}")
	private String dbPassword;
	@Value("${c3p0.url}")
	private String dbURL;
	@Value("${database}")
	private String dbName;
	@Value("${session.expired}")
	private int sessionExperied;
	
	@Value("${duckling.clb.service}")
	private String clbService;
	@Value("${duckling.clb.aone.user}")
	private String clbUser;
	@Value("${duckling.clb.aone.password}")
	private String clbPassword;
	@Value("${duckling.clb.version}")
	private String clbVersion;
	

    @Value("${duckling.dsn.url}")
	private String dsnServerURL;
	
	/***以下是邮件服务*/
	@Value("${email.mail.smtp.host}")
	private String stmpHost;
	@Value("${email.mail.smtp.auth}")
	private String stmpAuth;
	@Value("${email.mail.pop3.host}")
	private String pop3Auth;
	@Value("${email.username}")
	private String emailUserName;
	@Value("${email.password}")
	private String emailPassword;
	@Value("${email.fromAddress}")
	private String emailFromAddress;
	@Value("${email.admin.list}")
	private String emailAdminList;
	
	@Value("${dhome.memcached.url}")
	private String cacheURL;
	
	@Value("${dhome.maintain}")
	private boolean isMaintain;
	@Value("${dhome.file.access.mode}")
	private String fileAccessMode;
	@Value("${dhome.file.proxy.gateway}")
	private String fileProxyGateway;
	
	@Value("${oauth.umt.client_id}")
	private String oauthUmtClientId;
	@Value("${oauth.umt.client_secret}")
	private String oauthUmtClientSecret;
	@Value("${oauth.umt.redirect_uri}")
	private String oauthUmtRedirectURL;
	@Value("${oauth.umt.access_token_URL}")
	private String oauthUmtAccessTokenURL;
	@Value("${oauth.umt.authorize_URL}")
	private String oauthUmtAuthorizeURL;
	@Value("${oauth.umt.scope}")
	private String oauthUmtScope;
	@Value("${oauth.umt.theme}")
	private String oauthUmtTheme;
	
	@Value("${vmt.service.url}")
	private String vmtServiceUrl;
	
	@Value("${vmt.service.member}")
	private String umtServerMemberURL;
	

	
	
	public String getVmtServiceUrl() {
		return vmtServiceUrl;
	}
	public String getOauthUmtAuthorizeURL() {
		return oauthUmtAuthorizeURL;
	}
	public Properties getOauthUmtProp(){
		Properties prop=new Properties();
		prop.setProperty("client_id", getOauthUmtClientId());
		prop.setProperty("client_secret", getOauthUmtClientSecret());
		prop.setProperty("redirect_uri", getOauthUmtRedirectURL());
		prop.setProperty("access_token_URL",getOauthUmtAccessTokenURL());
		prop.setProperty("authorize_URL", getOauthUmtAuthorizeURL());
		prop.setProperty("scope",getOauthUmtScope());
		prop.setProperty("theme", getOauthUmtTheme());
		return prop;
		
	}
	

	public String getOauthUmtClientId() {
		return oauthUmtClientId;
	}

	public String getOauthUmtClientSecret() {
		return oauthUmtClientSecret;
	}

	public String getOauthUmtRedirectURL() {
		return oauthUmtRedirectURL;
	}

	public String getOauthUmtAccessTokenURL() {
		return oauthUmtAccessTokenURL;
	}

	public String getOauthUmtScope() {
		return oauthUmtScope;
	}

	public String getOauthUmtTheme() {
		return oauthUmtTheme;
	}

	public String getFileProxyGateway() {
        return fileProxyGateway;
    }

    public String getFileAccessMode() {
        return fileAccessMode;
    }

    public boolean isMaintain() {
		return isMaintain;
	}

	public String getCacheURL() {
        return cacheURL;
    }

    public String getClbVersion() {
        return clbVersion;
    }

    public void setCacheURL(String cacheURL) {
        this.cacheURL = cacheURL;
    }

    public String getEmailAdminList() {
		return emailAdminList;
	}

	public String getEmailFromAddress() {
		return emailFromAddress;
	}

	public String getEmailUserName() {
		return emailUserName;
	}

	public String getEmailPassword() {
		return emailPassword;
	}

	public String getPop3Auth() {
		return pop3Auth;
	}

	public String getStmpAuth() {
		return stmpAuth;
	}

	public String getStmpHost() {
		return stmpHost;
	}

	public String getClbService() {
		return clbService;
	}

	public String getClbUser() {
		return clbUser;
	}

	public String getClbPassword() {
		return clbPassword;
	}

	public String getDbName() {
		return dbName;
	}

	public String getDbURL() {
		return dbURL;
	}

	public String getDbUser() {
		return dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public String getUmtLogoutURL() {
		return umtLogoutURL;
	}

	public String getUmtLoginURL() {
		return umtLoginURL;
	}
	
	public String getUmtBaseURL(){
		return umtBaseURL;
	}
	
	public String getUmtServerURL(){
		return umtServerURL;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public String getAppName() {
		return appName;
	}

	public String getTheme() {
		return theme;
	}
	
	public int getSessionExperied(){
		return sessionExperied;
	}
	
	public String getDsnServerURL(){
		return dsnServerURL;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	public String getUmtServerMemberURL() {
		return umtServerMemberURL;
	}
	public void setUmtServerMemberURL(String umtServerMemberURL) {
		this.umtServerMemberURL = umtServerMemberURL;
	}
	
	
	

}
