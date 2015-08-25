package com.piaoyou.crawler.spider;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.piaoyou.util.Const;

public class PiaowudawangSpider extends TicketSpider implements SpiderTask {
	
	
	private static final String BASE_URL = "http://www.piaowudawang.com/";
	private static final String URL ="http://www.piaowudawang.com/All_Ticket.html";
	private static final Class<HxpwSpider> BASE_CLASS=HxpwSpider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();

	@Override
	public void extract() {
		Set<String> set = new HashSet<String>();
		set = getURL();
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url);
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url),e);
			}
		}
//		parseEach("http://www.piaowudawang.com/Ticket_134101.html");
	}
	
	public Set<String> getURL(){
		Set<String> url = new HashSet<String>();
		Document document = getDoc(URL);
		for(Element element :document.select("img[height=19]")){
			url.add(element.parent().attr("abs:href"));
		}
		return url;
	}
	public void parseEach(String url) throws SQLException{
		Document document = getDoc(url);
		Show show = new Show();
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		List<TicketPrice> list = null ;
		TicketPrice ticket = null ;
		
		
		String showTypeName = document.select("table[width=968] tr td[background=image/top.jpg] a").first().nextElementSibling().text();
		if("电影票".equals(showTypeName)){
			return ;
		}
		int showType = getShowType(showTypeName);
		String showName = document.select("td.font1").text();
		String siteName = document.select("td.font_nei a").first().text();
		String showTime = "";
		show.setName(showName);
		show.setType(showType);
		show.setAgent_id(agentID);
		show.setSiteName(siteName);
		show.setImage_path("");
		show.setIntroduction("");
		for(Element element : document.select("table.biaoge").eq(3).select("table tr")){
			Elements elements = element.select("td[align=center]");
			if("".equals(elements.text())){
				continue;
			}
			showTime = (elements.eq(0).text());
			try{
				showTime = newDate.format(newDate.parse(showTime));
			}catch(Exception e){
				
			}
			list = new ArrayList<TicketPrice>();
			String prices[] = elements.eq(2).text().split(" ");
			for(int i=0;i<prices.length;i++){
				ticket = new TicketPrice();
				ticket.setMainURL(url);
				ticket.setPrice(prices[i]);
				list.add(ticket);
			}
			prices = elements.eq(3).text().split(" ");
			for(int i=0;i<prices.length;i++){
				if("".equals(prices[i])){
					continue;
				}
				ticket = new TicketPrice();
				ticket.setMainURL(url);
				ticket.setPrice(prices[i]);
				list.add(ticket);
			}
			
			for(Element ele :elements.eq(2).select("a")){
				for(int i=0;i<list.size();i++){
					if(list.get(i).getPrice().equals(ele.text())){
						list.get(i).setExist(true);
						list.get(i).setDetailURL(ele.attr("abs:href"));
					}
				}
			}
			for(Element ele :elements.eq(3).select("span[onmouseover=showremark(this)]")){
				for(int i=0;i<list.size();i++){
					if(list.get(i).getPrice().equals(ele.text()+ele.nextElementSibling().text())){
						list.get(i).setPrice(ele.text());
						list.get(i).setRemark(ele.nextElementSibling().text());
						if(ele.select("a")!=null&&ele.select("a").size()!=0){
							list.get(i).setDetailURL(ele.select("a").first().attr("abs:href"));
							list.get(i).setExist(true);
						}
						
					}
				}
			}
			timeAndPrice.put(showTime, list);
		}
		show.setTimeAndPrice(timeAndPrice);
		getDao().saveShow(show);
	}
	
	private int getShowType(String showTypeName) {
		int showType = 0 ;
		if("演唱会".equals(showTypeName)){
			showType=ShowType.CONCERT;
		}else if("话剧歌剧".equals(showTypeName)||"人艺话剧".equals(showTypeName)||"歌剧院".equals(showTypeName)
				||"小剧场".equals(showTypeName)||"戏剧场".equals(showTypeName)){
			showType=ShowType.DRAMA;
		}else if("音乐会".equals(showTypeName)||"打开音乐之门（北京音乐厅）".equals(showTypeName)
				||"音乐厅".equals(showTypeName)||"打开音乐之门（中山音乐堂）".equals(showTypeName)){
			showType=ShowType.SHOW;
		}else if("戏曲综艺".equals(showTypeName)||"风尚剧场".equals(showTypeName)
				||"嘻哈包袱铺专场演出".equals(showTypeName)||"梅兰芳大剧院".equals(showTypeName)
				||"长安大戏院".equals(showTypeName)||"戏曲曲艺".equals(showTypeName)
				||"魔术马戏".equals(showTypeName)||"综艺晚会".equals(showTypeName)){
			showType=ShowType.OPERA;
		}else if("体育休闲".equals(showTypeName)||"体育赛事".equals(showTypeName)){
			showType=ShowType.SPORTS;
		}else if("舞蹈芭蕾".equals(showTypeName)||"北京9剧场".equals(showTypeName)){
			showType=ShowType.DANCE;
		}else if("儿童亲子".equals(showTypeName)){
			showType=ShowType.CHILDREN;
		}else if("通票".equals(showTypeName)||"休闲类 ".equals(showTypeName)){
			showType=ShowType.HOLIDAY;
		}else{
			showType=ShowType.OTHER;
		}
		return showType;
	}

	public static void main(String args[]){
		PiaowudawangSpider spider = new PiaowudawangSpider();
		spider.setDao(new DataDao());
		spider.extract();
	}
	
	

}
