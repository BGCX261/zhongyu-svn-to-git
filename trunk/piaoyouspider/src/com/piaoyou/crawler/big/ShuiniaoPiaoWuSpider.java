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

public class ShuiniaoPiaoWuSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(ShuiniaoPiaoWuSpider.class);
	private static final int timeout = 60*1000;
	private static final int tryTimes = 5;//重试次数
	private static final String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)";
	private static final String BASE_URL = "http://www.shuiniaoticket.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	@Override
	public void extract() {
	   Document doc=getDoc("http://www.shuiniaoticket.com/pc_nzgx1681.htm");
	   Iterator<Element> lilist=doc.select("div.sn_top div.sn_navmain ul li:gt(1)").subList(0, 8).iterator();
	   while(lilist.hasNext()){
		   Element liEle=lilist.next();
		   Element temp=liEle.select("a").first();
		   String sort=temp.text();
		   String url=temp.absUrl("href");
		   try{
		      processPageBySort(url,sort);
		   }catch(Exception ee){
			   log.error(Const.concat(url,sort),ee); 
		   }
	   }
	}
	public void processPageBySort(String url,String sort){
		   Document doc=getDoc(url);
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
		    Element ele=doc.select("div.sng_conbox div.sng_page.sngb_ppage a:containsOwn(末)").first();
		    int pageCount=1;
		    String href=ele.absUrl("href");
		    if(!href.contains("http")){
		    	pageCount=1;
		    	try{
		    	   ProccessPageByPageCount(url,sortType);
		    	}catch(Exception ee){
		    		 log.error(Const.concat(url,sortType),ee); 
		    	}
		    }
		    else{
		    	String strPageCount=PubFun.getRegex("order_0-([\\d]+)\\.htm\\?", href, 1);
		    	try{
		    		pageCount=Integer.parseInt(strPageCount);
		    	}catch(Exception ee){
		    		pageCount=1;
		    	}
		    	for(int i=1;i<=pageCount;i++){
		    		if(i==1){
		    			try{
		 		    	   ProccessPageByPageCount(url,sortType);
		 		    	}catch(Exception ee){
		 		    		 log.error(Const.concat(url,sortType),ee); 
		 		    	}
		    		}
		    		else{
		    			String pageUrl=href.substring(0,href.lastIndexOf("-")+1)+i+".htm"+href.substring(href.lastIndexOf("?"));
		    			try{
		 		    	   ProccessPageByPageCount(pageUrl,sortType);
		 		    	}catch(Exception ee){
		 		    		 log.error(Const.concat(pageUrl,sortType),ee); 
		 		    	}
		    		}
		    	}
		    }
	}
	public void ProccessPageByPageCount(String url,int type){
		Document doc=getDoc(url);
		Iterator<Element> divEleList=doc.select("div.sngb_rigtop div.sng_botbox div.sng_conbox div.sng_prod").iterator();
		while(divEleList.hasNext()){
			Element divEle=divEleList.next();
			String lastUrl=divEle.select("div.right h4 a").first().absUrl("href");
			try{
		     	prcocessLastPage(lastUrl,type);
			}catch(Exception ee){
				 log.error(Const.concat(lastUrl,type),ee); 
			}
		}
	}
	public void prcocessLastPage(String url,int type){
		boolean isSave=true;
		Document doc=getDoc(url);
		Show show=new Show();
		String showName=doc.select("div.snc_maintt div.snc_tty h5").first().text().trim();
		Element divEleB=doc.select("div.snc_maintt div.snc_tty div.addr div strong").first();
		if(divEleB==null)
			return;
		String showSite=divEleB.text().trim();
		if("".equals(showSite)||showSite.contains("待定"))
			return;
		String imagePath=doc.select("div#prod div.image img").first().absUrl("src");
		String  summary=PubFun.cleanElement(doc.select("div#tab_06 div.snc_mbltcon div.main").first()).text();
		show.setImage_path(imagePath);
		show.setIntroduction(summary);
		show.setType(type);
		show.setSiteName(showSite);
		show.setName(showName);
		show.setAgent_id(agentID);
		int z= doc.select("div#tabbd div.snc_tbtit ul li").size();
		if(z>=3){
			Iterator<Element> siteList=doc.select("div#tabbd div.snc_tbtit ul li[class^=bg]").iterator();
			int i=0;
			while(siteList.hasNext()){
				Element siteEle=siteList.next();
				showSite=siteEle.select("span").first().text().trim();
				show.setSiteName(showSite);
				Map<String,List<TicketPrice>> map;
				if(i==0)
					map=GetMap(doc,url,"div#tabbg span[id=venueZWZX] script");
				else
					 map=GetMap(doc,url,"div#tabbg span[id=venueZWLH] script");
				if(map==null)
					isSave=false;
				show.setTimeAndPrice(map);
				
				if(isSave){
					try{
						getDao().saveShow(show);
					}catch(Exception ee){
						log.error(url+ee.getMessage());
					}
				}
				i++;
			}
		}
		else{
			Map<String,List<TicketPrice>> map=GetMap(doc,url,"div#tabbg span[id^=venue] script");
			if(map==null)
				isSave=false;
			show.setTimeAndPrice(map);
		
			if(isSave){
				try{
					getDao().saveShow(show);
				}catch(Exception ee){
					ee.printStackTrace();
				}
			}
			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception{
		ShuiniaoPiaoWuSpider Spider = new ShuiniaoPiaoWuSpider();
		Spider.setDao(new DataDao());
		Spider.extract();
		//Spider.prcocessLastPage("http://www.shuiniaoticket.com/p_6963.htm",8);
		//Document doc=Jsoup.connect("http://www.shuiniaoticket.com/page/ticketInnerVenue.jsp?eventId=3137&venueId=ZGGJDJY&isBankOffers=null&time=1307595581321").timeout(50*1000).get();
	}
	public  Map<String,List<TicketPrice>> GetMap(Document doc,String mainUrl,String selector){
		if(doc==null)
			return null;
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Element requestEle=doc.select(selector).first();//
		if(requestEle==null)
			return null;
		String request=requestEle.html().trim();
		String requerUrl="http://www.shuiniaoticket.com"+PubFun.getRegex("\\)\\.load\\('([^']*)'\\)", request, 1);
		Document priceDoc=getDoc(requerUrl);
		if(priceDoc==null)
			return null;
		Iterator<Element> ullist=priceDoc.select("ul").iterator();
		while(ullist.hasNext()){
			Element ulEle=ullist.next();
			String showTime=ulEle.select("li.snc_tbli01").first().text();
			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
	    	if(newshowTime!=null)
	    		showTime=newshowTime;
		    Iterator<Element> divEleList=ulEle.select("li.snc_tbli03>div").iterator();//snc_tbli03
		    List<TicketPrice> list=new ArrayList<TicketPrice>();
		    while(divEleList.hasNext()){
		    	Element eleDiv=divEleList.next();
		    	String remark=eleDiv.select("b").first().text().replace(":", "");
		    	Iterator<Element> noExistsPriceList=eleDiv.select("div>div>s").iterator();
		    	while(noExistsPriceList.hasNext()){
		    		Element sEle=noExistsPriceList.next();
		    		TicketPrice ticket=new TicketPrice();
		    		ticket.setExist(false);
		    		ticket.setPrice(sEle.text().trim());
		    		ticket.setRemark(remark);
		    		ticket.setMainURL(mainUrl);
		    		list.add(ticket);
		    	}
		    	Iterator<Element> existsPriceList=eleDiv.select("div>span>a").iterator();
		    	while(existsPriceList.hasNext()){
		    		Element aEle=existsPriceList.next();
		    		TicketPrice ticket=new TicketPrice();
		    		ticket.setExist(true);
		    		ticket.setPrice(aEle.text().trim());
		    		ticket.setRemark(remark);
		    		ticket.setMainURL(mainUrl);
		    		ticket.setDetailURL(aEle.absUrl("href").trim());
		    		list.add(ticket);
		    	}
		    	
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

}
