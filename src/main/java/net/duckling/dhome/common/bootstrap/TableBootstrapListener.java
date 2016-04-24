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
package net.duckling.dhome.common.bootstrap;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * @title: TableBootstrapListener.java
 * @package net.duckling.dhome.common.bootstrap
 * @description: TODO 类说明
 * @author clive
 * @date 2012-9-13 下午2:55:37
 */
public class TableBootstrapListener implements ServletContextListener {

    @Override
    public void contextDestroyed(ServletContextEvent event) {
    }
    
    @Override
    public void contextInitialized(ServletContextEvent event) {
        WebApplicationContext factory = getWebApplicationContext(event);
        BootstrapService bootstrapService = factory.getBean(BootstrapService.class);
        bootstrapService.bootstrap();
    }
    
    private WebApplicationContext getWebApplicationContext(ServletContextEvent event){
        ServletContext servletContext = event.getServletContext();
        return WebApplicationContextUtils.getWebApplicationContext(servletContext);
    }


}
