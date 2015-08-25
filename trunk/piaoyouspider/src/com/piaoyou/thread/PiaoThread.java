/**
 * 票友会创建索引
 */
package com.piaoyou.thread;

import java.sql.Connection;
import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.crawler.baseInfo.BaiduStarInfoSpider;
import com.piaoyou.crawler.baseInfo.StarSpiderCache;
import com.piaoyou.crawler.merge.MergeRecord;
import com.piaoyou.util.db.DBUtils;

/**
 * @author jinquan
 * @date   2011-9-19
 */
public class PiaoThread {
	private static final Log log = LogFactory.getLog(PiaoThread.class);
	
	/**
	 * 每半小时更新一次
	 * @return 0 完成 -1 失败
	 */
	public static int updateShowStatus(){
		System.out.println("演出状态更新");
		String sql = " UPDATE    t_show_info	LEFT JOIN      (SELECT t.show_id         "
				+ "FROM (SELECT tst.show_id, max(tst.show_time) show_time              FROM t_show_time tst  "
				+ "                  JOIN t_agency_price tap                        ON tap.one_show_id = tst.one_show_id     "
				+ "               JOIN t_show_info tsi                     ON tsi.show_id = tst.show_id         "
				+ "                AND tap.agency_id = tsi.standard_agency_id             GROUP BY tsi.show_id   "
				+ "          UNION ALL              SELECT tsd.show_id, max(tsd.show_end_date) show_time       "
				+ "     FROM    t_show_date tsd                  "
				+ "    JOIN                       t_show_info tsi                    "
				+ "   ON     tsi.show_id = tsd.show_id                         AND tsi.standard_agency_id = tsd.agency_id      "
				+ "                   AND tsd.show_end_date IS NOT NULL            GROUP BY tsd.show_id         ) t      "
				+ "   WHERE TO_DAYS(now()) - TO_DAYS(t.show_time) > 0 ) tmp ON t_show_info.show_id = tmp.show_id SET "
				+ "t_show_info.status = 2 WHERE tmp.show_id IS NOT NULL";
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			conn.createStatement().execute(sql);
		} catch(Exception ee){
			ee.printStackTrace();
		}
		finally{
			DBUtils.closeConnection(conn);
		}
		log.info("演出状态更新结束");
		return 0;
	}
	
	/**
	 * 抓完演出信息后进行同步
	 * @return 0 完成 -1 失败
	 */
	public static int synPartnerShow(){
		System.out.println("合作票商演出同步");
		return PartnerDateTrans.synPartners();
	}
	/**
	 * 每天抓取一次明星新聞
	 * @return
	 */
    public static int starNews(){
    	BaiduStarInfoSpider spider=new BaiduStarInfoSpider();
    	StarSpiderCache spiderCache=new StarSpiderCache();
    	Calendar calendar = Calendar.getInstance();   
    	Date date = new Date();   
    	calendar.setTime(date);   
    	int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;  
    	if(dayOfWeek>=1){
    		spiderCache.cleanAllStarNews(dayOfWeek);
			spider.extractMainNews(dayOfWeek); 
			spiderCache.addAllStarNewsCache(dayOfWeek);
			spiderCache.addAllStarNewsFrontCache(dayOfWeek);
    	}
    	return 0;
    }

	public static void main(String e[]){
		/**用于SHELL定时启动调用所有任务**/
		log.info(new Date()+"启动PiaoThread任务...");
		updateShowStatus();
		synPartnerShow();
		MergeRecord.setFinishFlag();
		MergeRecord.delFinishPattern();
	}
	

}

