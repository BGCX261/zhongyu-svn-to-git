package com.piaoyou.service.impl;

import java.util.List;
import java.util.Map;

import com.piaoyou.dao.CommonDao;
import com.piaoyou.service.CommonService;
public class CommonServiceImpl implements CommonService {
	private CommonDao commonDao;

	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	@Override
	public int count(String sqlAlise) {
		return commonDao.count(sqlAlise);
	}

	@Override
	public int deleteObject(String sqlAlise, int primaryKey) {
		
		return commonDao.deleteObject(sqlAlise, primaryKey);
	}

	@Override
	public <T> int insertObject(String sqlAlise, T t) {
		return commonDao.insertObject(sqlAlise, t);
	}

	@Override
	public <T> T selectObject(String sqlAlise, int primaryKey) {
		return commonDao.selectObject(sqlAlise, primaryKey);
	}

	@Override
	public <T> List<T> selectObjects(String sqlAlise, Map<Object, Object> params) {
		
		return commonDao.selectObjects(sqlAlise, params);
	}

	@Override
	public <T> boolean updateObject(String sqlAlise, T t) {
		
		return commonDao.updateObject(sqlAlise, t);
	}

	@Override
	public <T> T checkByColumn(String sqlAlise, Map<Object, Object> params) {
		return commonDao.checkByColumn(sqlAlise, params); 
	}

	@Override
	public int countParams(String sqlAlise, Map<Object, Object> params) {
		return commonDao.countParams(sqlAlise,params);
	}

	@Override
	public int deleteObject(String sqlAlise, Map<Object, Object> params) {
		return commonDao.deleteObject(sqlAlise, params);
	}

	@Override
	public <T> T selectObject(String sqlAlise, Map<Object, Object> params) {
		return commonDao.selectObject(sqlAlise, params);
	}

}
