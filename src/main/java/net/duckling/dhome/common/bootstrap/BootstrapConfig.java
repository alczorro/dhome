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
/**
 * Bootstrap配置
 * @author Yangxp
 *
 */
public class BootstrapConfig {

	private String checkTable;
	private String dbUser;
	private String dbPassword;
	private String dbURL;
	private String dbName;
	private String driverClass;
	private String sqlDir;
	private String sqlFiles;
	
	public String getSqlDir() {
        return sqlDir;
    }

    public void setSqlDir(String sqlDir) {
        this.sqlDir = sqlDir;
    }

    public String getSqlFiles() {
        return sqlFiles;
    }

    public void setSqlFiles(String sqlFiles) {
        this.sqlFiles = sqlFiles;
    }


	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getCheckTable() {
		return checkTable;
	}

	public void setCheckTable(String checkTable) {
		this.checkTable = checkTable;
	}

	public String getDbUser() {
		return dbUser;
	}

	public void setDbUser(String dbUser) {
		this.dbUser = dbUser;
	}

	public String getDbPassword() {
		return dbPassword;
	}

	public void setDbPassword(String dbPassword) {
		this.dbPassword = dbPassword;
	}

	public String getDbURL() {
		return dbURL;
	}

	public void setDbURL(String dbURL) {
		this.dbURL = dbURL;
	}

	public String getDbName() {
		return dbName;
	}

	public void setDbName(String dbName) {
		this.dbName = dbName;
	}

}
