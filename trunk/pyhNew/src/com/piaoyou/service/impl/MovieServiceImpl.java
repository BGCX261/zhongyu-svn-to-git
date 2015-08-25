package com.piaoyou.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.piaoyou.dao.CommonDao;
import com.piaoyou.domain.Category;
import com.piaoyou.domain.CommonInfo;
import com.piaoyou.service.MovieService;
import com.piaoyou.util.CacheKey;
import com.piaoyou.util.CacheUtil;
import com.piaoyou.util.Const;
import com.piaoyou.util.DateTime;
import com.piaoyou.util.GetByCache;

public class MovieServiceImpl implements MovieService {
	private CommonDao commonDao;
	private CacheUtil cu = CacheUtil.getInstance();

	public CommonDao getCommonDao() {
		return commonDao;
	}

	public void setCommonDao(CommonDao commonDao) {
		this.commonDao = commonDao;
	}

	private SimpleDateFormat YYYY_MM_DD_HH_MM = new SimpleDateFormat(
			"yyyy-MM-dd");

	@Override
	public Map<Object, Object> getCommonInfoTimeMap() {
		Map<Object, Object> timeMap = new HashMap<Object, Object>();
		Map<Object, Object> params = new HashMap<Object, Object>();
		List<CommonInfo> result = null;
		result = GetByCache.loadByCache(cu, CacheKey.COMMON_TIME_MAP);
		if (result == null) {
			result = commonDao.selectObjects("commonInfo.getCommonTimeList",
					params);
		}
		cu.putCache(CacheKey.COMMON_TIME_MAP, result, DateTime
				.getPeriodOfvalidityTime());
		if (result != null) {
			for (CommonInfo commonInfo : result) {
				timeMap.put(commonInfo.getId() + "", commonInfo.getShow_time());
			}
		}
		return timeMap;
	}

	@Override
	public Map<Object, CommonInfo> getCommonInfoPriceMap() {
		Map<Object, CommonInfo> priceMap = new HashMap<Object, CommonInfo>();
		Map<Object, Object> params = new HashMap<Object, Object>();
		List<CommonInfo> result = null;
		result = GetByCache.loadByCache(cu, CacheKey.COMMON_PRICE_MAP);
		if (result == null) {
			result = commonDao.selectObjects("commonInfo.getCommonPriceList",
					params);
		}
		cu.putCache(CacheKey.COMMON_PRICE_MAP, result, DateTime
				.getPeriodOfvalidityTime());
		if (result != null) {
			for (CommonInfo commonInfo : result) {
				priceMap.put(commonInfo.getId() + "", commonInfo);
			}
		}
		return priceMap;
	}

	@Override
	public List<CommonInfo> getCommonInfoList(int categoryId, String order,String pageIndex,
			String pageCount) {
		Map<Object, Object> params = new HashMap<Object, Object>();
		params = new HashMap<Object, Object>();
		params.put("category_id", categoryId);
		params.put("pageIndex", (Integer.parseInt(pageIndex) - 1)
				* Integer.parseInt(pageCount));
		params.put("pageCount", Integer.parseInt(pageCount));
		List<CommonInfo> commonList = commonDao.selectObjects(
				"movie.getMovieList", params);
		Map<Object, Object> timeMap = this.getCommonInfoTimeMap();
		Map<Object, CommonInfo> priceMap = this.getCommonInfoPriceMap();
		for (int i = 0, length = commonList.size(); i < length; i++) {
			CommonInfo commonInfo = commonList.get(i);
			String time = timeMap.get(commonInfo.getId() + "") + "";
			if (time != null && !"null".equals(time) && !time.contains("全年")) {
				Calendar calendar = Calendar.getInstance();
				try {
					calendar.setTime(YYYY_MM_DD_HH_MM.parse(time));
					int year = calendar.get(Calendar.YEAR);
					int month = calendar.get(Calendar.MONTH) + 1;
					int day = calendar.get(Calendar.DAY_OF_MONTH);
					int week = calendar.get(Calendar.DAY_OF_WEEK);
					int AM_PM = calendar.get(Calendar.AM_PM);
					if (AM_PM == 0) {
						time = "早";
					} else {
						time = "晚";
					}
					commonList.get(i).setYear(year + "");
					commonList.get(i).setDay(day + "");
					if (month < 10) {
						commonList.get(i).setMonth("0" + month);
					} else {
						commonList.get(i).setMonth(month + "");
					}
					commonList.get(i).setWeek(Const.week[week - 1]);
					commonList.get(i).setShow_time(time);
				} catch (ParseException e) {
					e.printStackTrace();
				}
			} else {
				commonList.get(i).setShow_time(time);
			}

			CommonInfo commonPrice = priceMap.get(commonInfo.getId() + "");
			if (commonPrice != null) {
				commonList.get(i).setPrice(commonPrice.getPrice());
				commonList.get(i).setDiscount(commonPrice.getDiscount());
				commonList.get(i).setMin_price(commonPrice.getMin_price());
				commonList.get(i).setMainURL(commonPrice.getMainURL());
			}
		}
		return commonList;
	}

