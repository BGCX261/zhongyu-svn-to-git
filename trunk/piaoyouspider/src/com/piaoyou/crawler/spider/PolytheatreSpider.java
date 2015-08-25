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

public class PolytheatreSpider extends TicketSpider implements SpiderTask {
	//北京保利剧院
	private static final String BASE_URL ="http://www.polytheatre.com/";
	private static final Class<PolytheatreSpider> BASE_CLASS =PolytheatreSpider.class;
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
		PolytheatreSpider template = new PolytheatreSpider();
		template.setDao(new DataDao());
		template.extract();
	}
	private  Set<String> getMainURL(){
		Set<String> set = new HashSet<String>();
		 Document document  = getDoc(BASE_URL);
		 for(Element element:document.select("#rightAdName a")){
			 String main_url = element.attr("abs:href");
			 if(!main_url.endsWith("/")){
				 main_url+="/";
			 }
			 set.add(main_url);
		 }
		 return set;
	}

	public Set<String> getURL() {
		 Set<String> set = new HashSet<String>();
		 Set<String> mainSet = new HashSet<String>();
		 mainSet = getMainURL();     //获取所有网站的首页链接
		 for (Iterator<String> iterator = mainSet.iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			if(url.contains("shoac")){    //解析上海东方艺术中心的网站
				getOtherDetailURL(url,set);
			}else{    //解析保利其他剧院的网站
				getMainDetailURL(url,set);
			}
		}
		 return set;
	}
	
	private void getMainDetailURL(String url, Set<String> set) {
		Set<String> tmp = new HashSet<String>();
		Document document = getDoc(url + "search");
		tmp.add(url + "search");
		while (document.select("div.pagination a:contains(下一页)").size() != 0) {
			tmp.add(document.select("div.pagination a:contains(下一页)").first().attr("abs:href"));
			document = getDoc(document.select("div.pagination a:contains(下一页)").first().attr("abs:href"));
		}
		for (Iterator<String> iterator1 = tmp.iterator(); iterator1.hasNext();) {
			String url1 = (String) iterator1.next();
			Document doc = getDoc(url1);
			for (Element ele : doc.select("div.showItemTitle a")) {
				set.add(ele.attr("abs:href"));
			}
		}
	}
	
	
	private void getOtherDetailURL(String url,Set<String> set){
		url = "http://cn.shoac.com.cn/tickets.asp";
		 Map<String,String> map = new HashMap<String,String>();
		 String num = "1";
		 map.put("pageNo", num);
		 Document document= getDocPost(url, map);
		 Element element = document.select("center form img").get(2);
		while ("下一页".endsWith(element.attr("alt"))&& element.hasAttr("onclick")) {
			for (Element ele : document.select("td[width=437] a")) {
				set.add(ele.attr("abs:href"));
			}
			element = document.select("center form img").get(2);
			String tmp = document.select("center form img").get(2).attr("onclick");
			num = tmp.replaceAll("[document.forms('List').pageNo.value='a-z+';goSearch()]","");
			map.put("pageNo", num);
			document = getDocPost(url, map);
		}
	}
	
	public void parseEach(String url) throws Exception{
		if(url.contains("shoac")){    //解析上海东方艺术中心的网站
			parseEachOther(url);
		}else{    //解析保利其他剧院的网站
			parseEachMain(url);
		}
	}
	
	private void parseEachMain(String url) throws Exception{
	    String base_url = PubFun.getRegex("http://([\\w-]+\\.)+[\\w-]+(/)", url, 0);
		Show show = new Show();
		Map<String,List<TicketPrice>> timeAndPrice = null ;
		List<TicketPrice> list = null ;
		TicketPrice ticket = null ;
		Document document = getDoc(url);
		show.setAgent_id(agentID);
		String showName = document.select("div.showTitle").first().text();
		show.setName(showName);
		if(showName.contains("音乐会")){
			show.setType(ShowType.SHOW);
		}else if(showName.contains("魔术")||showName.contains("京剧")){
			show.setType(ShowType.OPERA);
		}else{
			show.setType(ShowType.DRAMA);
		}
		show.setStatus(1);
		show.setImage_path(document.select("#mainColumn div div[align=center] img").first().attr("abs:src"));
		String introduction = PubFun.cleanElement(document.select("td.test")).html();
		if("".equals(introduction)){
			   introduction = PubFun.cleanElement(document.select("div.showDesc")).html();
		}
		show.setIntroduction(introduction);
		String site = document.select("div.showDetails").text();
		site = site.substring(site.indexOf("演出地点:")+5,site.indexOf("演出时间"));
		show.setSiteName(site);
		show.setFirstAgent(true);
		timeAndPrice = new HashMap<String,List<TicketPrice>>();
		String showTime = document.select("div.showDetails").text();
		showTime = showTime.substring(showTime.indexOf("演出时间:")+5,showTime.indexOf("演出票价"));
		try {
			showTime = newDate.format(newDate.parse(showTime));
		} catch (Exception e) {
		}
		//临近演出，不接受网上订票
		if("".equals(document.select("div.showDetailsIcons a").toString())){
			return;
		}
		String url1 = document.select("div.showDetailsIcons a").first().attr("abs:href");
		Document doc = getDoc(url1);
		String[] sss = doc.select("script").last().toString().split(",");
        doc= getDoc(base_url+sss[1].substring(2,sss[1].length()-1));
        String price = base_url+sss[7].substring(2,sss[7].length()-1);
        Map<String,String> typeAndPrice = new HashMap<String,String>();
        typeAndPrice = getTypeAndPrice(price);
         String html = doc.html();
         list = new ArrayList<TicketPrice>();
         for (Iterator<String> iterator = typeAndPrice.keySet().iterator(); iterator.hasNext();) {
			     String pricetype  = (String) iterator.next();
			     if(html.contains(pricetype)){
			    	 ticket = new TicketPrice();
			    	 ticket.setMainURL(url);
			    	 ticket.setDetailURL(url1);
			    	 ticket.setPrice(typeAndPrice.get(pricetype));
			    	 ticket.setExist(true);
			    	 list.add(ticket);
			     }else{
			    	 ticket = new TicketPrice();
			    	 ticket.setMainURL(url);
			    	 ticket.setPrice(typeAndPrice.get(pricetype));
			    	 ticket.setExist(false);
			    	 list.add(ticket);
			     }
		}
        timeAndPrice.put(showTime, list);
        show.setTimeAndPrice(timeAndPrice);
        getDao().saveShow(show);
}
	private void parseEachOther(String url) throws Exception{
		String base_url = PubFun.getRegex("http://([\\w-]+\\.)+[\\w-]+(/)", url, 0);
		Document document  = getDoc(url);
		Show show = new Show();
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		List<TicketPrice> list = null ;
		show.setAgent_id(agentID);
		String site = document.select("span.font_gray").first().text();
		show.setSiteName(site.substring(site.indexOf("地点：")+3,site.indexOf("票价")));
		String name =document.select("span.font_or").first().text();
		show.setName(name);
		show.setFirstAgent(true);
		if(name.contains("剧")){
			  show.setType(ShowType.DRAMA);
		}else{
			show.setType(ShowType.SHOW);
		}
		show.setStatus(1);
		show.setImage_path(document.select("table[width=95%] table[width=100%] img").first().attr("abs:src"));
		show.setIntroduction(PubFun.cleanElement(document.select("table[height=30]").get(1)).html());
		for(Element ele :document.select("select[name=Playday] option")){
			list = new ArrayList<TicketPrice>();
			  String uri = ele.parent().attr("onchange");
			  uri = uri.replaceAll("window.location='", "");
			  uri = uri.substring(0,uri.length()-1);
			  uri = base_url+uri.replace("' + this.value + '", ele.attr("value"));
			  document = getDoc(uri);
			  String showTime ="";
				for(Element element :document.select("table[bgcolor=#999999] tr")){
					if(element.select("td").size()==5&&element.select("td select").size()!=0){
						  TicketPrice ticket = new TicketPrice();
						  ticket.setMainURL(uri);
						  ticket.setDetailURL(url);
						  ticket.setPrice(element.select("td").get(1).text());
						  ticket.setExist("有余票".equals(element.select("td").get(3).text())?true:false);
						  showTime=  element.select("td").get(0).text();
						  list.add(ticket);
					}
				}
				try {
					showTime = newDate.format(newDate.parse(showTime));
				} catch (Exception e) {
				}
				timeAndPrice.put(showTime, list);
		}
		show.setTimeAndPrice(timeAndPrice);
		getDao().saveShow(show);
	}
	
	private Map<String,String> getTypeAndPrice(String price){
		Map<String,String> map = new HashMap<String,String>();
		Document doc = getDoc(price);
		for(Element ele :doc.select("p")){
			if(!ele.text().contains("已售")){
				map.put(ele.select("img").first().attr("src"),ele.text().replaceAll("[^0-9|\\.]", ""));
			}
		}
		return map;
	}
	
	
}
