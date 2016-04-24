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
package net.duckling.dhome.common.clb;

import net.duckling.dhome.common.util.CommonUtils;

public class FileNameSafeUtil {
	public static final String[] DANGEROUS_SUFFIX=new String[]{"exe","sh","php","py","java","pl","rb","bat","vbs","js","jsp"};
	
	public static String makeSafe(String fileName){
		if(CommonUtils.isNull(fileName)){
			return fileName;
		}
		String lower=fileName.toLowerCase();
		for(String dan:DANGEROUS_SUFFIX){
			if(lower.endsWith("."+dan)){
				return fileName+"_重命名";
			}
		}
		return fileName;
	}

}
