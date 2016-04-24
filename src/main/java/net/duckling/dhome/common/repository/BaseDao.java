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

import java.util.List;
import java.util.Map;

import net.duckling.dhome.common.util.CommonUtils;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public abstract class BaseDao {
    private static final Logger LOG = Logger.getLogger(BaseDao.class);
    private JdbcTemplate jdbcTemplate;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }

    @Autowired
    @Qualifier("jdbcTemplate")
    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    @Autowired
    @Qualifier("namedParameterJdbcTemplate")
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    /**
     * @description 命名规则正常的情况下，可使用，基本插入,排除"id"字段
     * @author lvly
     * @since 2012-08-07
     * @param t
     *            需要插入的对象
     * @return 新增的id
     * **/
    public <T> int insert(T t) {
        return insert(t, "");
    }

    /**
     * @description 命名规则正常的情况下，可使用，基本插入,排除"id"字段
     * @author lvly
     * @since 2012-08-07
     * @param t
     *            需要插入的对象
     * @param expect
     *            需要忽略的字段，bean中
     * @return 新增的id
     * **/
    public <T> int insert(T t, String expect) {
        DAOUtils<T> daoUtils = new DAOUtils<T>(t.getClass());
        String sql = daoUtils.getInsert("id," + expect);
        LOG.debug(sql);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(sql, new MapSqlParameterSource(daoUtils.getParamMap(t,true)), keyHolder);
        return keyHolder.getKey().intValue();
    }

    /***
     * @description 命名规则正常的情况下，可使用，基本条件查询
     * @author lvly
     * @since 2012-08-07
     * @param t
     *            实例，其中的所有属性，除了@TempField和初始值以外都会当做条件来使用
     * */
    public <T> List<T> findByProperties(T t) {
        return findByProperties(t, "");
    }

    /***
     * @description 命名规则正常的情况下，可使用，基本条件查询
     * @author lvly
     * @since 2012-08-07
     * @param t
     *            实例，其中的所有属性，除了@TempField和初始值以外都会当做条件来使用
     * @param extendSQL
     *            排序SQL语句
     * */
    public <T> List<T> findByProperties(T t, String extendSQL) {
        DAOUtils<T> daoUtils = new DAOUtils<T>(t.getClass());
        String sql = daoUtils.getSelect(t) + (CommonUtils.isNull(extendSQL) ? "" :" "+ extendSQL.trim());
        LOG.debug(sql);
        Map<String, Object> paramMap = daoUtils.getParamMap(t);
        return getNamedParameterJdbcTemplate().query(sql, paramMap, daoUtils.getRowMapper(null));
    }

    /***
     * @description 命名规则正常的情况下，可使用，只返回一个结果
     * @author lvly
     * @since 2012-08-07
     * @param t
     * */
    public <T> T findAndReturnOnly(T t) {
        return findAndReturnOnly(t, "");
    }
    /***
     * @description 命名规则正常的情况下，可使用，只返回一个结果
     * @author lvly
     * @since 2012-08-07
     * @param t
     * */
    public <T> T findAndReturnOnly(T t,String extend) {
        List<T> list = findByProperties(t,extend+" limit 0,1");
        return !CommonUtils.isNull(list) ? CommonUtils.first(list) : null;
    }

    /***
     * @description 命名规则正常的情况下，可使用，返回布尔值，查询结果集存不存在
     * @author lvly
     * @since 2012-08-07
     * @param t
     * @return boolean 记录存在吗？
     * */
    public <T> boolean findAndReturnIsExist(T t) {
        return !CommonUtils.isNull(findByProperties(t));
    }
    /***
     * @description 命名规则正常的情况下，可使用，返回布尔值，查询结果集存不存在
     * @author lvly
     * @since 2012-08-07
     * @param t
     * @return boolean 记录存在吗？
     * */
    public <T> boolean findAndReturnIsExist(T t,String extend) {
        return !CommonUtils.isNull(findByProperties(t,extend));
    }

    /***
     * @description 命名规则正常的情况下，可使用，基本更新查询,如果不想更新某个字段的值
     * 				那么就传进来之前，赋值成null或者0即可
     * @author lvly
     * @since 2012-08-07
     * @param t
     * */
    public <T> int update(T t) {
        DAOUtils<T> daoUtils = new DAOUtils<T>(t.getClass());
        String sql = daoUtils.getUpdate(t);
        LOG.debug(sql);
        Map<String,Object> paramMap=daoUtils.getParamMap(t);
        return getNamedParameterJdbcTemplate().update(sql, paramMap);
    }

    /***
     * @description 命名规则正常的情况下，可使用，记录的数量
     * @author lvly
     * @since 2012-08-07
     * @param int 匹配记录
     * */
    public <T> int getCount(T t) {
    	DAOUtils<T> daoUtils = new DAOUtils<T>(t.getClass());
    	String sql = daoUtils.getSelect(t).replace("*", "count(*)");
        LOG.debug(sql);
        Map<String, Object> paramMap = daoUtils.getParamMap(t);
        return getNamedParameterJdbcTemplate().queryForInt(sql, paramMap);
    }

    /***
     * @description 命名规则正常的情况下，可使用，基本删除，未经测试，请谨慎使用
     * @author lvly
     * @since 2012-08-07
     * @param t
     * */
    public <T> void remove(T t) {
        DAOUtils<T> daoUtils = new DAOUtils<T>(t.getClass());
        String sql = daoUtils.getDelete(t);
        LOG.debug(sql);
        Map<String, Object> paramMap = daoUtils.getParamMap(t);
        getNamedParameterJdbcTemplate().update(sql, paramMap);
    }

}
