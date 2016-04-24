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

import net.duckling.dhome.common.repository.EField;
import net.duckling.dhome.common.repository.ETable;

import org.apache.log4j.Logger;

public final class EnumUtils {
	private static final Logger LOG = Logger.getLogger(EnumUtils.class);
	private static final String DELIMETER = ",";

	private EnumUtils() {
	}

	public static String getSqlInPhrase(List<String> values) {
		StringBuilder vStr = new StringBuilder();
		vStr.append("(");
		for (String v : values) {
			vStr.append("'" + v + "'");
			vStr.append(",");
		}
		String result = vStr.toString();
		result = result.substring(0, result.length() - 1) + ")";
		return result;
	}

	private static String getFieldByPattern(EField field, SQLPattern p) {
		switch (p) {
		case INSERT:
			return getValuePhrase(field);
		case UPDATE:
			return getKeyValuePhrase(field);
		case QUERY:
		default:
			return field.toString();
		}
	}

	public static String getKeyValuePhrase(EField field) {
		return field + "=:" + replaceUnderbar(field.toString());
	}

	public static String getValuePhrase(EField field) {
		return ":" + replaceUnderbar(field.toString());
	}

	private static String replaceUnderbar(String src) {
		if (src == null) {
			return null;
		}
		String[] words = src.split("_");
		if (words != null && words.length > 1) {
			StringBuilder sb = new StringBuilder();
			sb.append(words[0]);
			for (int i = 1; i < words.length; i++) {
				if (!"_".equals(words[i])) {
					sb.append(Character.toUpperCase(words[i].charAt(0)));
					sb.append(words[i].substring(1));
				}
			}
			return sb.toString();
		}
		return src;
	}

	private static String getAllFieldByPattern(ETable table, SQLPattern pattern) {
		StringBuilder sb = new StringBuilder();
		sb.append(" ");
		for (EField f : table.getTableFields()) {
			sb.append(getFieldByPattern(f, pattern));
			sb.append(DELIMETER);
		}
		sb.deleteCharAt(sb.length() - 1);
		sb.append(" ");
		return sb.toString();
	}

	public static void main(String args[]) {
		LOG.info(EnumUtils.getUpdateFields(ETable.SIMPLE_USER));
	}

	public static String getInsertFields(ETable table) {
		return getAllFieldByPattern(table, SQLPattern.INSERT);
	}

	public static String getUpdateFields(ETable table) {
		return getAllFieldByPattern(table, SQLPattern.UPDATE);
	}

	public static String getQueryFields(ETable page) {
		return getAllFieldByPattern(page, SQLPattern.QUERY);
	}

}

enum SQLPattern {
	INSERT, UPDATE, QUERY;
}
