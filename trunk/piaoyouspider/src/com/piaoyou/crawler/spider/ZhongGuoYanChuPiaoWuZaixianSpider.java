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

public class ZhongGuoYanChuPiaoWuZaixianSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(ZhongGuoYanChuPiaoWuZaixianSpider.class);
	private final static String BASE_URL = "http://www.3721piao.com";
	private final static String home_URL = "http://www.3721piao.com";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
		.get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void extract() {
		Map<String,String> map=new HashMap<String,String>();
		map.put("1", "演唱会");
		map.put("2", "音乐会");
		map.put("3", "话剧歌剧");
		map.put("4", "舞蹈芭蕾");
		map.put("5", "戏曲综艺");
		map.put("6", "亲子儿童");
		map.put("7", "梅兰芳大剧院演出");
		map.put("8", "长安大戏院演出");
		map.put("9", "国家大剧院音乐厅");
		map.put("10", "国家大剧院戏剧场");
		map.put("11", "国家大剧院歌剧院");
		map.put("12", "国家大剧院小剧场");
		map.put("13", "电影通票");
		map.put("15", "常年演出");
		Iterator<String> itList=map.keySet().iterator();
		while(itList.hasNext()){
			String key=itList.next();
			String href="http://www.3721piao.com/Perform/a/performList.asp?p="+key;
			String sort=map.get(key);
			int sortType=getSortType(sort);
			Document doc=getDoc(href);
			if(sortType==ShowType.OTHER){
				Iterator<Element> aList=doc.select("a[style]:contains(订 票)").iterator();
				while(aList.hasNext()){
					Element aEle=aList.next();
					String url=aEle.absUrl("href").trim();
					String showName=aEle.parent().previousElementSibling().previousElementSibling().previousElementSibling().previousElementSibling().select("a").first().attr("title").trim();
					sortType=getSortType(sort,showName);
					try{
						processLastPage(url,showName,sortType);
					}catch(Exception ee){
						log.error(Const.concat(url,showName,sortType), ee);
					}
				}
				
			}else{
				Iterator<Element> aList=doc.select("a[style]:contains(订 票)").iterator();
				while(aList.hasNext()){
					Element aEle=aList.next();
					String url=aEle.absUrl("href").trim();
					String showName=aEle.parent().previousElementSibling().previousElementSibling().previousElementSibling().previousElementSibling().select("a").first().attr("title").trim();
					try{
						processLastPage(url,showName,sortType);
					}catch(Exception ee){
						log.error(Const.concat(url,showName,sortType), ee);
					}
				}
				
			}
		}
	}
	
	public void processLastPage(String url,String showName,int sortType ){
		Document doc=getDoc(url);
		String site=doc.select("td[height=20]:contains(场馆)").first().nextElementSibling().select("a").first().text().trim();
		Element imgOne=doc.select("img[src$=zi_01.gif]").get(0);
		String info=PubFun.cleanElement(imgOne.parent().parent().nextElementSibling()).html();
		String pic=doc.select("td[height=221]>img").first().absUrl("src").trim();
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Show show=new Show();
		show.setImage_path(pic);
		show.setIntroduction(info);
		show.setName(showName);
		show.setSiteName(site);
		show.setType(sortType);
		show.setAgent_id(agentID);
		Iterator<Element> itAList=doc.select("a:contains(订 购)").iterator();
		while(itAList.hasNext()){
			Element eleA=itAList.next();
			String showTime=eleA.parent().previousElementSibling().previousElementSibling().previousElementSibling().text().trim();
			try {
				showTime = newDate.format(oldDate.parse(showTime));
			} catch (Exception ee) {
				
			}
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Iterator<Element>  itHasPriceList=eleA.parent().previousElementSibling().previousElementSibling().select("a").iterator();
			while(itHasPriceList.hasNext()){
				TicketPrice ticket=new TicketPrice();
				Element eleHasPrice=itHasPriceList.next();
				String href=eleHasPrice.absUrl("href").trim();
				ticket.setDetailURL(href);
				ticket.setExist(true);
				ticket.setMainURL(url);
				ticket.setPrice(eleHasPrice.text().trim());
				list.add(ticket);
			}
			Element tempList=eleA.parent().previousElementSibling().previousElementSibling();
			for(Element temp:tempList.select("a")){
				temp.remove();
			}
			String[] noPriceList=tempList.text().trim().split("、");
			for(String tempPrice:noPriceList){
				if(tempPrice==null || "".equals(tempPrice.trim())){
					continue;
				}
				TicketPrice ticket=new TicketPrice();
				ticket.setDetailURL(url);
				ticket.setMainURL(url);
				ticket.setExist(false);
				ticket.setPrice(tempPrice);
				list.add(ticket);
			}
			//套票
			Iterator<Element>  itHasTaoPiaoPriceList=eleA.parent().previousElementSibling().select("a").iterator();
			while(itHasTaoPiaoPriceList.hasNext()){
				TicketPrice ticket=new TicketPrice();
				Element eleHasTaoPiaoPrice=itHasTaoPiaoPriceList.next();
				String href=eleHasTaoPiaoPrice.absUrl("href").trim();
				ticket.setDetailURL(href);
				ticket.setExist(true);
				ticket.setMainURL(url);
				ticket.setRemark("套票");
				ticket.setPrice(eleHasTaoPiaoPrice.text().trim());
				list.add(ticket);
			}
			Element tempTaoPiaoList=eleA.parent().previousElementSibling();
			for(Element temp:tempTaoPiaoList.select("a")){
				temp.remove();
			}
			String[] noTaoPiaoPriceList=tempTaoPiaoList.text().trim().split("、");
			for(String tempPrice:noTaoPiaoPriceList){
				if(tempPrice==null || "".equals(tempPrice.trim())){
					continue;
				}
				TicketPrice ticket=new TicketPrice();
				ticket.setDetailURL(url);
				ticket.setMainURL(url);
				ticket.setExist(false);
				ticket.setPrice(tempPrice);
				ticket.setRemark("套票");
				list.add(ticket);
			}
			map.put(showTime, list);
		}
		show.setTimeAndPrice(map);
		try{
			this.getDao().saveShow(show);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ZhongGuoYanChuPiaoWuZaixianSpider spider=new ZhongGuoYanChuPiaoWuZaixianSpider();
		spider.setDao(new DataDao());
		spider.extract();
	}
	public int getSortType(String sort) {
		int sortType = ShowType.OTHER;
		if (sort.contains("国家大剧院戏剧场") || sort.contains("国家大剧院音乐厅")
				|| sort.contains("国家大剧院歌剧院") || sort.contains("国家大剧院戏剧场")
				|| sort.contains("国家大剧院小剧场") || sort.contains("梅兰芳大剧院")
				|| sort.contains("长安大戏院") || sort.contains("北京9剧场")
				|| sort.contains("北京音乐厅") || sort.contains("北大百年讲堂演出")) {
			return sortType;
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

	public int getSortType(String sort, String name) {
		int sortType = ShowType.OTHER;
		if (sort.contains("国家大剧院戏剧场")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("国家大剧院音乐厅")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("国家大剧院歌剧院")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("国家大剧院戏剧场")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("国家大剧院小剧场")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("梅兰芳大剧院")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("长安大戏院")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("北京9剧场")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("北京音乐厅")) {
			sortType = getChildSortType(name);
		} else if (sort.contains("北大百年讲堂演出")) {
			sortType = getChildSortType(name);
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
}
