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
package net.duckling.dhome.common.util;

/**
 * 所有的缓存的key在此管理
 * @author lvly
 * @since 2012-12-21
 */
public final class MemCacheKeyGenerator {
	private MemCacheKeyGenerator(){}
	private static final String VISITED_USER_CACHE="dhome.accessLog.visted.user.cache.";
	private static final String VISITED_USER_COUNT_CACHE="dhome.accessLog.visted.user.count.cache.";
	private static final String URL_MAPPING_CACHE_KEY = "dhome.domain-custom.mapping.";
	private static final String MEMKEY_SIMPLEUSER = "dhome.uid-user.simpleuser.";
	private static final String MEMKEY_DETAILEDUSER = "dhome.uid-user.detaileduser.";
	private static final String FILE_CLB_ID = "dhome.file.clbid.";
	private static final String LAST_COMMENT_ID="dhome.last.comment.";
	private static final String LAST_ADMIN_COMMENT_REPLY="dhome.last.admin.reply.";
	
	 /**
     * 获得最近访问列表
     * @param clbid 文件id号
	 * @param agent 
     * @return
     */
	public static String getFileClbId(int clbid, String agent) {
        return FILE_CLB_ID+clbid+"_"+agent;
    }
    /**
	 * 获得最近访问列表
	 * @param uid 被访问人uid
	 * @return
	 */
	public static String getAccessLogListKey(int uid) {
		return VISITED_USER_CACHE+uid;
	}
	/**
	 * 获得最近来访人数
	 * @param visitedUid 被访问人uid
	 * @return
	 */
	public static String getAccessLogCountKey(int visitedUid) {
		return VISITED_USER_COUNT_CACHE+visitedUid;
	}
	/**
	 * 获得urlMapping
	 * @param url 访问的url
	 * @return
	 */
	public static String getUrlMappingKey(String url) {
		return URL_MAPPING_CACHE_KEY+url;
	}
	/**
	 * 获得simpleuser的缓存key
	 * @param uid 
	 * @return
	 */
	public static String getSimpleUserKey(int uid){
		return MEMKEY_SIMPLEUSER+uid;
	}
	/**
	 * 获得detailUser的缓存key
	 * @param uid
	 * @return
	 */
	public static String getDetailUserKey(int uid){
		return MEMKEY_DETAILEDUSER+uid;
	}
	public static String getLastCommentKey(int uid){
		return LAST_COMMENT_ID+uid;
	}
	public static String getLastAdminReplyKey(int uid){
		return LAST_ADMIN_COMMENT_REPLY+uid;
	}

}
