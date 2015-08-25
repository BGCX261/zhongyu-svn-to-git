package com.piaoyou.bean;

/**
 * 票价信息
 * @author Administrator
 *
 */
public class TicketPrice {
	
	private String price;		//票价（如果没有出票或者票价待定 票价为-1）
	private boolean isExist;	//是否有票
	private String mainURL;		//买票的主页面
	private String detailURL;	//买票的详细页面
	private String remark;		//票价的备注信息

	public TicketPrice(){
		
	}
	
	public TicketPrice(String price,boolean isExist,String mainURL){
		this.price = price;
		this.isExist = isExist;
		this.mainURL = mainURL;
	}
	
	public TicketPrice(String price,Boolean isExist,String mainURL,String detailURL){
		this.price = price;
		this.isExist = isExist;
		this.mainURL = mainURL;
		this.detailURL = detailURL;
	}
	
	public TicketPrice(String price,Boolean isExist,String mainURL,String detailURL,String remark){
		this.price = price;
		this.isExist = isExist;
		this.mainURL = mainURL;
		this.detailURL = detailURL;
		this.remark = remark;
	}
	
	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = "".equals(price)?"-1":price;
	}
	public boolean isExist() {
		return isExist;
	}
	public void setExist(boolean isExist) {
		this.isExist = isExist;
	}
	public String getMainURL() {
		return mainURL;
	}
	public void setMainURL(String mainURL) {
		this.mainURL = mainURL;
	}
	public String getDetailURL() {
		return (detailURL == null||detailURL.length()==0)
					?mainURL:detailURL;
	}
	public void setDetailURL(String detailURL) {
		this.detailURL = detailURL;
	}

	@Override
	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append("url:").append(this.getMainURL());
		message.append(" 票价：").append(this.getPrice());
		message.append(" 备注：").append(this.getRemark());
		message.append(" 是否有票：").append(this.isExist());
		return message.toString();
	}
	
	
}
