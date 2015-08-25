package com.piaoyou.bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.piaoyou.dao.DataDao;

public class Show {
	private String id;				//演出id
	private String name;			//演出名称
	private String siteID;			//剧场代码
	private String siteName;		//剧场名称
	private String city;			//城市
	private String introduction;	//简介
	private String image_path;		//图片地址
	private String seatPic_path;	//座位图
	private String agent_id;		//代理商ID
	private int type;				//演出类型
	private int status = 9;			//演出状态(0预售中1售票中2结束3:删除9状态未明)
	private boolean firstAgent;		//是否总代
	private boolean watermark;		//是否有水印
	private boolean onlineSeat;		//是否支持在线选座
	private Map<String,List<TicketPrice>> timeAndPrice;//演出时间和票价
	
	public String getSeatPic_path() {
		return seatPic_path;
	}
	public void setSeatPic_path(String seatPicPath) {
		seatPic_path = seatPicPath;
	}
	
	public boolean isOnlineSeat() {
		return onlineSeat;
	}
	public void setOnlineSeat(boolean onlineSeat) {
		this.onlineSeat = onlineSeat;
	}
	
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public boolean isWatermark() {
		return watermark;
	}
	public void setWatermark(boolean watermark) {
		this.watermark = watermark;
	}
	public boolean isFirstAgent() {
		return firstAgent;
	}
	public void setFirstAgent(boolean isFirstAgent) {
		this.firstAgent = isFirstAgent;
	}
	public String getId() {
		return id;
	}
	public String getSiteID() {
		return siteID;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSiteName() {
		return siteName;
	}
	public static void main(String e[]){
		Show s = new Show();
		s.setSiteName(" 万事达中心M空间");
	}
	public void setSiteName(String siteName) {
		siteName = siteName.replaceAll("[ 　]*$", "").replaceAll(" ", "").trim();
		StringBuffer sql = new StringBuffer();
		sql.append("select site.site_id from t_site_info site ");
		sql.append(" join t_site_cor tmp ");
		sql.append("where tmp.site_name=? and tmp.site_name_cor=site.site_name ");
		sql.append(" union all ");
		sql.append("select tar.site_id from t_site_info tar where ");
		sql.append("tar.site_name=?");
//		sql.append("select site.site_id from t_site_info site ");
//		sql.append("where (site.site_name=? or LOCATE(site.site_name,?)>0 or LOCATE(?,site.site_name)>0) ");
		DataDao dao = new DataDao();
//		List<Map<String,Object>> siteID =dao.searchBySQL(sql.toString(), siteName, siteName, siteName);
		List<Map<String,Object>> siteID =dao.searchBySQL(sql.toString(), siteName.trim(), siteName.trim());
		if(siteID.size()>0){
			this.siteID = siteID.get(0).get("site_id").toString();
		}else{
			dao.executeSQL("insert into t_site_info (site_name) values (?) ", siteName);
			dao.executeSQL("replace into t_site_cor (site_name,insert_time) values (?,?)", siteName,new Date());
			List<Map<String,Object>> tmp = dao.searchBySQL("select site_id from t_site_info where site_name=?", siteName);
			if(tmp.size()>0){
				this.siteID = tmp.get(0).get("site_id").toString();
			}
		}
		this.siteName = siteName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name.trim();
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public String getImage_path() {
		return image_path;
	}
	public void setImage_path(String imagePath) {
		image_path = imagePath;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getAgent_id() {
		return agent_id;
	}
	public void setAgent_id(String agentId) {
		agent_id = agentId;
	}
	public Map<String, List<TicketPrice>> getTimeAndPrice() {
		return timeAndPrice;
	}
	public void setTimeAndPrice(Map<String, List<TicketPrice>> timeAndPrice) {
		this.timeAndPrice = timeAndPrice;
	}
	
	@Override
	public String toString() {
		StringBuffer message = new StringBuffer();
		message.append("演出信息：").append("\n\t");
		message.append("演出名称：").append(this.getName()).append("\n\t");
		message.append("演出场馆：").append(this.getSiteName()).append("\n\t");
		message.append("场馆ID：").append(this.getSiteID()).append("\n\t");
		message.append("演出类型：").append(this.getType()).append("\n\t");
//		message.append("演出简介：").append(this.getIntroduction()).append("\n\t");
		message.append("演出图片：").append(this.getImage_path()).append("\n\t");
		message.append("是否总代：").append(this.firstAgent);
        java.util.Iterator<Entry<String,List<TicketPrice>>> it = this.getTimeAndPrice().entrySet().iterator();
	    while(it.hasNext()){
	    	java.util.Map.Entry<String,List<TicketPrice>> entry = (java.util.Map.Entry<String,List<TicketPrice>>)it.next();
	    	message.append("演出时间：").append(entry.getKey()).append("对应的票价是");
	    	List<TicketPrice> list=new ArrayList<TicketPrice>();
	    	list=entry.getValue();
	    	for(int i=0;i<list.size();i++){
	    	    message.append(":"+list.get(i).getPrice());
	    	}
	    }

		for(Map.Entry<String, List<TicketPrice>> time:this.getTimeAndPrice().entrySet()){
			message.append("\n\t演出时间：").append(time.getKey());
			for(TicketPrice price:time.getValue()){
				message.append("\n\t\t票务信息："+price);
			}
		}
		return message.toString();
	}
	
}
