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
package net.duckling.dhome.testutil;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import net.duckling.dhome.common.util.CommonUtils;

/**
 * 用于注入bean使用，用于单元测试用例,亦可用于不可告人之特殊用途
 * @author lvly
 * @since 2012-10-30
 */
public final class SetFieldUtils {
	private SetFieldUtils(){}
	/**
	 * 在一个实体里面注入一个bean
	 * @param obj 目标对象，需要被注入的对象
	 * @param name 属性名字
	 * @param bean 待注入的bean
	 * @result 返回是否注入成功
	 * 
	 * */
	public static  boolean setValue(Object obj,String name,Object bean){
		ReflectUtils.setValue(obj, name, bean);
		return true;
	}
	/**
	 * 在一个实体里面注入一个bean
	 * @param obj 目标对象，需要被注入的对象
	 * @param values Map<属性名，属性值>待注入的一系列对象序列
	 * @result 返回是否注入成功
	 * 
	 * */
	public static  boolean setValues(Object obj,Map<String,Object> values){
		if(CommonUtils.isNull(values)){
			return false;
		}
		for(Iterator<Entry<String, Object>> it=values.entrySet().iterator();it.hasNext();){
			Entry<String,Object> entry=it.next();
			setValue(obj, entry.getKey(), entry.getValue());
		}
		return true;
	}
}
