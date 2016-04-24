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
package net.duckling.dhome.common.util;

import java.util.List;

/**
 * 计算G-Index和H-Index的工具类
 * @author Yangxp
 *
 */
public final class GHIndexCaculator {
	private GHIndexCaculator(){}
	
	/**
	 * 计算H-Index
	 * @param citeNums
	 * @return
	 */
	public static int caculateHIndex(List<Integer> citeNums){
		int result = 0;
		int index = 1;
		if(null != citeNums && !citeNums.isEmpty()){
			int size = citeNums.size();
			int i=0;
			while(i<size && index <= citeNums.get(i)){
				i++;
				index++;
			}
			result = index-1;
		}
		return (result >=0)?result:0;
	}
	/**
	 * 计算G-Index
	 * @param citeNums
	 * @return
	 */
	public static int caculateGIndex(List<Integer> citeNums){
		int result = 0;
		if(null != citeNums && !citeNums.isEmpty()){
			int size = citeNums.size();
			int index =1;
			if(size >1){
				/** sumg:前g篇论文的被引次数累积和，sumgplus:前g+1篇论文的被引次数累积和  **/
				int i=0, sumg=0, sumgplus=0;
				boolean cond = false;
				do{
					sumg += citeNums.get(i);
					sumgplus = sumg + citeNums.get(i+1);
					cond = sumg >= index*index && sumgplus <= (index+1)*(index+1);
					i++;
					index++;
				}while(i<size-1 && !cond);
				result = index;
			}
		}
		return result;
	}
}
