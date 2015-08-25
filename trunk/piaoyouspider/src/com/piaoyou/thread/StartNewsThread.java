
package com.piaoyou.thread;

import java.util.Calendar;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.crawler.baseInfo.BaiduStarInfoSpider;
import com.piaoyou.crawler.baseInfo.StarSpiderCache;

/**
 * @author jinquan
 * @date   2011-9-19
 */
public class StartNewsThread {
	private static final Log log = LogFactory.getLog(StartNewsThread.class);
	

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
		log.info("采集新闻任务启动");
		starNews();
	}
	

}

