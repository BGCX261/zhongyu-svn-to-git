package com.piaoyou.service;

import java.util.List;
import java.util.Map;

import com.piaoyou.domain.CommonInfo;

public interface TaopiaoService {

	Map<Object,Object> getCommonInfoTimeMap();
	Map<Object,CommonInfo> getCommonInfoPriceMap();
	List<CommonInfo> getCommonInfoList(int category_id,String city_id,String pageIndex,String pageCount);
	String getCommonTotalCount(int category_id,String city_id);
	List<CommonInfo> getGroupList(int category_id,String city_id,String pageIndex,String pageCount);
	String getGroupTotalCount(int category_id,String city_id);
	CommonInfo getCommonInfoDetail(int id);
	CommonInfo getGroupDetail(int commonId);
	
	
}
