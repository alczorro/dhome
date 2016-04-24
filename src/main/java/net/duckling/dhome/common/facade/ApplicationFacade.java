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
package net.duckling.dhome.common.facade;

import javax.servlet.ServletContext;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

public final class ApplicationFacade {

    private ApplicationFacade() {
    }

    private static final String APPSERVLET = "org.springframework.web.servlet.FrameworkServlet.CONTEXT.appServlet";

    private static final WebApplicationContext APP_CONTEXT = ContextLoader.getCurrentWebApplicationContext();

    public static Object getAnnotationBean(Class clazz) {
        ServletContext ctx = APP_CONTEXT.getServletContext();
        ApplicationContext application = (ApplicationContext) ctx.getAttribute(APPSERVLET);
        if (application != null) {
            return application.getBean(clazz);
        }
        return null;
    }

    public static Object getRootContextBean(Class clazz) {
        return APP_CONTEXT.getBean(clazz);
    }
    

}
