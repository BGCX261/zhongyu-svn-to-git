package com.piaoyou.crawler.spider;

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

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class Pw228Spider extends TicketSpider implements SpiderTask {
	private static final String BASE_URL = "http://www.228pw.com/";
	private static final String URL ="http://www.228pw.com/allpiao.asp";
	private static final Class<Pw228Spider> BASE_CLASS= Pw228Spider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();

	@Override
	public void extract() {
		Set<String> set = new HashSet<String>();
		set = getURL();
		for (Iterator<String> iterator = set.iterator(); iterator	.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url);
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url),e);
			}
		}
	}
	public static void main(String[] args) {
		Pw228Spider pw228 = new Pw228Spider();
		pw228.setDao(new DataDao());
		pw228.extract();
	}

	public Set<String> getURL() {
		Set<String> set = new HashSet<String>();
		Document document = getDoc(URL);
		for(Element element :document.select("a img[src=image/go_d_5.gif]")){
			set.add(element.parent().attr("abs:href"));
		}
		return set;
	}

	public void parseEach(String url) throws Exception {
		Show show = new Show();
		Map<String, List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
		List<TicketPrice> list = null;
		TicketPrice ticket = null;
		Document document = getDoc(url);
		show.setAgent_id(agentID);
		show.setName(document.select("div[style=FONT-SIZE: 12pt; COLOR: red]").text());
		show.setSiteName(document.select("table[width=770] table[style=BORDER-COLLAPSE: collapse] table[cellspacing=1] a").text());
		show.setImage_path(document.select("table tr  td[vAlign=top] img").first().attr("abs:src"));
		show.setIntroduction(PubFun.cleanElement(document.select("div.main")).html());
		String showType = document.select("table[width=770]").get(5).select("a[href^=piao.asp?sclass=]").text();
		show.setType(getShowType(showType));
		for (Element element : document.select("table.table-shang tr[bgcolor^=#F]")) {
			String showTime = element.select("td").get(1).text();
			try {
				showTime=showTime.replace("：", ":");
				showTime = newDate.format(newDate.parse(showTime));
			} catch (Exception e) {
			}
			list = new ArrayList<TicketPrice>();
			for (Element ele : element.select("td").get(2).select("span")) {
				if ("a".equals(ele.parent().nodeName())) {
					ticket = new TicketPrice();
					ticket.setMainURL(url);
					ticket.setDetailURL(ele.parent().attr("abs:href"));
					ticket.setExist(true);
					String remark = ele.text().replaceAll("^\\d+", "");
					String price = ele.text().replaceAll(remark, "");
					remark = remark.replaceAll("[（）()]", "");
					ticket.setPrice(price);
					ticket.setRemark(remark);
					list.add(ticket);
				} else {
					ticket = new TicketPrice();
					ticket.setMainURL(url);
					ticket.setExist(false);
					String remark = ele.text().replaceAll("^\\d+", "");
					String price = ele.text().replaceAll(remark, "");
					remark = remark.replaceAll("[（）()]", "");
					ticket.setPrice(price);
					ticket.setRemark(remark);
					list.add(ticket);
				}
			}
			timeAndPrice.put(showTime, list);
		}
		show.setTimeAndPrice(timeAndPrice);
		getDao().saveShow(show);
	}
	private int getShowType(String type){
		int showType = 0;
		if("演唱会".equals(type)){
			 showType = ShowType.CONCERT;
		}else if("音乐会".equals(type)||"国家大剧院音乐厅".equals(type)||"打开音乐之门(北京音乐厅)".equals(type)){
			showType = ShowType.SHOW ;
		}else if("戏曲曲艺".equals(type)||"魔术杂技".equals(type)||"长安大戏院".equals(type)||"国家大剧院小剧场".equals(type)){
			showType = ShowType.OPERA ;
		}else if("芭蕾舞蹈".equals(type)){
			showType = ShowType.DANCE ;
		}else if("歌剧话剧".equals(type)||"人艺话剧".equals(type)||"朝阳9个剧场".equals(type)
				||"国家大剧院戏剧场".equals(type)||"国家大剧院歌剧院".equals(type)||"梅兰芳大剧院".equals(type)
				||"戏曲综艺".equals(type)){
			showType = ShowType.DRAMA ;
		}else if("电影通票".equals(type)){
			showType = ShowType.HOLIDAY ;
		}else if("儿童节目".equals(type)){
			showType = ShowType.CHILDREN ;
		}else if("体育赛事".equals(type)){
			showType =ShowType.SPORTS;
		}else{
			showType = ShowType.OTHER;
		}
		return showType;
	}

}
