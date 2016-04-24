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
package net.duckling.dhome.common.repository;

import java.util.Locale;

/**
 * @title: EField.java
 * @package net.duckling.dhome.dao
 * @description: TODO 类说明
 * @author clive
 * @date 2012-9-13 下午2:47:49
 */
public interface EField {
    enum Page implements EField {
        ID, TITLE, UID, CREATE_TIME, CONTENT, USED_FOR;
        @Override
        public String toString() {
            return this.name().toLowerCase(Locale.US);
        }
    }

    enum SimpleUser implements EField {
        ID, ZH_NAME, EN_NAME, IMAGE, EMAIL, SALUTATION, REGIST_TIME, PINYIN;
        @Override
        public String toString() {
            return this.name().toLowerCase(Locale.US);
        }
    }

    enum MenuItem implements EField {
        ID, UID, TITLE, URL, SEQUENCE, STATUS;
        @Override
        public String toString() {
            return this.name().toLowerCase(Locale.US);
        }
    }

}
