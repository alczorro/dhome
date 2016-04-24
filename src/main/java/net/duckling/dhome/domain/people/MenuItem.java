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
package net.duckling.dhome.domain.people;

import java.io.Serializable;

import net.duckling.dhome.dao.TempField;

/**
 * @group: net.duckling
 * @title: MenuItem.java
 * @description: 菜单项
 * @author clive
 * @date 2012-8-4 下午7:55:48
 */
public class MenuItem implements Comparable<MenuItem>, Serializable {

    @TempField
    private static final long serialVersionUID = 1L;

    /**
     * @Fields id : 菜单项的唯一数字标示
     */
    private int id;

    /**
     * @Fields name : 菜单项所属的uid
     */
    private int uid;

    /**
     * @Fields name : 菜单项标题
     */

    private String title;

    /**
     * @Fields sequence : 菜单项的排序字段
     */
    private int sequence;

    /**
     * @Fields 菜单项状态 : 菜单项的状态 0是隐藏，1是显示
     */
    private int status;

    private String url;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int compareTo(MenuItem o) {
        return (sequence == o.getSequence() ? 0 : (sequence > o.getSequence() ? 1 : -1));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof MenuItem) {
            return o.hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        int result = 0;
        if (result == 0) {
            result = HASH_CODE_PRIME_1;
            result = getHashCodeByField(result, id);
            result = getHashCodeByField(result, sequence);
            if (url != null) {
                result = getHashCodeByField(result, url.hashCode());
            }
            if (title != null) {
                result = getHashCodeByField(result, title.hashCode());
            }
            result = getHashCodeByField(result, status);
        }
        return result;
    }

    private static int getHashCodeByField(int result, int field) {
        return HASH_CODE_PRIME_2 * result + field;
    }

    private static final int HASH_CODE_PRIME_1 = 17;
    private static final int HASH_CODE_PRIME_2 = 31;

    public static final int MENU_ITEM_STATUS_HIDING = 0;
    /** 未发布 */
    public static final int MENU_ITEM_STATUS_SHOWING = 1;
    /** 发布 */
    public static final int MENU_ITEM_STATUS_REMOVED = 2;
    /** 删除 */

}
