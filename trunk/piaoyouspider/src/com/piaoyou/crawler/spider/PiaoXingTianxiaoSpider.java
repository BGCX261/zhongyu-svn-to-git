package com.piaoyou.crawler.spider;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;
public class PiaoXingTianxiaoSpider extends TicketSpider implements SpiderTask {
	private static final int timeout = 60*1000;
	private static final int tryTimes = 5;//重试次数
	private static final String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)";
	public static Log log=LogFactory.getLog(PiaoXingTianxiaoSpider.class);
	public static String[] urlArray={"http://www.ticketnet.cn/projectlist.aspx","http://sh.ticketnet.cn/projectlist.aspx"};
	public void extract() {
		for(String url:urlArray){
			Document doc=getDoc(url);
			Iterator<Element> divEleList= doc.select("#CollapsiblePanel1").iterator();
			while(divEleList.hasNext()){
				  Element divEle=divEleList.next();
				  String sort=divEle.select("a[style]").first().text().trim();
				  int sortType=9;
				  if(sort.contains("演唱")){
					    sortType=1;
				  }
				  else if(sort.contains("音乐")&&!sort.contains("剧")){
						sortType=2;
				  }
				  else if(sort.contains("话剧") || sort.contains("歌剧")||sort.contains("音乐剧")){
						sortType=3;
				  }
				  else if(sort.contains("芭蕾") || sort.contains("舞蹈")||sort.contains("舞")){
						sortType=4;
				  }
				  else if(sort.contains("戏剧")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
					    sortType=5;
				  }
				  else if(sort.contains("儿童亲子")){
						sortType=6;
				  }
				  else if(sort.contains("体育") ){
						sortType=7;
				  }
				  else if(sort.contains("休闲")||sort.contains("旅游")|| sort.contains("电影")){
						sortType=8;
				  }
				  String selector="";
				  if(url.contains("sh.ticketnet.cn")){
					  selector="#CollapsiblePanel2 tr";
				  }
				  else{
					  selector="div[id~=togglediv[\\d]*] table:not(#table_tr01) tr";
				  }
				  Iterator<Element> trEleList=divEle.select(selector).iterator();//
				  while(trEleList.hasNext()){
				     Element trEle=trEleList.next();
					 String lastUrl=trEle.select("a").first().absUrl("href");
					 try{
					    processLastPage(lastUrl,sortType);
					 }catch(Exception ee){
							log.error(Const.concat(lastUrl,sortType),ee); 
					 }
				  }
			}
		}
	}
	public void processLastPage(String url,int sorttype){
		Document doc=getDoc(url);
		String showName=doc.select("#threemain01p").first().text().trim();
		String imagePath=doc.select("#threemain01p02>img").first().absUrl("src");
		Element site=doc.select("#a0102").first();
		if(null==site||"".equals(site.text().trim())||site.text().trim().contains("待定"))
		    	 return;
		String showSite=site.text().trim();//演出场馆
		Map<String,List<TicketPrice>> map=getMap(doc,url);
		if(map==null){
			map=new HashMap<String,List<TicketPrice>>();
		}
		String summary;
		if(url.contains("sh.ticketnet.cn"))
		summary=PubFun.cleanElement(doc.select("#threemain01p05").get(1)).html();
		else
			summary=PubFun.cleanElement(doc.select(".detail_content").first()).html();	
		Show show=new Show();
		show.setWatermark(true);
		show.setAgent_id("18");
		show.setImage_path(imagePath);
		show.setName(showName);
		show.setSiteName(showSite);
		show.setType(sorttype);
		show.setIntroduction(summary);
		show.setTimeAndPrice(map);
	    try{
	    	getDao().saveShow(show);
	    }catch(Exception ee){
	    	
	    }
	}
	public  Map<String,List<TicketPrice>> getMap(Document doc,String url){
		if(doc==null)
			return null;
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Iterator<Element> timeEleList=doc.select("#threemain01p05_01>table>tr:has(td)").subList(1, doc.select("#threemain01p05_01>table>tr:has(td)").size()-1).iterator();
		while(timeEleList.hasNext()){
			Element timeEle=timeEleList.next();
			String showTime=timeEle.select("div").first().text();
			if(showTime.contains("待定"))
				continue;
			if(showTime==null ||showTime.equals("")){
				continue;
			}
			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
			if(newshowTime!=null){
				showTime=newshowTime;
			}
			List<TicketPrice> list=new ArrayList<TicketPrice>();//not（span>a）
			Iterator<Element> noexistsPriceList=timeEle.select("td:eq(2) span:not(:has(a))").iterator();//td:eq(2) span
			while(noexistsPriceList.hasNext()){
				Element eldPrice=noexistsPriceList.next();
				TicketPrice ticket=new TicketPrice();
				String price=eldPrice.text().trim();
				String remark=null;
				ticket.setPrice(price);
				ticket.setExist(false);
				ticket.setMainURL(url);
				if(price.contains("点击预定")){
					price="-1";
					continue;
				}
				if("".equals(price))
					continue;
			    if(PubFun.isMatchRegex(price, "[\\d]*[（|\\(]([^&]*)[）|\\)]")){
			    	String newPrice=price;
			    	price=PubFun.getRegex("([\\d]*)[（|\\(]([^&]*)[）|\\)]", newPrice, 1);
			    	remark=PubFun.getRegex("([\\d]*)[（|\\(]([^&]*)[）|\\)]", newPrice, 2);
			    	ticket.setPrice(price);
			    	ticket.setRemark(remark);
			    }
				list.add(ticket);
			}
			Iterator<Element> ExistsPriceList=timeEle.select("td:eq(2) a").iterator();//td:eq(2) a
			while(ExistsPriceList.hasNext()){
				String remark=null;
				Element eldPrice=ExistsPriceList.next();
				TicketPrice ticket=new TicketPrice();
				String price=eldPrice.text().trim();
				String lastUrl=eldPrice.absUrl("href").trim();
				if("".equals(lastUrl)){
			    	price="-1";
			    	continue;
				}
				ticket.setPrice(price);
				ticket.setExist(true);
				ticket.setMainURL(url);
				ticket.setDetailURL(lastUrl);
				if(price.contains("点击预定")){
					price="-1";
					continue;
				}
				 if(PubFun.isMatchRegex(price, "[\\d]*[（|\\(]([^&]*)[）|\\)]")){
				    	String newPrice=price;
				    	price=PubFun.getRegex("([\\d]*)[（|\\(]([^&]*)[）|\\)]", newPrice, 1);
				    	remark=PubFun.getRegex("([\\d]*)[（|\\(]([^&]*)[）|\\)]", newPrice, 2);
				    	ticket.setPrice(price);
				    	ticket.setRemark(remark);
				    }
				list.add(ticket);
			}
			map.put(showTime, list);
		}
		return map;
	}
	
	public static Document getDoc(String url) {
		Document doc = null;
		for(int i=0;i<tryTimes;i++){
			try {
				doc = Jsoup.connect(url).referrer(url)
					.userAgent(userAgent).timeout(timeout).get();
			} catch (IOException e) {
				//ignore
			}
			if(doc != null){
				break;
			}
		}
		return doc;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PiaoXingTianxiaoSpider spider=new PiaoXingTianxiaoSpider();
		spider.setDao(new DataDao());
		//spider.extract();
		spider.processLastPage("http://www.ticketnet.cn/project33950.html", 1);
	}
}
