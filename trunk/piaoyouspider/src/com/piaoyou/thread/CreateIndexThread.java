/**
 * 票友会创建索引
 */
package com.piaoyou.thread;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.analyzer.index.ConstantsIndex;
import com.piaoyou.analyzer.index.IShow;
import com.piaoyou.analyzer.starIndexShow.CreateIndex;
import com.piaoyou.bean.Show2;
import com.piaoyou.crawler.baseInfo.BaiduStarInfoSpider;
import com.piaoyou.util.db.DBUtils;

/**
 * @author jinquan
 * @date   2011-9-19
 */
public class CreateIndexThread {
	private static final Log log = LogFactory.getLog(CreateIndexThread.class);
	/**
	 * 每半小时创建一次
	 * @return 0 完成 -1 失败
	 */
	public static int usedIndex(){
		System.out.println("2手票索引创建");
		List<Show2> list = new ArrayList<Show2>();//二手票
		List<IShow> list2 = new ArrayList<IShow>();
		String sql = "select distinct(tsi.show_id) showID,tsi.show_name showName,tsi.show_info introduction,ts.site_name" +
				" siteName,ts.site_id siteId,tsi.show_type_id type,tc.city_id cityId,tc.city_name cityName" +
				" from t_show_info tsi join t_site_info ts on ts.site_id = tsi.site_id join t_city tc on tc.city_id" +
				" = ts.city_id ";
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try{
			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement(sql);
			rs=stmt.executeQuery();
			while(rs.next()){
				Show2 ticket = new Show2();
				ticket.setShowID(rs.getString("showID"));
				ticket.setShowName(rs.getString("showName"));
				ticket.setIntroduction(rs.getString("introduction"));
				ticket.setSiteName(rs.getString("siteName"));
				ticket.setSiteID(rs.getString("siteId"));
				ticket.setType(rs.getInt("type"));
				ticket.setCityId(rs.getString("cityId"));
				ticket.setCityName(rs.getString("cityName"));
				list.add(ticket);
			}
		}catch(Exception ee){
			ee.printStackTrace();
		}
		finally{
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(stmt);
			DBUtils.closeConnection(conn);
		}
		
		list2.addAll(list);
		CreateIndex cn = new CreateIndex();
		cn.CreateConfigDocAndIndex(ConstantsIndex.SHOWINFO_USEDTICKET_INDEX_PATH, list2, true);
		
		return 0;
	}
	/**
	 * 每半小时创建一次
	 * @return 0 完成 -1 失败
	 */
	public static int starIndex(){
		long start=System.currentTimeMillis();
		System.out.println("明星索引创建开始");
		CreateIndex.creatStarIndex();
		System.out.println("明星索引创建结束");
		long end=System.currentTimeMillis();
		System.out.println("共花费："+(end-start)/1000+"秒创建明星索引");
		return 0;
	}
	
    public static int starNews(){
    	BaiduStarInfoSpider spider=new BaiduStarInfoSpider();
    	Calendar calendar = Calendar.getInstance();   
    	Date date = new Date();   
    	calendar.setTime(date);   
    	int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;  
    	switch(dayOfWeek){
	    	case 1: spider.extractMainNews(1); 
	    		break;
	    	case 2: spider.extractMainNews(2); 
	    		break;
	    	case 3: spider.extractMainNews(3); 
	    		break;
	    	case 4: spider.extractMainNews(4); 
	    		break;
	    	case 5: spider.extractMainNews(5); 
	    		break;
	    	case 6: spider.extractMainNews(6); 
	    		break;
	    	default:break;
    	}
    	return 0;
    }
	  
	public static void main(String e[]){
		/**用于SHELL定时启动调用所有任务**/
		log.info(new Date()+"启动PiaoThread任务...");
		usedIndex();
		starIndex();
	}
	

}
