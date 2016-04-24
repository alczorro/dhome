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

import net.duckling.dhome.web.helper.InterestSplitHelper;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author lvly
 * @since 2012-12-26
 */
public class InterestSplitHelperTestCase {
	@Test
	public void test_getInterestSplit(){
		//en
		Assert.assertArrayEquals(new String[]{"a","b","c"}, InterestSplitHelper.getInterestSplit("a,b,c"));
		Assert.assertArrayEquals(new String[]{"a","b","c"}, InterestSplitHelper.getInterestSplit("a,b,c,,"));
		Assert.assertArrayEquals(new String[]{"a","b","c"}, InterestSplitHelper.getInterestSplit("a,b，c,"));
		Assert.assertArrayEquals(new String[]{"a","b","c"}, InterestSplitHelper.getInterestSplit("a，b，c"));
		Assert.assertArrayEquals(new String[]{"a","b","c"}, InterestSplitHelper.getInterestSplit("a，b;c"));
		Assert.assertArrayEquals(new String[]{"a","b","c"}, InterestSplitHelper.getInterestSplit("a；b;c"));
		Assert.assertArrayEquals(new String[]{"a","b","c"}, InterestSplitHelper.getInterestSplit("a；b；c"));
		Assert.assertArrayEquals(new String[]{"a","b","c"}, InterestSplitHelper.getInterestSplit("a、b、c"));
		Assert.assertArrayEquals(new String[]{"a","b","c"}, InterestSplitHelper.getInterestSplit("a、b、、、、、c"));
		//zh
		Assert.assertArrayEquals(new String[]{"请","叫","我","小","纯","洁"}, InterestSplitHelper.getInterestSplit("请，叫,我;小；纯、洁"));

	}
}
