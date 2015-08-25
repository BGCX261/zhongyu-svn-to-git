package com.piaoyou.domain;

public class Site {
	private int id;
	private String site_name;
	private String site_info;
	private String site_photo_url;
	private String img_path;
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
	public String getSite_photo_url() {
		return site_photo_url;
	}
	public void setSite_photo_url(String sitePhotoUrl) {
		site_photo_url = sitePhotoUrl;
	}
	public String getImg_path() {
		return img_path;
	}
	public void setImg_path(String imgPath) {
		img_path = imgPath;
	}
	

}
