package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.PubFun;

public class MeiLanFangDaJuYuanSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(BjrySpider.class);
	private final static String BASE_URL = "http://mlfdjy.mypiao.com/";
	private final static String home_URL = "http://mlfdjy.mypiao.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat otherDate = new SimpleDateFormat("yyyy-M-d HH:mm");
	@Override
	public void extract() {
		Document doc=getDoc(home_URL);
		Iterator<Element> formList=doc.select("form[method=LINK]").iterator();
		Map<String,Show> map=new HashMap<String,Show>();
		while(formList.hasNext()){
			Element tempForm=formList.next();
			Element temp=tempForm.select("input[value=网上选座]").first().parent();
			Element eleInput=temp.previousElementSibling().previousElementSibling();
			String showTime=eleInput.text().replace("  ", "").trim();
			String showName=eleInput.previousElementSibling().text().trim();
			String url=tempForm.absUrl("action").trim();
			try{
				processLastPage(url,showName,showTime,map);
			}catch(Exception ee){
			}
		}
		Iterator<String> itShowList=map.keySet().iterator();
		while(itShowList.hasNext()){
			String showName=itShowList.next();
			Show show=map.get(showName);
			try{
				this.getDao().saveShow(show);
			}catch(Exception ee){
				ee.printStackTrace();
			}
		}
	}
	
	public void processLastPage(String url,String showName,String showTime,Map<String,Show> map){
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
				String requestUrl="http://mlfdjy.mypiao.com"+strSeatArray[1].replace("'", "");
				try{
					Document ajaxDocument=Jsoup.connect(requestUrl).post();
					for(Element clear:ajaxDocument.select("img[src$=Z.JPG]")){
			    		clear.remove();
			    	}
					Iterator<Element> itSet=ajaxDocument.select("img[width=12]").iterator();
				    while(itSet.hasNext()){
				    	Element eleIt=itSet.next();
				    	String priceContent=eleIt.attr("onmouseover").trim();
				    	priceContent=PubFun.getRegex("价格([^状]*)状态", priceContent, 1) ;
				    	priceContent=priceContent.replaceAll("<[^>]*>", "").replace(": ", "").replace("元", "");
				    	priceContent=PubFun.getRegex("[^\\d]*([\\d]+)\\.[^\\.]*", priceContent, 1);
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
				String requestUrl="http://mlfdjy.mypiao.com"+strSeatArray[1].replace("'", "");
				try{
					Document ajaxDocument=Jsoup.connect(requestUrl).timeout(50000).post();
					Iterator<Element> trList=ajaxDocument.select("tr").iterator();
					while(trList.hasNext()){
						Element eleTr=trList.next();
						Element trElem=eleTr.select("td:eq(2)").first();
						if(trElem!=null){
							String price=trElem.text().replaceAll("   ", "").replace(": ", "").replace("   ", "").replace("元", "").trim();
								price=PubFun.getRegex("([\\d]*\\.[\\d]*)[\\d]*", price, 1);
								price=PubFun.getRegex("([\\d]+)\\.[^\\.]*", price, 1);
								if(!priceSet.contains(price)&& price!=null){
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
			show.setSiteName("梅兰芳大剧院");
			show.setType(ShowType.DRAMA);
			show.setImage_path("");
			show.setFirstAgent(true);
			Map<String,List<TicketPrice>> tiketPrice=new HashMap<String,List<TicketPrice>>();
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Set<String> priceSet=new HashSet<String>();
			List<String> ajaxUrlList=PubFun.getRegexs("Ajax.Updater\\(([^\\{]*)\\{", html, 1);
			if(ajaxUrlList.get(0)!=null){
				String strSeatUrl=ajaxUrlList.get(0).trim();
				String[] strSeatArray=strSeatUrl.split(",");
				String requestUrl="http://mlfdjy.mypiao.com"+strSeatArray[1].replace("'", "");
				try{
					Document ajaxDocument=Jsoup.connect(requestUrl).post();
					for(Element clear:ajaxDocument.select("img[src$=Z.JPG]")){
			    		clear.remove();
			    	}
				    Iterator<Element> itSet=ajaxDocument.select("img[width=12]").iterator();
				    while(itSet.hasNext()){
				    	Element eleIt=itSet.next();
				    	String priceContent=eleIt.attr("onmouseover").trim();
				    	priceContent=PubFun.getRegex("价格([^状]*)状态", priceContent, 1) ;
				    	priceContent=priceContent.replaceAll("<[^>]*>", "").replace(": ", "").replace("元", "");
				    	priceContent=PubFun.getRegex("[^\\d]*([\\d]+)\\.[^\\.]*", priceContent, 1);
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
				String requestUrl="http://mlfdjy.mypiao.com"+strSeatArray[1].replace("'", "");
				try{
					Document ajaxDocument=Jsoup.connect(requestUrl).timeout(50000).post();
					Iterator<Element> trList=ajaxDocument.select("tr").iterator();
					while(trList.hasNext()){
						Element eleTr=trList.next();
						Element trElem=eleTr.select("td:eq(2)").first();
						if(trElem!=null){
							String price=trElem.text().replaceAll("   ", "").replace(": ", "").replace("   ", "").replace("元", "").trim();
								price=PubFun.getRegex("([\\d]*\\.[\\d]*)[\\d]*", price, 1);
								price=PubFun.getRegex("([\\d]+)\\.[^\\.]*", price, 1);
								if(!priceSet.contains(price)&& price!=null){
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
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		MeiLanFangDaJuYuanSpider spider=new MeiLanFangDaJuYuanSpider();
		spider.setDao(new DataDao());
		spider.extract();
	}

}
