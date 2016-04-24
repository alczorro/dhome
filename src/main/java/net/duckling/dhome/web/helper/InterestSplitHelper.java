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
package net.duckling.dhome.web.helper;

import java.util.ArrayList;
import java.util.List;

import net.duckling.dhome.common.util.CommonUtils;

/**
 * 研究兴趣分隔符，自定义
 * @author lvly
 * @since 2012-12-26
 */
public final class InterestSplitHelper {
	private InterestSplitHelper(){}
	/**
	 * 需要分隔的分隔符
	 * */
	private static final String SPLIT=",|，|;|；|、";
	/**
	 * 所有分隔符，都替换成','，然后split返回
	 * @param interests a,b；c，d
	 * @return [a,b,c,d]
	 * */
	public static String[] getInterestSplit(String interests){
		if(CommonUtils.isNull(interests)){
			return new String[]{};
		}
		return removeEmpty(interests.split(SPLIT));
	}
	/**
	 * 把中间米有内容，比如null，或者“”的元素去掉
	 * @param split 待处理数组
	 * @return 处理好的数组
	 * */
	private static String[] removeEmpty(String[] split){
		List<String> result=new ArrayList<String>();
 		for(String str:split){
			if(CommonUtils.isNull(str)){
				continue;
			}else{
				result.add(str);
			}
		}
 		String[] resultArray=new String[result.size()];
		return result.toArray(resultArray);
	}
 
}
