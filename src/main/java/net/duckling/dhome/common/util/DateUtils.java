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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期类工具类
 * @author lvly
 * @since 2012-9-21
 */
public final class DateUtils {
	private DateUtils(){}
	private static final String DEFAULT_FORMAT="yyyy-MM-dd HH:mm";
	private static final String MAX_DATE_FORMAT="yyyy-MM-dd HH:mm:ss";
	private static final String MAX_DATE_STR="3000-01-01 00:00:00";
	/**
	 * 最大时间，默认是公元三千年
	 * @return date 3000-1-1 0:0:0
	 * 
	 * */
	public static Date getMaxDate(){
		try {
			return new SimpleDateFormat(MAX_DATE_FORMAT).parse(MAX_DATE_STR);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 根据字符串计息出一个Date对象
	 * @param dateString 字符串，必须以 yyyy-mm-dd hh:mm形式
	 * @return Date()
	 */
	public static Date getDate(String dateString){
		if(CommonUtils.isNull(dateString)){
			return null;
		}
		try {
			return new SimpleDateFormat(DEFAULT_FORMAT).parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
	/**
	 * 根据Date计息出一个String对象
	 * @param dateString 字符串，必须以 yyyy-mm-dd hh:mm形式
	 * @return Date()
	 */
	public static String getDateStr(Date date){
		if(date==null){
			return "";
		}
		return new SimpleDateFormat(DEFAULT_FORMAT).format(date);
	}

}
