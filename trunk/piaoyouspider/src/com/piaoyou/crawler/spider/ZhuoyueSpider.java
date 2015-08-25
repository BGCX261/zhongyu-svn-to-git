package com.piaoyou.crawler.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class ZhuoyueSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(ZhuoyueSpider.class);
	private static String rootUrl="http://www.joyopiao.com/p/";
	public static String encode="utf-8";
	@Override
	public void extract() {
	   Document doc=getDoc(rootUrl);
	   Iterator<Element> divlist=doc.select("div#container div#content").iterator();//.not("div#top,div[class~=],div[style]")7
	   while(divlist.hasNext()){
		   Element eleBySort=divlist.next();
		   String sort=eleBySort.child(0).child(0).text();//
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
		   else if(sort.contains("京剧")||sort.contains("戏剧")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
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
		    Iterator<Element> listUrl=eleBySort.select("div#info_l div.l_line").iterator();
		    while(listUrl.hasNext()){
			   Element div=listUrl.next();
			   String  absUrl=div.select("div[class*=l_def] a").first().absUrl("href");
			   String  site=div.select("div[class~=(?i)l_def[\\w]*].l_04").first().text();//l_def_footer l_04//.l_def
			   if(absUrl==null){
				   continue;
			   }
			   try{
			     procesLastUrl(absUrl,sortType,site);
			   }catch(Exception ee){
				   log.error(Const.concat(absUrl,sortType,site),ee);
			   }
		    }  
	   }
	 }
    
	public void procesLastUrl(String lastUrl,int sorttype,String showsite){
		Document doc=getDoc(lastUrl);
		if(null==showsite||"".equals(showsite)||showsite.contains("待定"))
			return;
		Show show=new Show();
		show.setAgent_id("2");
		String showName=doc.select("div#container div.center_cen div.menu_center div.menu_center_1 h1").first().text().trim();
		String imagePath=doc.select("div#container div.center_cen div.menu_left div.menu_hot img").first().absUrl("src").trim();
		String instruction=PubFun.cleanElement(doc.select("div#container div.center_cen div.menu_center div.menu_center_5 ul li").first()).text().trim();
		showsite=doc.select("div#container div.center_cen div.menu_center div.menu_center_1 ul li:eq(1)").first().text().trim();
		show.setType(sorttype);
		show.setSiteName(showsite);
		show.setName(showName);
		show.setImage_path(imagePath);
		show.setIntroduction(instruction);
		Map<String,List<TicketPrice>> map=GetMap(doc,lastUrl);
		show.setTimeAndPrice(map);
		try{
		    this.getDao().saveShow(show);
		}
		catch(Exception ee){
			
		}
	}
	public Map<String,List<TicketPrice>> GetMap(Document doc,String url){
		Map<String,List<TicketPrice>> timeAndPrice=new HashMap<String,List<TicketPrice>>();
		Iterator<Element> trList=doc.select("div#container div.center_cen div.menu_center div.menu_center_3").first().select("table tr").not("tr:eq(0)").iterator(); 
		while(trList.hasNext()){
			Element trEle=trList.next();
			String showTime=trEle.child(0).text().trim();//演出时间
			if(showTime==null)
				continue;
			if(showTime.contains("待定")){
				continue;
			}
			String newshowTime=PubFun.getRegex("([^\\$]*)星期", showTime, 1);
			if(newshowTime!=null){
				showTime=PubFun.parseDateToStr(PubFun.parseStringToDate(newshowTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
				//showTime=newshowTime.trim().substring(0,15);
			}
			if(showTime.contains("全年")||showTime.contains("每")||showTime.contains("年")||showTime.contains("雪季")){
				
				List<TicketPrice> priceList=GetPriceListByTime(trEle,url);
				timeAndPrice.put(showTime, priceList);
			}
			else if(PubFun.isMatchRegex(showTime, "[^\\d]*[\\d]+\\.[\\d]+\\.[\\d]+-[\\d]+\\.[\\d]+[^\\d]*")){
				/*String startDate=PubFun.getRegex("[^\\d]*([\\d]+\\.[\\d]+\\.[\\d]+)-([\\d]+\\.[\\d]+)[^\\d]*", showTime, 1).replace(".", "-");
				String endDate=PubFun.getCurrentYear()+"-"+PubFun.getRegex("[^\\d]*([\\d]+\\.[\\d]+\\.[\\d]+)-([\\d]+\\.[\\d]+)[^\\d]*", showTime, 2).replace(".", "-");
				List<String> list=PubFun.getTwoDateAllDate(startDate, endDate, "", "yyyy-MM-dd");
				for(String date:list){
					List<TicketPrice> priceList=GetPriceListByTime(trEle,url);
					timeAndPrice.put(date, priceList);
				}*/
				List<TicketPrice> priceList=GetPriceListByTime(trEle,url);
				timeAndPrice.put(showTime, priceList);
			}
			else{
				List<TicketPrice> priceList=GetPriceListByTime(trEle,url);
				timeAndPrice.put(showTime, priceList);
			}
		}
		return timeAndPrice;
		
	}
	public static void main(String[] args) {
	  
		 ZhuoyueSpider Spider = new ZhuoyueSpider();
		 Spider.setDao(new DataDao());
		 Spider.extract();
	}
	public  List<TicketPrice> GetPriceListByTime(Element trEle ,String url){
		List<TicketPrice> priceList=new ArrayList<TicketPrice>();
		Iterator<Element>  alist=trEle.child(1).getElementsByTag("a").iterator();
		while(alist.hasNext()){
			Element ele=alist.next();
			TicketPrice ticket=new TicketPrice();
			String realPrice=null;
			String remark=null;
			if(ele.attr("style").equals("")){
				ticket.setExist(true);
				ticket.setMainURL(url);
				String shopUrl=ele.absUrl("href");
				ticket.setDetailURL(shopUrl);
				String ticketPriceInfo=ele.text();
				realPrice=PubFun.getRegex("[^\\d]*([\\d]*)[^\\(]*", ticketPriceInfo, 1);
	    		if(realPrice==null)
	    			realPrice=ticketPriceInfo;
    			remark=PubFun.getRegex("[\\d]*\\(([^\\)]*)\\)", ticketPriceInfo, 1);
    			ticket.setPrice(realPrice);
    			ticket.setRemark(remark);
			}
			else{
				ticket.setExist(false);
				ticket.setMainURL(url);
				String shopUrl=ele.absUrl("href");
				ticket.setDetailURL(shopUrl);
				String ticketPriceInfo=ele.text();
				realPrice=PubFun.getRegex("[^\\d]*([\\d]*)[^\\(]*", ticketPriceInfo, 1);
	    		if(realPrice==null)
	    			realPrice=ticketPriceInfo;
    			remark=PubFun.getRegex("[\\d]*\\(([^\\)]*)\\)", ticketPriceInfo, 1);
    			ticket.setPrice(realPrice);
    			ticket.setRemark(remark);
			}
			priceList.add(ticket);
		}
		return priceList;
	}

}
