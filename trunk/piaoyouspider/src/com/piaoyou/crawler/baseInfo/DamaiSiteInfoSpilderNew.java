package com.piaoyou.crawler.baseInfo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import com.piaoyou.bean.SiteBean;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.util.db.DBUtils;

public class DamaiSiteInfoSpilderNew extends TicketSpider{
	
	/**
	 * 抓取前先要判断数据是否存在，存在则跳过
	 * 第一步：抓取各地区信息到城市表
	 * 第二步：抓取各场馆信息
	 */
	static final String SITE_HOME_PAGE = "http://venue.damai.cn/search.aspx?cityID=0";
	//库中已经保存的场馆信息
	/**
	 * 1:读取总页数
	 * @param arges
	 */
	public static void main(String []arges){
		Document homePageDoc= getDoc(SITE_HOME_PAGE);
		int pageCount=1;
		String pageUrl="";
		String oneSiteUrl="";
		Elements sites;
		String cityName="";
		int cityId=0;
		String siteName="";
		SiteBean siteBean=null;
		//得到场馆页数
		if(!homePageDoc.getElementsByClass("pagination").isEmpty()){
			String urlTemp=homePageDoc.getElementsByClass("pagination").get(0).getElementsByTag("a").get(1).attr("abs:href");
			String temp=homePageDoc.getElementsByClass("pagination").get(0).getElementsByTag("button").attr("onclick");
			pageCount=Integer.parseInt(temp.substring(temp.lastIndexOf(",")+1,temp.lastIndexOf("")-1));
			pageUrl=urlTemp.substring(0,urlTemp.lastIndexOf("=")+1);
		}else{
			pageCount=1;
			pageUrl=SITE_HOME_PAGE;
		}
		//根据总页数循环
		for(int i=1;i<=pageCount;i++){//pageCount
			if(pageCount!=1){
				homePageDoc=getDoc(pageUrl+i);
			}
			sites=homePageDoc.getElementsByClass("type").tagName("a");
			for(int x=0;x<sites.size();x++){//sites.size()
				oneSiteUrl=sites.get(x).getElementsByTag("a").get(1).attr("abs:href");
				Document oneSites=getDoc(oneSiteUrl);
				try{
					cityName=oneSites.getElementById("city").attr("value").trim();
					siteName=oneSites.getElementById("ends").attr("value").trim();
					cityId=selectCityIdByCityName(cityName);
				}catch(Exception e){
					e.printStackTrace();
					continue;
				}
				//如果场馆不存在，增加场馆
				if(!getExSiteBSiteNameByCityId(siteName,cityId)){
					siteBean=new SiteBean();
					siteBean.setCityId(cityId);
					siteBean.setSiteName(siteName);
					siteBean.setSiteAddress(sites.get(x).getElementsByClass("txt").get(0).text().replace("场馆地址：", ""));
					siteBean.setSiteInfo(oneSites.select("div.info").text().replace("[查看地图]", ""));
					String lineUrl=oneSites.getElementById("page2").getElementsByClass("submenu").select("a[href]").last().attr("abs:href");
					String img=oneSites.getElementsByClass("venueDetal").get(0).getElementsByTag("img").get(0).attr("src");
				//	siteBean.setSitePhoto(img);
					Document line=getDoc(lineUrl);
					siteBean.setSiteBusLine(line.getElementsByTag("dd").get(0).text());
					siteBean.setSiteDriveLine(line.getElementsByTag("dd").get(1).text());
					addSite(siteBean);
				}
			}
		}
		
	}
	
	static int selectCityIdByCityName(String cityName){
		Connection connection =null;
		PreparedStatement  statement = null;
		ResultSet rs = null;
		String selectSql="SELECT t_city.city_id FROM t_city where t_city.city_name=?";
		int city_id=0;
		try{
			connection=DBUtils.getConnection();
			statement = connection.prepareStatement(selectSql);
			statement.setString(1, cityName);
			statement.executeQuery();
			rs = statement.getResultSet();
			if(rs.next()){
				city_id=rs.getInt(1);
			}else{
				//需呀添加城市
				String addSql="INSERT INTO t_city (city_name) VALUES(?)";
				statement = connection.prepareStatement(addSql);
				statement.setString(1, cityName);
				statement.executeUpdate();
				rs = statement.executeQuery("select LAST_INSERT_ID()");
				if(rs.next()){
					city_id=rs.getInt(1);
				}
			}
		}catch(SQLException e){
			e.printStackTrace();
		}finally{
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(statement);
			DBUtils.closeConnection(connection);
		}
		return city_id;
		
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
		//	statement.setString(5, siteBean.getSitePhoto());
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
	
	private static boolean  getExSiteBSiteNameByCityId(String siteName,int cityId) {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		boolean b =false;
		String sql = "select count(*)  from t_site_info t where t.site_name=? and t.city_id=?";
		try {
			connection = DBUtils.getConnection();
			statement = connection.prepareStatement(sql.toString());
			statement.setString(1, siteName);
			statement.setInt(2, cityId);
			statement.executeQuery();
			rs = statement.getResultSet();
			if(rs.next()){
				if(rs.getInt(1)>0){
					b=true;
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(statement);
			DBUtils.closeConnection(connection);
		}
		return b;
	}
}
