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

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.duckling.dhome.common.auth.aop.annotation.NPermission;
import net.duckling.dhome.common.clb.FileNameSafeUtil;
import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.DefaultValuePageUtils;
import net.duckling.dhome.common.util.ImageUtils;
import net.duckling.dhome.common.util.JSONHelper;
import net.duckling.dhome.common.util.SessionUtils;
import net.duckling.dhome.domain.object.EmailSendLog;
import net.duckling.dhome.domain.people.Menu;
import net.duckling.dhome.domain.people.SimpleUser;
import net.duckling.dhome.service.IEmailSendLogService;
import net.duckling.dhome.service.IHomeService;
import net.duckling.dhome.service.IInstitutionVmtService;
import net.duckling.dhome.service.IMailService;
import net.duckling.dhome.service.IMenuService;
import net.duckling.dhome.service.IMessageBoardService;
import net.duckling.dhome.service.IRegistService;
import net.duckling.dhome.service.IUserService;
import net.duckling.dhome.service.impl.ClbFileService;

import org.apache.commons.compress.utils.IOUtils;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * 注册模块中关于图像处理的controller
 * @author lvly
 * @since 2012-11-2
 */
@Controller
@RequestMapping("/system/regist")
public class QuickRegistImageController {
	private static final Logger LOG=Logger.getLogger(QuickRegistImageController.class);
	@Autowired
	private IHomeService homeService;
	@Autowired
	private IEmailSendLogService emailSendLogService;
	@Autowired
	private IMailService emailService;
	@Autowired
	private ClbFileService imgService;
	@Autowired
	private IMenuService menuService;
	@Autowired
	private IRegistService registService;
	@Autowired
	private IUserService userService;
	@Autowired
	private IMessageBoardService boardService;
	@Autowired
	private IInstitutionVmtService institutionVmtService;
	
	/** 第五步，完成 */
	@NPermission(authenticated = true)
	@RequestMapping(params = "func=complete")
	public synchronized ModelAndView complete(HttpServletRequest request) throws IOException {
		SimpleUser su=userService.getSimpleUserByUid(SessionUtils.getUserId(request));
		LOG.info("user regist complete!"+su.getEmail());
		String domain = homeService.getDomain(su.getId());
		//这是注册
		String url = generateRedirectURL(request, su, domain);
		ModelAndView mv=new ModelAndView(new RedirectView(url));
		String isDefault=CommonUtils.trim(request.getParameter("isDefault"));
		/** 创建默认左菜单*/
		createLeftMenu(su,domain);
		if("true".equals(isDefault)){
			su.setStep(SimpleUser.STEP_COMPLETE);
			updateUser(request,su);
			return mv;
		}
		storeImageAndUpdateUser(request, su);
		boardService.updateUserInfo(su.getId(),su.getImage(),null,null);
		return mv;
	}
	private String generateRedirectURL(HttpServletRequest request, SimpleUser su, String domain){
		String url = "";
		if(!CommonUtils.isNull(request.getParameter("fromRegist"))){
			url=request.getContextPath() + "/people/"
					+ homeService.getDomain(SessionUtils.getUserId(request))+"?fromRegist=true";
			//发送邮件
			if(!emailSendLogService.isSended(su.getEmail(), EmailSendLog.TYPE_REGIST_TO_USER)){
				emailService.sendRegistMail(DefaultValuePageUtils.REGIST_EMAIL_TEMP, su, domain);
				emailService.sendRegistMailToAdmin(DefaultValuePageUtils.REGIST_ADMIN_EMAIL_TEMP, su, domain);
				emailSendLogService.writeLog(su.getEmail(), EmailSendLog.TYPE_REGIST_TO_USER, su.getId());
			}
		}
		//这是头像编辑
		else{
			url=request.getContextPath() + "/people/"
					+ homeService.getDomain(SessionUtils.getUserId(request))+"/admin/personal/photo?isSaved=true";
		}
		return url;
	}

	
	
	
	private void createLeftMenu(SimpleUser su, String domain){
		Menu menu = menuService.getMenu(su.getId());
		if (menu == null) {
			menuService.createDefautMenuForNewUser(su, domain);
		}
	}
	
