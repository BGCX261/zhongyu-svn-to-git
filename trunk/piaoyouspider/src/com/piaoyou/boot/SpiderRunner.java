package com.piaoyou.boot;

import java.lang.reflect.Method;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.dao.DataDao;

/**
 * 启动一个抓取任务
 * @author Administrator
 *
 */
public class SpiderRunner implements Runnable {
	
	private static final Log log = LogFactory.getLog(SpiderRunner.class);

	private final String spiderName;
	private final DataDao dao;
	
	public SpiderRunner(String name, DataDao dao){
		this.spiderName = name;
		this.dao = dao;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		try {
			Class tar = Class.forName(spiderName);
			Object spider = tar.newInstance();
			if(spider instanceof SpiderTask){
				Method setDao = tar.getMethod("setDao", DataDao.class);
				setDao.invoke(spider, dao);
				Method extract = tar.getMethod("extract");
				log.info("准备加载spider"+spiderName);
				dao.executeSQL("insert into t_spider_log (spider_name,start_time) values (?,now())",spiderName);
				extract.invoke(spider);
				dao.executeSQL("update t_spider_log set end_time=now(),remark='成功' where spider_name=? and TO_DAYS(now())-TO_DAYS(start_time)<=1",spiderName);
				log.info("spider"+spiderName+"执行完毕");
			}
		} catch (Throwable e) {
			dao.executeSQL("update t_spider_log set remark='失败' where spider_name=? and TO_DAYS(now())-TO_DAYS(start_time)<=1",spiderName);
			log.error("加载spider失败!"+spiderName,e);
		}
	}

}
