package com.piaoyou.bean;

import java.io.Serializable;

public class StarPhoto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6210909433075370557L;
	private int PHOTO_ID;
	private int STAR_ID;
	private String PHOTO_TITLE;
	private String PHOTO_URL;
	private String PHOTO_PATH;
	private String CRAWLED_TIME;
	public int getPHOTO_ID() {
		return PHOTO_ID;
	}
	public void setPHOTO_ID(int pHOTOID) {
		PHOTO_ID = pHOTOID;
	}
	public int getSTAR_ID() {
		return STAR_ID;
	}
	public void setSTAR_ID(int sTARID) {
		STAR_ID = sTARID;
	}
	public String getPHOTO_TITLE() {
		return PHOTO_TITLE;
	}
	public void setPHOTO_TITLE(String pHOTOTITLE) {
		PHOTO_TITLE = pHOTOTITLE;
	}
	public String getPHOTO_URL() {
		return PHOTO_URL;
	}
	public void setPHOTO_URL(String pHOTOURL) {
		PHOTO_URL = pHOTOURL;
	}
	
	public String getCRAWLED_TIME() {
		return CRAWLED_TIME;
	}
	public void setCRAWLED_TIME(String cRAWLEDTIME) {
		CRAWLED_TIME = cRAWLEDTIME;
	}
	public String getPHOTO_PATH() {
		return PHOTO_PATH;
	}
	public void setPHOTO_PATH(String pHOTOPATH) {
		PHOTO_PATH = pHOTOPATH;
	}

}
