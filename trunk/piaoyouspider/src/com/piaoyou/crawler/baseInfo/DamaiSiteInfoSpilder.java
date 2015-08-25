package com.piaoyou.crawler.baseInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.piaoyou.bean.CityBean;
import com.piaoyou.bean.SiteBean;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.util.db.DBUtils;

public class DamaiSiteInfoSpilder extends TicketSpider{
	
	/**
	 * 抓取前先要判断数据是否存在，存在则跳过
	 * 第一步：抓取各地区信息到城市表
	 * 第二步：抓取各场馆信息
	 */
	static final String SITE_HOME_PAGE = "http://venue.damai.cn/";
	static final String SITE_CITY_PAGE="http://venue.damai.cn/search.aspx?cityID=";
	//库中已经保存的场馆信息
	static final HashSet<String> containsSiteName = new HashSet<String>();
	static final HashSet<Integer> containsSiteCityId = new HashSet<Integer>();
	static final HashSet<Integer> containsSiteId = new HashSet<Integer>();
	static final List<CityBean> cityBeanList=new ArrayList<CityBean>();
	static final List<CityBean> newCityBeanList=new ArrayList<CityBean>();
	static final  HashSet<String> containsCityName = new  HashSet<String>();
	static boolean isNotAddCity=true;
	
	public static void main(String []arges) throws SQLException{
		
		getExistsSiteName();
		Document document= getDoc(SITE_HOME_PAGE);
		while(isNotAddCity){
			getCityExist();
			updateCityInfo(document);
		}
		//根据城市循环
		for(int i=0;i<newCityBeanList.size();i++){
			SiteBean siteBean=new SiteBean();
			CityBean cityBean=newCityBeanList.get(i);
			Document sites=getDoc(cityBean.getDmCityUrl());
			Elements d=sites.getElementsByClass("type").tagName("a");
			siteBean.setCityId(cityBean.getCityId());
			int pageCount=1;
			String pageUrl="";
			//得到场馆总页数
			if(!sites.getElementsByClass("pagination").isEmpty()){
				String urlTemp=sites.getElementsByClass("pagination").get(0).getElementsByTag("a").get(1).attr("abs:href");
				String temp=sites.getElementsByClass("pagination").get(0).getElementsByTag("button").attr("onclick");
				pageCount=Integer.parseInt(temp.substring(temp.lastIndexOf(",")+1,temp.lastIndexOf("")-1));
				pageUrl=urlTemp.substring(0,urlTemp.lastIndexOf("=")+1);
			}else{
				pageUrl=cityBean.getDmCityUrl();
			}
			//页数循环
			for(int n=1;n<=pageCount;n++){
				//第一页不需要再抓取数据
				if(n>1){
					sites=getDoc(pageUrl+n);
					d=sites.getElementsByClass("type").tagName("a");
				}
				//每页的场馆循环
				for(int z=0;z<d.size();z++){
					//场馆名称
					String siteName="";
					//单个场馆的URL
					String oneUrl="";
					//场馆图片
					String img="";
					//场馆地址
					String site_name="";
					siteName=d.get(z).getElementsByTag("a").get(1).attr("title").trim();
					oneUrl=d.get(z).getElementsByTag("a").get(1).attr("abs:href");
					if(containsSiteName.contains(siteName)){
						System.out.println(siteName+"：场馆已经存在!");
						continue;
					}
					Document oneSites=getDoc(oneUrl);
					siteBean.setSiteInfo(oneSites.select("div.info").text());
					String lineUrl=oneSites.getElementById("page2").getElementsByClass("submenu").select("a[href]").last().attr("abs:href");
					Document line=getDoc(lineUrl);
					try{
						site_name=d.get(z).getElementsByClass("txt").get(0).text().replace("场馆地址：", "");
						img=oneSites.getElementsByClass("venueDetal").get(0).getElementsByTag("img").get(0).attr("src");
						siteBean.setSiteName(siteName);
						siteBean.setSiteAddress(site_name);
						siteBean.setSiteBusLine(line.getElementsByTag("dd").get(0).text());
						siteBean.setSiteDriveLine(line.getElementsByTag("dd").get(1).text());
					}catch(Exception e){
					}
					try {
					     Date de = new Date();
						 long longtime = de.getTime();
					//	 siteBean.setSitePhoto(saveImg(img,"site_"+String.valueOf(longtime),"E:\\image\\site\\"));
					} catch (Exception e) {
						e.printStackTrace();
					}
					addSite(siteBean);
					System.out.println(siteBean.getSiteName()+"---添加成功!");
				}
			}
		}
		System.exit(0);
	}
	
