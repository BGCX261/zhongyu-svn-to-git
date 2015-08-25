package com.piaoyou.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;

import com.piaoyou.dao.CommonDao;
import com.piaoyou.domain.Category;
import com.piaoyou.domain.CommonInfo;
import com.piaoyou.service.CommonInfoService;
import com.piaoyou.util.CacheKey;
import com.piaoyou.util.CacheUtil;
import com.piaoyou.util.Const;
import com.piaoyou.util.DateTime;
import com.piaoyou.util.GetByCache;
public class CommonInfoServiceImpl implements CommonInfoService {
	private CommonDao commonDao;
	private CacheUtil cu = CacheUtil.getInstance();

	public CommonDao getCommonDao() {
		return commonDao;
	}
	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}
	
	private SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	@Override
	public Map<Object,Object> getCommonInfoTimeMap(){
		Map<Object,Object> timeMap = new HashMap<Object,Object>();
		Map<Object,Object> params =new HashMap<Object,Object>();
		List<CommonInfo> result = null;
		result = GetByCache.loadByCache(cu,CacheKey.COMMON_TIME_MAP);
		if(result==null){
			result=commonDao.selectObjects("commonInfo.getCommonTimeList", params);
		}
		cu.putCache(CacheKey.COMMON_TIME_MAP, result,DateTime.getPeriodOfvalidityTime());
		if(result!=null){
			for(CommonInfo commonInfo:result){
				timeMap.put(commonInfo.getId()+"", commonInfo);
			}
		}
		return timeMap;
	}
	@Override
	public Map<Object, CommonInfo> getCommonInfoPriceMap() {
		Map<Object,CommonInfo> priceMap = new HashMap<Object,CommonInfo>();
		Map<Object,Object> params =new HashMap<Object,Object>();
		List<CommonInfo> result = null;
		result = GetByCache.loadByCache(cu,CacheKey.COMMON_PRICE_MAP);
		if(result==null){
			result = commonDao.selectObjects("commonInfo.getCommonPriceList", params);
		}
		cu.putCache(CacheKey.COMMON_PRICE_MAP, result,DateTime.getPeriodOfvalidityTime());
		if(result!=null){
			for(CommonInfo commonInfo:result){
				priceMap.put(commonInfo.getId()+"", commonInfo);
			}
		}
		return priceMap;
	}
	
	@Override
	public List<CommonInfo> getCityList() {
		Map<Object,Object> params = new HashMap<Object,Object>();
		List<CommonInfo> result = null;
		result = GetByCache.loadByCache(cu,CacheKey.ALL_CITY_LIST);
		if(result==null){
			result=commonDao.selectObjects("common.getCityListByShow", params);
		}
		cu.putCache(CacheKey.ALL_CITY_LIST, result,DateTime.getPeriodOfvalidityTime());
		return result;
	}
	@Override
	public List<CommonInfo> getCommonInfoList(int categoryId, String order,String cityId,String pageIndex, String pageCount) {
		
		Map<Object,Object> params = new HashMap<Object,Object>();
		params = new HashMap<Object,Object>();
		params.put("category_id", categoryId);
		params.put("city_id", cityId);
		params.put("pageIndex", (Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageCount));
		params.put("pageCount", Integer.parseInt(pageCount));
		params.put("order", order);
		
		
		int key_code = params.toString().hashCode();
		
		List<CommonInfo> commonList = null;
		commonList = GetByCache.loadByCache(cu,CacheKey.COMMONINFO_LIST+key_code);
		
		if(commonList==null){
			commonList=commonDao.selectObjects("commonInfo.getCommonList", params);
			Map<Object,Object> timeMap = this.getCommonInfoTimeMap();
			Map<Object,CommonInfo> priceMap = this.getCommonInfoPriceMap();
			for(int i=0,length=commonList.size();i<length;i++){
				CommonInfo commonInfo = commonList.get(i);
				String introduction = commonInfo.getIntroduction().replaceAll("\\s*", "");
				if(introduction!=null&&!"".equals(introduction)){
					introduction = Jsoup.parse(introduction).text().trim();
					if(introduction.length()>350){
						introduction=introduction.substring(0,350);
					}
					commonList.get(i).setIntroduction(introduction);
				}
				
				commonInfo.setCategory(getShow_category(commonInfo));
				
				if(commonInfo.getStatus()==0){
					commonInfo.setShow_status("预售中");
				}else if(commonInfo.getStatus()==2){
					commonInfo.setShow_status("已过期");
				}else{
					commonInfo.setShow_status("热售中");
				}
				CommonInfo time = (CommonInfo)timeMap.get(commonInfo.getId()+"");
				if(time!=null){
					String show_time = time.getShow_time();
					String limit_time = time.getLimit_time();
					if(show_time!=null&&!"null".equals(show_time)&&!show_time.contains("全年")){
						Calendar calendar = Calendar.getInstance();
						try {
							calendar.setTime(YYYY_MM_DD_HH_MM.parse(show_time));
							int year = calendar.get(Calendar.YEAR);
							int month=calendar.get(Calendar.MONTH)+1;    
							int day =calendar.get(Calendar.DAY_OF_MONTH);  
							int hour =calendar.get(Calendar.HOUR);  
							int minute =calendar.get(Calendar.MINUTE);
							int week = calendar.get(Calendar.DAY_OF_WEEK);
							int AM_PM = calendar.get(Calendar.AM_PM);
							if(AM_PM==0){
								show_time = "早";
							}else{
								show_time = "晚";
							}
							if(hour<10){
								show_time=show_time+"0"+hour+":";
							}else{
								show_time=show_time+hour+":";
							}
							if(minute<10){
								show_time=show_time+"0"+minute;
							}else{
								show_time=show_time+minute;
							}
							commonList.get(i).setYear(year+"");
							commonList.get(i).setDay(day+"");
							if(month<10){
								commonList.get(i).setMonth("0"+month);
							}else{
								commonList.get(i).setMonth(month+"");
							}
							commonList.get(i).setWeek(Const.week[week-1]);
							commonList.get(i).setShow_time(show_time);
							commonList.get(i).setLimit_time(limit_time);
						} catch (ParseException e) {
							e.printStackTrace();
						}
					}else{
						commonList.get(i).setShow_time(show_time);
					}
					
				}
				CommonInfo commonPrice = priceMap.get(commonInfo.getId()+"");
				if(commonPrice!=null){
					commonList.get(i).setPrice(commonPrice.getPrice());
					commonList.get(i).setDiscount(commonPrice.getDiscount());
					commonList.get(i).setMin_price(commonPrice.getMin_price());
				}
			}
			cu.putCache(CacheKey.COMMONINFO_LIST+key_code, commonList,DateTime.getPeriodOfvalidityTime());
		}
		return commonList;
	}
	@Override
	public List<CommonInfo> getGroupList(int categoryId, String cityId,String name,String order,String pageIndex, String pageCount) {
		Map<Object,Object> params = new HashMap<Object,Object>();
		params = new HashMap<Object,Object>();
		params.put("category_id", categoryId);
		params.put("city_id", cityId);
		params.put("name", name);
		params.put("order", order);
		params.put("pageIndex", (Integer.parseInt(pageIndex)-1)*Integer.parseInt(pageCount));
		params.put("pageCount", Integer.parseInt(pageCount));
		Map<Object,CommonInfo> priceMap = this.getCommonInfoPriceMap();
		List<CommonInfo> groupList = commonDao.selectObjects("group.getGroupList", params);
		for(int i=0;i<groupList.size();i++){
			CommonInfo price = priceMap.get(groupList.get(i).getId()+"");
			if(price!=null){
				groupList.get(i).setPrice(price.getPrice());
				groupList.get(i).setDiscount(price.getDiscount());
				groupList.get(i).setMin_price(price.getMin_price());
				groupList.get(i).setAgency_name(price.getAgency_name());
			}
		}
		
		return groupList;
	}
	@Override
	public String getCommonTotalCount(int categoryId, String cityId) {
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("city_id", cityId);
		params.put("category_id", categoryId);
		int key_code = params.toString().hashCode();
		String totalCount = null ;
		totalCount = (String)GetByCache.loadByCacheBasic(cu, CacheKey.COMMONINFO_LIST_COUNT+key_code);
		if(totalCount == null){
			totalCount = commonDao.selectObject("commonInfo.getCommonTotalCount", params);
			cu.putCache(CacheKey.COMMONINFO_LIST_COUNT+key_code, totalCount);
		}
		return totalCount;
	}
	@Override
	public String getGroupTotalCount(int categoryId, String cityId,String name) {
		Map<Object,Object> params = new HashMap<Object,Object>();
		String totalCount="0";
		params.put("city_id", cityId);
		params.put("name", name);
		params.put("category_id", categoryId);
		totalCount = commonDao.selectObject("group.getGroupTotalCount", params);
		return totalCount;
	}
	@Override
	public CommonInfo getCommonInfoDetail(int common_id) {
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("id", common_id);
		//获取演出基本信息
		CommonInfo commonInfo = commonDao.selectObject("commonInfo.getCommonInfoDetail", params);
		//获取演出时间
		 Map<Object,Object> timeMap = this.getCommonInfoTimeMap();
		 CommonInfo time = (CommonInfo)timeMap.get(common_id+"");
		 if(time!=null){
			 String show_time = time.getShow_time();
			 String limit_time = time.getLimit_time();
			 if(show_time!=null&&!show_time.contains("全年")){
			    Calendar calendar = Calendar.getInstance();
			    try {
					calendar.setTime(YYYY_MM_DD_HH_MM.parse(show_time));
					int year = calendar.get(Calendar.YEAR);
					int month=calendar.get(Calendar.MONTH)+1;    
					int day =calendar.get(Calendar.DAY_OF_MONTH);  
					int hour =calendar.get(Calendar.HOUR_OF_DAY);  
					int minute =calendar.get(Calendar.MINUTE);
					int week = calendar.get(Calendar.DAY_OF_WEEK);
					show_time="";
					if(hour<10){
						show_time=show_time+"0"+hour+":";
					}else{
						show_time=show_time+hour+":";
					}
					if(minute<10){
						show_time=show_time+"0"+minute;
					}else{
						show_time=show_time+minute;
					}
					commonInfo.setYear(year+"");
					commonInfo.setDay(day+"");
					commonInfo.setMonth(month+"");
					commonInfo.setWeek(Const.week[week-1]);
					commonInfo.setShow_time(show_time);
					commonInfo.setLimit_time(limit_time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			 }
		 }
		 
		Map<Object,CommonInfo> priceMap = this.getCommonInfoPriceMap();
		CommonInfo commonPrice = priceMap.get(common_id+"");
		if(commonPrice!=null){
			commonInfo.setPrice(commonPrice.getPrice());
			commonInfo.setDiscount(commonPrice.getDiscount());
			commonInfo.setMin_price(commonPrice.getMin_price());
			commonInfo.setMainURL(commonPrice.getMainURL());
		}
		commonInfo.setCategory(getShow_category(commonInfo));
		return commonInfo;
	}
	@Override
	public CommonInfo getGroupDetail(int common_id) {
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("id", common_id);
		//获取演出基本信息
		CommonInfo commonInfo = commonDao.selectObject("group.getGroupDetail", params);
		//获取演出时间
		 Map<Object,Object> timeMap = this.getCommonInfoTimeMap();
		 CommonInfo time  = (CommonInfo)timeMap.get(common_id+"");
		 if(time!=null){
			 String show_time = time.getShow_time();
			 String limit_time = time.getLimit_time();
			 if(!"null".equals(show_time)&&!show_time.contains("全年")){
			    Calendar calendar = Calendar.getInstance();
			    try {
					calendar.setTime(YYYY_MM_DD_HH_MM.parse(show_time));
					int year = calendar.get(Calendar.YEAR);
					int month=calendar.get(Calendar.MONTH)+1;    
					int day =calendar.get(Calendar.DAY_OF_MONTH);  
					int hour =calendar.get(Calendar.HOUR_OF_DAY);  
					int minute =calendar.get(Calendar.MINUTE);
					int week = calendar.get(Calendar.DAY_OF_WEEK);
					show_time="";
					if(hour<10){
						show_time=show_time+"0"+hour+":";
					}else{
						show_time=show_time+hour+":";
					}
					if(minute<10){
						show_time=show_time+"0"+minute;
					}else{
						show_time=show_time+minute;
					}
					commonInfo.setYear(year+"");
					commonInfo.setDay(day+"");
					commonInfo.setMonth(month+"");
					commonInfo.setWeek(Const.week[week-1]);
					commonInfo.setShow_time(show_time);
					commonInfo.setLimit_time(limit_time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			 }
		 }
		Map<Object,CommonInfo> priceMap = this.getCommonInfoPriceMap();
		
		CommonInfo commonPrice = priceMap.get(common_id+"");
		if(commonPrice!=null){
			commonInfo.setPrice(commonPrice.getPrice());
			commonInfo.setDiscount(commonPrice.getDiscount());
			commonInfo.setMin_price(commonPrice.getMin_price());
			commonInfo.setMainURL(commonPrice.getMainURL());
		}
		return commonInfo;
	}
	private Category getShow_category(CommonInfo commonInfo){
		Category category = new Category();
		int showType = commonInfo.getShow_type();
		if(commonInfo.getType()==0){
			if(showType==1){
				category.setId(Const.TAOPIAO_YANCHANGHUI_ID);
				category.setTitle("演唱会");
			}else if(showType==2){  
				category.setId(Const.TAOPIAO_GUDIAN_ID);
				category.setTitle("古典");
			}else if(showType==3){
				category.setId(Const.TAOPIAO_HUAJU_ID);
				category.setTitle("话剧");
			}else if(showType==4){
				category.setId(Const.TAOPIAO_GUDIAN_ID);
				category.setTitle("古典");
			}else if(showType==5){
				category.setId(Const.TAOPIAO_XIJU_ID);
				category.setTitle("戏剧曲艺");
			}else if(showType==6){
				category.setId(Const.TAOPIAO_QINZI_ID);
				category.setTitle("亲子");
			}else if(showType==7){
				category.setId(Const.TAOPIAO_SAISHI_ID);
				category.setTitle("赛事");
			}
		}else if(commonInfo.getType()==1){
			category.setId(Const.TAOPIAO_DIANYING_ID);
			category.setTitle("电影");
		}
		return category;
	}
	@Override
	public int insertHistoty(int userId, int commonId) {
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("user_id", userId);
		params.put("common_id", commonId);
		int isExist = Integer.parseInt((String)commonDao.selectObject("common.isHistoryIsExist", params));
		if(isExist==0){
			CommonInfo tmp = new CommonInfo();
			tmp.setId(commonId);
			tmp.setUser_id(userId);
			commonDao.insertObject("common.insertHistory", tmp);
		}
		return 1;
	}
	
	
	@Override
	public List<CommonInfo> getTimeList(String commonId){
		List<CommonInfo> timeList = new ArrayList<CommonInfo>();
		Map<Object,Object> params = new HashMap<Object,Object>();
		params.put("id", commonId);
		timeList = commonDao.selectObjects("commonInfo.getTimeList", params);
		for(int i=0;i<timeList.size();i++){
			 String show_time = timeList.get(i).getShow_time();
			 String limit_time = timeList.get(i).getLimit_time();
			 if(!"null".equals(show_time)&&!show_time.contains("全年")){
			    Calendar calendar = Calendar.getInstance();
			    try {
					calendar.setTime(YYYY_MM_DD_HH_MM.parse(show_time));
					int year = calendar.get(Calendar.YEAR);
					int month=calendar.get(Calendar.MONTH)+1;    
					int day =calendar.get(Calendar.DAY_OF_MONTH);  
					int hour =calendar.get(Calendar.HOUR_OF_DAY);  
					int minute =calendar.get(Calendar.MINUTE);
					int week = calendar.get(Calendar.DAY_OF_WEEK);
					show_time="";
					if(hour<10){
						show_time=show_time+"0"+hour+":";
					}else{
						show_time=show_time+hour+":";
					}
					if(minute<10){
						show_time=show_time+"0"+minute;
					}else{
						show_time=show_time+minute;
					}
					timeList.get(i).setYear(year+"");
					timeList.get(i).setDay(day+"");
					timeList.get(i).setMonth(month+"");
					timeList.get(i).setWeek(Const.week[week-1]);
					timeList.get(i).setShow_time(show_time);
					timeList.get(i).setLimit_time(limit_time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			 }
		 }
		return timeList;
	}
}
