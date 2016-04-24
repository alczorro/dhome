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
package net.duckling.dhome.util;

import junit.framework.Assert;
import net.duckling.dhome.common.util.PinyinUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class PinyinUtilTest {
	
	private static final String YXP1 = "杨小澎";
	private static final String YXP2 = "yangxp";
	private static final String YXP3 = "杨a";
	private static final String YXP4 = "杨xiao澎";
	private static final String YXP = "yangxiaopeng";
	
	@Before
	public void setUp(){
	}
	
	@Test
	public void testGetPinyin(){
		String hanzi = PinyinUtil.getPinyin(YXP1);
		String eng = PinyinUtil.getPinyin(YXP2);
		String hanziEng = PinyinUtil.getPinyin(YXP3);
		String hanziEnHanzi = PinyinUtil.getPinyin(YXP4);
		Assert.assertEquals("yangxiaopeng;yxp", hanzi);
		Assert.assertEquals("yangxp;", eng);
		Assert.assertEquals("yanga;y", hanziEng);
		Assert.assertEquals("yangxiaopeng;yp", hanziEnHanzi);
	}
	
	@Test 
	public void testGetShortPinyin(){
		String hanzi = PinyinUtil.getShortPinyin(YXP1);
		String eng = PinyinUtil.getShortPinyin(YXP2);
		String hanziEng = PinyinUtil.getShortPinyin(YXP3);
		String hanziEnHanzi = PinyinUtil.getShortPinyin(YXP4);
		Assert.assertEquals("yxp", hanzi);
		Assert.assertEquals(YXP2, eng);
		Assert.assertEquals("y", hanziEng);
		Assert.assertEquals("yp", hanziEnHanzi);
	}
	
	@Test
	public void testGetPinyinOnly(){
		String hanzi = PinyinUtil.getPinyinOnly(YXP1);
		String eng = PinyinUtil.getPinyinOnly(YXP2);
		String hanziEng = PinyinUtil.getPinyinOnly(YXP3);
		String hanziEnHanzi = PinyinUtil.getPinyinOnly(YXP4);
		Assert.assertEquals(YXP, hanzi);
		Assert.assertEquals(YXP2, eng);
		Assert.assertEquals("yanga", hanziEng);
		Assert.assertEquals(YXP, hanziEnHanzi);
	}
	
	@Test
	public void testGetPinyinMingXing(){
		String hanzi = PinyinUtil.getPinyinMingXing(YXP1);
		String eng = PinyinUtil.getPinyinMingXing(YXP2);
		String hanziEng = PinyinUtil.getPinyinMingXing(YXP3);
		String hanziEnHanzi = PinyinUtil.getPinyinMingXing(YXP4);
		Assert.assertEquals("xiaopeng yang", hanzi);
		Assert.assertEquals(" ", eng);
		Assert.assertEquals("yanga", hanziEng);
		Assert.assertEquals(YXP, hanziEnHanzi);
	}
	
	@Test
	public void testGetPinyinXingMing(){
		String hanzi = PinyinUtil.getPinyinXingMing(YXP1);
		String eng = PinyinUtil.getPinyinXingMing(YXP2);
		String hanziEng = PinyinUtil.getPinyinXingMing(YXP3);
		String hanziEnHanzi = PinyinUtil.getPinyinXingMing(YXP4);
		Assert.assertEquals("yang xiaopeng", hanzi);
		Assert.assertEquals(" ", eng);
		Assert.assertEquals("yanga", hanziEng);
		Assert.assertEquals(YXP, hanziEnHanzi);
	}
	
	@After
	public void tearDown(){
	}
}
