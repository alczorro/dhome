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
package net.duckling.dhome.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.clb.FileNameSafeUtil;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.JSONHelper;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.people.PageImg;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.ClbFileService;
import net.duckling.dhome.web.helper.PageHelper;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * 
 * 上传下载图片的controller
 * 
 * @author lvly
 * @since 2012-8-14
 */
@Controller
@RequestMapping("/system")
public class UEditorController {
    @Autowired
    private IUserService userService;
    @Autowired
    private ClbFileService resourceService;
    private static final Logger LOG = Logger.getLogger(UEditorController.class);

    /** 获得图片 ,用于显示 */
    @RequestMapping("/img")
    public void getHeadImg(HttpServletRequest request, HttpServletResponse response) {
        if (CommonUtils.isNull(request.getParameter("imgId"))) {
            return;
        }
        int imgId = Integer.valueOf(CommonUtils.trim(request.getParameter("imgId")));
        if (!resourceCanRead(request, imgId)) {
            return;
        }
        resourceService.fetchContent(imgId, request, response);
    }

    /** 获取文件，用于下载 */
    @RequestMapping("/file")
    public void getFile(HttpServletRequest request, HttpServletResponse response) {
        if (CommonUtils.isNull(request.getParameter("fileId"))) {
            return;
        }
        int fileId = Integer.valueOf(CommonUtils.trim(request.getParameter("fileId")));
        if (!resourceCanRead(request, fileId)) {
            return;
        }
        
        resourceService.fetchContent(fileId, request, response);
    }

    /**
     * 上传图片，用于百度编辑器
     * 
     * @throws IOException
     */
    @RequestMapping(value = "/img", params = "func=uploadImg")
    public void uploadImg(@RequestParam("upfile") MultipartFile uplFile, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
    	String fileName=uplFile.getName();
    	fileName=FileNameSafeUtil.makeSafe(fileName);
        int clbId = resourceService.createFile(fileName, uplFile.getInputStream());
        resourceService.createResourceInfo(SessionUtils.getUserId(request), clbId);
        JSONObject object = new JSONObject();
        object.put("original", uplFile.getOriginalFilename());
        object.put("url", request.getContextPath() + "/system/img?imgId=" + clbId);
        object.put("title", "title");
        object.put("state", "SUCCESS");
        JSONHelper.writeJSONObject(response, object);
    }

    /** 管理图片，用于百度编辑器 */
    @RequestMapping(value = "/img", params = "func=manageImg")
    public void manageImg(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<PageImg> imgs = resourceService.getPageImgsByUid(SessionUtils.getUserId(request));
        String result = "";
        String seperate = "ue_separate_ue";
        if (imgs != null) {
            StringBuffer buffer = new StringBuffer("");
            for (PageImg page : imgs) {
                buffer.append(request.getContextPath());
                buffer.append("/system/img?imgId=");
                buffer.append(page.getClbId());
                buffer.append(seperate);
            }
            result = buffer.toString();
        }
        response.getWriter().print(CommonUtils.format(result, seperate));
    }

    /** 上传文件 */
    @RequestMapping(value = "/file", params = "func=uploadFile")
    public void uploadFile(@RequestParam("upfile") MultipartFile uplFile, HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        String fileName = uplFile.getOriginalFilename();
        fileName=FileNameSafeUtil.makeSafe(fileName);
        String fileType = fileName.substring(fileName.lastIndexOf('.'));
        int clbId = resourceService.createFile(fileName, uplFile.getInputStream());
        resourceService.createResourceInfo(SessionUtils.getUserId(request), clbId);
        JSONObject obj = new JSONObject();
        obj.put("url", request.getContextPath() + "/system/file?fileId=" + clbId);
        obj.put("fileType", fileType);
        obj.put("state", "SUCCESS");
        obj.put("original", fileName);
        JSONHelper.writeJSONObject(response, obj);
    }

    /**
     * 搜索视频
     * 
     * @throws UnsupportedEncodingException
     */
    @RequestMapping(value = "/video", params = "func=search")
    public void searchVideo(HttpServletRequest request, HttpServletResponse response)
            throws UnsupportedEncodingException {
        StringBuffer readOneLineBuff = new StringBuffer();
        String content = "";
        String searchkey = URLEncoder.encode(CommonUtils.trim(request.getParameter("searchKey")), "utf8");
        String videotype = CommonUtils.trim(request.getParameter("videoType"));
        BufferedReader reader = null;
        try {
            URL url = new URL("http://api.tudou.com/v3/gw?method=item.search&appKey=myKey&format=json&kw=" + searchkey
                    + "&pageNo=1&pageSize=20&channelId=" + videotype + "&inDays=7&media=v&sort=s");
            URLConnection conn = url.openConnection();
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String line = "";
            while ((line = reader.readLine()) != null) {
                readOneLineBuff.append(line);
            }
            content = readOneLineBuff.toString();
            reader.close();
            response.getWriter().print(content);

        } catch (IOException e) {
            LOG.error(e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOG.error(e);
                }
            }
        }

    }

    private boolean resourceCanRead(HttpServletRequest request, int resourceId) {
        SimpleUser user = userService.getSimpleUserByImgId(resourceId);
        if (user != null && guestCanNotRead(request, user)) {
            return false;
        }
        int uid = resourceService.getUidFromResourceId(resourceId);
        user = userService.getSimpleUserByUid(uid);
        if (user != null && guestCanNotRead(request, user)) {
            return false;
        }
        return true;
    }

    private boolean guestCanNotRead(HttpServletRequest request, SimpleUser user) {
        return !SessionUtils.isSameUser(request, user) && !PageHelper.guestCanRead(user)
                && !SessionUtils.isAdminUser(request);
    }
}
