package com.piaoyou.boot;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.dao.DataDao;

/**
 * 启动票价抓取
 * @author Administrator
 *
 */
public class Startup {
	private static final Log log = LogFactory.getLog(Startup.class);
	private static final int threadCount = 10;
	public static void main(String[] args) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n").append("/*****************任务开始启动**********************************/").append("\r\n");
		sb.append("/*****************日期").append(new Date()).append("**************/").append("\r\n");
		sb.append("/*****************版权所有:北京众娱****************************/").append("\r\n");
		System.out.println(sb.toString());
		DataDao dao = new DataDao();
		startAllSpider(dao,"show");//启动演出爬虫
		startAllSpider(dao,"movie");//启动电影爬虫
		startAllSpider(dao,"group");//启动团购爬虫
		retryFailureSpider(dao);//重试失败的spider
	}
	
	/**
	 * 启动所有抓取spider,遍历所有spider下文件。执行extract方法
	 * @param dao
	 */
	public static void startAllSpider(DataDao dao){
		startAllSpider(dao,"newspider");
	}
	public static void startAllSpider(DataDao dao ,String path){
		ExecutorService exec = Executors.newFixedThreadPool(threadCount);
		File dir = new File(Startup.class.getResource("").getPath()+"../crawler/"+path+"/");
		for(File className:dir.listFiles(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String name) {
				return name.endsWith(".class") && !name.contains("$");
			}
		})){
			String name = "com.piaoyou.crawler."+path+"."+className.getName().replace(".class", "");
			SpiderRunner runner = new SpiderRunner(name, dao);
			exec.execute(runner);
		}
		exec.shutdown();
		try {
			exec.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			log.error("线程异常终止", e);
		}
	}
	/**
	 * 重试启动失败的spider
	 * @param dao
	 * @throws IOException 
	 */
	public static void retryFailureSpider(DataDao dao) throws IOException{
		ExecutorService exec = Executors.newFixedThreadPool(threadCount);
		
		List<Map<String,Object>> failure = dao.searchBySQL("select distinct spider_name from t_spider_log where remark='失败' and TO_DAYS(now())-TO_DAYS(start_time)<=1");
		
		for(Map<String,Object> record:failure){
			SpiderRunner runner = new SpiderRunner(record.get("spider_name").toString(), dao);
			exec.execute(runner);
		}
		
		exec.shutdown();
		try {
			exec.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
			log.error("重试抓取线程异常终止", e);
		}
	}
	
}