	private void storeImageAndUpdateUser(HttpServletRequest request, SimpleUser su){
		String fileName = CommonUtils.trim(request.getParameter("fileName"));
		fileName=FileNameSafeUtil.makeSafe(fileName);
		String path = request.getSession().getServletContext().getRealPath("");
		path += ImageUtils.PATH;
		path += fileName;
		File file = new File(("true".equals(CommonUtils.trim(request.getParameter("isCut")))) ? ImageUtils.getCutPath(path) : path);
		file = ImageUtils.scale(file.getAbsolutePath());
		int id = imgService.createFile(file);
		if (id > 0) {
			su.setImage(id);
			su.setStep(SimpleUser.STEP_COMPLETE);
			updateUser(request,su);
			ImageUtils.delete(file);
		}  
	}
	private void updateUser(HttpServletRequest request,SimpleUser user){
		registService.updateSimpleUser(user);
		
		//设置用户所属组织ID
		user.setInstitutionId(institutionVmtService.getInstituionId(user.getId()));
		SessionUtils.updateUser(request, user);
	}
	/**
	 * IE上传图片
	 * */
	@RequestMapping(method = RequestMethod.POST, params = "func=uploadImage")
	public void uploadImage(@RequestParam("qqfile") MultipartFile uplFile, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String filename = uplFile.getOriginalFilename();
		createTempFile(request, response, filename, uplFile.getSize(), uplFile.getInputStream());
	}

	/**
	 * FileFox上传图片
	 */
	@RequestMapping(method = RequestMethod.POST, headers = { "X-File-Name" }, params = "func=uploadImage")
	public void uploadImage(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String filename = getFileNameFromHeader(request);
		createTempFile(request, response, filename, request.getContentLength(), request.getInputStream());
	}

	/**
	 * 用于从FileFox上传的文件中提取文件名
	 * */
	private String getFileNameFromHeader(HttpServletRequest request) {
		String filename = request.getHeader("X-File-Name");
		try {
			filename = URLDecoder.decode(filename, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			LOG.warn("Your system doesn't support utf-8 character encode. so sucks.");
		}
		return filename;
	}
	/** 上传到临时文件夹 */
	private void createTempFile(HttpServletRequest request, HttpServletResponse response, String filename, long size,
			InputStream is) throws IOException {
		String path = request.getSession().getServletContext().getRealPath("");
		long fileShortName = System.currentTimeMillis();
		String folder = path + ImageUtils.PATH;
		File file = new File(folder);
		if (!file.isDirectory()) {
			boolean isCreate=file.mkdirs();
			if(isCreate){
				LOG.info("create a temp directory");
			}
		}
		file = new File(folder + fileShortName + filename.substring(filename.lastIndexOf('.')));
		FileOutputStream fos = new FileOutputStream(file);
		IOUtils.copy(is, fos);
		is.close();
		fos.close();
		String imgUri=request.getContextPath() + ImageUtils.PATH.replaceAll("\\\\", "/");
		
		JSONObject object = new JSONObject();
		object.put("success", true);
		object.put("fileName", file.getName());
		object.put("size", size + "");
		object.put("imgPath",  imgUri+file.getName());
		// 获得原图的高和宽，以便缩放
		BufferedImage src = ImageIO.read(file);
		// 缩放比例 高:宽
		object.put("width", src.getWidth());
		object.put("height", src.getHeight());
		object.put("proportion", src.getHeight() / (float) src.getWidth());
		//默认裁剪
		boolean defaultCut=!CommonUtils.isNull(request.getParameter("defaultCut"));
		if(defaultCut){
			object.put("defaultCutImgPath", imgUri+ImageUtils.defaultCut(file).getName());
		}
		response.setContentType("application/json");
		response.setStatus(HttpServletResponse.SC_OK);
		JSONHelper.writeJSONObject(response, object);
	}
	
}
