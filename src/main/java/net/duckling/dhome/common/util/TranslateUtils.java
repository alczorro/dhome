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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;


/**
 * 转义工具类，主要转义字符串和对象里的String字符,谨防js注入，很危险
 * 
 * @author lvly
 * @since 2012-10-22
 */
public final class TranslateUtils {
	private static final Logger LOG=Logger.getLogger(TranslateUtils.class);
	private TranslateUtils() {
	}
	/**转义集合对应关系*/
	private static final Map<String, String> TRANSLATE_MAP = new HashMap<String, String>();
	/**不进行转义的类型，暂定为基本类型**/
	private static final List<Class<? extends Object>> CAN_NOT_CONVERT_TYPE = new ArrayList<Class<? extends Object>>();
	private static final int DELTA_FROM_UPPER_TO_LOWER = 32;
	
	static {
		TRANSLATE_MAP.put("<", "&lt;");
		TRANSLATE_MAP.put(">", "&gt;");
		TRANSLATE_MAP.put("'", "&acute;");
		TRANSLATE_MAP.put("\"", "&quot;");

		CAN_NOT_CONVERT_TYPE.add(int.class);
		CAN_NOT_CONVERT_TYPE.add(Integer.class);
		CAN_NOT_CONVERT_TYPE.add(short.class);
		CAN_NOT_CONVERT_TYPE.add(Short.class);
		CAN_NOT_CONVERT_TYPE.add(long.class);
		CAN_NOT_CONVERT_TYPE.add(Long.class);
		CAN_NOT_CONVERT_TYPE.add(byte.class);
		CAN_NOT_CONVERT_TYPE.add(Byte.class);
		CAN_NOT_CONVERT_TYPE.add(float.class);
		CAN_NOT_CONVERT_TYPE.add(Float.class);
		CAN_NOT_CONVERT_TYPE.add(double.class);
		CAN_NOT_CONVERT_TYPE.add(Double.class);
		CAN_NOT_CONVERT_TYPE.add(boolean.class);
		CAN_NOT_CONVERT_TYPE.add(Boolean.class);
		CAN_NOT_CONVERT_TYPE.add(char.class);
		CAN_NOT_CONVERT_TYPE.add(Date.class);
	}

	/**
	 * 转义字符
	 * 
	 * @param str
	 *            需要转义的字符
	 * @return 转义后的字符
	 * */
	public static String translateStr(String str) {
		if (CommonUtils.isNull(str)) {
			return null;
		}
		String result = str;
		for (Entry<String, String> entry : TRANSLATE_MAP.entrySet()) {
			result = result.replaceAll(entry.getKey(), entry.getValue());
		}
		return result;
	}
	public static <T> List<T> translateList(List<T> list){
		if(CommonUtils.isNull(list)){
			return null;
		}
		for(T t:list){
			translateObj(t);
		}
		return list;
	}

	/**
	 * 转义对象的字符,找出string类型的属性，并进行转义，如果包含bean对象，则递归运行
	 * @param obj
	 * @return the object what has translated
	 * 
	 * */
	public static <T> T translateObj(T obj) {
		if (obj == null) {
			return null;
		}
		Class<? extends Object> objClass = obj.getClass();
		Field[] allFields = objClass.getDeclaredFields();
		for (int i = 0; i < allFields.length; i++) {
			Field field = allFields[i];
			if (canConvert(field)) {
					Method getMethod;
					try {
						getMethod = objClass.getDeclaredMethod(getGetMethodName(field.getName()), new Class[]{});
					
					Object value = getMethod.invoke(obj, new Object[]{});
					//如果是字符串，直接转义
					if (value instanceof String) {
						value = translateStr((String) value);
					} else {
					//要么是bean，那就递归运行
						value = translateObj(value);
					}
					Method setMethod = objClass.getDeclaredMethod(getSetMethodName(field.getName()), field.getType());
					setMethod.invoke(obj, value);
					} catch (NoSuchMethodException e) {
						LOG.error("translate error",e);
					} catch (ReflectiveOperationException e) {
						LOG.error("translate error",e);
					} 
			}
		}
		return obj;
	}

	private static String getGetMethodName(String fieldName) {
		return "get" + low2up(fieldName.charAt(0)) + fieldName.substring(1);
	}

	private static String getSetMethodName(String fieldName) {
		return "set" + low2up(fieldName.charAt(0)) + fieldName.substring(1);
	}

	private static char low2up(char c) {
		return (char) (c - DELTA_FROM_UPPER_TO_LOWER);
	}

	/**
	 * 判断是否可以可以转义，除了基本类型都可以转换 int,float,double,boolean,short,long,char,byte
	 * @param
	 * @return boolean
	 * **/
	private static boolean canConvert(Field f) {
		for (Class<? extends Object> c : CAN_NOT_CONVERT_TYPE) {
			if (f.getType().equals(c)||f.getType().isAssignableFrom(c)) {
				return false;
			}
		}
		return !Modifier.isStatic(f.getModifiers()) && Modifier.isPrivate(f.getModifiers());
	}
}
