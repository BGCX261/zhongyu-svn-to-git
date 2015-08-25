package com.piaoyou.domain;

import java.io.Serializable;

public class UserRecommend implements Serializable {
	private static final long serialVersionUID = 7270387321974543245L;
	private int id;
	private int common_id;
	private int user_id;
	private String create_date;
	private String user_nick;
	private String recommend_reason;
	private String user_name;
	private String user_portrait;
	private String name_clean;
	private String introduction;
	private String  img_url;
	private String common_time;
	private String siteName;
	private String price;
	private String status;
	private String location_url;
	
	
	
	public String getUser_nick() {
		return user_nick;
	}
	public void setUser_nick(String userNick) {
		user_nick = userNick;
	}
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String userName) {
		user_name = userName;
	}
	public String getUser_portrait() {
		return user_portrait;
	}
	public void setUser_portrait(String userPortrait) {
		user_portrait = userPortrait;
	}
	public int getId() {
		return id;
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
	public String getRecommend_reason() {
		return recommend_reason;
	}
	public void setRecommend_reason(String recommendReason) {
		recommend_reason = recommendReason;
	}
	public String getName_clean() {
		return name_clean;
	}
	public void setName_clean(String nameClean) {
		name_clean = nameClean;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String imgUrl) {
		img_url = imgUrl;
	}
	public String getCommon_time() {
		return common_time;
	}
	public void setCommon_time(String commonTime) {
		common_time = commonTime;
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
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getLocation_url() {
		return location_url;
	}
	public void setLocation_url(String locationUrl) {
		location_url = locationUrl;
	}
	
}
