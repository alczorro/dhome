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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HTMLFilter {



	   
    public static String removeHTMLTags(String str, String tags) {
        if (str == null)
            return null;
        if (tags == null)
            return str;
        String regx="(</?)("+tags+")([^>]*>)";
         Matcher matcher ;
         Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE+Pattern.MULTILINE);// 不区分大小写
        //此处需要循环匹配，防止恶意构造的字符串
        while((matcher=pattern.matcher(str)).find()){
            str= matcher.replaceAll("");
        }

        return str;
    }

   
    public static String removeEvents(String content){

        String regx="(<[^<]*)(on\\w*\\x20*=|javascript:)";
        Pattern pattern = Pattern.compile(regx, Pattern.CASE_INSENSITIVE+Pattern.MULTILINE);// 不区分大小写
        Matcher matcher ;
        String ts= content;
        //此处需要循环匹配，防止恶意构造的字符串如 onclick=onclick=XXX
        while((matcher=pattern.matcher(ts)).find()){
            ts= matcher.replaceAll("$1");
        }
        return ts;
    }

   
    public static String makeSafe(String content){
        return removeEvents(removeHTMLTags(content,"html|body|head|title|style|video|canvas|script|frameset|frame|object|xml|input|button|textarea|select|pre|option|plaintext|form"));
    }

   
    public static String makeSafe(String content,String tags){
        if(tags==null){
        	return makeSafe(content);
        }
        return removeEvents(removeHTMLTags(content,tags));
    }
}

