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
/**
 * 
 */
package net.duckling.dhome.domain.people;

import java.io.Serializable;

import net.duckling.dhome.dao.TempField;

/**
 * 
 * 个人自定义urlMapping
 * 
 * @author lvly
 * @since 2012-9-5
 */
public class UrlMapping implements Serializable {
    public static final String STATUS_VALID = "valid";
    public static final String STATUS_INVALID = "invalid";
    
    public static final String TYPE_PERSON = "person";
    public static final String TYPE_INSTITUTION = "institution";

    @TempField
    private static final long serialVersionUID = 1L;
    /**
     * primary key
     * */
    private int id;
    /**
     * user id
     * */
    private int uid;
    /***
     * user domain
     * */
    private String domain;
    /**
     * url a user can define many url
     * */
    private String url;
    /**
     * status must be the constant of STATUS_XX
     * */
    private String status;
    
    private int institutionId;
    
    /**
     * 机构或个人，默认是个人
     */
    private String type;

	public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

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

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
    
	public int getInstitutionId() {
		return institutionId;
	}

	public void setInstitutionId(int institutionId) {
		this.institutionId = institutionId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
