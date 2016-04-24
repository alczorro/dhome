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
package net.duckling.dhome.dao;

/**
 * 键值对数据访问层
 * @author lvly
 * @since 2012-9-28
 */
public interface IKeyValueDAO {
	
	/**通过key获取value
	 * @param key key
	 * @return value
	 * */
	String getValue(String key);
	
	/**增加一个keyValue
	 * @param key key
	 * @param value value
	 **/
	void addKeyValue(String key,String value);
}
