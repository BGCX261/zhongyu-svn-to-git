package com.piaoyou.bean;

import java.io.Serializable;

public class Star implements Serializable{
	private static final long serialVersionUID = 5855263605918066356L;
	private int star_id;
	private String cn_name;
	private String en_name;
	private String sex;
	private String region;
	private String vocation;
	private String native_place;
	private String info;
	private String head_photo;
	private String first_word;
	private String birthday;
	private String blood;
	private String star_seat;
	private String height;
	private String weight;
	private String company;
	private String rank;
	private String newsUrl;
	private String vedioUrl;
	public String getCn_name() {
		return cn_name;
	}
	public void setCn_name(String cnName) {
		cn_name = cnName;
	}
	public String getEn_name() {
		return en_name;
	}
	public void setEn_name(String enName) {
		en_name = enName;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public String getVocation() {
		return vocation;
	}
	public void setVocation(String vocation) {
		this.vocation = vocation;
	}
	public String getNative_place() {
		return native_place;
	}
	public void setNative_place(String nativePlace) {
		native_place = nativePlace;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public String getHead_photo() {
		return head_photo;
	}
	public void setHead_photo(String headPhoto) {
		head_photo = headPhoto;
	}
	public String getFirst_word() {
		return first_word;
	}
	public void setFirst_word(String firstWord) {
		first_word = firstWord;
	}
	public int getStar_id() {
		return star_id;
	}
	public void setStar_id(int starId) {
		star_id = starId;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getBlood() {
		return blood;
	}
	public void setBlood(String blood) {
		this.blood = blood;
	}
	public String getStar_seat() {
		return star_seat;
	}
	public void setStar_seat(String starSeat) {
		star_seat = starSeat;
	}
	public String getHeight() {
		return height;
	}
	public void setHeight(String height) {
		this.height = height;
	}
	public String getWeight() {
		return weight;
	}
	public void setWeight(String weight) {
		this.weight = weight;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getRank() {
		return rank;
	}
	public void setRank(String rank) {
		this.rank = rank;
	}
	public String getNewsUrl() {
		return newsUrl;
	}
	public void setNewsUrl(String newsUrl) {
		this.newsUrl = newsUrl;
	}
	public String getVedioUrl() {
		return vedioUrl;
	}
	public void setVedioUrl(String vedioUrl) {
		this.vedioUrl = vedioUrl;
	}

}
