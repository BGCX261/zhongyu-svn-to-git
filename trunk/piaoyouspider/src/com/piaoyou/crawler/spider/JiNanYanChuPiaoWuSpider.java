package com.piaoyou.crawler.spider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

public class JiNanYanChuPiaoWuSpider extends TicketSpider implements SpiderTask {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final Log log = LogFactory.getLog(JiNanYanChuPiaoWuSpider.class);
	private final static String BASE_URL = "http://www.jinanpiao.com/";
	private final static String home_URL = "http://www.jinanpiao.com/ticketList.aspx";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void extract() {
		Document doc=getDoc(home_URL);
	    Element temp=doc.select("div.menu_a li.li_2 a:contains(演 出)").first();
	    Element tType=temp.nextElementSibling();
	    Iterator<Element> it=tType.select("li").iterator();
	    while(it.hasNext()){
	    	Element eleIt=it.next();
	    	String sort=eleIt.text().trim();
	    	System.out.println(sort);
	    	int sortType=getSortType(sort);
	    	String url = eleIt.select("a").first().absUrl("href").trim();
	    	try{
	    		procesSortPage(url,sortType);
	    	}catch(Exception ee){
	    	    log.error(Const.concat(url,sortType),ee);
	    	}
	    }
	}

	public void procesSortPage(String url,int sortType){
		Document doc=getDoc(url);
		Iterator<Element> itShowList=doc.select("div.neirong_0>div.neirong_2.adjust div.neirong_img a").iterator();
		while(itShowList.hasNext()){
			Element it=itShowList.next();
			String lastPageUrl=it.absUrl("href").trim();
			processLastPage(lastPageUrl,sortType);
		}
		Element t=doc.select("#AspNetPager1 td a[href]:contains(下一页)").first();
		if(t!=null){
			String nextPageUrl=t.absUrl("href").trim();
			procesSortPage(nextPageUrl,sortType);
		}else{
			return ;
		}
	}
	/**
	 * 处理最终页面
	 * @param url
	 * @param sortType
	 */
	public void processLastPage(String url,int sortType){
		System.out.println(url);
		Document doc=getDoc(url);
		String imagePath=doc.select("div.deta_tu>img").first().absUrl("src").trim();
		String showName=doc.select("div.deta_wenzi>h2").first().text().trim();
		Element eleSite=doc.select("div.deta_wenzi>ul>li:contains(演出地点)").first();
		/*if(eleSite==null)
			return;*/
		String site=eleSite.text().trim().replace("：", "").replace(":", "").replaceAll("\\s", "").replace("演出地点", "");
		String info=PubFun.cleanElement(doc.select("div.way_xinxi").last()).html();
		Show show=new Show();
		show.setAgent_id(agentID);
		show.setImage_path(imagePath);
		show.setIntroduction(info);
		show.setName(showName);
		show.setSiteName(site);
		show.setType(sortType);
		Map<String,List<TicketPrice>> map=getMap(doc,url);
		if(map==null||map.size()==0){
			return ;
		}
		show.setTimeAndPrice(map);
		try{
			//System.out.println(show);
			this.getDao().saveShow(show);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	
	/**
	 * @throws ParseException 
	 * 
	 * 
	 */
	public Map<String,List<TicketPrice>> getMap(Document doc,String url) {
		Element ele=doc.select("div.way_xinxi").get(1);
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Iterator<Element> itNext=ele.select("table>tr:gt(0)").iterator();
		while(itNext.hasNext()){
			Element eleNext=itNext.next();
			String showTime=eleNext.select("td:eq(2)").first().text().trim();
			try {
				if(new Date().after(oldDate.parse(showTime))){
					continue;
				}
				showTime = newDate.format(oldDate.parse(showTime));
			} catch (Exception ee) {

			}
			Iterator<Element> noPriceList=eleNext.select("td>span.red_1>span").iterator();
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			while(noPriceList.hasNext()){
				Element eleNoPrice=noPriceList.next();
				if(!eleNoPrice.text().contains("元")){
					continue;
				}
				TicketPrice ticket=new TicketPrice();
				ticket.setDetailURL(url);
				ticket.setMainURL(url);
				ticket.setPrice(eleNoPrice.text().replace("元", ""));
				ticket.setExist(false);
				list.add(ticket);
			}
			Iterator<Element> yesPriceList=eleNext.select("td>span.red_1>a").iterator();
			while(yesPriceList.hasNext()){
				Element eleYesPrice=yesPriceList.next();
			/*	String onClick=eleYesPrice.attr("onclick").trim();
				onClick=PubFun.getRegex("[^']*'([^']+)'[^']*", onClick, 1);
				onClick=onClick.replace("&amp;", "&");
				String detailUrl="http://www.jinanpiao.com"+eleYesPrice.select("#form1").first().attr("action")
				String detailUrl="http://www.jinanpiao.com"+eleYesPrice.select("#form1").first().attr("action")*/
				TicketPrice ticket=new TicketPrice();
				ticket.setDetailURL(url);
				ticket.setMainURL(url);
				ticket.setPrice(eleYesPrice.text().replace("元", ""));
				ticket.setExist(true);
				list.add(ticket);
			}
			map.put(showTime, list);
		}
		return map;
	}
	
	/**
	 * @param ar
	 */
	public static void main(String[] args) {
		JiNanYanChuPiaoWuSpider spider=new JiNanYanChuPiaoWuSpider();
		spider.setDao(new DataDao());
		spider.extract();
		//spider.processLastPage("http://www.jinanpiao.com/ticket_1074.htm", 2);
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

}
