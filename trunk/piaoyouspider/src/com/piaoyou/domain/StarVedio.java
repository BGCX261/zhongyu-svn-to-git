package com.piaoyou.domain;

import java.io.Serializable;

public class StarVedio implements Serializable {

	/**
	 * 自动生成序列化号
	 */
	private static final long serialVersionUID = -71931199580341602L;
	private int VIDEO_ID;
	private int STAR_ID;
	private String VIDEO_TITLE;
	private String VIDEO_PHOTO_URL;
	private String VIDEO_PHOTO_PATH;
	private String VIDEO_URL;
	private String CRAWLED_TIME;
	public int getVIDEO_ID() {
		return VIDEO_ID;
	}
	public void setVIDEO_ID(int vIDEOID) {
		VIDEO_ID = vIDEOID;
	}
	public int getSTAR_ID() {
		return STAR_ID;
	}
	public void setSTAR_ID(int sTARID) {
		STAR_ID = sTARID;
	}
	public String getVIDEO_TITLE() {
		return VIDEO_TITLE;
	}
	public void setVIDEO_TITLE(String vIDEOTITLE) {
		VIDEO_TITLE = vIDEOTITLE;
	}
	public String getVIDEO_PHOTO_URL() {
		return VIDEO_PHOTO_URL;
	}
	public void setVIDEO_PHOTO_URL(String vIDEOPHOTOURL) {
		VIDEO_PHOTO_URL = vIDEOPHOTOURL;
	}
	public String getVIDEO_PHOTO_PATH() {
		return VIDEO_PHOTO_PATH;
	}
	public void setVIDEO_PHOTO_PATH(String vIDEOPHOTOPATH) {
		VIDEO_PHOTO_PATH = vIDEOPHOTOPATH;
	}
	public String getVIDEO_URL() {
		return VIDEO_URL;
	}
	public void setVIDEO_URL(String vIDEOURL) {
		VIDEO_URL = vIDEOURL;
	}
	public String getCRAWLED_TIME() {
		return CRAWLED_TIME;
	}
	public void setCRAWLED_TIME(String cRAWLEDTIME) {
		CRAWLED_TIME = cRAWLEDTIME;
	}
	

}
