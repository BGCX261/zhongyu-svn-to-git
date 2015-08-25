package com.piaoyou.crawler.spider;

import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;

public class T1Spider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(T1Spider.class);
    String[] urlArray={"http://www.t1.cn/ticket/Sing.html","http://gz.t1.cn/ticket/Sing.html"};//
	@Override
	public void extract() {
	/*	int nCity=0;
		for(String url:urlArray){
			String city=null;
			Document doc=getDoc(url);
			if(doc==null){
				log.error(url+"有错误");
				continue;
			}
			Iterator<Element> AList=doc.select(".seoSelect a").iterator();
			while(AList.hasNext()){
				Element AEle=AList.next();
				String href=AEle.absUrl("href").trim();
				String sort=AEle.text().trim();
				int sortType=ShowType.OTHER;
				if(sort.contains("演唱")){
					    sortType=ShowType.CONCERT;
				}
				else if(sort.contains("音乐")&&!sort.contains("剧")){
						sortType=ShowType.SHOW;
			    }
				else if(sort.contains("话剧") || sort.contains("歌剧")||sort.contains("剧")){
						sortType=ShowType.DRAMA;
				}
				else if(sort.contains("芭蕾") || sort.contains("舞蹈")||sort.contains("舞")){
						sortType=ShowType.DANCE;
				}
			    else if(sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
					    sortType=ShowType.OPERA;
				}
				else if(sort.contains("儿童亲子")){
					 sortType=ShowType.CHILDREN;
				}
				else if(sort.contains("体育") ){
					 sortType=ShowType.SPORTS;
				}
				else if(sort.contains("休闲")||sort.contains("旅游")|| sort.contains("电影")){
					 sortType=ShowType.HOLIDAY;
				}
				if(nCity==0)
					city="北京";
				else if(nCity==1)
					city="广州";
				processBySort(url,sortType, city);
			
			}
			nCity++;
		}*/
	}
	
	public void processBySort(String url,int sortType,String city){
		Document doc=getDoc(url);
		if(doc==null){
			log.error(url+"有错误");
			return;
		}
		Iterator<Element> aList=doc.select("table.coltable.s-table.seo-table tr:gt(0) td.caption a").iterator();
		while(aList.hasNext()){
			String href=aList.next().absUrl("href");
			processConcentratePage(href,sortType,city);
		}
	}
	public void processConcentratePage(String url,int Type,String city){
		Document doc=getDoc(url);
		if(doc==null){
			log.error(url+"有错误");
			return;
		}
	}
	public static void main(String[] args) {
		new T1Spider().extract();
	}
}
