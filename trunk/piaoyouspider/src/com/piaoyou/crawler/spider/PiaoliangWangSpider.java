package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
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

public class PiaoliangWangSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(Piao321Spider.class);
	private final static String BASE_URL = "http://www.ticketchina.net/";
	private final static String home_URL = "http://show.ticketchina.net/show/list.php";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy年MM月dd日 hh:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void extract() {
		Document doc=getDoc(home_URL);
		Iterator<Element> it=doc.select(".wapper_list .show_concert").iterator();
		while(it.hasNext()){
			Element eleIt=it.next();
			String sort=eleIt.select("h4 img").first().attr("alt");
			int sortType=getSortType(sort);
			Iterator<Element> trList=eleIt.select(".sq_show_table tr").iterator();
			while(trList.hasNext()){
				Element tr=trList.next();
				String lastUrl=tr.select("td.w1 a").first().absUrl("href").trim();
				String showName=tr.select("td.w1 a").first().text().trim();
				String site=tr.select("td.w3").first().text();
				try{
					processLastPage(sortType,lastUrl,site,showName);
				}catch(Exception ee){
					 log.error(Const.concat(sortType,lastUrl,site,showName),ee);
				}
			}
		}
	}
	/**
	 * @param args
	 */
	
	public void processLastPage(int sortType,String url,String site,String showName){
		Document doc=getDoc(url);
		String imagePath=doc.select("div.ticket_detail>a>img").first().absUrl("src").trim();
		String info=PubFun.cleanElement(doc.select("div.s_i_text").first()).html().trim();
		Show show=new Show();
	    show.setAgent_id(agentID);
		show.setImage_path(imagePath);
		show.setIntroduction(info);
		show.setName(showName);
		show.setSiteName(site);
		show.setType(sortType);
		Map<String,List<TicketPrice>> map=getMap(doc,url);
		show.setTimeAndPrice(map);
		try{
         	getDao().saveShow(show);
        }catch(Exception ee){
        	
        }
	}
	public static void main(String[] args) {
		PiaoliangWangSpider spider=new PiaoliangWangSpider();
		spider.setDao(new DataDao());
		//spider.extract();
		//spider.processLastPage(1, "http://show.ticketchina.net/show/activity_detail.php?city_id=1&activity_id=1109", "工人体育馆", "afasfaf");
	}
	
	public int getSortType(String sort){
		int sortType = ShowType.OTHER;
		if (sort.contains("演唱")) {
			sortType = ShowType.CONCERT;
		} else if (sort.contains("音乐") && !sort.contains("剧")) {
			sortType = ShowType.SHOW;
		} else if (sort.contains("话剧") || sort.contains("歌剧")
				|| sort.contains("音乐剧")) {
			sortType = ShowType.DRAMA;
		} else if (sort.contains("芭蕾") || sort.contains("舞蹈")
				|| sort.contains("舞")) {
			sortType = ShowType.DANCE;
		} else if (sort.contains("戏剧") || sort.contains("戏曲")
				|| sort.contains("二人转") || sort.contains("魔术")
				|| sort.contains("杂技") || sort.contains("相声")
				|| sort.contains("小品") || sort.contains("马戏")) {
			sortType = ShowType.OPERA;
		} else if (sort.contains("儿童亲子")) {
			sortType = ShowType.CHILDREN;
		} else if (sort.contains("体育")) {
			sortType = ShowType.SPORTS;
		} else if (sort.contains("旅游") || sort.contains("电影")) {
			sortType = ShowType.HOLIDAY;
		}
		return sortType;
	}
	
	public Map<String,List<TicketPrice>> getMap(Document doc,String url){
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Iterator<Element> it=doc.select("ul.show_date_all li.onsale").iterator();
		while(it.hasNext()){
			Element eleLi=it.next();
			String showId=eleLi.select("li").attr("show").trim();
			String showTime=eleLi.select("li").text().trim();
			showTime=PubFun.getCurrentYear()+"年"+showTime;
			showTime=showTime.replaceAll("\\(([^\\)]*)\\)", "");
			try{
				showTime = newDate.format(oldDate.parse(showTime));
			}catch(Exception ee){
				
			}
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Iterator<Element> elePriceList=doc.select("#price_list_"+showId +" li[show]").iterator();
			while(elePriceList.hasNext()){
				Element elePrice=elePriceList.next();
				String onclick=elePrice.attr("onclick").trim();
				TicketPrice ticket=new TicketPrice();
				ticket.setMainURL(url);
				ticket.setDetailURL(url);
				if(onclick!=null && !"".equals(onclick)){
					ticket.setExist(true);
				}else{
					ticket.setExist(false);
				}
				String price=elePrice.text().trim().replace("元", "");
				if(price.matches("[^\\d]*[\\d]+\\([^\\)]*\\)[^\\d]*")){
					price=PubFun.getRegex("[^\\d]*([\\d]+)\\([^\\)]*\\)[^\\d]*", price, 1);
				}
				ticket.setPrice(price);
				list.add(ticket);
			}
			map.put(showTime, list);
		}
		return map;
	}
}
