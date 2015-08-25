package com.piaoyou.bean;

import java.io.Serializable;

import com.piaoyou.analyzer.index.IShow;

public class Show2 implements IShow, Serializable{


	private String bigImgPath;
	private String cityId;
	private String cityName;
	private String endTime;
	private String imgPath;
	private String introduction;
	private float maxPrice;
	private float minPrice;
	private String price;
	private String showID;
	private String showName;
	private String showTime;
	private String siteID;
	private String siteName;
	private String smallImgPath;
	private String startTime;
	private String status;
	private String showTypeID;
	private String showTypeName;
	private String grade;
	private String updateTime;
	private String isFinish;
	private String remote_img_path;
	private String agencyID;
	private String seatImg;
	private String siteMapCoord;
	private String city_img;
	public String getCity_img() {
		return city_img;
	}
	public void setCity_img(String cityImg) {
		city_img = cityImg;
	}
	public String getSiteMapCoord() {
		return siteMapCoord;
	}
	public void setSiteMapCoord(String siteMapCoord) {
		this.siteMapCoord = siteMapCoord;
	}
	public String getSeatImg() {
		return seatImg;
	}
	public void setSeatImg(String seatImg) {
		this.seatImg = seatImg;
	}
	public String getAgencyID() {
		return agencyID;
	}
	public void setAgencyID(String agencyID) {
		this.agencyID = agencyID;
	}
	public String getRemote_img_path() {
		return remote_img_path;
	}
	public void setRemote_img_path(String remoteImgPath) {
		remote_img_path = remoteImgPath;
	}
	public String getIsFinish() {
		return isFinish;
	}
	public void setIsFinish(String isFinish) {
		this.isFinish = isFinish;
	}

	private int type ;

	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public String getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
	public String getBigImgPath() {
		return bigImgPath;
	}

	public String getCityId() {
		return cityId;
	}

	public String getCityName() {
		return cityName;
	}

	public String getEndTime() {
		return endTime;
	}

	public String getImgPath() {
		return imgPath;
	}

	public float getMaxPrice() {
		return maxPrice;
	}

	public float getMinPrice() {
		return minPrice;
	}

	public String getPrice() {
		return price;
	}

	public String getShowID() {
		return showID;
	}

	public String getShowName() {
		return showName;
	}

	public String getShowTime() {
		return showTime;
	}

	public String getSiteID() {
		return siteID;
	}

	public String getSiteName() {
		return siteName;
	}

	public String getSmallImgPath() {
		return smallImgPath;
	}

	public String getStartTime() {
		return startTime;
	}

	public String getStatus() {
		return status;
	}

	public int getType() {
		return type;
	}

	public void setBigImgPath(String bigImgPath) {
		this.bigImgPath = bigImgPath;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public void setImgPath(String imgPath) {
		this.imgPath = imgPath;
	}

	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}

	public void setMinPrice(float minPrice) {
		this.minPrice = minPrice;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public void setShowID(String showID) {
		this.showID = showID;
	}

	public void setShowName(String showName) {
		this.showName = showName;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}

	public void setSiteID(String siteID) {
		this.siteID = siteID;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public void setSmallImgPath(String smallImgPath) {
		this.smallImgPath = smallImgPath;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getIntroduction() {
		return introduction;
	}

	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}

	public String getShowTypeID() {
		return showTypeID;
	}

	public void setShowTypeID(String showTypeID) {
		this.showTypeID = showTypeID;
	}

	public String getShowTypeName() {
		return showTypeName;
	}

	public void setShowTypeName(String showTypeName) {
		this.showTypeName = showTypeName;
	}

	@Override
	public String[] getPropertyName() {
		// TODO Auto-generated method stub
		return new String[] { "bigImgPath", "cityId", "cityName", "endTime",
				"imgPath", "maxPrice", "minPrice", "price",
				"showID", "showName", "showTime", "siteID", "siteName",
				"smallImgPath", "startTime", "status", "showTypeID",
				"showTypeName", "type" };
		//return new String[] { "showID", "showName","siteName", "siteID","type","cityId", "cityName"};
		
	}
}
