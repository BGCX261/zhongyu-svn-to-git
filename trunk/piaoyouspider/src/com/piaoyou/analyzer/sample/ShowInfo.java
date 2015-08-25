package com.piaoyou.analyzer.sample;

import com.piaoyou.analyzer.index.IShow;



public class ShowInfo implements IShow{
	public String show_id;
	public String show_name_clean;
	public String site_id;
	public String show_info;
	public String getShow_id() {
		return show_id;
	}
	public void setShow_id(String show_id) {
		this.show_id = show_id;
	}

	public String getSite_id() {
		return site_id;
	}
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	public String getShow_info() {
		return show_info;
	}
	public void setShow_info(String show_info) {
		this.show_info = show_info;
	}
	public String[] getPropertyName() {
		return  new String[]{"show_id","show_name_clean","site_id","show_info"};
	}
	public String getShow_name_clean() {
		return show_name_clean;
	}
	public void setShow_name_clean(String show_name_clean) {
		this.show_name_clean = show_name_clean;
	}

}
