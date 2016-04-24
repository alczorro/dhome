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

import java.io.UnsupportedEncodingException;

import junit.framework.Assert;

import org.junit.Test;

public class BrowserTest {
	@Test
	public void testEncodeFileNameInIE() throws UnsupportedEncodingException{
		Assert.assertEquals("attachment;filename=\"xx\"", Browser.encodeFileName("msie", "xx"));
	}
	@Test
	public void testEncodeFileNameInFirefox() throws UnsupportedEncodingException{
		Assert.assertEquals("attachment;filename=\"xx\"", Browser.encodeFileName("firefox", "xx"));
	}
	@Test
	public void testEncodeFileNameInChrome() throws UnsupportedEncodingException{
		Assert.assertEquals("attachment;filename=\"xx\"", Browser.encodeFileName("chrome", "xx"));
	}
	@Test
	public void testEncodeFileNameInSafari() throws UnsupportedEncodingException{
		Assert.assertEquals("attachment;filename=\"xx\"", Browser.encodeFileName("safari", "xx"));
	}
	@Test
	public void testEncodeFileNameInUnknowBrowser() throws UnsupportedEncodingException{
		Assert.assertEquals("attachment;filename=\"xx\"", Browser.encodeFileName("opera", "xx"));
	}
	@Test
	public void testEncodeFileNameWithLongFileName() throws UnsupportedEncodingException{
		String longfilename = "xxxxxxxxxxx             xxxxxxxxxxxxxxxxxxxxxxxx                 xxxxxxxxxxxxxxxxx" +
				"xxxxxxx                  xxxxxxxxxxxxxxxxxxxx                                          xxxxxxxx";
		
		Assert.assertEquals("attachment;filename=\""+longfilename.replace(" ", "%20")+"\"", 
				Browser.encodeFileName("chrome", longfilename));
	}
}
