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
package net.duckling.dhome.web;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.springframework.ui.Model;

public class MockModel implements Model {

	private Map<String, Object> map = new HashMap<String, Object>();
	
	@Override
	public Model addAllAttributes(Collection<?> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model addAllAttributes(Map<String, ?> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model addAttribute(Object arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Model addAttribute(String arg0, Object arg1) {
		map.put(arg0, arg1);
		return this;
	}

	@Override
	public Map<String, Object> asMap() {
		return map;
	}

	@Override
	public boolean containsAttribute(String arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Model mergeAttributes(Map<String, ?> arg0) {
		// TODO Auto-generated method stub
		return null;
	}

}
