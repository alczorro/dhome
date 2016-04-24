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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * 编码转换器
 * @author Yangxp
 * @since 2012-12-05
 */
public final class EncodeConverter {
	private EncodeConverter(){};
	/**
	 * 将输入流从源编码srcEncode转换成desEncode的输入流
	 * @param in 待转换的输入流
	 * @param srcEncode 输入流的原始编码类型
	 * @param desEncode 转换完后的编码类型
	 * @return 编码类型为desEncode的输入流
	 * @throws IOException
	 */
	public static InputStream convert(InputStream in, String srcEncode, String desEncode) throws IOException{
		StringBuilder tempContent = new StringBuilder();
		Reader reader = new InputStreamReader(in, srcEncode);
		char[] tempChars = new char[1024];
		while(reader.read(tempChars)>0){
			tempContent.append(new String(tempChars));
		}
		reader.close();
		return new ByteArrayInputStream(tempContent.toString().getBytes(desEncode));
	}
}
