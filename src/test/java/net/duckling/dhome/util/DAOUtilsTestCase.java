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
package net.duckling.dhome.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junit.framework.Assert;
import net.duckling.dhome.common.repository.DAOUtils;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.jdbc.core.RowMapper;

/**对DAOUtils的单元测试
 * @author lvly
 * @since 2012-08-13
 * */
public class DAOUtilsTestCase {
	private DAOUtils<TestBean> daoUtils=new DAOUtils<TestBean>(TestBean.class);
	
	@BeforeClass
	public static void before() throws Exception {
	}

	@AfterClass
	public static void after() throws Exception {
	}

	@Test
	public void testInsertSQL() {
		String sql=daoUtils.getInsert("");
		Assert.assertEquals("insert into test_bean(id,name,date,an_long_name,is_admin) values(:id,:name,:date,:anLongName,:isAdmin)", sql);
		sql=daoUtils.getInsert("id");
		Assert.assertEquals("insert into test_bean(name,date,an_long_name,is_admin) values(:name,:date,:anLongName,:isAdmin)",sql);
		sql=daoUtils.getInsert("id,date");
		Assert.assertEquals("insert into test_bean(name,an_long_name,is_admin) values(:name,:anLongName,:isAdmin)",sql);
	}
	@Test
	public void testUpdateSQL(){
		TestBean tb=new TestBean();
		tb.setDate(new Date());
		tb.setId(10);
		tb.setName("test");
		String sql=daoUtils.getUpdate(tb);
		Assert.assertEquals("update test_bean set id=:id,name=:name,date=:date where id=:id", sql);
		tb.setName(null);
		tb.setIsAdmin(false);
		sql=daoUtils.getUpdate(tb);
		Assert.assertEquals("update test_bean set id=:id,date=:date,is_admin=:isAdmin where id=:id", sql);
	}
	@Test
	public void testSelectSQL(){
		TestBean tb=new TestBean();
		tb.setDate(new Date());
		tb.setId(10);
		String sql=daoUtils.getSelect(tb);
		
		Assert.assertEquals("select * from test_bean where 1=1 and id=:id  and date=:date ",sql);
		tb.setDate(null);
		sql=daoUtils.getSelect(tb);
		Assert.assertEquals("select * from test_bean where 1=1 and id=:id ",sql);
		tb.setId(0);
		sql=daoUtils.getSelect(tb);
		Assert.assertEquals("select * from test_bean where 1=1",sql);
		tb.setIsAdmin(true);
		sql=daoUtils.getSelect(tb);
		Assert.assertEquals("select * from test_bean where 1=1 and is_admin=:isAdmin ",sql);
	}
	@Test
	public void testDeleteSQL(){
		TestBean tb=new TestBean();
		tb.setDate(new Date());
		String sql=daoUtils.getDelete(tb);
		Assert.assertEquals("delete from test_bean where 1=1 and date=:date",sql);
		tb.setId(12);
		sql=daoUtils.getDelete(tb);
		Assert.assertEquals("delete from test_bean where 1=1 and id=:id and date=:date",sql);
		tb=null;
		sql=daoUtils.getDelete(tb);
		Assert.assertEquals("delete from test_bean where 1=1", sql);
	}
	@Test
	public void testParamMap(){
		TestBean tb=new TestBean();
		tb.setDate(new Date());
		tb.setId(12);
		tb.setName("zhangSan");
		tb.setAnLongName("longName");
		tb.setIsAdmin(true);
		Map<String,Object> map=daoUtils.getParamMap(tb);
		Map<String,Object> mapP=new HashMap<String,Object>();
		mapP.put("id",tb.getId());
		mapP.put("name", tb.getName());
		mapP.put("anLongName", tb.getAnLongName());
		mapP.put("date", tb.getDate());
		mapP.put("isAdmin", "1");
		Assert.assertEquals(mapP, map);
	}
	@Test
	public void testRowMapper()throws Exception{
		RowMapper<TestBean> tbMapper = new RowMapper<TestBean>() {
			public TestBean mapRow(ResultSet rs, int index) throws SQLException {
				TestBean tb = new TestBean();
				tb.setId(rs.getInt("id"));
				tb.setName(rs.getString("name"));
				tb.setDate(rs.getTimestamp("date"));
				tb.setAnLongName(rs.getString("an_long_name"));
				tb.setIsAdmin(rs.getBoolean("is_admin"));
				return tb;
			}
		};
		Timestamp date=new Timestamp(new Date().getTime());
		IMocksControl control=EasyMock.createControl();
		ResultSet rs=control.createMock(ResultSet.class);
		EasyMock.expect(rs.getInt("id")).andReturn(12);
		EasyMock.expect(rs.getString("name")).andReturn("zhangsan");
		EasyMock.expect(rs.getTimestamp("date")).andReturn(date);
		EasyMock.expect(rs.getString("an_long_name")).andReturn("longName");
		EasyMock.expect(rs.getBoolean("is_admin")).andReturn(true);
		control.replay();
		TestBean tb=tbMapper.mapRow(rs, 1);
		
		RowMapper<TestBean> tbGenerateMapper=daoUtils.getRowMapper(null);
		IMocksControl controlX=EasyMock.createControl();
		ResultSet rsX=controlX.createMock(ResultSet.class);
		EasyMock.expect(rsX.getInt("id")).andReturn(12);
		EasyMock.expect(rsX.getString("name")).andReturn("zhangsan");
		EasyMock.expect(rsX.getTimestamp("date")).andReturn(date);
		EasyMock.expect(rsX.getString("an_long_name")).andReturn("longName");
		EasyMock.expect(rsX.getBoolean("is_admin")).andReturn(true);
		controlX.replay();
		TestBean tbGenerate=tbGenerateMapper.mapRow(rsX, 1);
		
		Assert.assertEquals(tb.getId(), tbGenerate.getId());
		Assert.assertEquals(tb.getName(), tbGenerate.getName());
		Assert.assertEquals(tb.getDate(), tbGenerate.getDate());
		Assert.assertEquals(tb.getAnLongName(), tbGenerate.getAnLongName());
		Assert.assertEquals(tb.getAnLongName(), tbGenerate.getAnLongName());
	}
}

