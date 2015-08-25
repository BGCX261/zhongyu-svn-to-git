package com.piaoyou.crawler.spider;

import java.sql.SQLException;
import java.text.ParseException;
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
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.PubFun;

public class CultureShSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(CultureShSpider.class);
	private final static String HOME_PAGE = "http://www.culture.sh.cn/alltickets.htm";
	private final static String BASE_URL = "http://www.culture.sh.cn/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL).get(0).get("agency_id").toString();

	@Override
	public void extract() {
		
		Document start = getDoc(HOME_PAGE);
		Elements every = start.select("a[title=点击查看此票券详细信息]");

		for (Element element : every) {
			String url = element.attr("abs:href");
			try {
				extractEach(url);
			} catch (Exception e) {
				log.error(url,e);
				continue;
			}
		}
	}

	private void extractEach(String url) {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setAgent_id(agentID);
		show.setName(ticket.select("font.Title").get(0).text());//演出标题
		show.setSiteName(ticket.select("a[href^=venue.asp?venueid=]").get(0).text());//演出场馆
		Elements introduction = ticket.select("#ticketinfo");
		for (Element clear : introduction.select("font,script,noscript,img,a,embed")) {
			clear.remove();
		}
		show.setIntroduction(PubFun.cleanElement(introduction).html());//演出简介
		Element element4img = ticket.select("img[src^=images/upfile/").first();
		if(element4img != null) {
			show.setImage_path(element4img.attr("abs:src"));//演出图片
		} else {
			show.setImage_path(null);
		}
		for(Element element:ticket.select("a[href^=class.asp?aid=]")) {//演出类型
			String showType = element.text(); 
			if("[演唱会]".equals(showType)) {
				show.setType(ShowType.CONCERT);
			} else if("[音乐会]".equals(showType)) {
				show.setType(ShowType.SHOW);
			}  else if("[歌舞芭蕾]".equals(showType)) {
				show.setType(ShowType.DANCE);
			}  else if("[喜剧歌曲]".equals(showType)) {
				show.setType(ShowType.DRAMA);
			}  else if("[杂技魔术]".equals(showType)) {
				show.setType(ShowType.OPERA);
			}  else if("[戏曲曲艺]".equals(showType)) {
				show.setType(ShowType.OPERA);
			}  else if("[少儿剧场]".equals(showType)) {
				show.setType(ShowType.CHILDREN);
			}  else if("[体育竞技]".equals(showType)) {
				show.setType(ShowType.SPORTS);
			} else {
				show.setType(ShowType.OTHER);
			}
		}
		
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();//具体的时间与票价
		Iterator<Element> times=ticket.select("#acc li").iterator();
		while(times.hasNext()) {
			Element timeEle = times.next();
			if(!timeEle.select("a").isEmpty()) {
				List<TicketPrice> list=new ArrayList<TicketPrice>();
				String  time = timeEle.select("h3").text();
				String standardTime = string2Date(time.replaceAll("\\(周.\\)", "").trim());
				timeAndPrice.put(standardTime, list);
				Iterator<Element> trList = timeEle.select("tr").iterator();
				while(trList.hasNext()){
					Element trEle = trList.next();
					Iterator<Element> tdList = trEle.select("td").iterator();	
					while(tdList.hasNext()) {
						Element tdEle = tdList.next();
						if(tdEle.text().contains("元")) {
							TicketPrice ticketPrice = new TicketPrice();
							ticketPrice.setPrice(string2Number(tdEle.text()));
							 Element nextElement = tdEle.nextElementSibling();
							 if("".equals(nextElement.text().trim())) {
								 ticketPrice.setRemark(null);
							 } else {
								 ticketPrice.setRemark(nextElement.text());
							 }
							if(!nextElement.nextElementSibling().select("a>img").isEmpty()) {
								ticketPrice.setExist(true);
								String ticketUrl = nextElement.nextElementSibling().select("a").first().attr("abs:href");
								ticketPrice.setDetailURL(ticketUrl);//具体的买票详细地址
							}
							else {
								ticketPrice.setExist(false);
								ticketPrice.setDetailURL(null);
							}
							ticketPrice.setMainURL(url);//买票的主页
							list.add(ticketPrice);//加入具体的票价
						} 
					}
				}
			}
		}
		show.setTimeAndPrice(timeAndPrice);//添加所有的票价信息
			try {
				getDao().saveShow(show);
			} catch (SQLException e) {
				log.error(e.getMessage());
			}
	}

	public static void main(String[] args) {
		CultureShSpider spider = new CultureShSpider();
		spider.setDao(new DataDao());
		spider.extract();
	}
	//转化成标准存储日期
	private String string2Date(String str) { 
		SimpleDateFormat old = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
		SimpleDateFormat  now = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String result = null;
		try {
			result = now.format(old.parse(str));
			return result;
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return result;
	}
	//转化成标准存储数字
	private String string2Number(String str) {
		String r = str.replaceAll("\\D","").trim();
			String r2 = r.substring(0,r.length()-2);
			return r2;
	}
}
