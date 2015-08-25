package com.piaoyou.dao;

import java.util.List;
import java.util.Map;

public interface CommonDao {
	public <T> int insertObject(String sqlAlise,T t);
	
	public int deleteObject(String sqlAlise,int primaryKey);
	
	public int deleteObject(String sqlAlise,Map<Object,Object> params);
	
	public <T> T selectObject(String sqlAlise,int primaryKey);
	
	public <T> T selectObject(String sqlAlise, Map<Object, Object> params);
	
	public <T> List<T> selectObjects(String sqlAlise,Map<Object,Object> params);
	
	public <T> boolean updateObject(String sqlAlise,T t);
	
	public int count(String sqlAlise);
	
	public int countParams(String sqlAlise, Map<Object, Object> params);
	
	public <T> T checkByColumn(String sqlAlise,Map<Object,Object> params);
}
