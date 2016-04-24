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
package net.duckling.dhome.common.clb;

import java.io.InputStream;

import cn.vlabs.clb.api.document.MetaInfo;
import cn.vlabs.rest.IFileSaver;

/**
 * @group: net.duckling
 * @title: IClbClient.java
 * @description: TODO
 * @author clive
 * @date 2012-8-5 下午8:53:39
 */
public interface IClbClient {
	/**
	 * 创建文件
	 * @param filename	文件名
	 * @param length	文件的长度
	 * @param in		文件内容
	 * @return			新创建的文件的ID号，新创建的文件版本号为1。
	 */
	int createFile(String filename, long length, InputStream in);
	/**
	 * 更新文件
	 * @param docid		被更新的文件的ID号，由createFile返回
	 * @param filename	文件名
	 * @param length	文件长度
	 * @param in		文件内容
	 * @return			新的版本号。
	 */
	int updateFile(int docid, String filename, long length,	InputStream in);
	/**
	 * 下载文件内容
	 * @param docid		文件的ID号，由createFile返回。
	 * @param version	要下载的版本号，如果版本号为-1，则下载最新版本。
	 * @param fs		文件保存对象。
	 */
	void getContent(int docid, int version, IFileSaver fs);
	/**
	 * 初始化服务
	 * @param serverUrl clb服务URL
	 * @param clbUserName clb用户名
	 * @param clbPassword clb密码
	 * @param accessMode clb的文件获取方式 nginx/tomcat
	 */
	void init(String serverUrl, String clbUserName, String clbPassword,String clbVersion,String accessMode);
	/**
	 * 销毁该对象
	 */
	void destroy();
	/**
	 * 获取文件的元信息
	 * @param docid		文件的ID号
	 * @return			文件的元信息（文档ID，文档的最新版本，更新时间等）
	 */
	MetaInfo getMeta(int docid);
	/**
	 * 下载编号为docid,版本号为version的文档所对应的PDF文档
	 * @param docid 文件ID号
	 * @param version 版本号
	 * @param fs 文件保存对象
	 */
	void getPdfContent(int docid, int version, IFileSaver fs);
	/**
	 * 查询编号为docid,版本号为version的文档所对应的PDF文档状态
	 * @param docid 文件ID号
	 * @param version 版本号
	 * @return status 文档的PDF状态
	 */
	int queryPdfStatus(int docid, String version);
	/**
	 * 发送将编号为docid,版本号为version的文档转换成PDF文档的请求事件
	 * @param docid 文件ID号
	 * @param version 版本号
	 */
	void sendPdfTransformEvent(int docid, String version);
    String getDirectURL(int imgId);
    String getFileAccessMode();
}
