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
package net.duckling.dhome.domain.object;

/**
 * 社交媒体，类似于收藏夹似的东西，给别人看的
 * @author lvly
 * @since 2012-12-21
 */
public class FavoriteUrl {
	/**主键id*/
	private int id;
	/**所属用户id*/
	private int uid;
	/**搜藏地址*/
	private String url;
	/**用户命名*/
	private String title;
	/**图片的clbId,或者是静态图片名字*/
	private String img;
	/**
	 * 添加或者编辑的时候选择的默认项，如为other则是其他，如果不是other，存的即是国际化的key
	 * */
	private String selectMedia;
	
	
	public String getSelectMedia() {
		return selectMedia;
	}
	public void setSelectMedia(String selectMedia) {
		this.selectMedia = selectMedia;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getImg() {
		return img;
	}
	public void setImg(String imgId) {
		this.img = imgId;
	}
	
	
}
