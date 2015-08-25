package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

public class JiujiupiaowuSpider extends TicketSpider implements SpiderTask {
	
	private static final String BASE_URL =  "http://www.99piao.com/";
	private static final String URL = "http://www.99piao.com/allpiao.asp";
	private static final Class<JiujiupiaowuSpider> BASE_CLASS = JiujiupiaowuSpider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();


	@Override
	public void extract() {
		  Map<String,Integer> map = new HashMap<String,Integer>();
		  map = getURL();
		  for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url, map.get(url));
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url,map.get(url)),e);
			}
		}
	}
	public Map<String,Integer> getURL(){
		Map<String ,Integer> map  = new HashMap<String,Integer>();
		Document document  = getDoc(URL);
		for (int i = 9; i <= 21; i++) {
			Element element = document.select("table[width=960]").get(i);
			String name = element.select("td[style^=font-weight:bold;width:2%;]").text();
			for (Element ele : element	.select("td[valign=top] table tr[style=border:1px solid #999;]")) {
				String url =  ele.select("td a").first().attr("abs:href");
				int showType = getShowType(name);
				map.put(url, showType);
			}
		}
		return map;
	}
	public void parseEach(String url,int showType) throws Exception{
		   url ="http://www.99piao.com/piaodisp.asp?id=4688";
			Document document = getDoc(url);
			Element element = document.select("table[width=960]").get(9);
			Show show = new Show();
			Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
			List<TicketPrice> list = null ;
			TicketPrice ticket = new TicketPrice();
			show.setAgent_id(agentID);
			show.setImage_path(element.select("div[style=FONT-SIZE: 10pt; COLOR: black] img").first().attr("abs:src"));
			show.setName(element.select("div[style=FONT-SIZE: 10pt; COLOR: black] b").first().text());
			show.setSiteName(element.select("table table table table").get(2).select("table tr:contains(场馆) td:has(a)").text());
			show.setIntroduction(PubFun.cleanElement(element.select("table table table table").get(17)).text());
			show.setType(showType);
			//票价相关
			for (Element ele : element.select("table table table table").get(6)
					.select("tr[bgcolor^=#F]")) {
				list = new ArrayList<TicketPrice>();
				String showTime = ele.select("td").get(1).text();
				try {
					showTime = newDate.format(newDate.parse(showTime));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				for (Element ele1 : ele.select("td").get(2).select("a")) {
					ticket = new TicketPrice();
					ticket.setMainURL(url);
					ticket.setDetailURL(ele1.attr("abs:href"));
					ticket.setPrice(ele1.text());
					ticket.setExist(true);
					list.add(ticket);
				}
				for (Element ele1 : ele.select("td").get(2).select("font[color=#999999]")) {
					ticket = new TicketPrice();
					ticket.setMainURL(url);
					ticket.setPrice(ele1.text());
					ticket.setExist(false);
					list.add(ticket);
				}
				if(list.isEmpty()||list.size()==0){
					continue;
				}
				timeAndPrice.put(showTime, list);
			}
			if(timeAndPrice.size()==0||timeAndPrice.isEmpty()){
				return ;
			}
			show.setTimeAndPrice(timeAndPrice);
			getDao().saveShow(show);
	}
	private int getShowType(String name){
		  int showType = 0 ;
		  if("演唱会".equals(name)){
			  showType = ShowType.CONCERT;
		  }else if("话剧".equals(name)||"国家大剧院歌剧院".equals(name)||"国家大剧院戏剧场".equals(name)||"人艺票品".equals(name)){
			  showType = ShowType.DRAMA;
		  }else if("戏曲综艺".equals(name)||"魔术杂技".equals(name)){
			  showType = ShowType.OPERA;
		  }else if("音乐会".equals(name)||"国家大剧院小剧场".equals(name)||"国家大剧院音乐厅".equals(name)){
			  showType = ShowType.SHOW;
		  }else if("芭蕾舞蹈".equals(name)){
			  showType = ShowType.DANCE;
		  }else if("儿童节目".equals(name)){
			  showType = ShowType.CHILDREN;
		  }else if("体育休闲".equals(name)){
			  showType = ShowType.HOLIDAY;
		  }
		  return showType;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		JiujiupiaowuSpider jiujiupiaowu = new JiujiupiaowuSpider();
		jiujiupiaowu.setDao(new DataDao());
		jiujiupiaowu.extract();
	}

}
