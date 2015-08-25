package com.piaoyou.crawler.spider;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
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
public class PiaoWuBeijingSpider extends TicketSpider implements SpiderTask {
    private static Log log=LogFactory.getLog(PiaoWuBeijingSpider.class);
	public void extract() {
		Document doc=getDoc("http://www.piaobeijing.com/All.Html");
		Iterator<Element> divEleList=doc.select("#Body_Right2>div.new_fenlei_nav").iterator();
		while(divEleList.hasNext()){
			Element eleDiv=divEleList.next();
			Element conDiv=eleDiv.nextElementSibling();
			String sort=eleDiv.select("span").first().text().trim().replace("[", "").replace("]", "");
		 	int sortType=ShowType.OTHER;
		    if(sort.contains("国家大剧院戏剧场")){
				Iterator<Element> listEleDiv=conDiv.select("ul[class^=new_fenlei_cont]").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("li:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("li:eq(0) a").first().text();
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
					   processLastPage(href,sortType);
					}
	        		catch(Exception ee){
	        			log.error(Const.concat(href,sortType),ee);
	        		}
				}
				continue;
			}
			else if(sort.contains("国家大剧院小剧场")){
				Iterator<Element> listEleDiv=conDiv.select("ul[class^=new_fenlei_cont]").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("li:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("li:eq(0) a").first().text();
					
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")||name.contains("戏剧")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						   processLastPage(href,sortType);
						}
		        		catch(Exception ee){
		        			log.error(Const.concat(href,sortType),ee);
		        		}
				}
				continue;
			}
			else if(sort.contains("国家大剧院音乐厅")){
				Iterator<Element> listEleDiv=conDiv.select("ul[class^=new_fenlei_cont]").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("li:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("li:eq(0) a").first().text();
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						   processLastPage(href,sortType);
						}
		        		catch(Exception ee){
		        			log.error(Const.concat(href,sortType),ee);
		        		}
				}
				continue;
			}
			else if(sort.contains("国家大剧院歌剧院")){
				Iterator<Element> listEleDiv=conDiv.select("ul[class^=new_fenlei_cont]").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("li:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("li:eq(0) a").first().text();
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						   processLastPage(href,sortType);
					}
		        	catch(Exception ee){
		        			log.error(Const.concat(href,sortType),ee);
		        	}
				}
				continue;
			}
			else if(sort.contains("打开艺术之门")){
				Iterator<Element> listEleDiv=conDiv.select("ul[class^=new_fenlei_cont]").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("li:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("li:eq(0) a").first().text();
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						   processLastPage(href,sortType);
					}
		            catch(Exception ee){
		        			log.error(Const.concat(href,sortType),ee);
		        	}
				}
				continue;
			}
			else if(sort.contains("打开音乐之门")){
				Iterator<Element> listEleDiv=conDiv.select("ul[class^=new_fenlei_cont]").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("li:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("li:eq(0) a").first().text();
					if(name.contains("音乐会")){
	        			sortType=ShowType.SHOW;
	        		}
	        		if(name.contains("芭蕾")||name.contains("舞")){
	        			sortType=ShowType.DANCE;
	        		}
	        		if(name.contains("京剧")||name.contains("戏")){
	        			sortType=ShowType.OPERA;
	        		}
	        		else if(name.contains("演唱会")){
	        			sortType=ShowType.CONCERT;
	        		}
	        		else if(name.contains("歌剧")||name.contains("话剧")||name.contains("剧")){
	        			sortType=ShowType.DRAMA;
	        		}
	        		try{
						   processLastPage(href,sortType);
					}
		        		catch(Exception ee){
		        			log.error(Const.concat(href,sortType),ee);
		        	}
				}
				continue;
			}
			else if(sort.contains("演唱")){
				    sortType=ShowType.CONCERT;
			}
			else if(sort.contains("音乐")&&!sort.contains("剧")){
					sortType=ShowType.SHOW;
		    }
			else if(sort.contains("话剧") || sort.contains("歌剧")||sort.contains("音乐剧")){
					sortType=ShowType.DRAMA;
			}
			else if(sort.contains("芭蕾") || sort.contains("舞蹈")||sort.contains("舞")){
					sortType=ShowType.DANCE;
			}
		    else if(sort.contains("戏剧")||sort.contains("京剧")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
				    sortType=ShowType.OPERA;
			}
			else if(sort.contains("儿童")){
				 sortType=ShowType.CHILDREN;
			}
			else if(sort.contains("体育") ){
				 sortType=ShowType.SPORTS;
			}
			else if(sort.contains("休闲")||sort.contains("旅游")|| sort.contains("电影")||sort.contains("大片")){
				 sortType=ShowType.HOLIDAY;
			}
		    Iterator<Element> listEleDiv=conDiv.select("ul[class^=new_fenlei_cont]").iterator();
			while(listEleDiv.hasNext()){
				Element childeleDiv=listEleDiv.next();
				String href=childeleDiv.select("li:eq(0) a").first().absUrl("href").trim();
				try{
					   processLastPage(href,sortType);
				}
	        	catch(Exception ee){
	        		log.error(Const.concat(href,sortType),ee);
	        	}
			}

	   }
    }
	
	public void processLastPage(String url,int sort){
	    Document doc=getDoc(url);
	    Element eleImage=doc.select(".PiaoShow_Limg img").first();
	    if(eleImage==null){
	    	log.error(url+"有错误");
	    	return;
	    }
	    String imagePath=doc.select(".PiaoShow_Limg img").first().absUrl("src").trim();
	    String showName=doc.select(".PiaoShow_Rtxt>div:eq(0)").first().text().trim();
	    String showSite=doc.select(".PiaoShow_Rtxt>div:eq(2)").first().text().replace("[乘车路线]", "").replace("演出场馆：", "").replace("演出场馆", "").replace("：", "");
	    String summary=PubFun.cleanElement(doc.select(".B_R_C").first()).html();
	    Map<String,List<TicketPrice>>  map=getMap(doc,url);
	    Show show=new Show();
	    show.setAgent_id("47");
	    show.setImage_path(imagePath);
	    show.setSiteName(showSite);
	    show.setName(showName);
	    show.setIntroduction(summary);
	    show.setType(sort);
	    show.setWatermark(true);
	    show.setTimeAndPrice(map);
	    try{
	      getDao().saveShow(show);
	    }catch(Exception ee){
	    	
	    }
   }
   public Map<String,List<TicketPrice>> getMap(Document doc,String url){
	    	Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
	    	Iterator<Element> trList=doc.select("#Body_Right2>table>tr:gt(0)").iterator();
	    	while(trList.hasNext()){
	    		Element eleTr=trList.next();
	    		Element eleDate=eleTr.select("td:eq(0)").first();
	    		if(eleDate==null)
	    			continue;
	    		String date=eleTr.select("td:eq(0)").first().text();
	    		if("".equals(date))
	    			continue;
	    		String time=eleTr.select("td:eq(1)").first().text();
	    		String showTime=date+time;
	    		showTime=showTime.replaceAll("星期[^$]{1}", "");
	    		showTime=showTime.replaceAll(" ", "");
	    		showTime=showTime.replace("：", ":");
    			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
    			if(newshowTime!=null){
    				showTime=newshowTime;
    			}
	    		Iterator<Element> YespirceList=eleTr.select("td:eq(3)>a.A_S_2").iterator();
	    		List<TicketPrice> list=new ArrayList<TicketPrice>();
	    		while(YespirceList.hasNext()){
	    			Element elePrice=YespirceList.next();
	    			TicketPrice ticket=new TicketPrice();
	    			String price=elePrice.text().trim();
	    			if("".equals(price)||price.contains("待定")||price.contains("套票"))
	    				continue;
	    			if(!elePrice.attr("title").trim().equals("")){
	    				String remark=elePrice.attr("title").trim();
	    				if(remark.matches("[^\\(]*\\(([^\\)]+)\\)[^\\)]*"))
	    					remark=PubFun.getRegex("[^\\(]*\\(([^\\)]+)\\)[^\\)]*", remark, 1);
	    				remark=remark.replace("(", "").replace(")", "").replace("（", "").replace("）", "");
	    				ticket.setRemark(remark);
	    			}
	    			ticket.setDetailURL(url);
	    			ticket.setExist(true);
	    			ticket.setMainURL(url);
	    			ticket.setPrice(price);
	    			list.add(ticket);
	    		}
	    		Iterator<Element> nOpirceList=eleTr.select("td:eq(3)>span").iterator();
	    		while(nOpirceList.hasNext()){
	    			Element elePrice=nOpirceList.next();
	    			TicketPrice ticket=new TicketPrice();
	    			String price=elePrice.text().trim();
	    			if("".equals(price)||price.contains("待定")||price.contains("套票")||price.equalsIgnoreCase("tp"))
	    				continue;
	    			ticket.setPrice(price);
	    			if(price.matches("([\\d]+):[\\(（]([^\\)）]+)[\\)）]")){
	    				String newprice=PubFun.getRegex("([\\d]+):[\\(（]([^\\)）]+)[\\)）]", price, 1);
	    				String remark=PubFun.getRegex("([\\d]+):[\\(（]([^\\)）]+)[\\)）]", price, 2);
	    				remark=remark.replace("(", "").replace(")", "").replace("（", "").replace("）", "");
	    				ticket.setPrice(newprice);
	    				ticket.setRemark(remark);
	    			}//1000:（680X2）
	    			ticket.setExist(false);
	    			ticket.setMainURL(url);
	    			list.add(ticket);
	    		}
	    		map.put(showTime, list);
	    	}
	    	return map;
   }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		PiaoWuBeijingSpider spider=new PiaoWuBeijingSpider();
		spider.setDao(new DataDao());
		spider.extract();
		//spider.processLastPage("http://www.piaobeijing.com/YanChuPiao/3740.Html", 1);
	}

}
