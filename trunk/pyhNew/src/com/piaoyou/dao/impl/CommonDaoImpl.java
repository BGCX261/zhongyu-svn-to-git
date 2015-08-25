package com.piaoyou.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.orm.ibatis.support.SqlMapClientDaoSupport;

import com.piaoyou.dao.CommonDao;

public class CommonDaoImpl extends SqlMapClientDaoSupport implements CommonDao {

	@Override
	public int deleteObject(String sqlAlise, int primaryKey) {
		return  this.getSqlMapClientTemplate().delete(sqlAlise, primaryKey);
	}

	@Override
	public <T> int insertObject(String sqlAlise, T t) {
		Object obj = this.getSqlMapClientTemplate().insert(sqlAlise,t);
		return obj==null?0:(Integer)obj;
	}

	@Override
	public <T> T selectObject(String sqlAlise, int primaryKey) {
		return (T) this.getSqlMapClientTemplate().queryForObject(sqlAlise, primaryKey);
	}

	@Override
	public <T> List<T> selectObjects(String sqlAlise, Map<Object, Object> params) {
		return (List<T>)this.getSqlMapClientTemplate().queryForList(sqlAlise, params);
	}

	@Override
	public <T> boolean updateObject(String sqlAlise, T t) {
		return this.getSqlMapClientTemplate().update(sqlAlise, t)>0;
	}

	@Override
	public int count(String sqlAlise) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(sqlAlise);
	}

	@Override
	public <T> T checkByColumn(String sqlAlise, Map<Object, Object> params) {
		return (T) this.getSqlMapClientTemplate().queryForObject(sqlAlise, params);
	}

	@Override
	public int countParams(String sqlAlise, Map<Object, Object> params) {
		return (Integer) this.getSqlMapClientTemplate().queryForObject(sqlAlise,params);
	}

	@Override
	public int deleteObject(String sqlAlise, Map<Object, Object> params) {
		return  this.getSqlMapClientTemplate().delete(sqlAlise, params);
	}

	@Override
	public <T> T selectObject(String sqlAlise, Map<Object, Object> params) {
		return (T) this.getSqlMapClientTemplate().queryForObject(sqlAlise, params);
	}
}
