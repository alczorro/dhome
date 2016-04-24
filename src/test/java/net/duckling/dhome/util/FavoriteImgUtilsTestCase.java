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
package net.duckling.dhome.util;

import junit.framework.Assert;
import net.duckling.dhome.common.util.FavoriteImgUtils;

import org.junit.Test;

/**
 * @author lvly
 * @since 2012-12-21
 */
public class FavoriteImgUtilsTestCase {
	@Test
	public void test_addSchema(){
		String result=FavoriteImgUtils.addSchema("www.baidu.com");
		Assert.assertEquals("http://www.baidu.com", result);
		result=FavoriteImgUtils.addSchema("ftp://www.baidu.com");
		Assert.assertEquals("ftp://www.baidu.com", result);
	}
	@Test
	public void test_removeSchema(){
		String result=FavoriteImgUtils.removeSchema("www.baidu.com");
		Assert.assertEquals("www.baidu.com", result);
		result=FavoriteImgUtils.removeSchema("ftp://www.baidu.com");
		Assert.assertEquals("www.baidu.com", result);
		result=FavoriteImgUtils.removeSchema("https://www.baidu.com");
		Assert.assertEquals("www.baidu.com", result);
	}
	@Test
	public void test_getSchema(){
		String result=FavoriteImgUtils.getSchema("www.baidu.com");
		Assert.assertEquals("http://", result);
		result=FavoriteImgUtils.getSchema("ftp://www.baidu.com");
		Assert.assertEquals("ftp://", result);
		result=FavoriteImgUtils.getSchema("https://www.baidu.com");
		Assert.assertEquals("https://", result);
	}
	@Test
	public void test_getBaseUrl(){
		String result=FavoriteImgUtils.getBaseUrl("www.baidu.com");
		Assert.assertEquals("www.baidu.com", result);
		result=FavoriteImgUtils.getBaseUrl("ftp://www.baidu.com/a/b/c/cd.json");
		Assert.assertEquals("ftp://www.baidu.com", result);
		result=FavoriteImgUtils.getBaseUrl("https://www.baidu.com");
		Assert.assertEquals("https://www.baidu.com", result);
	}
	@Test
	public void test_hasSchema(){
		boolean flg=FavoriteImgUtils.hasSchema("http://www.baidu.com");
		Assert.assertTrue(flg);
		flg=FavoriteImgUtils.hasSchema("www.baidu.com");
		Assert.assertFalse(flg);
	}
	@Test
	public void test_getFaviconUrl(){
		String result=FavoriteImgUtils.getFaviconUrl("http://www.baidu.com/a/b/c/c.json");
		Assert.assertEquals("http://www.baidu.com/favicon.ico",result);
		result=FavoriteImgUtils.getFaviconUrl("www.baidu.com/a/b/c/c.json");
		Assert.assertEquals("http://www.baidu.com/favicon.ico",result);
		result=FavoriteImgUtils.getFaviconUrl("emu://www.baidu.com/a/b/c/c.json");
		Assert.assertEquals("emu://www.baidu.com/favicon.ico",result);
		result=FavoriteImgUtils.getFaviconUrl("emu://www.baidu.com");
		Assert.assertEquals("emu://www.baidu.com/favicon.ico",result);
	}
}

