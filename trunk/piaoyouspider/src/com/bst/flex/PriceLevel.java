package com.bst.flex;

import java.io.Serializable;

public class PriceLevel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4191478203384139681L;
	
	private String plName;
	private String color;
	private String plPrice;
	private String description;
	private String plId;
	public String getPlName() {
		return plName;
	}
	public void setPlName(String plName) {
		this.plName = plName;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public String getPlPrice() {
		return plPrice;
	}
	public void setPlPrice(String plPrice) {
		this.plPrice = plPrice;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getPlId() {
		return plId;
	}
	public void setPlId(String plId) {
		this.plId = plId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
