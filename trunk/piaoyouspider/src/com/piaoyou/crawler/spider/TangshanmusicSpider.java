package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;
public class TangshanmusicSpider extends TicketSpider implements SpiderTask {
	private static final String BASE_URL = "http://www.tangshanmusic.com/";
	private static final String URL ="http://www.tangshanmusic.com/ticketsall.html";
	private static final Class<TangshanmusicSpider> BASE_CLASS=TangshanmusicSpider.class;
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
		
	/*	try {
			parseEach("");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	public static void main(String[] args) {
		TangshanmusicSpider tangshang = new TangshanmusicSpider();
		tangshang.setDao(new DataDao());
		tangshang.extract();
	}
	public Set<String> getURL() {
		Set<String> set = new HashSet<String>();
		Document document = getDoc(URL);
		for(Element ele:document.select("img[src=images/dp.gif]")){
			  set.add(ele.parent().attr("abs:href"));
		}
		return set;
	}
	public void parseEach(String url) throws Exception{
		Show show = new Show();
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>() ;
		List<TicketPrice> list = null ;
		TicketPrice ticket = null ;
		Document document = getDoc(url);
		show.setAgent_id(agentID);
		show.setName(document.select("span.b").get(0).text());
		show.setType(getShowType(document.select("table[width=980]").get(4).select("a").get(1).text()));
		String site = document.select("span.b1:contains(场馆)").text();
		site = site.substring(site.indexOf("场馆：")+3);
		show.setSiteName(site);
		//有的演出没有演出图片，因此此处需要try catch
		try {
			show.setImage_path(document.select("table[style=border:1px solid silver;]").get(1).select("td[width=220] img").first().attr("abs:src"));
		} catch (Exception e1) {
		}
		show.setIntroduction(PubFun.cleanElement(document.select("div[style=line-height:20px;margin-top:10px;padding:6px; border:1px solid silver; width:728px;]")).html());
		for (Element ele : document.select("table[width=728] tr[onmouseover^=this]")) {
			String showTime = ele.select("td").get(0).text() + " "+ ele.select("td").get(1).text();
			try {
				showTime = newDate.format(newDate.parse(showTime));
				if(newDate.parse(showTime).before(new Date())){
					  continue;
				}
			} catch (Exception e) {
			}
			list = new ArrayList<TicketPrice>();
			List<Node> listNode = ele.select("td").get(2).childNodes();
			for (int i = 0, length = listNode.size(); i < length; i++) {
				Node node = (Node) listNode.get(i);
				if (node instanceof Element) {
					ticket = new TicketPrice();
					ticket.setMainURL(url);
					ticket.setDetailURL(node.attr("abs:href"));
					ticket.setExist(true);
					String pri = ((Element) node).text();
					String remk = pri.replaceAll("^\\d+", "");
					pri = pri.replace(remk, "");
					ticket.setPrice(pri);
					ticket.setRemark(remk);
					list.add(ticket);
				} else if (node instanceof TextNode) {
					String price[] = ((TextNode) node).text().split("、");
					for (int j = 0; j < price.length; j++) {
						if (!("".equals(price[j]) || "".equals(price[j]))) {
							ticket = new TicketPrice();
							ticket.setMainURL(url);
							ticket.setExist(false);
							String pri = price[j];
							String remk = pri.replaceAll("^\\d+", "");
							pri = pri.replace(remk, "");
							ticket.setPrice(pri);
							ticket.setRemark(remk);
							list.add(ticket);
						}
					}
				}
			}
			timeAndPrice.put(showTime, list);
			show.setTimeAndPrice(timeAndPrice);
		}
		getDao().saveShow(show);
	}
	private int getShowType(String type){
		int showType = 0;
		if("演唱会".equals(type)){
			 showType = ShowType.CONCERT;
		}else if("音乐会".equals(type)||"国家大剧院 音乐厅".equals(type)){
			showType = ShowType.SHOW;
		}else if("话剧歌剧".equals(type)||"国家大剧院 歌剧院".equals(type)||"大剧院戏剧场".equals(type)||"话剧 歌剧".equals(type)){
			showType = ShowType.DRAMA;
		}else if("舞蹈芭蕾".equals(type)){
			showType = ShowType.DANCE;
		}else if("曲苑杂坛".equals(type)||"戏曲相声".equals(type)||"杂技".equals(type)){
			showType = ShowType.OPERA;
		}else if("儿童亲子".equals(type)){
			showType = ShowType.CHILDREN;
		}else if("体育比赛".equals(type)){
			showType = ShowType.SPORTS;
		}else if("度假休闲".equals(type)||"体育休闲".equals(type)){
			showType = ShowType.HOLIDAY;
		}
		return showType;
	}

}
