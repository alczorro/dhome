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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import net.duckling.dhome.common.exception.BibResolveFailedException;
import net.duckling.dhome.domain.people.Paper;

import org.apache.log4j.Logger;
import org.jbibtex.BibTeXDatabase;
import org.jbibtex.BibTeXEntry;
import org.jbibtex.BibTeXParser;
import org.jbibtex.BibTeXString;
import org.jbibtex.Key;
import org.jbibtex.ParseException;
import org.jbibtex.TokenMgrError;
import org.jbibtex.Value;
/**
 * 解析Bib文件的类，并将解析后的论文以集合形式返回
 * @author Yangxp
 * @date 2012-08-15
 */
public class BibReader {

	public static final String FIELD_AUTHOR = "author";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_BOOKTITLE = "booktitle";
	public static final String FIELD_YEAR = "year";
	public static final String FIELD_VOLUME = "volume";
	public static final String FIELD_PAGES = "pages";
	public static final String FIELD_MONTH = "month";
	public static final String FIELD_DOI = "doi";
	public static final String FIELD_ISSN = "issn";
	public static final String FIELD_KEYWORDS = "keywords";
	public static final String FIELD_URL = "url";
	public static final String FIELD_NUMBER = "number";
	public static final String FIELD_JOURNAL = "journal";
	public static final String FIELD_LOCATION = "location";
	public static final String FIELD_PUBLISHER = "publisher";
	public static final String FIELD_ADDRESS = "address";
	
	public static final String BIB_ENCODE = "GBK";

	private static final Logger LOG = Logger.getLogger(BibReader.class);

	private BufferedInputStream in;
	private int uid;
	/**
	 * 构造函数
	 * @param in bib文件的输入流
	 * @param uid 用户ID，主要用于生成Paper对象
	 */
	public BibReader(InputStream in, int uid) {
		this.in = new BufferedInputStream(in);
		this.uid = uid;
	}

	/**
	 * 分析Bib文件，将其中的字段填入Paper对象。最终返回解析出的Paper集合。
	 * 若遇到解析出的title字段为空的情况，则视bib文件中 的该项无效，不会返回相应的Paper对象。
	 * 
	 * @return
	 * @throws BibResolveFailedException 
	 */
	public List<Paper> analyze(int fileLength) throws BibResolveFailedException {
		String encode = FileEncodeDetector.guessEncoding(in, fileLength);
		LOG.info("文件编码："+encode);
		List<Paper> result = new ArrayList<Paper>();
		in = transformEncode(encode);
		if(null != in){
			BibTeXDatabase database = parseBibTeX(encode);
			if(null != database){
				Collection<BibTeXEntry> entries = (database.getEntries()).values();
				for (BibTeXEntry entry : entries) {
					Paper paper = getPaperFromEntry(entry);
					if (null != paper) {
						result.add(paper);
					}
				}
			}
		}
		return result;
	}
	
	private BufferedInputStream transformEncode(String encode){
		InputStream temp = null;
		try {
			if(BIB_ENCODE.equals(encode)){
				temp = in;
			}else{
				temp = EncodeConverter.convert(in, encode, BIB_ENCODE);
			}
		} catch (IOException e) {
			LOG.error("转换编码类型错误！", e);
		}
		return new BufferedInputStream(temp);
	}

	private BibTeXDatabase parseBibTeX(String encode) throws BibResolveFailedException {
		try {
			Reader reader = new InputStreamReader(in,encode);
			BibTeXParser parser = new DHomeBibTexParser();
			return parser.parse(reader);
		} catch (IOException e) {
			LOG.error("读取Bib文件错误！", e);
			throw new BibResolveFailedException("无法读取文件");
		} catch (ParseException e) {
			LOG.error("解析Bib文件错误！", e);
			throw new BibResolveFailedException("无法解析bib文件");
		} catch (TokenMgrError e){
			LOG.error("解析Bib文件错误！", e);
			throw new BibResolveFailedException("不是合法的bib格式文件，可能包含非法字符");
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					LOG.error("关闭输入流失败！", e);
				}
			}
		}
	}

	private Paper getPaperFromEntry(BibTeXEntry entry) {
		String title = getSimpleField(entry, FIELD_TITLE);
		String author = getSimpleField(entry, FIELD_AUTHOR);
		String source = getFieldSource(entry);
		String volumeIssue = getFieldVolume(entry);
		String publishedTime = getFieldPublishedTime(entry);
		String keywords = getSimpleField(entry, FIELD_KEYWORDS);
		String paperURL = getSimpleField(entry, FIELD_URL);
		String pages = getSimpleField(entry, FIELD_PAGES);

		if (null == title) {//如果论文没有title，则为不合法的论文，从解析结果中剔除
			return null;
		}

		return Paper.build(uid, title, author, source, volumeIssue, publishedTime, "0", "", "", keywords, "", paperURL,
				0, 0, 0, pages);
	}

	private String getSimpleField(BibTeXEntry entry, String key) {
		Value keyV = entry.getField(new Key(key));
		return (null == keyV) ? null : keyV.toUserString();
	}

	/** 论文来源取booktitle或journal字段 **/
	private String getFieldSource(BibTeXEntry entry) {
		Value keyV = entry.getField(new Key(FIELD_BOOKTITLE));
		if (null == keyV) {
			keyV = entry.getField(new Key(FIELD_JOURNAL));
		}
		return (null == keyV) ? null : keyV.toUserString();
	}

	/** 期号取 Vol(No)格式 **/
	private String getFieldVolume(BibTeXEntry entry) {
		String result = "";
		Value volumeV = entry.getField(new Key(FIELD_VOLUME));
		result += (null == volumeV) ? "" : volumeV.toUserString();
		Value numberV = entry.getField(new Key(FIELD_NUMBER));
		result += (null == numberV) ? "" : ("(" + numberV.toUserString() + ")");
		return result;
	}

	/** 发表时间取 "年(月)" 格式 **/
	private String getFieldPublishedTime(BibTeXEntry entry) {
		String result = "";
		Value yearV = entry.getField(new Key(FIELD_YEAR));
		result += (null == yearV) ? "" : yearV.toUserString();
		Value monthV = entry.getField(new Key(FIELD_MONTH));
		result += (null == monthV) ? "" : ("(" + monthV.toUserString() + ")");
		return result;
	}
	
	private static class DHomeBibTexParser extends BibTeXParser{
		@Override
		public void checkStringResolution(Key key, BibTeXString string) {
			if (string == null) {
				LOG.error("Unresolved string: \"" + key.getValue() + "\"");
			}
		}
		
		@Override
		public void checkCrossReferenceResolution(Key key, BibTeXEntry entry) {
			if (entry == null) {
				LOG.error("Unresolved cross-reference: \"" + key.getValue() + "\"");
			}
		}
	}
}



