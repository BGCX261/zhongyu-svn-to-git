package com.piaoyou.bean;

import java.io.Serializable;

public class StarMusic implements Serializable {

	private static final long serialVersionUID = 345374605298709797L;
	
	private int SOUND_ID;
	private int STAR_ID;
	private String SOUND_TITLE;
	private String SOUND_URL;
	private String CRAWLED_TIME;
	public int getSOUND_ID() {
		return SOUND_ID;
	}
	public void setSOUND_ID(int sOUNDID) {
		SOUND_ID = sOUNDID;
	}
	public int getSTAR_ID() {
		return STAR_ID;
	}
	public void setSTAR_ID(int sTARID) {
		STAR_ID = sTARID;
	}
	public String getSOUND_TITLE() {
		return SOUND_TITLE;
	}
	public void setSOUND_TITLE(String sOUNDTITLE) {
		SOUND_TITLE = sOUNDTITLE;
	}
	public String getSOUND_URL() {
		return SOUND_URL;
	}
	public void setSOUND_URL(String sOUNDURL) {
		SOUND_URL = sOUNDURL;
	}
	public String getCRAWLED_TIME() {
		return CRAWLED_TIME;
	}
	public void setCRAWLED_TIME(String cRAWLEDTIME) {
		CRAWLED_TIME = cRAWLEDTIME;
	}

}
