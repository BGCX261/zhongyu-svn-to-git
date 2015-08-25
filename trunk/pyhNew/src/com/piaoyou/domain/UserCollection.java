package com.piaoyou.domain;

import java.io.Serializable;

public class UserCollection implements Serializable {
	private static final long serialVersionUID = -5774996199929020551L;
	private int id;
	private int common_id;
	private int user_id;
	private int show_type;
	private String create_date;
	
	
	
	public int getShow_type() {
		return show_type;
	}
	public void setShow_type(int showType) {
		show_type = showType;
	}
	public String getLocation_url() {
		return location_url;
	}
	public void setLocation_url(String locationUrl) {
		location_url = locationUrl;
	}
	private String location_url;
	
	private String status;
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getId() {
		return id;
	}
	public String getName_clean() {
		return name_clean;
	}
	public void setName_clean(String nameClean) {
		name_clean = nameClean;
	}
	public String getCommon_time() {
		return common_time;
	}
	public void setCommon_time(String commonTime) {
		common_time = commonTime;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getSiteName() {
		return siteName;
	}
	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCommon_id() {
		return common_id;
	}
	public void setCommon_id(int commonId) {
		common_id = commonId;
	}
	public int getUser_id() {
		return user_id;
	}
	public void setUser_id(int userId) {
		user_id = userId;
	}
	public String getCreate_date() {
		return create_date;
	}
	public void setCreate_date(String createDate) {
		create_date = createDate;
	}
	private String img_url;
	
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String imgUrl) {
		img_url = imgUrl;
	}
	private String name_clean;
	
	private String common_time;
	
	private String introduction;
	
	private String siteName;
	
	private String price;
	
}
