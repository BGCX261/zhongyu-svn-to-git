package com.piaoyou.crawler.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
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
public class ZongYiPiaoWuSpider extends TicketSpider implements SpiderTask {
	private static Log log=LogFactory.getLog(ZongYiPiaoWuSpider.class);
	private final static String BASE_URL = "http://www.zypw.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
		.get(0).get("agency_id").toString();
	@Override
	public void extract() {
		Document doc=getDoc("http://www.zypw.com/allperforms.aspx");
		Iterator<Element> listDiv=doc.select("div[class=more_list]:not(#tag21)").iterator();
		while(listDiv.hasNext()){
			Element divEle=listDiv.next();
			String ajax="http://www.zypw.com"+PubFun.getRegex("\\.load\\('([^']*)'\\)", divEle.html(), 1);
			try{
				Document ajaxDoc = Jsoup.connect(ajax).timeout(60*1000).get();
			    String sort=ajaxDoc.select("body>table>tr:eq(0) td").first().text().trim().replace(" ", "");
			    int sortType=ShowType.OTHER;
				if(sort.contains("演唱")){
					    sortType=ShowType.CONCERT;
				}
				else if(sort.contains("音乐")&&!sort.contains("剧")||sort.contains("壹空间")){
						sortType=ShowType.SHOW;
			    }
				else if(sort.contains("话剧") || sort.contains("歌剧")||sort.contains("音乐剧")){
						sortType=ShowType.DRAMA;
				}
				else if(sort.contains("芭蕾") || sort.contains("舞蹈")||sort.contains("舞")){
						sortType=ShowType.DANCE;
				}
			    else if(sort.contains("戏剧")||sort.contains("朝阳")||sort.contains("梅兰芳")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
					    sortType=ShowType.OPERA;
				}
				else if(sort.contains("儿童")){
					 sortType=ShowType.CHILDREN;
				}
				else if(sort.contains("体育") ){
					 sortType=ShowType.SPORTS;
				}
				else if(sort.contains("休闲")||sort.contains("旅游")|| sort.contains("电影")){
					 sortType=ShowType.HOLIDAY;
				}
				else if(sort.contains("国家大剧院")){//单独处理
					Iterator<Element> listA=ajaxDoc.select("td.con_td03>table a").iterator();
					while(listA.hasNext()){
						Element eleA=listA.next();
						String name=eleA.text().trim();
						if(name.contains("音乐会")){
		        			sortType=ShowType.SHOW;
		        		}
		        		if(name.contains("芭蕾")||name.contains("舞")){
		        			sortType=ShowType.DANCE;
		        		}
		        		if(name.contains("京剧")){
		        			sortType=ShowType.OPERA;
		        		}
		        		else if(name.contains("演唱会")){
		        			sortType=ShowType.CONCERT;
		        		}
		        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("音乐剧")){
		        			sortType=ShowType.DRAMA;
		        		}
						String href=eleA.absUrl("href").trim();
						try{
					    	processConcentratePage(href,sortType);
						}catch(Exception ee){
							log.error(Const.concat(href,sortType),ee);
						}
					}
					continue;
				}
				else if(sort.contains("艺术")){//单独处理
					Iterator<Element> listA=ajaxDoc.select("td.con_td03>table a").iterator();
					while(listA.hasNext()){
						Element eleA=listA.next();
						String name=eleA.text().trim();
						if(name.contains("音乐会")){
		        			sortType=ShowType.SHOW;
		        		}
		        		if(name.contains("芭蕾")||name.contains("舞")){
		        			sortType=ShowType.DANCE;
		        		}
		        		if(name.contains("京剧")){
		        			sortType=ShowType.OPERA;
		        		}
		        		else if(name.contains("演唱会")){
		        			sortType=ShowType.CONCERT;
		        		}
		        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("音乐剧")){
		        			sortType=ShowType.DRAMA;
		        		}
						String href=eleA.absUrl("href").trim();
						try{
					    	processConcentratePage(href,sortType);
						}catch(Exception ee){
							log.error(Const.concat(href,sortType),ee);
						}
					}
					continue;
				}
				Iterator<Element> listA=ajaxDoc.select("td.con_td03>table a").iterator();
				while(listA.hasNext()){
					Element eleA=listA.next();
					String href=eleA.absUrl("href").trim();
					try{
				    	processConcentratePage(href,sortType);
					}catch(Exception ee){
						log.error(Const.concat(href,sortType),ee);
					}
				}
			}catch(Exception ee){
				
			}
		}
	}
	public void processConcentratePage(String url,int sort){
		url=url.replace("api/", "");
		Document doc=getDoc(url);
		String imagePath=doc.select("#PhotoDescription img").first().absUrl("src").trim();
		String showName=doc.select("#ShowInfo>h2").first().text().trim();
		Element eleSite=doc.select("#ShowInfo>div>dl>dd>a").first();
		if(null==eleSite||"".equals(eleSite.text().trim()))
			return;
		String site=eleSite.text().trim();
		if(site.contains("待定"))
			return;
		String summary=PubFun.cleanElement(doc.select(".zd_xiangxi dd").first()).html().trim();
		Map<String,List<TicketPrice>> map=getMap(doc,url);
		Show show=new Show();
		show.setAgent_id(agentID);
		show.setType(sort);
		show.setImage_path(imagePath);
		show.setName(showName);
		show.setIntroduction(summary);
		show.setSiteName(site);
		show.setTimeAndPrice(map);
		try{
		   getDao().saveShow(show);
		}catch(Exception ee){
			
		}
    }
	
	public Map<String,List<TicketPrice>> getMap(Document doc,String url){
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Iterator<Element> ulEleList=doc.select(".zd_dingpiao ul:gt(1)").iterator();
		while(ulEleList.hasNext()){
			Element ulEle=ulEleList.next();
			String showTime=ulEle.select("li.zd12").first().text().trim();
			if(showTime.matches("([^$]*)星期[^$]*")){
				showTime=showTime.replace(".", "-");
				showTime=PubFun.getRegex("([^$]*)星期[^$]*", showTime, 1).trim();
				showTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
			}
			Iterator<Element> priceEleList=ulEle.select("li.zd13 a").iterator();
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			while(priceEleList.hasNext()){
				Element priceEle=priceEleList.next();
				boolean isExists=false;
				String price=null;
				String remark=null;
				TicketPrice ticket=new TicketPrice();
				if("de054".equals(priceEle.attr("class").trim())){
					price=priceEle.text().trim();
					remark="无票";
				}
				else if("de053".equals(priceEle.attr("class").trim())){
					price=priceEle.text().trim();
					remark="有票";
					isExists=true;
					ticket.setDetailURL(url);
				}
				else if("de051".equals(priceEle.attr("class").trim())){
					price=priceEle.text().trim();
					remark="vip";
					isExists=true;
					ticket.setDetailURL(url);
				}
				else if("de052".equals(priceEle.attr("class").trim())){
					price=priceEle.text().trim();
					String tip=priceEle.attr("tip").trim();
					remark=tip;
					isExists=true;
					ticket.setDetailURL(url);
				}
				if(price.contains("待定"))
					continue;
				ticket.setPrice(price);
				ticket.setRemark(remark);
				if(price.matches("([^\\d]+)([\\d]+)")){
    				String newPrice=PubFun.getRegex("([^\\d]+)([\\d]+)", price, 2);
    				remark=PubFun.getRegex("([^\\d]+)([\\d]+)", price, 1);
    				ticket.setPrice(newPrice);
    				ticket.setRemark(remark);
    			}
				ticket.setExist(isExists);
				ticket.setMainURL(url);
				list.add(ticket);
			}
			map.put(showTime, list);
		}
		return map;
	} 
	public static void main(String[] args) {
		ZongYiPiaoWuSpider Spider = new ZongYiPiaoWuSpider();
		Spider.setDao(new DataDao());
		Spider.extract();
	}
}
