package com.piaoyou.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CommonInfo implements Serializable {
	private static final long serialVersionUID = 2235414336823635225L;
	private int id;
	private String name; 
	private String name_clean;
	private String img_url ;  
	private String heng_image_path;
	private String shu_image_path;
	private String time; 
	private String movie_type;
	private int site_id;
	private String site_name;
	private String site_map_coord;
	private int status;
	private int is_first_agency;
	private String show_time;
	private String limit_time;
	private String introduction;
	private int show_type;
	private String type_name;
	private int type;
	private int agency_id ;
	private String price;		//票价（如果没有出票或者票价待定 票价为-1）
	private String mainURL;		//买票的主页面
	private String discount;    //折扣
	private String cityName;
	private String address;  //团购地址
	private String sub_name;
	private String city_id ;
	private String city_name;
	private String agency_name;
	
	private String min_price;  //最低票价
	private int recommend_count; //被推荐次数
	
	private String year;
	private String month;
	private String day;
	private String week;
	
	private String show_status;
	private int cityId;
	private int user_id;
	
	private String piaoshang_tel;
	
	
	
	
	
	
	
	public String getPiaoshang_tel() {
		return piaoshang_tel;
	}
	public void setPiaoshang_tel(String piaoshangTel) {
		piaoshang_tel = piaoshangTel;
	}
	//扩展字段
	private Category category;
	private List<UserRecommend> recommens = new ArrayList<UserRecommend>();
	private int districtID ;
	private int districtName;
	
	private List<CommonInfo> timeList = new ArrayList<CommonInfo>();

	public List<CommonInfo> getTimeList() {
		return timeList;
	}
	public void setTimeList(List<CommonInfo> timeList) {
		this.timeList = timeList;
	}
	public String getSite_map_coord() {
		return site_map_coord;
	}
	public void setSite_map_coord(String siteMapCoord) {
		site_map_coord = siteMapCoord;
	}
	public int getDistrictID() {
		return districtID;
	}
	public void setDistrictID(int districtID) {
		this.districtID = districtID;
	}
	public int getDistrictName() {
		return districtName;
	}
	public void setDistrictName(int districtName) {
		this.districtName = districtName;
	}
	public String getMovie_type() {
		return movie_type;
	}
	public void setMovie_type(String movieType) {
		movie_type = movieType;
	}
	
	public List<UserRecommend> getRecommens() {
		return recommens;
	}
	public void setRecommens(List<UserRecommend> recommens) {
		this.recommens = recommens;
	}
	public String getType_name() {
		return type_name;
	}
	public void setType_name(String typeName) {
		type_name = typeName;
	}
	public String getCity_id() {
		return city_id;
	}
	public void setCity_id(String cityId) {
		city_id = cityId;
	}
	public String getHeng_image_path() {
		return heng_image_path;
	}
	public void setHeng_image_path(String hengImagePath) {
		heng_image_path = hengImagePath;
	}
	public String getShu_image_path() {
		return shu_image_path;
	}
	public void setShu_image_path(String shuImagePath) {
		shu_image_path = shuImagePath;
	}
	public Category getCategory() {
		return category;
	}
	public void setCategory(Category category) {
		this.category = category;
	}
	public int getRecommend_count() {
		return recommend_count;
	}
	public String getAgency_name() {
		return agency_name;
	}
	public void setAgency_name(String agencyName) {
		agency_name = agencyName;
	}
	public String getShow_status() {
		return show_status;
	}
	public void setShow_status(String showStatus) {
		show_status = showStatus;
	}
	public void setRecommend_count(int recommendCount) {
		recommend_count = recommendCount;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public String getCity_name() {
		return city_name;
	}
	public void setCity_name(String cityName) {
		city_name = cityName;
	}
	public String getSub_name() {
		return sub_name;
	}
	public void setSub_name(String subName) {
		sub_name = subName;
	}
	public String getMin_price() {
		return min_price;
	}
	public void setMin_price(String minPrice) {
		min_price = minPrice;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
		this.cityName = cityName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIs_first_agency() {
		return is_first_agency;
	}
	public void setIs_first_agency(int isFirstAgency) {
		is_first_agency = isFirstAgency;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName_clean() {
		return name_clean;
	}
	public void setName_clean(String nameClean) {
		name_clean = nameClean;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public String getShow_time() {
		return show_time;
	}
	public void setShow_time(String showTime) {
		show_time = showTime;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String imgUrl) {
		img_url = imgUrl;
	}
	public String getLimit_time() {
		return limit_time;
	}
	public void setLimit_time(String limitTime) {
		limit_time = limitTime;
	}
	public int getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(int agencyId) {
		agency_id = agencyId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getMainURL() {
		return mainURL;
	}
	public void setMainURL(String mainURL) {
		this.mainURL = mainURL;
	}
	public int getSite_id() {
		return site_id;
	}
	public void setSite_id(int siteId) {
		site_id = siteId;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getSite_name() {
		return site_name;
	}
	public void setSite_name(String siteName) {
		this.site_name = siteName;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getShow_type() {
		return show_type;
	}
	public void setShow_type(int showType) {
		show_type = showType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getWeek() {
		return week;
	}
	public void setWeek(String week) {
		this.week = week;
	}
	
}
