package com.piaoyou.crawler.spider;

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
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class CenturyticketSpider extends TicketSpider implements SpiderTask {
	
	private static final String URL ="http://www.centuryticket.com/";
	private static final Class<CenturyticketSpider> BASE_CLASS=CenturyticketSpider.class;
	private static final String BEIJING_URL = "http://www.centuryticket.com/index.php/ticket";
	private static final String SHANGHAI_URL= "http://sh.centuryticket.com/index.php/ticket";
	@SuppressWarnings("unused")
	private static final String JIANGSU_URL ="";
	
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", URL)
	.get(0).get("agency_id").toString();

	public void extract() {
		Set<String> set = new HashSet<String>();
		//添加北京票务中心地址
		set.add(BEIJING_URL);
		//添加上海票务中心地址
		set.add(SHANGHAI_URL);
		Map<String,Integer> map =new HashMap<String,Integer>();
		map = getURL(set);
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url, map.get(url));
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url,map.get(url)), e);
			}
			
		}
		
		/*try {
			parseEach("", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
	}
	public Map<String,Integer> getURL(Set<String> set){
		Map<String,Integer> map = new HashMap<String,Integer>();
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			Document document =getDoc(url);
			if(document==null){
				continue;
			}
			Elements elements =document.select("table.new-itcket-list");
			for (Element element : elements) {
				String show = element.select("tr th").first().text();
				for (Element element2 : element.select("tr.one,tr.two")) {
					String url1 = element2.select("div a").first().attr("abs:href");
					if("演唱会".equals(show)){
						map.put(url1, ShowType.CONCERT);
					}else if("音乐会".equals(show)){
						map.put(url1, ShowType.SHOW);
					}else if("歌剧".equals(show)||"话剧".equals(show)||"音乐剧".equals(show)){
						map.put(url1, ShowType.DRAMA);
					}else if("芭蕾舞".equals(show)||"舞蹈".equals(show)){
						map.put(url1, ShowType.DANCE);
					}else if("儿童亲子".equals(show)){
						map.put(url1,ShowType.CHILDREN);
					}else if("相声/小品".equals(show)||"二人转/戏曲".equals(show)||" 魔术/杂技/马戏".equals(show)){
						map.put(url1, ShowType.OPERA);
					}else if("综艺晚会".equals(show)){
						map.put(url1, ShowType.OTHER);
					}else if("体育".equals(show)){
						map.put(url1, ShowType.SPORTS);
					}else if("旅游景点通票".equals(show)||"会议展览".equals(show)||"娱乐休闲".equals(show)||"电影通票".equals(show)){
						map.put(url1,ShowType.HOLIDAY);
					}
				}
			}
		}
		return map;
	}
	public void parseEach(String url,int showType) throws Exception{
			Show show = new Show();
			Document document = getDoc(url);
			//宣传海报地址
			show.setImage_path(document.select("img.border2").first().attr("abs:src"));
			//演出名称
			show.setName(document.select("div.info h2").first().text());
			//演出介绍
			show.setIntroduction(PubFun.cleanElement(document.select("div.m-2")).html());
			//演出类型
			show.setType(showType);
			//售票状态
			TextNode tx = (TextNode) document.select("ul.item li[style=border-top:#CCCCCC solid 1px;border-bottom:#CCCCCC solid 1px;padding:8px 0px]").first().childNode(2);
			if("已出票".equals(tx.text())){
				 show.setStatus(1);
			}else if("预定".equals(tx.text())){
			    show.setStatus(0);
			}  
			Elements elements = document.select("table.listT-3");
			Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
			List<TicketPrice> list = null ;
			TicketPrice ticketPrice = null ;
			for(Element element:elements){
				Elements eles = element.select("tr.one,tr.two");
				for(Element ele1 : eles){
					list = new ArrayList<TicketPrice>();
					//演出时间
					String showTime = ele1.select("a.underline").first().text();
					if(showTime.contains("待定")){
						continue;
					}
					if(showTime.indexOf("星期")!=-1){
						showTime = showTime.replaceAll("[*\\s星期(一|二|三|四|五|六|日)]", "");
					}
					showTime = showTime.replaceAll("\\.", "-");
					showTime = showTime.replaceAll("  +", " ");
					//首先判断时间格式是否正常，如果出现异常，则说明该票的时间属于特殊情况，将相关的时间信息作为票价的备注存在数据库中
					String remk = "";
					try {
						showTime = newDate.format(newDate.parse(showTime));
					} catch (ParseException e) {
					} 
					//代理商ID
					show.setAgent_id(agentID);
					//演出场馆
					show.setSiteName(ele1.select("div a").text());
					for(Element ele4 : ele1.select("span")){
						ticketPrice = new TicketPrice();
						ticketPrice.setMainURL(url);
						if(ele4.select("a").size()!=0){
							//票价
							String prices = ele4.select("a").text();
							String price = PubFun.getRegex("((^\\d+\\.+\\d+)|(^\\d+)).*", prices, 1);
							String remark = prices.replaceAll(price, "");
							remark = remark.replaceAll("[()（）]","");
							if("待定".equals(price)){
								continue;
							}
							ticketPrice.setPrice(price);
							ticketPrice.setRemark(remark+"    "+remk);
							//是否有票
							ticketPrice.setExist(true);
							//票价详细链接
							ticketPrice.setDetailURL(ele4.select("a").first().attr("abs:href"));
							list.add(ticketPrice);
						}else if(ele4.select(".gray").size()!=0){
							//票价
							ticketPrice.setPrice(ele4.text());
							String prices = ele4.text();
							String price = PubFun.getRegex("((^\\d+\\.+\\d+)|(^\\d+)).*", prices, 1);
							String remark = prices.replaceAll(price, "");
							remark = remark.replaceAll("[()（）]","");
						    //票价
							if("待定".equals(price)){
								continue;
							}
							ticketPrice.setPrice(price);
							//备注
							ticketPrice.setRemark(remark+"    "+remk);
							//是否有票
							ticketPrice.setExist(false);
							list.add(ticketPrice);
						}
					}
					if(list.isEmpty()||list.size()==0){
						continue;
					}
					timeAndPrice.put(showTime, list);
				}
			}
			show.setTimeAndPrice(timeAndPrice);
			getDao().saveShow(show);
	}
	public static void main(String[] args){
		CenturyticketSpider centuryticket = new CenturyticketSpider();
		centuryticket.setDao(new DataDao());
		centuryticket.extract();
		
	}

}
