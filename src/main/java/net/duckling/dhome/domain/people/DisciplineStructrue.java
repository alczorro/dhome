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
package net.duckling.dhome.domain.people;

import java.util.List;

/**
 * 学科目录结构
 * @author lvly
 * @since 2012-8-27
 */
public class DisciplineStructrue {
	private static DisciplineStructrue instance;
	private int value;
	private String name;
	private List<DisciplineStructrue> sons;
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<DisciplineStructrue> getSons() {
		return sons;
	}
	public void setSons(List<DisciplineStructrue> sons) {
		this.sons = sons;
	}
	public DisciplineStructrue(){
		this.value=0;
		this.name="root";
	}
	public DisciplineStructrue(Discipline dis){
		this.value=dis.getId();
		this.name=dis.getName();
	}
	public static DisciplineStructrue getInstance(){
		return instance;
	}
	public static void setInstance(DisciplineStructrue dis){
		instance=dis;
	}
	
}
