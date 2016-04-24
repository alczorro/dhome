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
import net.duckling.dhome.web.helper.PageCharHelper;

import org.junit.Test;

/**
 * @author lvly
 * @since 2012-12-31
 */
public class PageCharHelperTestCase {
	@Test
	public void test_deleteEnter(){
		//case 1
		Assert.assertEquals("",PageCharHelper.deleteEnter(" "));
		//case 2
		String testStr=PageCharHelper.ENTER_HTML+" hahahah "+PageCharHelper.ENTER_HTML+PageCharHelper.ENTER_HTML;
		Assert.assertEquals("hahahah", PageCharHelper.deleteEnter(testStr));
		
		testStr=PageCharHelper.ENTER_HTML_IN_EDIT_HTML+" hahahah "+PageCharHelper.ENTER_HTML_IN_EDIT_HTML+PageCharHelper.ENTER_HTML_IN_EDIT_HTML;
		Assert.assertEquals("hahahah", PageCharHelper.deleteEnter(testStr));
		
		testStr=PageCharHelper.IE_ENTER_HTML+PageCharHelper.IE_ENTER_HTML+" hahahah "+PageCharHelper.IE_ENTER_HTML;
		Assert.assertEquals("hahahah", PageCharHelper.deleteEnter(testStr));
		
		testStr=" hahahah "+PageCharHelper.IE_ENTER_HTML_IN_EDIT_HTML;
		Assert.assertEquals("hahahah", PageCharHelper.deleteEnter(testStr));
		
		testStr=PageCharHelper.IE_ENTER_HTML_IN_EDIT_HTML+" hahahah ";
		Assert.assertEquals("hahahah", PageCharHelper.deleteEnter(testStr));
	}
	
	@Test
	 public void test_deleteHtmlEnter(){
		//this method is call by sub test case ,don't need more email
		String result=PageCharHelper.deleteHtmlEnter("sthhahaha sth sth", "sth");
		Assert.assertEquals("hahaha", result);
	}
	@Test
	public void test_getEnOnly(){
		String result=PageCharHelper.getEnOnly(" 我叫xiaochunjie哈哈123!@#$%^&*( ");
		Assert.assertEquals("xiaochunjie123", result);
	}
	@Test
	public void test_get(){
		StringBuilder sb=new StringBuilder("  ab  c ");
		PageCharHelper.trim(sb);
		Assert.assertEquals("ab  c",sb.toString());
	}

}
