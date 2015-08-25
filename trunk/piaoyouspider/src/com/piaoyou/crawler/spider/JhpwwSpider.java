package com.piaoyou.crawler.spider;

import java.text.ParseException;
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

public class JhpwwSpider extends TicketSpider implements SpiderTask {
	
	private static final String BASE_URL =  "http://www.jhpww.com/";
	private static final String URL = "http://www.jhpww.com/allpiao.asp";
	private static final Class<JhpwwSpider> BASE_CLASS=JhpwwSpider.class;
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	
	
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public void extract() {
		Map<String,Integer> map = new HashMap<String ,Integer>();
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
		 Map<String,Integer> map = new HashMap<String,Integer>();
		 Document document = getDoc(URL);
		 int showType=0;
		 for(Element element : document.select("table[width=780]").get(2).select("tr")){
			 if(element.select("td[colspan=5]").size()==1){
				String type = element.select("td table tr td:has(a)").text();
				showType =getShowType(type);
			 }
			 if(element.select("td a[target=_blank]").size()!=0){
				 map.put(element.select("td a[target=_blank]").first().attr("abs:href"), showType);
			 }
		 }
		 return map;
	}
	public int getShowType(String type){
		 int showType = 9 ;
		 if("演唱会".equals(type)){
			 showType=ShowType.CONCERT;
		 }else if("音乐会".equals(type)||"国家大剧院专场".equals(type)){
			 showType=ShowType.SHOW;
		 }else if("话剧歌剧".equals(type)||"人艺演出".equals(type)){
			 showType=ShowType.DRAMA;
		 }else if("舞蹈芭蕾".equals(type)){
			 showType=ShowType.DANCE;
		 }else if("曲苑杂坛".equals(type)||"相声/小品".equals(type)||"魔术杂技".equals(type)||"戏曲综艺".equals(type)){
			 showType=ShowType.OPERA;
		 }else if("儿童亲子".equals(type)||"亲子家庭".equals(type)){
			 showType=ShowType.CHILDREN;
		 }else if("体育比赛".equals(type)||"足球".equals(type)){
			 showType=ShowType.SPORTS;
		 }else if("度假休闲".equals(type)||"博物馆通票".equals(type)||"电影通票".equals(type)){
			 showType=ShowType.HOLIDAY;
		 }
		 return showType;
	}
	public void parseEach(String url,int showType) throws Exception{
		Show show = null ;
		List<TicketPrice> list = null ;
		TicketPrice ticket= null ;
		Document document = getDoc(url);
		show = new Show();
		show.setAgent_id(agentID);
		show.setType(showType);
		show.setName(document.select("div[style=FONT-SIZE: 15px;color:red]").text());
		String siteName = document.select("td a").get(28).text();
		//如果场馆待定，则不进行抓取
		if(siteName.contains("待定")){
			return ;
		}
		show.setSiteName(siteName);
		show.setImage_path(document.select("body table").get(14).select("td[width=248] img").first().attr("abs:src"));
		Element introduction =PubFun.cleanElement(document.select("td.flan").first());
		show.setIntroduction(introduction.html());
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		for(Element element:document.select("tr").get(24).select("table").get(2).select("tr td table")){
			for(Element ele :element.select("tr[bgcolor=#EEEEEE]")){
				String showTime = getDate(ele.select("td").get(0).text());
				if(showTime.contains("待定")){
					continue;
				}
				list = new ArrayList<TicketPrice>();
				for(Element ele1:ele.select("td").get(2).select("a")){
					ticket = new TicketPrice();
					ticket.setMainURL(url);
					ticket.setExist(true);
					//如果票价不是正常的数字格式，则不进行抓取
					try{
						Integer.parseInt(ele1.text());
					}catch(Exception e){
						continue;
					}
					ticket.setPrice(ele1.text());
					
					ticket.setDetailURL(ele1.attr("abs:href"));
					list.add(ticket);
				}
				for(Element ele1:ele.select("td").get(2).select("font")){
					ticket = new TicketPrice();
					if(ele1.text().contains("待定")){
						continue;
					}
					ticket.setPrice(ele1.text());
					ticket.setExist(false);
					ticket.setMainURL(url);
					list.add(ticket);
				}
				timeAndPrice.put(showTime, list);
			}
			for(Element ele:element.select("tr[bgcolor=#FFFFFF]")){
				String showTime = getDate(ele.select("td").get(0).text());
				list = new ArrayList<TicketPrice>();
				for(Element ele1:ele.select("td").get(2).select("a")){
					ticket = new TicketPrice();
					ticket.setMainURL(url);
					ticket.setExist(true);
					if(ele1.text().contains("待定")){
						continue;
					}
					ticket.setPrice(ele1.text());
					ticket.setDetailURL(ele1.attr("abs:href"));
					list.add(ticket);
				}
				for(Element ele1:ele.select("td").get(2).select("font")){
					ticket = new TicketPrice();
					ticket.setPrice(ele1.text());
					if(ele1.text().contains("待定")){
						continue;
					}
					ticket.setExist(false);
					ticket.setMainURL(url);
					list.add(ticket);
				}
				timeAndPrice.put(showTime, list);
			}
			show.setTimeAndPrice(timeAndPrice);
		}
		getDao().saveShow(show);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
        JhpwwSpider jjpww = new JhpwwSpider();
        jjpww.setDao(new DataDao());
        jjpww.extract();
	}
	
	
	public String getDate(String showTime){
		 if(showTime.indexOf("星期")!=-1){
				showTime = showTime.replaceAll("[*\\s星期(一|二|三|四|五|六|日)]", " ");
				showTime = showTime.replaceAll("[年|月]", "-");
				try {
					showTime = newDate.format(newDate.parse(showTime));
				} catch (ParseException e) {
				}
			}
		 return showTime ;
	}

}
