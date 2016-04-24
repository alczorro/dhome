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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.common.util.CommonUtils;

import org.bouncycastle.util.Arrays;
import org.junit.Test;

/**
 * 针对CommonUtils的单元测试
 * @author lvly
 * @since 2012-8-27
 */
public class CommonUtilsTestCase {
	@Test
	public void test_isNull(){
		String nullStr=null;
		String emptyStr="";
		Assert.assertTrue(CommonUtils.isNull(nullStr));
		Assert.assertTrue(CommonUtils.isNull(emptyStr));
		
		String[] nullArray=null;
		String[] emptyArray=new String[0];
		Assert.assertTrue(CommonUtils.isNull(nullArray));
		Assert.assertTrue(CommonUtils.isNull(emptyArray));
		
		int[] nullIntArray=null;
		int[] emptyIntArray=new int[0];
		Assert.assertTrue(CommonUtils.isNull(nullIntArray));
		Assert.assertTrue(CommonUtils.isNull(emptyIntArray));
		
		List nullList=null;
		List emptyList=new ArrayList();
		Assert.assertTrue(CommonUtils.isNull(nullList));
		Assert.assertTrue(CommonUtils.isNull(emptyList));
		
		Map nullMap=null;
		Map emptyMap=new HashMap();
		Assert.assertTrue(CommonUtils.isNull(nullMap));
		Assert.assertTrue(CommonUtils.isNull(emptyMap));
		
	}
	@Test
	public void test_first(){
		List<String> list=new ArrayList<String>();
		list.add("zs");
		list.add("ls");
		Assert.assertEquals("zs", CommonUtils.first(list));
		list=null;
		Assert.assertNull(CommonUtils.first(list));
	}
	@Test
	public void test_format(){
		String str="a,b,c,d,";
		Assert.assertEquals("a,b,c,d", CommonUtils.format(str));
		String str1="a@b@c@d@";
		Assert.assertEquals("a@b@c@d", CommonUtils.format(str1,"@"));
	}
	@Test
	public void test_getList(){
		String st="1";
		List<String> list=new ArrayList<String>();
		list.add("1");
		Assert.assertEquals(list,CommonUtils.getList(st));
	}
	@Test
	public void test_stringArray2IntArray(){
		String[] str=new String[2];
		str[0]="0";
		str[1]="2";
		Integer[] intA=CommonUtils.stringArray2IntArray(str);
		Assert.assertEquals(2, intA.length);
		Assert.assertTrue(0==intA[0]);
		Assert.assertTrue(2==intA[1]);
	}
	@Test
	public void test_trim(){
		Assert.assertEquals("xx", CommonUtils.trim(" xx "));
		Assert.assertEquals("xx", CommonUtils.trim(" xx"));
		Assert.assertEquals("xx", CommonUtils.trim("xx "));
		Assert.assertEquals("x x", CommonUtils.trim(" x x "));
		Assert.assertNull(CommonUtils.trim(null));
	}
}
