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
package net.duckling.dhome.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.clb.Browser;
import net.duckling.dhome.common.clb.ClbClient;
import net.duckling.dhome.common.clb.FileSaverBridge;
import net.duckling.dhome.common.clb.IClbClient;
import net.duckling.dhome.common.config.AppConfig;
import net.duckling.dhome.common.util.ImageUtils;
import net.duckling.dhome.common.util.MemCacheKeyGenerator;
import net.duckling.dhome.dao.IResourceInfoDAO;
import net.duckling.dhome.domain.people.PageImg;
import net.duckling.falcon.api.cache.ICacheService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import cn.vlabs.rest.IFileSaver;

/**
 * @author lvly
 * @since 2012-8-14
 */
@Component
public class ClbFileService {
    private static final Logger LOG = Logger.getLogger(ClbFileService.class);
    @Autowired
    private AppConfig config;
    @Autowired
    private IResourceInfoDAO pageImgDAO;
    @Autowired
    private ICacheService cacheService;

    private static final String X_ACCEL_REDIRECT = "X-Accel-Redirect";
    private static final String TOMCAT_FILE_ACCESS_MODE = "tomcat";

    /**
     * 上传文件到clb
     * 
     * @param file
     *            需要上传的文件
     */
    public int createFile(String fileName, InputStream is) {
        IClbClient cc = getFileStorageInstance();
        try {
            return cc.createFile(fileName, is.available(), is);
        } catch (IOException e) {
            LOG.error("Unable upload attachment.", e);
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
                LOG.error(ignored);
            }
        }
        return -1;
    }

    /**
     * 新建文件
     * */
    public int createFile(String fileName, int length, InputStream is) {
        IClbClient cc = getFileStorageInstance();
        try {
            return cc.createFile(fileName, length, is);
        } finally {
            try {
                is.close();
            } catch (IOException ignored) {
                LOG.error(ignored);
            }
        }
    }

    /**
     * 上传文件到clb
     * 
     * @param file
     *            需要上传的文件
     */
    public int createFile(File file) {
        IClbClient cc = getFileStorageInstance();
        InputStream is = null;
        try {
            is = new FileInputStream(file);
            return cc.createFile(ImageUtils.getOrgPath(file.getName()), is.available(), is);
        } catch (IOException e) {
            LOG.error("Unable upload attachment.", e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ignored) {
                LOG.error(ignored);
            }
        }
        return -1;
    }

    /**
     * 下载文件
     * 
     * @param clbId
     *            文件在CLB中的ID
     */
    public void fetchContent(int clbId, HttpServletRequest request, HttpServletResponse response) {
        IClbClient cc = getFileStorageInstance();
        if (TOMCAT_FILE_ACCESS_MODE.equals(cc.getFileAccessMode())) {
            IFileSaver saver = new FileSaverBridge(response, request);
            cc.getContent(clbId, -1, saver);
        } else {
            String agent = Browser.recognizeBrowser(request.getHeader("USER-AGENT")).toString().toLowerCase();
            String urlKey = MemCacheKeyGenerator.getFileClbId(clbId, agent);
            String directURL = (String) cacheService.get(urlKey);
            if (directURL == null) { //http://test.clb.cn/doc/ABCDE
                String url = cc.getDirectURL(clbId) + "?agent=" + agent;
                directURL = url.replace("http://", "/" + config.getFileProxyGateway() + "/");
                cacheService.set(urlKey, directURL);
            }
            response.addHeader(X_ACCEL_REDIRECT, directURL);
        }
    }

    /**
     * 获得clb 通讯实例
     * 
     * **/
    public IClbClient getFileStorageInstance() {
        String clbServiceURL = config.getClbService();
        String clbUserName = config.getClbUser();
        String clbPassword = config.getClbPassword();
        String clbVersion = config.getClbVersion();
        IClbClient client = new ClbClient();
        client.init(clbServiceURL, clbUserName, clbPassword, clbVersion, config.getFileAccessMode());
        return client;
    }

    /**
     * 存储pageImg或者fileUpload信息
     * 
     * @param uid
     * @param clbId
     * @return generateId
     * */
    public int createResourceInfo(int uid, int clbId) {
        return pageImgDAO.createResourceInfo(uid, clbId);
    }

    /**
     * 获得pageImg信息
     * 
     * @param clbId
     * @return uid
     * */
    public int getUidFromResourceId(int clbId) {
        return pageImgDAO.getUidFromResourceId(clbId);
    }

    /**
     * 获得用户页面图片信息
     * 
     * @param uid
     * @return 返回用户用过的所有的图片
     * */
    public List<PageImg> getPageImgsByUid(int uid) {
        return pageImgDAO.getResourceInfosByUid(uid);
    }
}