	@Override
	public String getCommonTotalCount(int categoryId) {
		Map<Object, Object> params = new HashMap<Object, Object>();
		String totalCount = "0";
		params.put("category_id", categoryId);
		totalCount = commonDao.selectObject("movie.getMovieTotalCount", params);
		return totalCount;
	}

	@Override
	public CommonInfo getCommonInfoDetail(int common_id) {
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("id", common_id);
		// 获取演出基本信息
		CommonInfo commonInfo = commonDao.selectObject(
				"commonInfo.getCommonInfoDetail", params);
		Category category = getShow_category(commonInfo);
		commonInfo.setCategory(category);
		// 获取演出时间
		Map<Object, Object> timeMap = this.getCommonInfoTimeMap();
		String time = timeMap.get(common_id + "") + "";
		if (time != null && !time.contains("全年")) {
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(YYYY_MM_DD_HH_MM.parse(time));
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				int week = calendar.get(Calendar.DAY_OF_WEEK);
				time = "";
				if (hour < 10) {
					time = time + "0" + hour + ":";
				} else {
					time = time + hour + ":";
				}
				if (minute < 10) {
					time = time + "0" + minute;
				} else {
					time = time + minute;
				}
				commonInfo.setYear(year + "");
				commonInfo.setDay(day + "");
				commonInfo.setMonth(month + "");
				commonInfo.setWeek(Const.week[week - 1]);
				commonInfo.setShow_time(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Map<Object, CommonInfo> priceMap = this.getCommonInfoPriceMap();
		CommonInfo commonPrice = priceMap.get(common_id + "");
		if (commonPrice != null) {
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
		Map<Object, Object> params = new HashMap<Object, Object>();
		params.put("id", common_id);
		// 获取演出基本信息
		CommonInfo commonInfo = commonDao.selectObject(
				"commonInfo.getGroupDetail", params);
		// 获取演出时间
		Map<Object, Object> timeMap = this.getCommonInfoTimeMap();
		String time = timeMap.get(common_id + "") + "";
		if (!"null".equals(time) && !time.contains("全年")) {
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(YYYY_MM_DD_HH_MM.parse(time));
				int year = calendar.get(Calendar.YEAR);
				int month = calendar.get(Calendar.MONTH) + 1;
				int day = calendar.get(Calendar.DAY_OF_MONTH);
				int hour = calendar.get(Calendar.HOUR_OF_DAY);
				int minute = calendar.get(Calendar.MINUTE);
				int week = calendar.get(Calendar.DAY_OF_WEEK);
				time = "";
				if (hour < 10) {
					time = time + "0" + hour + ":";
				} else {
					time = time + hour + ":";
				}
				if (minute < 10) {
					time = time + "0" + minute;
				} else {
					time = time + minute;
				}
				commonInfo.setYear(year + "");
				commonInfo.setDay(day + "");
				commonInfo.setMonth(month + "");
				commonInfo.setWeek(Const.week[week - 1]);
				commonInfo.setShow_time(time);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		Map<Object, CommonInfo> priceMap = this.getCommonInfoPriceMap();

		CommonInfo commonPrice = priceMap.get("2662");
		if (commonPrice != null) {
			commonInfo.setPrice(commonPrice.getPrice());
			commonInfo.setDiscount(commonPrice.getDiscount());
			commonInfo.setMin_price(commonPrice.getMin_price());
			commonInfo.setMainURL(commonPrice.getMainURL());
		}
		return commonInfo;
	}

	private Category getShow_category(CommonInfo commonInfo) {
		Category category = new Category();
		int showType = commonInfo.getShow_type();
		if (commonInfo.getType() == 0) {
			if (showType == 1) {
				category.setId(Const.TAOPIAO_YANCHANGHUI_ID);
				category.setTitle("演唱会");
			} else if (showType == 2) {
				category.setId(Const.TAOPIAO_GUDIAN_ID);
				category.setTitle("古典");
			} else if (showType == 3) {
				category.setId(Const.TAOPIAO_XIJU_ID);
				category.setTitle("戏剧曲艺");
			} else if (showType == 4) {
				category.setId(Const.TAOPIAO_GUDIAN_ID);
				category.setTitle("古典");
			} else if (showType == 5) {
				category.setId(Const.TAOPIAO_XIJU_ID);
				category.setTitle("戏剧曲艺");
			} else if (showType == 6) {
				category.setId(Const.TAOPIAO_QINZI_ID);
				category.setTitle("亲子");
			} else if (showType == 7) {
				category.setId(Const.TAOPIAO_SAISHI_ID);
				category.setTitle("赛事");
			}
		} else if (commonInfo.getType() == 1) {
			category.setId(Const.TAOPIAO_DIANYING_ID);
			category.setTitle("电影");
		}
		return category;
	}
}
