package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

public class YiSouPiaoWuSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(YiSouPiaoWuSpider.class);
	private final static String BASE_URL = "http://www.1sopiao.com";
	private final static String home_URL = "http://www.1sopiao.com/ycxx.asp";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
		.get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void extract() {
		Document doc=getDoc(home_URL);
		Iterator<Element> itList=doc.select("#list>div.c1").iterator();
		while(itList.hasNext()){
			Element eleDiv=itList.next();
			String sort=eleDiv.text().trim();
			Element eleSort=null;
			eleSort=eleDiv.nextElementSibling().nextElementSibling();
			if(eleSort==null ||!eleSort.hasClass("c3")){
				continue;
			}
			String title=eleSort.select("li.lia01 a").first().text().trim();
			int sortType=this.getSortType(sort,title);
			String url=eleSort.select("li.lia01 a").first().absUrl("href").trim();
			try{
				processLastPage(url,sortType);
			}catch(Exception ee){
				log.error(Const.concat(url,sortType),ee);
			}
			while(eleSort.nextElementSibling()!=null && eleSort.nextElementSibling().hasClass("c3")){
				eleSort=eleSort.nextElementSibling();
				String secondTitle=eleSort.select("li.lia01 a").first().text().trim();
				String secondUrl=eleSort.select("li.lia01 a").first().absUrl("href").trim();
				int secondSortType=this.getSortType(sort,secondTitle);
				try{
					processLastPage(secondUrl,secondSortType);
				}catch(Exception ee){
					log.error(Const.concat(url,sortType),ee);
				}
			}
		}

	}
	
	public void processLastPage(String url,int sortType){
		Document doc=getDoc(url);
		String pic=doc.select(".liB21-1 img").first().absUrl("src").trim();
		String showName=doc.select("ul.liB21-2>li.liB212-1").first().text().trim();
		String showSite=doc.select("ul.liB21-2>li.liB212-2>a").first().text().trim();
		String info=PubFun.cleanElement(doc.select("div.liB-2 div.liB2-3").first()).html();
		String showOnlyTime=doc.select("ul.liB21-2>li.liB212-2:eq(2)").first().ownText();
		Iterator<Element> itUlList=doc.select("div.liB-42 ul:gt(0)").iterator();
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		while(itUlList.hasNext()){
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Element ul=itUlList.next();
			String showDate=ul.select("li.liB6-2").first().text().trim();
			String realShowTime=showDate+" "+showOnlyTime;
			try {
				realShowTime = newDate.format(oldDate.parse(realShowTime));
			} catch (Exception ee) {
				ee.printStackTrace();
			}
			Iterator<Element> itHasPriceList=ul.select("li.liB6-4.liB6-4a a").iterator();
			while(itHasPriceList.hasNext()){
				Element elePrice=itHasPriceList.next();
				String href=elePrice.select("a").first().absUrl("href").trim();
				String content=elePrice.text().trim();
				if(content==null || "".equals(content)){
					continue;
				}
				TicketPrice ticket=new TicketPrice();
				ticket.setMainURL(url);
				ticket.setExist(true);
				ticket.setDetailURL(href);
				this.setPriceAndRemark(ticket, content);
				list.add(ticket);
			}
			Iterator<Element> itNoPriceList=ul.select("li.liB6-4.liB6-4a font").iterator();
			while(itNoPriceList.hasNext()){
				Element elePrice=itNoPriceList.next();
				String content=elePrice.text().trim();
				TicketPrice ticket=new TicketPrice();
				ticket.setMainURL(url);
				ticket.setExist(false);
				ticket.setDetailURL(url);
				this.setPriceAndRemark(ticket, content);
				list.add(ticket);
			}
			map.put(realShowTime, list);
		}
		Show show=new Show();
		show.setAgent_id(agentID);
		show.setImage_path(pic);
		show.setIntroduction(info);
		show.setName(showName);
		show.setSiteName(showSite);
		show.setTimeAndPrice(map);
		show.setType(sortType);
		try{
			this.getDao().saveShow(show);
		}catch(Exception ee){
			
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		YiSouPiaoWuSpider spider=new YiSouPiaoWuSpider();
		spider.setDao(new DataDao());
		spider.extract();
	}
	
	public int getSortType(String sort,String showName) {
			int sortType = ShowType.OTHER;
			if(sort.contains("国家大剧院")){
				sortType=getChildSortType(showName);
			}
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

	public int getChildSortType(String name) {
		int sortType = ShowType.OTHER;
		if (name.contains("音乐会")) {
			sortType = ShowType.SHOW;
		}
		if (name.contains("芭蕾") || name.contains("舞")) {
			sortType = ShowType.DANCE;
		}
		if (name.contains("京剧") || name.contains("戏")) {
			sortType = ShowType.OPERA;
		} else if (name.contains("演唱会")) {
			sortType = ShowType.CONCERT;
		} else if (name.contains("歌剧") || name.contains("话剧")
				|| name.contains("剧")) {
			sortType = ShowType.DRAMA;
		}
		return sortType;
	}

	private void setPriceAndRemark(TicketPrice ticketPrice, String content) {
		if (content.matches("\\d+")) {
			ticketPrice.setPrice(content);
			ticketPrice.setRemark("");
		} else if (content.matches("(\\D+)(\\d+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(1));
		} else if (content.matches("(\\d+)(\\D+)")) {
			Pattern p = Pattern.compile("(\\d+)(\\D+)");
			Matcher m = p.matcher(content.replaceAll("）|（|\\(|\\)", ""));
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2));
		} else if (content.matches("(\\D+)(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\D+)(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(2));
			ticketPrice.setRemark(m.group(3).replaceAll("）|（|\\(|\\)", ""));
		} else if (content.matches("(\\d+)(.+)")) {
			Pattern p = Pattern.compile("(\\d+)(.+)");
			Matcher m = p.matcher(content);
			m.matches();
			ticketPrice.setPrice(m.group(1));
			ticketPrice.setRemark(m.group(2).replaceAll("）|（|\\(|\\)", ""));
		}
	}


}
