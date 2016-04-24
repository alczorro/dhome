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
package net.duckling.dhome.common.bootstrap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import net.duckling.dhome.common.exception.WrongSQLException;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
/**
 * 读取SQL文件的工具类
 * @author Clive
 *
 */
public class SQLReader {
	/**
	 * 构造函数
	 * @param reader
	 */
	public SQLReader(Reader reader) {
		this.reader = new BufferedReader(reader);
	}
	/**
	 * 构造函数
	 * @param reader
	 */
	public SQLReader(BufferedReader reader) {
		this.reader = reader;
	}
	/**
	 * 构造函数
	 * @param in
	 * @param encode 编码类型
	 * @throws UnsupportedEncodingException
	 */
	public SQLReader(InputStream in, String encode) throws UnsupportedEncodingException {
		String tempEncode = (encode == null) ? "UTF-8" : encode;
		reader = new BufferedReader(new InputStreamReader(in, tempEncode));
	}
	/**
	 * 
	 * @return
	 */
	public String next(){
		if (!isClosed()) {
			StringBuffer buffer = new StringBuffer();

			String line = null;
			try {
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (isDelimiter(line)) {
						changeDelimiter(line);
					} else if (isComment(line)) {
						LOG.info("comment jumped");
					} else {
						if (line.endsWith(delimeter)) {
							line = line.substring(0, line.length() - delimeter.length());
							buffer.append(line).append("\n");
							return StringUtils.trimToNull(buffer.toString());
						} else{
							buffer.append(line).append("\n");
						}
					}
				}
			} catch (IOException e) {
				LOG.error(e.getMessage());
				LOG.debug("", e);
				close();
			}
			if (line == null) {
				close();
			}
			return StringUtils.trimToNull(buffer.toString());
		}
		return null;
	}

	private boolean isComment(String line) {
		return line.startsWith("/*");
	}

	private void changeDelimiter(String line) {
		delimeter = parseDelimiter(line);
		if (StringUtils.isEmpty(delimeter)) {
			LOG.error("Wrong format SQL");
			throw new WrongSQLException("delimiter is empty:" + line);
		}
	}
	/**
	 * 关闭流
	 */
	public void close() {
		if (reader != null) {
			try {
				reader.close();
			} catch (IOException e) {
				LOG.error("Exception has happended while reading initialize SQL.");
				LOG.error(e.getMessage());
				LOG.debug("", e);
			}
			reader = null;
		}
	}

	private String parseDelimiter(String line) {
		return line.substring("delimiter".length()).trim();
	}

	private boolean isDelimiter(String line) {
		if (line != null) {

			String ignored = line.toLowerCase();
			return ignored.startsWith("delimiter");
		}
		return false;
	}

	private boolean isClosed() {
		return reader == null;
	}

	private BufferedReader reader;
	private String delimeter = ";";

	private static final Logger LOG = Logger.getLogger(SQLReader.class);
}
