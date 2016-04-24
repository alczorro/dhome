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
package net.duckling.dhome.crawler.crawler;



import java.util.HashMap;

import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;



public class Rules {
	
	private Map<String, Object> map = new HashMap<String, Object>();

	public Object get(String key) {
		Object value = map.get(key);
		return (value == null) ? "" : value;
	}
	
	public void set(String key, final Object value) {
		if (value != null && !value.equals(""))
			map.put(key, value);
	}	
	public Set<String> getKeySet() {
		return map.keySet();		
	}
	public Set<Entry<String, Object>> getEntrySet() {
		return map.entrySet();		
	}
	public int getSize(){
		return map.size();
	}
	public Object toArray(){
		Object [] obja= map.keySet().toArray();
		return obja;
	}

}
