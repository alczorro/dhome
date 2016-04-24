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
package net.duckling.dhome.dao.test;

import java.io.FileOutputStream;
import java.io.InputStream;

import net.duckling.dhome.common.repository.BaseDao;

import org.dbunit.DataSourceDatabaseTester;
import org.dbunit.database.DatabaseSequenceFilter;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.FilteredDataSet;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.filter.ITableFilter;
import org.dbunit.dataset.xml.FlatDtdDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.ext.mysql.MySqlConnection;
import org.dbunit.operation.DatabaseOperation;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public abstract class AbstractDAOTest {

    private static JdbcTemplate jdbcTemplate;

    private static NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static ComboPooledDataSource dataSource;

    private static ApplicationContext context = new ClassPathXmlApplicationContext("classpath:spring/datasource.xml");

    private static DataSourceDatabaseTester dbtester;

    public static DataSourceDatabaseTester getDbtester() {
        return dbtester;
    }

    public static void setDbtester(DataSourceDatabaseTester dbtester) {
        AbstractDAOTest.dbtester = dbtester;
    }

    public static ComboPooledDataSource getDataSource() {
        return dataSource;
    }

    public static NamedParameterJdbcTemplate getNameedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    public static JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public static void setupDatabase(BaseDao dao, String dataSetFileName) throws Exception {
        jdbcTemplate = (JdbcTemplate) context.getBean("jdbcTemplate");
        namedParameterJdbcTemplate = (NamedParameterJdbcTemplate) context.getBean("namedParameterJdbcTemplate");
        dataSource = (ComboPooledDataSource) context.getBean("dataSource");
        dao.setJdbcTemplate(getJdbcTemplate());
        dao.setNamedParameterJdbcTemplate(getNameedParameterJdbcTemplate());
        dbtester = new DataSourceDatabaseTester(getDataSource());
        dbtester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);
        dbtester.setTearDownOperation(DatabaseOperation.NONE);
        dbtester.setDataSet(getDataSet(dataSetFileName));
        dbtester.onSetup();
    }

    private static IDataSet getDataSet(String name) throws DataSetException {
        InputStream inputStream = AbstractDAOTest.class.getResourceAsStream(name);
        FlatXmlDataSetBuilder builder = new FlatXmlDataSetBuilder();
        return builder.build(inputStream);
    }

    public static void closeDatabase() {
        dataSource.close();
    }

    public static void exportFK(String file) throws Exception {
        connection = new MySqlConnection(getDataSource().getConnection(), null);
        IDataSet fullDataSet = connection.createDataSet();
        ITableFilter filter = new DatabaseSequenceFilter(connection);
        FilteredDataSet filteredDatSet = new FilteredDataSet(filter, fullDataSet);
        FlatXmlDataSet.write(filteredDatSet, new FileOutputStream(file + ".xml"));
        FlatDtdDataSet.write(filteredDatSet, new FileOutputStream(file + ".dtd"));
    }

    private static MySqlConnection connection = null;

}
