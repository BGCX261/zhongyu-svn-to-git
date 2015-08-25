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

import com.piaoyou.crawler.baseInfo.DownPhotoSpider;
import com.piaoyou.crawler.merge.MergeRecord;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.NewSendMail;

/**
 * 启动票价抓取
 * @author Administrator
 *
 */
public class Startup {
	private static final Log log = LogFactory.getLog(Startup.class);
	private static final int threadCount = 10;
	public static void main(String[] args) throws IOException{
		long startTime = System.currentTimeMillis();
		String path = "";
		if(args.length>0) {
			path = args[0];
		}else{
			System.exit(0);
		}
		StringBuilder sb = new StringBuilder();
		sb.append("\r\n").append("/*****************任务开始启动**********************************/").append("\r\n");
		sb.append("/*****************日期").append(new Date()).append("**************/").append("\r\n");
		sb.append("/*****************版权所有:北京众娱****************************/").append("\r\n");
		log.info(sb.toString());

		DataDao dao = new DataDao();
		
		startAllSpider(dao,path);//启动所有spider
		retryFailureSpider(dao);//重试失败的spider
		MergeRecord.merge();//合并数据
		DownPhotoSpider down = new DownPhotoSpider();//下载图片
		down.setDao(dao);
		down.downShowImg();
		down.downSeatPic();
		long endTime = System.currentTimeMillis();
		log.info("抓取完成!");
		String[] emails = {"398885143@qq.com","921647812@qq.com","842988813@qq.com","291861450@qq.com","370546898@qq.com,duanxiaojie321@163.com"};
		long miao = (endTime-startTime)/1000;
		for(String email:emails){
			NewSendMail sendMail = new NewSendMail(email,"本次爬取会票花费时间"+miao/60+"分钟\r\n"+MergeRecord.sb.toString()+"\r\n"+dao.toString());
			sendMail.sendMail();
		}
		for(String email:emails){
			NewSendMail sendMail = new NewSendMail(email,DataDao.deleteShowTime.toString());
			sendMail.sendMail();
		}
	}
	
	/**
	 * 启动所有抓取spider,遍历所有spider下文件。执行extract方法
	 * @param dao
	 */
	public static void startAllSpider(DataDao dao){
		startAllSpider(dao,"spider");
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
