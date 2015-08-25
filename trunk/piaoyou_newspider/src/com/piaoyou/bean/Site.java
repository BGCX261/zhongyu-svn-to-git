package com.piaoyou.bean;

public class Site {
	private int id;
	private String site_name;
	private String site_info ;
	private int city_id ;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getSite_name() {
		return site_name;
	}
	public void setSite_name(String siteName) {
		site_name = siteName;
	}
	public String getSite_info() {
		return site_info;
	}
	public void setSite_info(String siteInfo) {
		site_info = siteInfo;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int cityId) {
		city_id = cityId;
	}
	
}
