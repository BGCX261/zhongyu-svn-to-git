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
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public  class ZongyipiaowSpider extends TicketSpider implements SpiderTask {
    private static Log log=LogFactory.getLog(ZongyipiaowSpider.class);
	private final static String BASE_URL = "http://www.zypww.com.cn";
	private final static String MAIN_URL = "http://www.zypww.com.cn/all_ticket.html";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
    
	@Override
	public void extract() {
		Document doc=getDoc(MAIN_URL);
        Iterator<Element> divList=doc.select("div[id^=all]").iterator();
        while(divList.hasNext()){
        	Element divEle=divList.next();
        	String sort=divEle.select("dl[class=alldlt]").first().text().trim();
        	int sortType=ShowType.OTHER;
			if(sort.contains("演唱")){
				    sortType=ShowType.CONCERT;
			}
			else if(sort.contains("音乐")&&!sort.contains("剧")){
					sortType=ShowType.SHOW;
		    }
			else if(sort.contains("话剧") || sort.contains("歌剧")||sort.contains("音乐剧")){
					sortType=ShowType.DRAMA;
			}
			else if(sort.contains("芭蕾") || sort.contains("舞蹈")||sort.contains("舞")){
					sortType=ShowType.DANCE;
			}
		    else if(sort.contains("戏剧")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
				    sortType=ShowType.OPERA;
			}
			else if(sort.contains("儿童亲子")){
				 sortType=ShowType.CHILDREN;
			}
			else if(sort.contains("体育") ){
				 sortType=ShowType.SPORTS;
			}
			else if(sort.contains("旅游")|| sort.contains("电影")){
				 sortType=ShowType.HOLIDAY;
			}
			Iterator<Element> aList=divEle.select("dl[class=alldlp]").iterator();
			while(aList.hasNext()){
				Element detail = aList.next();
				String href=detail.select("a").first().absUrl("href").trim();
				String site = detail.select("dd[class=alldlsv]").text().trim();
				String time = detail.select("dd[class=alldlst]").text().trim();
				String show_name = detail.select("dd[class=alldlsn]").text().trim();
				try{
				   processLastPage(href,sortType,show_name,site,time);
				}catch(Exception ee){
					log.error(Const.concat(href,sortType),ee);
				}
			}
        }
	}
	
	public void processLastPage(String url,int sort,String showName,String showSite,String time){
		Document doc=getDoc(url);
		String imagePath=doc.select("div[id=cmain1]").first().children().get(0).absUrl("src").trim();
		Element eleUL=doc.select("div[id=contentl]").first();
		Map<String,List<TicketPrice>> map=getMap(doc,url);
		String summary=PubFun.cleanElement(eleUL).html();
		Show show=new Show();
		show.setAgent_id(agentID);
		show.setImage_path(imagePath);
		show.setIntroduction(summary);
		show.setName(showName);
		show.setSiteName(showSite);
		show.setTimeAndPrice(map);
		show.setType(sort);
		try{
			getDao().saveShow(show);
		}catch(Exception ee){
			
		}
	}
	
	public Map<String,List<TicketPrice>> getMap(Document doc,String url){
		Map<String, List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		int size=doc.select("dd[class=date]").size();
		if(size==0)
			return map;
		List<Element> timeList=doc.select("dd[class=date]");
		List<Element> priceList=doc.select("dd[class=prices]");
		for(int i=1;i<size;i++){
			Element timeEle=timeList.get(i);
			Element priceEle=priceList.get(i);
			if(null==timeEle || "".equals(timeEle.text().trim())||timeEle.text().contains("待定"))
				continue;
			String showTime=timeEle.text().trim();//10月22日 星期六 19:30
			showTime=showTime.replaceAll("\\[.*?\\]", " ");
			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
			if(newshowTime!=null)
			   showTime=newshowTime;
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Iterator<Element> aEleList=priceEle.select("a").iterator();
			while(aEleList.hasNext()){
				Element eleA=aEleList.next();
				TicketPrice ticket=new TicketPrice();
				String price=eleA.text().trim();
				String remark=null;
//				if(price.matches("\\d")){
//					String newPrice=price;
//					price=PubFun.getRegex("([^\\(]+)(\\([^\\)]X\\))", newPrice, 1);
//					remark=PubFun.getRegex("([^\\(]+)(\\([^\\)]X\\))", newPrice, 2);
//				}
				if(price.indexOf("(")!=-1){
					remark = price.substring(price.indexOf("(")+1,price.indexOf(")"));
					price = price.substring(0,price.indexOf("("));
				}
				
				ticket.setPrice(price);
				ticket.setRemark(remark);
				ticket.setMainURL(url);
				ticket.setExist(true);
				ticket.setDetailURL(url);
				list.add(ticket);
			}
			aEleList=priceEle.select("span[class=nop]").iterator();
			while(aEleList.hasNext()){
				Element eleA=aEleList.next();
				TicketPrice ticket=new TicketPrice();
				String price=eleA.text().trim();
				String remark=null;
				if(price.indexOf("(")!=-1){
					remark = price.substring(price.indexOf("(")+1,price.indexOf(")"));
					price = price.substring(0,price.indexOf("("));
				}
				ticket.setPrice(price);
				ticket.setRemark(remark);
				ticket.setMainURL(url);
				ticket.setExist(false);
				list.add(ticket);
			}
			map.put(showTime, list);
		}
		return map;
	}
	public static void main(String[] args) {
		ZongyipiaowSpider spider=new ZongyipiaowSpider();
		spider.setDao(new DataDao());
		spider.extract();
	}
}
