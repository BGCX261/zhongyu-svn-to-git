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

public class SuoyouTicketSpider extends TicketSpider implements SpiderTask {
	public static int i=0;
	private static final Log log = LogFactory.getLog(SuoyouTicketSpider.class);
	private final static String BASE_URL = "http://www.alltickets.com.cn";
	private final static String home_URL = "http://www.alltickets.com.cn//Productlist_1.aspx?ClassId=4";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void extract() {
		Document doc=getDoc("http://www.alltickets.com.cn//Productlist_1.aspx?ClassId=4");
		Element temp=doc.select("a:contains(文艺演出)").first().parent().parent().parent().nextElementSibling();
		Iterator<Element> it=temp.select("a.classname").iterator();
		while(it.hasNext()){
			Element aEle=it.next();
			String url=aEle.absUrl("href").trim();
			String sort=aEle.text().trim();
			int sortType=getSortType(sort);
			processSortPage(url,sortType);
		}
	}
	/**
	 * 处理分页的算法
	 * @param url
	 * @param sortType
	 */
	
	public void processSortPage(String url,int sortType){
		Document doc = getDoc(url);
		Iterator<Element> itPicList = doc.select("img[src=Skin/Skin1/images/buy.gif]").iterator();
		while (itPicList.hasNext()) {
			Element elet = itPicList.next().parent();
			String lastUrl = elet.absUrl("href").trim();
			try {
				processLastPage(lastUrl, sortType);
			} catch (Exception ee) {
				log.error(Const.concat(lastUrl, sortType), ee);
			}
		}
		Element pa = doc.select("a:contains(尾页)").first();
		if (pa == null) {
			return;
		}
		Element t = pa.previousElementSibling();
		if (t.nodeName().equalsIgnoreCase("a") && t.text().contains("下一页")) {
			String newUrl = t.absUrl("href").trim();
			newUrl = newUrl.replace("http://", "http://www.alltickets.com.cn//");
			processSortPage(newUrl, sortType);
		} else {
			return;
		}
	}
	/***
	 * 处理最终页面
	 * @param url
	 * @param sortType
	 */
	public void processLastPage(String url,int sortType){
		Document doc=getDoc(url);
		Element temp=doc.select("table[width=210].kuang_4_cu img").first();
		String pic=temp.absUrl("src").trim();
		String showName=doc.select("a:has(img[src$=fav.gif])").first().parent().previousElementSibling().text().trim();
		String site=temp.parent().parent().parent().parent().nextElementSibling().select("tr:eq(1)").first().text().replace("场馆", "").replace("：", "").replace(":", "");
		Element orderDistinct=doc.select("table.kuang_4_xi:contains(订票区)").first();
		String info=orderDistinct.nextElementSibling().nextElementSibling().html().trim();
		Map<String,List<TicketPrice>> map=getMap(orderDistinct.nextElementSibling(),url);
		Show show=new Show();
		show.setAgent_id(agentID);
		show.setImage_path(pic);
		show.setName(showName);
		show.setSiteName(site);
		show.setIntroduction(info);
		show.setType(sortType);
		show.setTimeAndPrice(map);
		try{
			this.getDao().saveShow(show);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	/**
	 * 获取一下票价
	 * @return
	 */
	public Map<String,List<TicketPrice>> getMap(Element ele,String url){
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Iterator<Element> itList=ele.select("table#tabObject tr[id^=objTr_]").iterator();
		while(itList.hasNext()){
			Element eleIt=itList.next();
			String showTime=eleIt.select("td:eq(0)").first().text();
			try{
				showTime = newDate.format(oldDate.parse(showTime));
			}catch(Exception ee){
				ee.printStackTrace();
			}
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Iterator<Element> itHasPriceList=eleIt.select("td:eq(1) a").iterator();
			while(itHasPriceList.hasNext()){
				Element eleHasPrice=itHasPriceList.next();
				String href=eleHasPrice.absUrl("href").trim();
				String priceConetent=eleHasPrice.text().trim();
				TicketPrice ticket=new TicketPrice();
				ticket.setDetailURL(href);
				ticket.setExist(true);
				ticket.setMainURL(url);
				this.setPriceAndRemark(ticket, priceConetent);
				list.add(ticket);
			}
			Iterator<Element> itNoPriceList=eleIt.select("td:eq(1) font").iterator();
			while(itNoPriceList.hasNext()){
				Element eleNoPrice=itNoPriceList.next();
				String priceConetent=eleNoPrice.text().trim();
				TicketPrice ticket=new TicketPrice();
				ticket.setDetailURL(url);
				ticket.setExist(false);
				ticket.setMainURL(url);
				this.setPriceAndRemark(ticket, priceConetent);
				list.add(ticket);
			}
			map.put(showTime, list);
		}
		return map;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SuoyouTicketSpider spider=new SuoyouTicketSpider();
		//spider.setDao(new DataDao());
		//spider.extract();
		spider.processLastPage("http://www.alltickets.com.cn//ShowProduct.aspx?YX_MID=7248", 1);
	}
	/**
	 * 获取演出分类类型
	 * @param sort
	 * @return
	 */
	public int getSortType(String sort) {
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