	/**
	 * 更新城市信息，有新城市增加库中
	 * @param document
	 */
	static void updateCityInfo(Document document){
		Elements el=document.getElementById("page").getElementsByTag("dt");
		String cityName="";
		String dmCityUrl="";

		for(int i=0;i<el.size();i++){
			dmCityUrl=el.get(i).getElementsByTag("a").attr("href");
			cityName=el.get(i).getElementsByTag("a").text();
			cityName=cityName.substring(0,cityName.indexOf("[")).trim();
			//如果库中没有城市数据需要增加城市信息
			if(!containsCityName.contains(cityName)){
				isNotAddCity=true;
				addCity(cityName);
			}else{
				for(int x=0;x<cityBeanList.size();x++){
					CityBean cityBean=cityBeanList.get(x);
					if(cityBean.getCityName().equals(cityName)){
						cityBean.setDmCityUrl(dmCityUrl);
						newCityBeanList.add(cityBean);
					}
				}
				isNotAddCity=false;
			}
			
		}
		getCityExist();
	}
	
	static void addCity(String city){
		Connection connection =null;
		PreparedStatement  statement = null;
		String sql="INSERT INTO t_city (city_name) VALUES(?)";
		try{
			connection=DBUtils.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, city);
			statement.execute();
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			DBUtils.closeStatement(statement);
			DBUtils.closeConnection(connection);
		}
		
	}
	
	 /**
	 * 读取所有城市信息
	 */
	static void getCityExist(){
		
		Connection connection = null;
		PreparedStatement  statement = null;
		ResultSet rs = null;
		if(isNotAddCity){
			newCityBeanList.clear();
		}
		cityBeanList.clear();
		containsCityName.clear();
		String sql="select city_id,city_name from t_city  where 1=1 ";
			try {
				connection=DBUtils.getConnection();
				statement = connection.prepareStatement(sql);
				statement.execute();
				rs = statement.getResultSet();
				CityBean cityBean= null;
				while(rs.next()){
					cityBean=new CityBean();
					containsCityName.add(rs.getString(2));
					cityBean.setCityId(rs.getInt(1));
					cityBean.setCityName(rs.getString(2));
					cityBeanList.add(cityBean);
				}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(statement);
			DBUtils.closeConnection(connection);
		}
		
		
	}
	 /**
	  * 查询所有城市信息
	 * @return
	 * @throws SQLException
	 */
	static List<CityBean> getCity_info(){
		 
		 Connection connection = null;
			PreparedStatement  statement = null;
			ResultSet rs = null;
			String sql="select city_id,city_name from t_city  where 1=1";
			List<CityBean> list =new ArrayList<CityBean>();
			try{
				connection=DBUtils.getConnection();
				statement = connection.prepareStatement(sql);
				statement.execute();
				rs = statement.getResultSet();
				CityBean cityBean=null;
				
				while(rs.next()){
					cityBean=new CityBean();
					cityBean.setCityId(rs.getInt(1));
					cityBean.setCityName(rs.getString(3));
					list.add(cityBean);
				}
				
			}catch(SQLException e){
				e.printStackTrace();
			}finally{
				
				DBUtils.closeResultSet(rs);
				DBUtils.closeStatement(statement);
				DBUtils.closeConnection(connection);
			}
			return list;
	 }
	
	/**
	 * 保存场馆信息
	 * @param siteBean
	 * @throws SQLException 
	 */
	static void addSite(SiteBean siteBean){
		
		Connection connection = null;
		PreparedStatement  statement = null;
		String sql="INSERT INTO t_site_info (site_name,site_once_name,city_id,site_info,site_photo,site_name,site_bus_line,site_drive_line) VALUES(?,?,?,?,?,?,?,?)";
		try{
			connection=DBUtils.getConnection();
			statement = connection.prepareStatement(sql);
			statement.setString(1, siteBean.getSiteName());
			statement.setString(2, siteBean.getSiteOnceName());
			statement.setInt(3, siteBean.getCityId());
			statement.setString(4, siteBean.getSiteInfo());
			//statement.setString(5, siteBean.getSitePhoto());
			statement.setString(6, siteBean.getSiteAddress());
			statement.setString(7, siteBean.getSiteBusLine());
			statement.setString(8, siteBean.getSiteDriveLine());
			statement.execute();
		}catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeStatement(statement);
			DBUtils.closeConnection(connection);
		}
		
	}
	
	private static void getExistsSiteName() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		String sql = "select site_id,site_name,city_id  from t_site_info ";
		try {
			connection = DBUtils.getConnection();
			statement = connection.prepareStatement(sql.toString());
			statement.execute();
			rs = statement.getResultSet();
			while(rs.next()){
				containsSiteId.add(rs.getInt(1));
				containsSiteName.add(rs.getString(2));
				containsSiteCityId.add(rs.getInt(3));
			}
			
		} catch (SQLException e) {
			return;
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(statement);
			DBUtils.closeConnection(connection);
		}
	}
}
