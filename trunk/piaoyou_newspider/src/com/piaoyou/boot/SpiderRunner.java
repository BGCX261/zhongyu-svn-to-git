package com.piaoyou.boot;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.SQLException;

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
			boolean ret = true;
			try{
				Object tObject=tar.newInstance();
				Field field=tar.getDeclaredField("agentID");
				field.setAccessible(true);  //设置私有属性范围
			}catch(Exception e){
				log.info(spiderName+"无法实现关闭和开启控制");
			}
			Object spider = tar.newInstance();
			if(spider instanceof SpiderTask){
				Method setDao = tar.getMethod("setDao", DataDao.class);
				setDao.invoke(spider, dao);
				Method extract = tar.getMethod("extract");
				System.out.println("准备加载spider"+spiderName);
				dao.executeSQL("insert into t_spider_log (spider_name,start_time) values (?,now())",spiderName);
				extract.invoke(spider);
				dao.executeSQL("update t_spider_log set end_time=now(),remark='成功' where spider_name=? and TO_DAYS(now())-TO_DAYS(start_time)<=1",spiderName);
			}
		} catch (Throwable e) {
			try {
				dao.executeSQL("update t_spider_log set remark='失败' where spider_name=? and TO_DAYS(now())-TO_DAYS(start_time)<=1",spiderName);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			log.error("加载spider失败!"+spiderName,e);
			e.printStackTrace();
		}
	}
}
