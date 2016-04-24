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
package net.duckling.dhome.util.translateBean;

public class Son {
	private String name;
	private int size;
	private long lon;
	private boolean bool;
	private float floa;
	private short shor;
	private double doubl;
	private byte byt;

	public byte getByt() {
		return byt;
	}

	public void setByt(byte byt) {
		this.byt = byt;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getLon() {
		return lon;
	}

	public void setLon(long lon) {
		this.lon = lon;
	}

	public boolean isBool() {
		return bool;
	}

	public void setBool(boolean bool) {
		this.bool = bool;
	}

	public float getFloa() {
		return floa;
	}

	public void setFloa(float floa) {
		this.floa = floa;
	}

	public short getShor() {
		return shor;
	}

	public void setShor(short shor) {
		this.shor = shor;
	}

	public double getDoubl() {
		return doubl;
	}

	public void setDoubl(double doubl) {
		this.doubl = doubl;
	}

}