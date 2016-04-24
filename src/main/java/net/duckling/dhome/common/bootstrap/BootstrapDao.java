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

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import net.duckling.dhome.common.exception.WrongSQLException;
import net.duckling.dhome.common.repository.BaseDao;

import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.util.ResourceUtils;

import com.mchange.v2.c3p0.ComboPooledDataSource;

/**
 * @title: BootstrapDao.java
 * @package net.duckling.dhome.common.bootstrap
 * @description: 自动建库类
 * @author clive
 * @date 2012-9-27 下午4:19:57
 */
public class BootstrapDao extends BaseDao {

    private static final Logger LOG = Logger.getLogger(BootstrapDao.class);

    private JdbcTemplate bootstrapTemplate;

    private ComboPooledDataSource ds;

    private BootstrapConfig bootstrapConfig;

    /**
     * @description 自动建库类的构造方法，使用建库专用配置
     */
    public BootstrapDao(BootstrapConfig config) {
        this.bootstrapTemplate = new JdbcTemplate();
        this.bootstrapConfig = config;
        ds = new ComboPooledDataSource();
        ds.setUser(bootstrapConfig.getDbUser());
        ds.setPassword(bootstrapConfig.getDbPassword());
        ds.setJdbcUrl(bootstrapConfig.getDbURL());
        try {
            ds.setDriverClass(bootstrapConfig.getDriverClass());
        } catch (PropertyVetoException e) {
            LOG.error("Wrong driver class", e);
        }
        this.bootstrapTemplate.setDataSource(ds);
    }

    public BootstrapConfig getBootstrapConfig() {
        return bootstrapConfig;
    }

    public void setBootstrapConfig(BootstrapConfig bootstrapConfig) {
        this.bootstrapConfig = bootstrapConfig;
    }

    /**
     * @description 扫描多个sql文件并逐个执行建表命令
     */
    public void executeSQLFiles() {
        String[] fileArray = getSQLFilesArray();
        for (String fileName : fileArray) {
            createTables(fileName);
        }
    }

    private String[] getSQLFilesArray() {
        String files = bootstrapConfig.getSqlFiles();
        if (files != null) {
            return files.split(",");
        }
        return new String[] {};
    }

    private void createTables(String fileName) {
        File file = null;
        SQLReader reader = null;
        try {
            file = ResourceUtils.getFile("classpath:" + bootstrapConfig.getSqlDir() + "/" + fileName);
            LOG.info("Run sql file " + file);
            if (file.exists()) {
                reader = new SQLReader(new FileInputStream(file), "UTF-8");
                String sql;
                while ((sql = reader.next()) != null) {
                    getJdbcTemplate().execute(sql);
                }
            }
        } catch (FileNotFoundException e) {
            LOG.error("Init sql file is not found", e);
        } catch (UnsupportedEncodingException e) {
            LOG.error("Unsupported encode for UTF-8", e);
        } catch (DataAccessException e) {
            LOG.error("Data access exception", e);
        } catch (WrongSQLException e) {
            LOG.error("Init SQL has problem", e);
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * @description 判断某个特定检查表是否存在
     * @return
     */
    public boolean isTableExisted() {
        return getJdbcTemplate().query("show tables like '" + bootstrapConfig.getCheckTable() + "'",
                new BootstrapResultSetExtractor());
    }

    /**
     * @title: BootstrapDao.java
     * @package net.duckling.dhome.common.bootstrap
     * @description: 访问数据的返回结果的静态内部类
     * @author clive
     * @date 2012-9-27 下午4:20:30
     */
    public static class BootstrapResultSetExtractor implements ResultSetExtractor<Boolean> {
        public Boolean extractData(ResultSet rs) throws SQLException {
            return rs.next();
        }
    }

    public void close() {
        ds.close();
    }

    /**
     * @description 根据配置创建数据库
     */
    public void createDatabase() {
        bootstrapTemplate.execute("CREATE database IF NOT EXISTS " + bootstrapConfig.getDbName()
                + " CHARACTER SET utf8");
    }

    /**
     * @description 判断数据库是否存在
     * @return
     */
    public boolean isDatabaseExisted() {
        String sql = "show databases";
        final List<String> results = bootstrapTemplate.queryForList(sql, String.class);
        if (results == null) {
            return false;
        }
        return results.contains(bootstrapConfig.getDbName());
    }

}
