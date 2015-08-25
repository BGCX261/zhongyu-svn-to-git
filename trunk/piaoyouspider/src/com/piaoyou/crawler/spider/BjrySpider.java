package com.piaoyou.crawler.spider;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;
import com.piaoyou.util.date.DateUtils;

public class BjrySpider extends TicketSpider implements SpiderTask{
	private static final Log log = LogFactory.getLog(BjrySpider.class);
	private final static String home_URL = "http://219.237.6.16/";
	private final static String BASE_URL="http://www.bjry.com";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	public void extract() {
		Document start = getDoc(home_URL);
		Map<String,Show> map=new HashMap<String,Show>();
		Iterator<Element>  itList = start.select("img[src$=buy.gif]").iterator();
		while(itList.hasNext()){
			Element tEle=itList.next().parent();
			String href=tEle.select("a").first().absUrl("href").trim();
			String showTime=tEle.parent().previousElementSibling().previousElementSibling().text().replace("  ", "").trim();
			String site=tEle.parent().previousElementSibling().previousElementSibling().previousElementSibling().text().replace("  ", "").trim();
			String showName=tEle.parent().previousElementSibling().previousElementSibling().previousElementSibling().previousElementSibling().text().trim();
			try {
				processCenterPage(href,showName,showTime,site,map);
			} catch (Exception e) {
				log.error(Const.concat(href), e);
			}
		}
		Iterator<String> itNameList=map.keySet().iterator();
		while(itNameList.hasNext()){
			String  eleName=itNameList.next();
			Show show=map.get(eleName);
			try{
				this.getDao().saveShow(show);
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
	}

	public void processCenterPage(String url,String showName,String showTime,String site , Map<String,Show> map){
		Document doc=getDoc(url);
		String html=doc.html();
		if(map.containsKey(showName)){
			Show show=map.get(showName);
			Map<String,List<TicketPrice>> tiketPrice=show.getTimeAndPrice();
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Set<String> priceSet=new HashSet<String>();
			List<String> ajaxUrlList=PubFun.getRegexs("Ajax.Updater\\(([^\\{]*)\\{", html, 1);
			if(ajaxUrlList.get(0)!=null){
				String strSeatUrl=ajaxUrlList.get(0).trim();
				String[] strSeatArray=strSeatUrl.split(",");
				String requestUrl="http://219.237.6.16"+strSeatArray[1].replace("'", "");
				try{
					Document ajaxDocument=Jsoup.connect(requestUrl).timeout(50000).post();
					for(Element clear:ajaxDocument.select("a:has(img[src$=Z.JPG])")){
			    		clear.remove();
			    	}
				    Iterator<Element> itSet=ajaxDocument.select("a img").iterator();
				    while(itSet.hasNext()){
				    	Element eleIt=itSet.next();
				    	String priceContent=eleIt.attr("onmouseover").trim();
				    	priceContent=PubFun.getRegex("价格([^状]*)状态", priceContent, 1) ;
				    	priceContent=priceContent.replaceAll("<[^>]*>", "").replace(": ", "").replace("元", "");
				    	priceSet.add(priceContent);
				    }
				    Iterator<String> itPriceList=priceSet.iterator();
				    while(itPriceList.hasNext()){
				    	String price=itPriceList.next();
				    	TicketPrice ticket=new TicketPrice();
				    	ticket.setExist(true);
				    	ticket.setDetailURL(url);
				    	ticket.setMainURL(url);
				    	ticket.setPrice(price);
				    	list.add(ticket);
				    }
				}catch(Exception ee){
					
				}
			}
			String priceLevelContent=ajaxUrlList.get(1);
			if(priceLevelContent!=null){
				String[] strSeatArray=priceLevelContent.trim().split(",");
				String requestUrl="http://219.237.6.16"+strSeatArray[1].replace("'", "");
				try{
					Document ajaxDocument=Jsoup.connect(requestUrl).timeout(50000).post();
					Iterator<Element> trList=ajaxDocument.select("tr").iterator();
					while(trList.hasNext()){
						Element eleTr=trList.next();
						String price=eleTr.select("td:eq(2)").first().text().replaceAll("   ", "").replace(": ", "").replace("   ", "").replace("元", "").trim();
						if(!price.contains("--")){
							price=PubFun.getRegex("([\\d]*\\.[\\d]*)[\\d]*", price, 1);
							if(!priceSet.contains(price)){
								TicketPrice ticket=new TicketPrice();
						    	ticket.setExist(false);
						    	ticket.setDetailURL(url);
						    	ticket.setMainURL(url);
						    	ticket.setPrice(price);
						    	list.add(ticket);
							}
						}
					}
				}catch(Exception ee){
					ee.printStackTrace();
				}
			}
			tiketPrice.put(showTime, list);
			show.setTimeAndPrice(tiketPrice);
		}else{
			Show show=new Show();
			show.setAgent_id(agentID);
			show.setIntroduction("演出详细信息:请自己编辑");
			show.setName(showName);
			show.setSiteName(site);
			show.setType(ShowType.DRAMA);
			show.setImage_path("");
			show.setFirstAgent(true);
			Map<String,List<TicketPrice>> tiketPrice=new HashMap<String,List<TicketPrice>>();
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			//获取票价
			Set<String> priceSet=new HashSet<String>();
			List<String> ajaxUrlList=PubFun.getRegexs("Ajax.Updater\\(([^\\{]*)\\{", html, 1);
			if(ajaxUrlList.get(0)!=null){
				String strSeatUrl=ajaxUrlList.get(0).trim();
				String[] strSeatArray=strSeatUrl.split(",");
				String requestUrl="http://219.237.6.16"+strSeatArray[1].replace("'", "");
				try{
					Document ajaxDocument=Jsoup.connect(requestUrl).post();
					for(Element clear:ajaxDocument.select("a:has(img[src$=Z.JPG])")){
			    		clear.remove();
			    	}
				    Iterator<Element> itSet=ajaxDocument.select("a img").iterator();
				    while(itSet.hasNext()){
				    	Element eleIt=itSet.next();
				    	String priceContent=eleIt.attr("onmouseover").trim();
				    	priceContent=PubFun.getRegex("价格([^状]*)状态", priceContent, 1) ;
				    	priceContent=priceContent.replaceAll("<[^>]*>", "").replace(": ", "").replace("元", "");
				    	priceSet.add(priceContent);
				    }
				    Iterator<String> itPriceList=priceSet.iterator();
				    while(itPriceList.hasNext()){
				    	String price=itPriceList.next();
				    	TicketPrice ticket=new TicketPrice();
				    	ticket.setExist(true);
				    	ticket.setDetailURL(url);
				    	ticket.setMainURL(url);
				    	ticket.setPrice(price);
				    	list.add(ticket);
				    }
				}catch(Exception ee){
					
				}
			}
			String priceLevelContent=ajaxUrlList.get(1);
			if(priceLevelContent!=null){
				String[] strSeatArray=priceLevelContent.trim().split(",");
				String requestUrl="http://219.237.6.16"+strSeatArray[1].replace("'", "");
				try{
					Document ajaxDocument=Jsoup.connect(requestUrl).timeout(50000).post();
					Iterator<Element> trList=ajaxDocument.select("tr").iterator();
					while(trList.hasNext()){
						Element eleTr=trList.next();
						String price=eleTr.select("td:eq(2)").first().text().replaceAll("   ", "").replace(": ", "").replace("   ", "").replace("元", "").trim();
						if(!price.contains("--")){
							price=PubFun.getRegex("([\\d]*\\.[\\d]*)[\\d]*", price, 1);
							if(!priceSet.contains(price)){
								TicketPrice ticket=new TicketPrice();
						    	ticket.setExist(false);
						    	ticket.setDetailURL(url);
						    	ticket.setMainURL(url);
						    	ticket.setPrice(price);
						    	list.add(ticket);
							}
							
						}
					}
				}catch(Exception ee){
					ee.printStackTrace();
				}
			}
			tiketPrice.put(showTime, list);
			show.setTimeAndPrice(tiketPrice);
			map.put(showName, show);
		}
		
	}


	public static void main(String[] args) {
		BjrySpider nn = new BjrySpider();
		nn.setDao(new DataDao());
		nn.extract();
	}
}


