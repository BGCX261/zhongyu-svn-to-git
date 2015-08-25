package com.piaoyou.crawler.spider;

import java.sql.SQLException;
import java.text.ParseException;
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


public class HxpwSpider extends TicketSpider implements SpiderTask {
	
	private static final String BASE_URL = "http://www.hxpw.com/";
	private static final String URL ="http://www.hxpw.com/allpiao.asp";
	private static final Class<HxpwSpider> BASE_CLASS=HxpwSpider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();

	@Override
	public void extract() {
		  Set<String>set = new HashSet<String>();
		  set = getURL();
		  for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url);
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url),e);
			}
			
		}
	}
	public static void main(String[] args) {
		HxpwSpider hxpw = new HxpwSpider();
		hxpw.setDao(new DataDao());
		hxpw.extract();
	/*	try {
			hxpw.parseEach("http://www.hxpw.com/showinfo.asp?id=6549");
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	public Set<String> getURL(){
		Set<String>set = new HashSet<String>();
		Document document = getDoc(URL);
		for (Element ele : document.select("table[cellspacing=1]")) {
			for (Element ele1 : ele.select("tr td table[cellpadding=3]")) {
				if (ele1.select("tr").select("td").first().select(":has(a)").size() != 0) {
					if (!ele1.select("tr").get(1).text().contains("待定")) {
					     set.add(ele1.select("tr").get(0).select("td a").first().attr("abs:href"));
					}
				}
			}
		}
		return set;
	}
	public void parseEach(String url) throws SQLException,Exception{
		Show show = new Show();
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		List<TicketPrice> list = null ;
		TicketPrice ticket = null ;
		Document document = getDoc(url);
		show.setAgent_id(agentID);
		show.setName(document.select("td h3").text());
		show.setType(getShowType(document.select("table[cellpadding=2] tr td a").get(1).text()));
		show.setImage_path(document.select("td[width=180] a img").first().attr("abs:src"));
		show.setIntroduction(PubFun.cleanElement(document.select("table[borderColor=#808080]")).html());
		show.setSiteName(document.select("td[width=221] font[color=#FF0000]").first().text());
		show.setWatermark(false);
		for(Element ele :document.select("table table table table").get(0).select("tr")){
			  if(!"#d7d7b0".equals(ele.attr("bgcolor"))){
					list = new ArrayList<TicketPrice>();
					String showTime = ele.select("td").get(1).text();
					showTime = showTime.replaceAll("\\.","-");
					try {
						showTime=showTime.replace("：", ":");
						showTime = newDate.format(newDate.parse(showTime));
					} catch (ParseException e) {
					}
					List<Node> child = ele.select("td").get(2).childNodes();
					for(int i= 0 ,length = child.size();i<length;i++){
						  Node node = child.get(i);
						  if(node instanceof TextNode && !"".equals(((TextNode)node).text().trim())){
							  String prices[] = ((TextNode)node).text().trim().replaceAll("[( | )+]"," ").replaceAll(" +", "").replaceAll(" +", " ").split(" ");
							  for(int j = 0 ;j<prices.length;j++){
								  ticket = new TicketPrice();
								  ticket.setMainURL(url);
								  ticket.setPrice(prices[j]);
								  ticket.setExist(false);
								  list.add(ticket);
							  }
						  }else if(node instanceof Element){
							  ticket = new TicketPrice();
							  ticket.setMainURL(url);
							  ticket.setDetailURL(((Element)node).attr("abs:href"));
							  ticket.setExist(true);
							  ticket.setPrice(((Element)node).text());
							  list.add(ticket);
						  }
					}
					timeAndPrice.put(showTime, list);
			  }
			  show.setTimeAndPrice(timeAndPrice);
		}
		getDao().saveShow(show);
	}
	private int getShowType(String text) {
		//演唱会 话剧歌剧 音乐会 戏曲综艺 体育休闲 舞蹈芭蕾 儿童亲子 魔术马戏 中国京剧院演出 梅兰芳大剧院 通票
		int showType = 0 ;
		if("演唱会".equals(text)){
			showType=ShowType.CONCERT;
		}else if("话剧歌剧".equals(text)){
			showType=ShowType.DRAMA;
		}else if("音乐会".equals(text)){
			showType=ShowType.SHOW;
		}else if("戏曲综艺".equals(text)||"魔术马戏".equals(text)||"中国京剧院演出".equals(text)||"梅兰芳大剧院".equals(text)){
			showType=ShowType.OPERA;
		}else if("体育休闲".equals(text)){
			showType=ShowType.SPORTS;
		}else if("舞蹈芭蕾".equals(text)){
			showType=ShowType.DANCE;
		}else if("儿童亲子".equals(text)){
			showType=ShowType.CHILDREN;
		}else if("通票".equals(text)){
			showType=ShowType.HOLIDAY;
		}else{
			showType=ShowType.OTHER;
		}
		return showType;
	}

}
