package com.piaoyou.bean;

import java.io.Serializable;

public class StarNews implements Serializable {
	private static final long serialVersionUID = 3258206288889818813L;
	public StarNews() {
		
	}
	private int NEWS_ID;
	private int STAR_ID;
	private String NEWS_TITLE;
	private String NEWS_PHOTO;
	private String NEWS_URL;
	private String CRAWLED_TIME;
	private String CREATE_DATE;
	public int getNEWS_ID() {
		return NEWS_ID;
	}
	public void setNEWS_ID(int nEWSID) {
		NEWS_ID = nEWSID;
	}
	public int getSTAR_ID() {
		return STAR_ID;
	}
	public void setSTAR_ID(int sTARID) {
		STAR_ID = sTARID;
	}
	public String getNEWS_TITLE() {
		return NEWS_TITLE;
	}
	public void setNEWS_TITLE(String nEWSTITLE) {
		NEWS_TITLE = nEWSTITLE;
	}
	public String getNEWS_PHOTO() {
		return NEWS_PHOTO;
	}
	public void setNEWS_PHOTO(String nEWSPHOTO) {
		NEWS_PHOTO = nEWSPHOTO;
	}
	public String getNEWS_URL() {
		return NEWS_URL;
	}
	public void setNEWS_URL(String nEWSURL) {
		NEWS_URL = nEWSURL;
	}
	public String getCRAWLED_TIME() {
		return CRAWLED_TIME;
	}
	public void setCRAWLED_TIME(String cRAWLEDTIME) {
		CRAWLED_TIME = cRAWLEDTIME;
	}
	public String getCREATE_DATE() {
		return CREATE_DATE;
	}
	public void setCREATE_DATE(String cREATEDATE) {
		CREATE_DATE = cREATEDATE;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
