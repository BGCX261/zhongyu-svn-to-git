package com.piaoyou.bean;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;

public class CommonInfo implements Serializable {

	private static final long serialVersionUID = 2235414336823635225L;
	private int id;
	private String name; 
	private String img_url ;  
	private String remote_img_url;
	private String time; 
	private String movie_type;
	private int site_id;
	private String site_name;
	private int status;
	private int is_first_agency;
	private List<String> show_time;
	private String limit_time;
	private String introduction;
	private int show_type;
	private int type;
	private int agency_id ;
	private String price;		//票价（如果没有出票或者票价待定 票价为-1）
	private String mainURL;		//买票的主页面
	private String discount;    //折扣
	private String cityName;
	private String address;
	private String heng_image_path;
	private String is_check ;
	
	public String getIs_check() {
		return is_check;
	}
	public void setIs_check(String isCheck) {
		is_check = isCheck;
	}
	public String getRemote_img_url() {
		return remote_img_url;
	}
	public void setRemote_img_url(String remoteImgUrl) {
		remote_img_url = remoteImgUrl;
	}
	private String min_price;
	
	public String getMovie_type() {
		return movie_type;
	}
	public void setMovie_type(String movieType) {
		movie_type = movieType;
	}
	public String getMin_price() {
		return min_price;
	}
	public void setMin_price(String minPrice) {
		if(minPrice==null||"".equals(minPrice)){
			minPrice="0";
		}
		min_price = minPrice;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getHeng_image_path() {
		if(remote_img_url!=null&&remote_img_url.contains("http://pimg.damai.cn")){
			 heng_image_path = remote_img_url.replaceAll("n.jpg", "n1.jpg");
		}
		return heng_image_path;
	}
	public void setHeng_image_path(String hengImagePath) {
		heng_image_path = hengImagePath;
	}
	public int getCityId() {
		return cityId;
	}
	public void setCityId(int cityId) {
		this.cityId = cityId;
	}
	private int cityId;
	public String getCityName() {
		return cityName;
	}
	public void setCityName(String cityName) {
 		DataDao dao = new DataDao();
		List<Map<String,Object>> rs =dao.searchBySQL("select id from t_city where city_name = ?",cityName.trim());
		if(rs.size()>0){
			this.cityId = Integer.parseInt(rs.get(0).get("id").toString());
		}else{
			try {
				dao.executeSQL("insert into t_city (city_name) values (?) ", cityName);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			rs =dao.searchBySQL("select id from t_city where city_name =? ",cityName.trim());
			if(rs.size()>0){
				this.cityId = Integer.parseInt(rs.get(0).get("id").toString());
			}else{
				this.cityId = 0;
			}
		}
		this.cityName = cityName;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIs_first_agency() {
		return is_first_agency;
	}
	public void setIs_first_agency(int isFirstAgency) {
		is_first_agency = isFirstAgency;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public List<String> getShow_time() {
		return show_time;
	}
	public void setShow_time(List<String> showTime) {
		show_time = showTime;
	}
	public String getImg_url() {
		return img_url;
	}
	public void setImg_url(String imgUrl) {
		img_url = imgUrl;
	}
	
	public String getLimit_time() {
		return limit_time;
	}
	public void setLimit_time(String limitTime) {
		limit_time = limitTime;
	}
	public int getAgency_id() {
		return agency_id;
	}
	public void setAgency_id(int agencyId) {
		agency_id = agencyId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getMainURL() {
		return mainURL;
	}
	public void setMainURL(String mainURL) {
		this.mainURL = mainURL;
	}
	public int getSite_id() {
		return site_id;
	}
	public void setSite_id(int siteId) {
		site_id = siteId;
	}
	
	public String getSite_name() {
		return site_name;
	}
	public void setSite_name(String siteName) {
		siteName = siteName.replaceAll("[ 　]*$", "").replaceAll(" ", "").trim();
		String sql = 
			" SELECT site.id"+
			"  FROM    t_site_info site"+
			"       JOIN"+
			"          t_site_cor tmp"+
			"       ON tmp.site_name_cor = site.site_name AND tmp.site_name = ?"+
			" UNION ALL "+
			" SELECT site.id"+
			"  FROM t_site_info site"+
			" WHERE site.site_name = ?";
		DataDao dao = new DataDao();
		List<Map<String,Object>> siteID =dao.searchBySQL(sql, siteName.trim(),siteName.trim());
		if(siteID.size()>0){   //场馆已经存在  获取场馆ID
			this.site_id = Integer.parseInt(siteID.get(0).get("id").toString());
		}else{   //场馆不存在    加入新场馆
			try {
				dao.executeSQL("insert into t_site_info (site_name) values (?) ", siteName);
				dao.executeSQL("replace into t_site_cor (site_name,insert_time) values (?,?)", siteName,new Date());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			List<Map<String,Object>> tmp = dao.searchBySQL("select id from t_site_info where site_name=?", siteName);
			if(tmp.size()>0){
				this.site_id = Integer.parseInt(tmp.get(0).get("id").toString());
			}
		}
		this.site_name = siteName;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getIntroduction() {
		return introduction;
	}
	public void setIntroduction(String introduction) {
		this.introduction = introduction;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getShow_type() {
		return show_type;
	}
	public void setShow_type(int showType) {
		show_type = showType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		if(this.type==Const.COMMON_INFO_TYPE_MOVIE) {
			sb.append("名称："+name+"\n");
			sb.append("状态："+(status==1?"热映":"即将上映")+"\n");
			sb.append("海报："+remote_img_url+"\n");
			sb.append("电影类型："+movie_type+"\n");
			sb.append("上映时间:");
			if(show_time!=null){
				for(int i=0;i<show_time.size();i++){
					sb.append(show_time.get(i)+",");
				}
			}
			sb.append("\n");
			sb.append("演出时长："+time+"\n");
		} else if(this.type==Const.COMMON_INFO_TYPE_SHOW){
			sb.append("演出名称："+name+"\n");
			sb.append("演出场馆："+site_name+"\n");
			sb.append("购票网站："+mainURL+"\n");
			sb.append("是否总代："+(is_first_agency==1?"是":"否")+"\n");
			sb.append("状态："+(status==1?"售票中":"预售/退票")+"\n");
			sb.append("海报："+remote_img_url+"\n");
			sb.append("票价："+price+"\n");
			sb.append("最低价："+min_price+"\n");
			sb.append("演出时间:");
			if(show_time!=null){
				for(int i=0;i<show_time.size();i++){
					sb.append(show_time.get(i)+",");
				}
			}
			sb.append("\n");
			sb.append("演出简介："+introduction.substring(0,100)+"...\n");
			sb.append("------------------------------------------------------------------------------\n");
		}else if(this.type==Const.COMMON_INFO_TYPE_GROUP){
			sb.append("团购城市："+cityName+"\n");
			sb.append("演出名称："+name+"\n");
			sb.append("团购网站："+mainURL+"\n");
			sb.append("演出图片："+remote_img_url+"\n");
			sb.append("票价："+price+"\n");
			sb.append("最低票价："+min_price+"\n");
			sb.append("演出折扣："+discount+"\n");
			sb.append("演出介绍："+introduction+"\n");
			sb.append("演出时间:");
			if(show_time!=null){
				for(int i=0;i<show_time.size();i++){
					sb.append(show_time.get(i)+",");
				}
			}
		}
		return sb.toString();
	}
	

}
