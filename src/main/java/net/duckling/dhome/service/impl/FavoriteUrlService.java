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
package net.duckling.dhome.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.duckling.dhome.common.util.CommonUtils;
import net.duckling.dhome.common.util.FavoriteImgUtils;
import net.duckling.dhome.dao.IFavoriteUrlDAO;
import net.duckling.dhome.domain.object.FavoriteUrl;
import net.duckling.dhome.service.IFavoriteUrlService;
import net.duckling.dhome.service.IKeyValueService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author lvly
 * @since 2012-12-21
 */
@Service
public class FavoriteUrlService implements IFavoriteUrlService {
	private static final Logger LOG = Logger.getLogger(FavoriteUrlService.class);
	@Autowired
	private IFavoriteUrlDAO favoriteDAO;
	@Autowired
	private ClbFileService clbService;
	@Autowired
	private IKeyValueService keyValueService;
	private static final String DEFAULT_ICO="favico/default.gif";
	private static final Map<String, String> STATIC_IMG = new HashMap<String, String>();
	static {
		STATIC_IMG.put("google.com", "favico/google.gif");
		STATIC_IMG.put("baidu.com", "favico/baidu.ico");
		STATIC_IMG.put("twitter.com","favico/twitter.gif");
		STATIC_IMG.put("facebook.com", "favico/facebook.gif");
		STATIC_IMG.put("linkedin.com", "favico/linked.in.ico");
		STATIC_IMG.put("qing.weibo.com", "favico/qing.weibo.com.gif");
		STATIC_IMG.put("profile.pengyou.com", "favico/pengyou.com.gif");
		STATIC_IMG.put("blog.sina.com.cn", "favico/blog.sina.com.cn.gif");
		STATIC_IMG.put("renren.com", "favico/renren.com.gif");
		STATIC_IMG.put("xiaonei.com", "favico/renren.com.gif");
		STATIC_IMG.put("douban.com", "favico/douban.com.gif");
		STATIC_IMG.put("user.qzone.qq.com", "favico/qzone.qq.com.gif");
		STATIC_IMG.put("t.qq.com", "favico/t.qq.com.gif");
		STATIC_IMG.put("weibo.com", "favico/weibo.com.gif");
		STATIC_IMG.put("163.com", "favico/blog.163.ico");
		STATIC_IMG.put("t.sohu.com", "favico/t.sohu.com.gif");
		STATIC_IMG.put("sohu.com", "favico/sohu.com.gif");
	}

	@Override
	public void addFavorite(FavoriteUrl fav) {
		fav.setImg(getImg(fav.getUrl()));
		fav.setUrl(FavoriteImgUtils.addSchema(fav.getUrl()));
		favoriteDAO.addFavoriteUrl(fav);
	}

	private String getImg(String url) {
		String baseUrl = FavoriteImgUtils.addSchema(FavoriteImgUtils.getBaseUrl(url));
		// 先看看是不是静态文件
		String imgUrl = getStaticImgUrl(baseUrl);
		String img="";
		if (!CommonUtils.isNull(imgUrl)) {
			img=imgUrl;
		}
		// 没有的话，在看看别的人是不是已经用过了
		else {
			img=DEFAULT_ICO;
		}
		return img;
	}

	/**
	 * 下载网站图片
	 * @deprecated 因为响应速度过慢，暂时取消
	 * @param url
	 *            用户输入的原始url
	 * @return int clbId
	 * */
	
	@Deprecated
	public int downLoadFavico(String url) {
		int clbId = -1;
		InputStream inputStream = null;
		try {
			URL favIcoUrl = new URL(FavoriteImgUtils.getFaviconUrl(url));
			inputStream = favIcoUrl.openConnection().getInputStream();
			clbId=clbService.createFile("favicon", inputStream);
			if(clbId>0){
				keyValueService.addKeyValue(FavoriteImgUtils.getFaviconUrl(url), clbId+"");
			}
		} catch (MalformedURLException e) {
			LOG.error("can not fount favico.ico in url["+url+"]", e);
		} catch (IOException e) {
			LOG.error(e.getMessage(), e);
		} finally {
			try {
				if (inputStream != null) {
					inputStream.close();
				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return clbId;
	}

	private String getStaticImgUrl(String url) {
		Set<Entry<String, String>> set = STATIC_IMG.entrySet();
		for (Entry<String, String> entry : set) {
			if (url.endsWith(entry.getKey())) {
				return entry.getValue();
			}
		}
		return null;
	}

	@Override
	public List<FavoriteUrl> getFavoritesByUid(int uid) {
		return favoriteDAO.getFavoritesUrl(uid);
	}

	@Override
	public void deleteFavorite(int id) {
		favoriteDAO.deleteFavorite(id);
	}

	@Override
	public void updateFavorite(FavoriteUrl fav) {
		fav.setImg(getImg(fav.getUrl()));
		fav.setUrl(FavoriteImgUtils.addSchema(fav.getUrl()));
		favoriteDAO.updateFavorite(fav);
	}
	@Override
	public FavoriteUrl getFavoriteUrlById(int id) {
		return favoriteDAO.getFavoriteUrlById(id);
	}

}
