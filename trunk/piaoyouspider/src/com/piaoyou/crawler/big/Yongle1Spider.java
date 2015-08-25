package com.piaoyou.crawler.big;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class Yongle1Spider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(Yongle1Spider.class);
	public static HashMap<String,String> map=new HashMap<String,String>();
	public static String encode="utf-8";
	public static String preUrl="http://www.228.com.cn";
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	static{
	    map.put("北京", "http://www.228.com.cn/bj/ticketmenu.do?areaid=010&tag=Bb#");
		map.put("上海", "http://www.228.com.cn/sh/ticketmenu.do?areaid=021&tag=Bb");
		map.put("郑州", "http://www.228.com.cn/zz/ticketmenu.do?areaid=0371&tag=Bb#");
		map.put("天津", "http://www.228.com.cn/tj/ticketmenu.do?areaid=022&tag=Bb#");
	}
	@Override
	public void extract() {
		java.util.Iterator<Entry<String,String>> it = map.entrySet().iterator();
		while(it.hasNext()){
		java.util.Map.Entry<String,String> entry = (java.util.Map.Entry<String,String>)it.next();
		String city=(String)entry.getKey();//得到要的城市
		String url=(String)entry.getValue();//得到Url
	    Document doc=getDoc(url);
		Iterator<Element> elementsList= doc.select("li.p_cont_1 div.new_fenlei_bor").iterator();
	    while(elementsList.hasNext()){
			Element ele=elementsList.next();
			String sort=ele.child(0).child(0).text();//分类
			int sortType=9;
			if(sort.contains("繁星戏剧村") ||sort.contains("打开音乐之门") ||sort.contains("剧院") ||sort.contains("星光音乐现场")||sort.contains("舞台") ||sort.contains("厅")||sort.contains("中心") )
				continue;
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
			else if(sort.contains("戏剧")||sort.contains("戏")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
				sortType=5;
			}
		    else if(sort.contains("儿童亲子")){
				sortType=6;
			}
			else if(sort.contains("体育") ){
				sortType=7;
			}
			else if(sort.contains("休闲")||sort.contains("旅游")||sort.contains("电影通票") ){
				sortType=8;
			}
			else if(sort.contains("综艺晚会") ){
				sortType=9;
			}
		    Iterator<Element> elementsULList= ele.select("div[id^=id] ul[class^=new_fenlei_cont]").iterator();//
			while(elementsULList.hasNext()){
				Element elementsUL=elementsULList.next();
				Element elementsA=elementsUL.select("li a.clink").first();
				String concentrateUrl=preUrl+elementsA.attr("href");//取到最终页面的url
				String title =elementsA.text();//演出的标题
				String showSite=elementsUL.select("li.fenleicont_venue").first().text().replace("场馆：", "").trim();  
				//演出地点
				if(showSite.contains("待定")||"".equals(showSite.trim()))
					continue;
				try{
				    processLastPage(city, sortType,concentrateUrl, title, showSite);}
				catch(Exception ee){
					log.error(Const.concat(city,sortType,concentrateUrl,title,showSite),ee);
				}
			}
		}
	}
}

	public void processLastPage(String city, int sortType,
			String concentrateUrl, String title, String showSite ) {
		    Document concDoc=getDoc(concentrateUrl);
		    String status=concDoc.select("div.layout>div.new_product_bor>h1>font.color3").first().text();
			String summary=PubFun.cleanElement(concDoc.select("ul[class$=circuit_actout] li").get(1)).html();//演出的说明
			showSite=concDoc.select("div.new_product_pic.line20 a.c43").first().text().trim();
			String seatUrl=null;
			try{
			    seatUrl=concDoc.select("#new_product_ico222 li:eq(0) a").first().absUrl("href");
			}catch(Exception  ee){
			}
			if("".equals(showSite)||showSite.contains("待定"))
				return;
			String imagePath=concDoc.select("div.new_product_bor div[class*=new_product_pic line20] img[src]").first().attr("src");
			boolean isFirstAgent=false;
			Element style=concDoc.select("div.layout div[style].new_product_bor").first();
			if(style!=null){
			   isFirstAgent=true;
		    }
			else{
				 isFirstAgent=false;
			}
			Show show=new Show();
			if(status.contains("预订中")){
				show.setStatus(0);
			}
			else if(status.contains("售票中")){
				show.setStatus(1);
			}
			show.setSeatPic_path(seatUrl);
			show.setAgent_id("27");
			show.setCity(city);
			show.setImage_path(imagePath);
			show.setIntroduction(summary);
			show.setName(title);
		    show.setType(sortType);
			show.setSiteName(showSite);
		    show.setFirstAgent(isFirstAgent);
		    show.setWatermark(true);
			processMap(concentrateUrl, concDoc, show);
			try{
		    	getDao().saveShow(show);
			}
			catch(Exception ee){
				
		    }
	}

	private void processMap(String concentrateUrl, Document concDoc, Show show) {
		Map<String,List<TicketPrice>> timeAndPrice=new HashMap<String,List<TicketPrice>>();
		Element eleFlag=concDoc.select("#menu_1_1_div>li.ticket_cont>div.new_more_ticket.bold.font13>a:contains(点击查看更多场次)").first();
		if(eleFlag!=null){
			String onclick=eleFlag.attr("onclick").trim();
			String p1=PubFun.getRegex("open4\\('([\\d]*)','([\\d]*)'\\)", onclick, 1);
			String p2=PubFun.getRegex("open4\\('([\\d]*)','([\\d]*)'\\)", onclick, 2);
			int z=concentrateUrl.indexOf("product");
			String preMoreUrl=concentrateUrl.substring(0, z)+"newProductUtil.do?method=moreTicket&id="+p2+"&areaid="+p1;
			concDoc=getDoc(preMoreUrl);
		}
		Iterator<Element> eleUlList=concDoc.select("li[class$=cont] ul[class^=new_ticket_info]").iterator();
		while(eleUlList.hasNext()){
			Element newTicketInfoUL=eleUlList.next();
			String showTime=newTicketInfoUL.children().get(1).text();//演出时间
			if(showTime.contains("待定"))
				continue;
			String aferShowTime=showTime;
			aferShowTime=PubFun.getRegex("([^$]*)星期", aferShowTime, 1);
			if(aferShowTime!=null){	
				showTime=aferShowTime.trim();
				showTime=PubFun.getCurrentYear()+"-"+showTime;
			}
			showTime=showTime.replace("：", ":");
			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
			if(newshowTime!=null){
				showTime=newshowTime;
				String cmonth=showTime.substring(5,7);
				String cday=showTime.substring(8, 10);
				String month="";
				String day="";
				try{
					String d=newDate.format(new Date());
					month=d.substring(5,7);
					day=d.substring(8,10);
					if(cmonth.compareToIgnoreCase(month)<0){
						showTime=PubFun.getNextYear()+PubFun.getRegex("[\\d]{1,4}(-[\\d]{1,2}-[\\d]{1,2} [\\d]{1,2}:[\\d]{1,2})", newshowTime, 1);
						
					}else if(cmonth.compareToIgnoreCase(month)==0){
						if(cday.compareToIgnoreCase(day)<0){
							showTime=PubFun.getNextYear()+PubFun.getRegex("[\\d]{1,4}(-[\\d]{1,2}-[\\d]{1,2} [\\d]{1,2}:[\\d]{1,2})", newshowTime, 1);
						}
					}
				}catch(Exception ee){
					
				}
				
			}
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Iterator<Element>  elePricelist=newTicketInfoUL.select("li.info_03 span").iterator();//获取演出票价
		    while(elePricelist.hasNext()){
		      Element elePrice=elePricelist.next();
		      String[] arrayPrice=elePrice.html().split("&nbsp;");
		      for(int pricei=0;pricei<arrayPrice.length;pricei++){
		    	if(arrayPrice[pricei]==null ||arrayPrice[pricei].trim().equals("")||arrayPrice[pricei].contains("待定"))
		    		continue;
		        TicketPrice ticketPrice=new TicketPrice();
		    	String remark;
		    	String realPrice="";
		    	if(arrayPrice[pricei].contains("<a href")){
		    		arrayPrice[pricei]=arrayPrice[pricei].replace("<br />", "").replace("元", "");
		    	    ticketPrice.setExist(true);
		    		String ticketUrl=PubFun.getRegex("<a href=\"([^\"]*)[^>]*>([^>]*)</a>", arrayPrice[pricei], 1);
		    	    ticketPrice.setDetailURL(preUrl+ticketUrl);
		    		String price=PubFun.getRegex("<a href=\"([^\"]*)[^>]*>([^>]*)</a>", arrayPrice[pricei], 2);
		    		/*if("vip".equalsIgnoreCase(price.trim()))
		    			continue;*/
		    	    ticketPrice.setMainURL(concentrateUrl);
		    		realPrice=PubFun.getRegex("[^\\d]*([\\d]*)[^\\(]*", price, 1);
		    		if(realPrice==null){
		    			realPrice=price;
		    		}
					remark=PubFun.getRegex("[\\d]*\\(([^\\)]*)\\)", price, 1);
					if(price.trim().contains("vip")||price.trim().contains("VIP")){
		    			Document doc=getDoc(preUrl+ticketUrl);
		    			String realDetailPrice=doc.select("#price0").first().text().trim();
		    			realPrice=realDetailPrice;
		    			remark=price.trim();
		    		}
				    ticketPrice.setPrice(realPrice);
					ticketPrice.setRemark(remark);
			    }
		    	 else{
		    	   String price=arrayPrice[pricei].trim();
		    	   if(null==price||"vip".equalsIgnoreCase(price.trim())||price.contains("待定"))
		    			continue;
		    	   ticketPrice.setExist(false);
		    	   ticketPrice.setMainURL(concentrateUrl);
		    	   realPrice=PubFun.getRegex("[^\\d]*([\\d]*)[^\\(]*", price, 1);
		    		if(realPrice==null)
		    			realPrice=price;
					remark=PubFun.getRegex("[\\d]*\\(([^\\)]*)\\)", price, 1);
					ticketPrice.setPrice(realPrice);
					ticketPrice.setRemark(remark);
				  }
		    	  list.add(ticketPrice);
		    	}
		    }
		    timeAndPrice.put(showTime, list);
		    }
	 	
		    show.setTimeAndPrice(timeAndPrice);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//http://www.228.com.cn/bj/product/detail-43503.html
		//http://www.228.com.cn/bj/product/detail-44685.html
		Yongle1Spider yongle1 = new Yongle1Spider();
		yongle1.setDao(new DataDao());
	    yongle1.extract();
		//yongle1.processLastPage("abc", 1, "http://www.228.com.cn/bj/product/detail-42395.html", "李玉刚·大型歌舞诗剧 四美图", "人民大会堂");
	}

}
