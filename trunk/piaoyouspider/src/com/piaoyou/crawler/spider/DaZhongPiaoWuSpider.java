package com.piaoyou.crawler.spider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.management.RuntimeErrorException;

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

public class DaZhongPiaoWuSpider extends TicketSpider implements SpiderTask {
	private static Log log=LogFactory.getLog(DaZhongPiaoWuSpider.class);
	@Override
	public void extract() {
		Document doc=getDoc("http://www.iticket.com.cn/allticket.html");
		if(doc==null){
			throw new RuntimeException();
		}
		Iterator<Element> divEleList=doc.select("#zhu>div.zhutitle").iterator();
		while(divEleList.hasNext()){
			Element eleDiv=divEleList.next();
			Element conDiv=eleDiv.nextElementSibling();
			String sort=eleDiv.select("div.zhutit").first().text().trim();
			int sortType=ShowType.OTHER;
			if(sort.contains("梅兰芳大剧院")){
				Iterator<Element> listEleDiv=conDiv.select("div.zhu3-2>div:gt(0)").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("div a").first().absUrl("href").trim();
					String name=childeleDiv.select("div a").first().text();
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
					}catch(Exception e){
						log.error(Const.concat(href,sortType),e) ;
					}
				}
				continue;
			}
			else if(sort.contains("国家大剧院演出")){
				Iterator<Element> listEleDiv=conDiv.select("div.zhu3-2>div:gt(0)").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("div a").first().absUrl("href").trim();
					String name=childeleDiv.select("div a").first().text();
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
					}catch(Exception e){
						log.error(Const.concat(href,sortType),e);
					}
				}
				continue;
			}
			else if(sort.contains("国家大剧院小剧场")){
				Iterator<Element> listEleDiv=conDiv.select("div.zhu3-2>div:gt(0)").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("div a").first().absUrl("href").trim();
					String name=childeleDiv.select("div a").first().text();
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
					}catch(Exception e){
						log.error(Const.concat(href,sortType),e);
					}
				}
				continue;
			}
			else if(sort.contains("北京9剧场")){
				Iterator<Element> listEleDiv=conDiv.select("div.zhu3-2>div:gt(0)").iterator();
				while(listEleDiv.hasNext()){
					Element childeleDiv=listEleDiv.next();
					String href=childeleDiv.select("div a").first().absUrl("href").trim();
					String name=childeleDiv.select("div a").first().text();
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
					}catch(Exception e){
						log.error(Const.concat(href,sortType),e);
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
		    else if(sort.contains("戏剧")||sort.contains("长安大戏院")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
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
			Iterator<Element> listEleDiv=conDiv.select("div.zhu3-2>div:gt(0)").iterator();
			while(listEleDiv.hasNext()){
				Element childeleDiv=listEleDiv.next();
				String href=childeleDiv.select("div a").first().absUrl("href").trim();
				try{
					 processLastPage(href,sortType);
				}catch(Exception e){
					log.error(Const.concat(href,sortType),e) ;
				}
			}
		}
	}
    public void processLastPage(String url,int sort){
		Document doc=getDoc(url);
		String imagePath=doc.select(".box_img1 img").first().absUrl("src").trim();
    	String showName=doc.select(".box_font span").first().text().trim();
    	Element eleSite=doc.select("#class_one option").first();
    	String showSite=eleSite.text().trim();
    	if("".equals(showSite))
    		return;
    	String summary=PubFun.cleanElement(doc.select("#text1").first()).html().trim();
    	Map<String,List<TicketPrice>> map=getMap(doc,url);
    	Show show=new Show();
    	show.setAgent_id("46");
    	show.setImage_path(imagePath);
    	show.setIntroduction(summary);
    	show.setName(showName);
    	show.setSiteName(showSite);
    	show.setType(sort);
    	show.setTimeAndPrice(map);
    	try{
    		getDao().saveShow(show);
    	}catch(Exception ee){
    	}
    }
    public Map<String,List<TicketPrice>> getMap(Document doc,String url){
    	Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
    	Iterator<Element> trList=doc.select("div.left>div.box2>table>tr").iterator();
    	while(trList.hasNext()){
    		Element eleTr=trList.next();
    		String showTime=eleTr.select("td:eq(1)").first().text().trim();
    		if(showTime.matches("([^星]*)星期[^$]+")){
    			String tshowTime=PubFun.getRegex("([^星]*)星期[^$]+", showTime, 1);
    			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(tshowTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
    			if(newshowTime!=null)
    				showTime=newshowTime;
    		}
    		Iterator<Element> YespirceList=eleTr.select("td:eq(3)>a").iterator();
    		List<TicketPrice> list=new ArrayList<TicketPrice>();
    		while(YespirceList.hasNext()){
    			Element elePrice=YespirceList.next();
    			TicketPrice ticket=new TicketPrice();
    			String price=elePrice.text().trim();
    			ticket.setPrice(price);
    			if("".equals(price)||price.contains("待定"))
    				continue;
    			if(price.matches("([\\d]+人)([\\d]+)")){
    				String newPrice=PubFun.getRegex("([\\d]+人)([\\d]+)", price, 2);
    				String remark=PubFun.getRegex("([\\d]+人)([\\d]+)", price, 1);
    				ticket.setPrice(newPrice);
    				ticket.setRemark(remark);
    			}
    			if(price.matches("([^\\d]+)([\\d]+)")){
    				String newPrice=PubFun.getRegex("([\\d]+人)([\\d]+)", price, 2);
    				String remark=PubFun.getRegex("([\\d]+人)([\\d]+)", price, 1);
    				ticket.setPrice(newPrice);
    				ticket.setRemark(remark);
    			}
    			if(price.matches("([\\d]+)([^\\d]+)")){
    				String newPrice=PubFun.getRegex("([\\d]+)([^\\d]+)", price, 1);
    				String remark=PubFun.getRegex("([\\d]+)([^\\d]+)", price, 2);
    				ticket.setPrice(newPrice);
    				ticket.setRemark(remark);
    			}
    			ticket.setDetailURL(url);
    			ticket.setExist(true);
    			ticket.setMainURL(url);
    			list.add(ticket);
    		}
    		Iterator<Element> nOpirceList=eleTr.select("td:eq(3)>font").iterator();
    		while(nOpirceList.hasNext()){
    			Element elePrice=nOpirceList.next();
    			TicketPrice ticket=new TicketPrice();
    			String price=elePrice.text().trim();
    			ticket.setPrice(price);
    			if("".equals(price)||price.contains("待定"))
    				continue;
    			if(price.matches("([\\d]+人)([\\d]+)")){
    				String newPrice=PubFun.getRegex("([\\d]+人)([\\d]+)", price, 2);
    				String remark=PubFun.getRegex("([\\d]+人)([\\d]+)", price, 1);
    				ticket.setPrice(newPrice);
    				ticket.setRemark(remark);
    			}
    			if(price.matches("([^\\d]+)([\\d]+)")){
    				String newPrice=PubFun.getRegex("([\\d]+人)([\\d]+)", price, 2);
    				String remark=PubFun.getRegex("([\\d]+人)([\\d]+)", price, 1);
    				ticket.setPrice(newPrice);
    				ticket.setRemark(remark);
    			}
    			if(price.matches("([\\d]+)([^\\d]+)")){
    				String newPrice=PubFun.getRegex("([\\d]+)([^\\d]+)", price, 1);
    				String remark=PubFun.getRegex("([\\d]+)([^\\d]+)", price, 2);
    				ticket.setPrice(newPrice);
    				ticket.setRemark(remark);
    			}
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
		DaZhongPiaoWuSpider spider=new DaZhongPiaoWuSpider();
		spider.setDao(new DataDao());
		spider.extract();
		//spider.processLastPage("http://www.iticket.com.cn/Product/2094.shtml", 1);
	}

}
