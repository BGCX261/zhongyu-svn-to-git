package com.piaoyou.crawler.big;

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

public class YiPiaoTongSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(YiPiaoTongSpider.class);
	public static String encode="utf-8";

	public static Map<String,String> map=new HashMap<String,String>();
	static{
		map.put("1", "2");//
		map.put("2", "3");
		map.put("5", "13");
		map.put("4", "12");
		map.put("3", "27");
		map.put("6", "16");
	}
	@Override
	public void extract() {
		try{
	       this.getImport("http://www.tickets.com.cn/setcity_2.html","city=2");
		}catch(Exception ee){
			log.error(Const.concat("http://www.tickets.com.cn/setcity_2.html","city=2"),ee);
		}
		try{
           this.getImport("http://www.tickets.com.cn/setcity_1.html","city=1");
		}catch(Exception ee){
			log.error(Const.concat("http://www.tickets.com.cn/setcity_1.html","city=1"),ee);
		}
    }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		YiPiaoTongSpider Spider = new YiPiaoTongSpider();
		Spider.setDao(new DataDao());
		Spider.processLastPage("http://www.tickets.com.cn/products_6521.html", 1, "aa");
	}
	public void processConcentratePage(String conutUrl,int sortType,String city){
		Document doc=getDetailDoc(conutUrl,city);
		Iterator<Element> showEleList=doc.select("div#AllShow dl").iterator();
		while(showEleList.hasNext()){
			Element showEle=showEleList.next();
			String lastUrl=showEle.select("dt a:has(img)").first().absUrl("href");
			try{
			  processLastPage(lastUrl,sortType,city);
			}catch(Exception ee){
				log.error(Const.concat(lastUrl,sortType,city),ee);
			}
		}
	}
	public void processLastPage(String url,int type,String city){
		Document doc=getDetailDoc(url,city);
		Show show=new Show();
		String showName=doc.select("div#ShowInfo h2").first().text();
		Element site=doc.select("div#showInfoBox dl dd a").first();
		if(null==site||"".equals(site.text().trim())||site.text().trim().contains("待定"))
		    	 return;
		String showSite=site.text().trim();//演出场馆
		String summary=PubFun.cleanElement(doc.select("div#ShowDescriptionContent").first()).text();
		String imagePath=doc.select("div#PhotoDescription img").first().absUrl("src");
		Map<String,List<TicketPrice>> timeAndPrice =getMap(doc,url);
		show.setAgent_id("9");
		show.setName(showName);
		show.setFirstAgent(false);
		show.setImage_path(imagePath);
		show.setIntroduction(summary);
		show.setSiteName(showSite);
		show.setType(type);
		show.setTimeAndPrice(timeAndPrice);
		try{
			getDao().saveShow(show);
		}
		catch(Exception ee){
			
		}
	}
	
	public Map<String,List<TicketPrice>> getMap(Document doc,String url ){
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Iterator<Element> aElelist=doc.select("ul#mycarousel li a").iterator();
		while(aElelist.hasNext()){
			Element aEle=aElelist.next();
			String day=PubFun.getCurrentYear()+"-"+aEle.text();
			day=day.replaceAll("[^\\d&&[^-]]", "-");
			day=day.substring(0,day.length()-1);
			String href=aEle.attr("href").replace("#", "").trim();
			String selector="div#"+href+" ol";
			String time=doc.select(selector).first().select("li.rYCSJ").first().text().trim();
			String showTime=day+" "+time;
			//showTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy年MM月dd HH:mm"),"yyyy-MM-dd HH:mm");
			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
	    	if(newshowTime!=null)
	    		showTime=newshowTime;
			Iterator<Element> olList=doc.select(selector).iterator();
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			while(olList.hasNext()){
				TicketPrice ticket=new TicketPrice();
				Element eleli=olList.next();
				String price=eleli.select("li.rPJ").first().text().trim();
				if("".equals(price))
					continue;
				if(price.contains("元")){
					price=price.replaceAll("元", "");
				}
				if(price.contains("待定")){
					continue;
				}
				boolean isHaveTicket=false;
				Element form=eleli.select("li.rDP form").first();
				if(form==null){
					isHaveTicket=false;
				}
				else{
					isHaveTicket=true;
					ticket.setDetailURL(url);
				}
				ticket.setPrice(price);
				ticket.setExist(isHaveTicket);
				ticket.setMainURL(url);
				list.add(ticket);
			}
			map.put(showTime, list);
		}//
		return map;
	}
	public void processFirstUrl(String url,String city){
	   Document doc=getDetailDoc(url,city);
	   Iterator<Element> sort=doc.select("div#searchTips dl.fenlei dd a").not("a[class*=hover]").iterator();
	   while(sort.hasNext()){
		   Element a=sort.next();
		   String sortUrl=a.absUrl("href");
		   String sortText=a.text();
		   int sortType=9;
		   if(sortText.contains("演唱")){
			    sortType=1;
		   }
   		   else if(sortText.contains("音乐")&&!sortText.contains("剧")){
				sortType=2;
		   }
		   else if(sortText.contains("话剧") || sortText.contains("歌剧")||sortText.contains("音乐剧")){
				sortType=3;
		   }
		   else if(sortText.contains("芭蕾") || sortText.contains("舞蹈")||sortText.contains("舞")){
				sortType=4;
		   }
		   else if(sortText.contains("戏") || sortText.contains("魔术")||sortText.contains("杂技") || sortText.contains("相声")||sortText.contains("小品") || sortText.contains("马戏")){
				sortType=5;
		   }
		    else if(sortText.contains("儿童亲子")){
				sortType=6;
			}
			else if(sortText.contains("体育") ){
				sortType=7;
			}
			else if(sortText.contains("休闲")||sortText.contains("旅游")|| sortText.contains("电影")){
				sortType=8;
		    }
		   Document sortDoc=getDetailDoc(sortUrl,city);
		   int pageCount=0;
		   Element pageCountEle=sortDoc.select("div#pager div.orangePage a[title*=lastpage]").first();
		   if(pageCountEle==null){
			   pageCount=1;
		   }
		   else{
			  String strPageCount= pageCountEle.absUrl("href");
			  try{
				  Integer intPageCount=Integer.parseInt(PubFun.getRegex("http://www\\.tickets\\.com\\.cn/perform_all.asp\\?page=([\\d]*)&", strPageCount, 1));
				  pageCount=intPageCount;
			  }
			  catch(Exception ee){
				  
			  }
		   }
		   if(pageCount==1){
			   try{
			     processConcentratePage(sortUrl,sortType,city) ;
			   }catch(Exception ee){
				   log.error(Const.concat(sortUrl,sortType,city),ee); 
			   }
		   }
		   else{
			   for(int i=1;i<=pageCount;i++){//
				   String pageUrl="http://www.tickets.com.cn/perform_all.asp?page="+i+"&cateid="+map.get(String.valueOf(sortType))+"&performStartDate=&performEndDate=&searchKW=";
				   try{
					     processConcentratePage(sortUrl,sortType,city) ;
					   }catch(Exception ee){
						   log.error(Const.concat(sortUrl,sortType,city),ee); 
					   }
			   }
		   }
	   }
	}
	public static Document getDetailDoc(String url,String cookieValue) {
		Document doc = null;
		for(int i=0;i<tryTimes;i++){
			try {
				doc = Jsoup.connect(url).referrer(url)
					.userAgent(userAgent).timeout(timeout).cookie("t", cookieValue).get();
			} catch (IOException e) {
				//ignore
			}
			if(doc != null){
				break;
			}
		}
		return doc;
	}
	public void getImport(String url,String city){
		Document doc=getDetailDoc(url,city);
		//String ele=doc.select("div#nCityLanguage").first().html();
		 String href=doc.select("div#nMenu ul.nMenuLi li a").last().absUrl("href");
		 try{
		    processFirstUrl(href, city);
		 }catch(Exception ee){
			 log.error(Const.concat(href,city),ee);
		 }
		 
	}
}
