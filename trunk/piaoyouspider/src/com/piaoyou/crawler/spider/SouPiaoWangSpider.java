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

public class SouPiaoWangSpider extends TicketSpider implements SpiderTask {
	private final static String BASE_URL = "http://sopiao.com.cn/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
		.get(0).get("agency_id").toString();
    private static Log log=LogFactory.getLog(SouPiaoWangSpider.class);
	@Override
	public void extract() {
	      Document doc=getDoc("http://sopiao.com.cn/allticket.asp");
	      List<Element> list=doc.select("body>table");
	      System.out.println("length:"+list.size());
	      Iterator<Element> listeleTable=doc.select("body>table").subList(2, 23).iterator();
	      while(listeleTable.hasNext()){
	    	  Element eleTable=listeleTable.next();	    	
	    	  String sort=eleTable.select("tr:eq(0) strong font").first().text();
	  		  int sortType=ShowType.OTHER;
			  if(sort.contains("朝阳九个剧场专区")){
				    Iterator<Element> listEleTr=eleTable.select("tr:eq(1) table tr:gt(0)").iterator();
					while(listEleTr.hasNext()){
						Element childeleDiv=listEleTr.next();
						if(null!=childeleDiv.select("td>div:contains(没有记录)").first())
							break;
						String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
						String name=childeleDiv.select("td:eq(0) a").first().text().trim();
						sortType = getType(sort, sortType, name);
						try{
					    	processLastPage(href,sortType);
						}catch(Exception ee){
							log.error(Const.concat(href,sortType),ee);
						}
					}
					continue;
				}
			  else if(sort.contains("梅兰芳大剧院演出季")){
				  Iterator<Element> listEleTr=eleTable.select("tr:eq(1) table tr:gt(0)").iterator();
					while(listEleTr.hasNext()){
						Element childeleDiv=listEleTr.next();
						if(null!=childeleDiv.select("td>div:contains(没有记录)").first())
							break;
						String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
						String name=childeleDiv.select("td:eq(0) a").first().text().trim();
						sortType = getType(sort, sortType, name);
						try{
					    	processLastPage(href,sortType);
						}catch(Exception ee){
							log.error(Const.concat(href,sortType),ee);
						}
					}
					continue;
				}
			  else if(sort.contains("国家大剧院五月音乐节")){
				    Iterator<Element> listEleTr=eleTable.select("tr:eq(1) table tr:gt(0)").iterator();
					while(listEleTr.hasNext()){
						Element childeleDiv=listEleTr.next();
						if(null!=childeleDiv.select("td>div:contains(没有记录)").first())
							break;
						String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
						String name=childeleDiv.select("td:eq(0) a").first().text().trim();
						sortType = getType(sort, sortType, name);
						try{
					    	processLastPage(href,sortType);
						}catch(Exception ee){
							log.error(Const.concat(href,sortType),ee);
						}
					}
					continue;
				}
			    else  if(sort.contains("国家大剧院戏剧场")){
			    	Iterator<Element> listEleTr=eleTable.select("tr:eq(1) table tr:gt(0)").iterator();
					while(listEleTr.hasNext()){
						Element childeleDiv=listEleTr.next();
						if(null!=childeleDiv.select("td>div:contains(没有记录)").first())
							break;
						String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
						String name=childeleDiv.select("td:eq(0) a").first().text().trim();
						sortType = getType(sort, sortType, name);
						try{
					    	processLastPage(href,sortType);
						}catch(Exception ee){
							log.error(Const.concat(href,sortType),ee);
						}
				}
				continue;
			}
			else if(sort.contains("国家大剧院小剧场")){
				Iterator<Element> listEleTr=eleTable.select("tr:eq(1) table tr:gt(0)").iterator();
				while(listEleTr.hasNext()){
					Element childeleDiv=listEleTr.next();
					if(null!=childeleDiv.select("td>div:contains(没有记录)").first())
						break;
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text().trim();
					sortType = getType(sort, sortType, name);
					try{
				    	processLastPage(href,sortType);
					}catch(Exception ee){
						log.error(Const.concat(href,sortType),ee);
					}
				}
				continue;
			}
			else if(sort.contains("国家大剧院音乐厅")){
				Iterator<Element> listEleTr=eleTable.select("tr:eq(1) table tr:gt(0)").iterator();
				while(listEleTr.hasNext()){
					Element childeleDiv=listEleTr.next();
					if(null!=childeleDiv.select("td>div:contains(没有记录)").first())
						break;
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text().trim();
					sortType = getType(sort, sortType, name);
					try{
				    	processLastPage(href,sortType);
					}catch(Exception ee){
						log.error(Const.concat(href,sortType),ee);
					}
				}
				continue;
			}
			else if(sort.contains("国家大剧院歌剧院")){
				Iterator<Element> listEleTr=eleTable.select("tr:eq(1) table tr:gt(0)").iterator();
				while(listEleTr.hasNext()){
					Element childeleDiv=listEleTr.next();
					if(null!=childeleDiv.select("td>div:contains(没有记录)").first())
						break;
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text().trim();
					sortType = getType(sort, sortType, name);
					try{
				    	processLastPage(href,sortType);
					}catch(Exception ee){
						log.error(Const.concat(href,sortType),ee);
					}
				}
				continue;
			}
			else if(sort.contains("国家大剧院歌剧节")){
				Iterator<Element> listEleTr=eleTable.select("tr:eq(1) table tr:gt(0)").iterator();
				while(listEleTr.hasNext()){
					Element childeleDiv=listEleTr.next();
					if(null!=childeleDiv.select("td>div:contains(没有记录)").first())
						break;
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text().trim();
					sortType = getType(sort, sortType, name);
					try{
				    	processLastPage(href,sortType);
					}catch(Exception ee){
						log.error(Const.concat(href,sortType),ee);
					}
				}
				continue;
			}
			else if(sort.contains("打开音乐之门")){
				Iterator<Element> listEleTr=eleTable.select("tr:eq(1) table tr:gt(0)").iterator();
				while(listEleTr.hasNext()){
					Element childeleDiv=listEleTr.next();
					if(null!=childeleDiv.select("td>div:contains(没有记录)").first())
						break;
					String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
					String name=childeleDiv.select("td:eq(0) a").first().text().trim();
					sortType = getType(sort, sortType, name);
					try{
				    	processLastPage(href,sortType);
					}catch(Exception ee){
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
		    else if(sort.contains("长安大戏院")||sort.contains("京剧")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
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
		    Iterator<Element> listEleTr=eleTable.select("tr:eq(1) table tr:gt(0)").iterator();
			while(listEleTr.hasNext()){
				Element childeleDiv=listEleTr.next();
				String href=childeleDiv.select("td:eq(0) a").first().absUrl("href").trim();
				try{
			    	processLastPage(href,sortType);
				}catch(Exception ee){
					log.error(Const.concat(href,sortType),ee);
				}
			}
	      }
	}
	public int getType(String sort, int sortType, String name) {
		if(name.contains("歌剧")||name.contains("话剧")||name.contains("音乐剧")){
			sortType=ShowType.DRAMA;
		}
		else if(name.contains("音乐会")){
			sortType=ShowType.SHOW;
		}
		else if(name.contains("芭蕾")||name.contains("舞")){
			sortType=ShowType.DANCE;
		}
		if(name.contains("京剧")||name.contains("戏")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
			sortType=ShowType.OPERA;
		}
		else if(name.contains("演唱会")){
			sortType=ShowType.CONCERT;
		}
		return sortType;
	}
	public void processLastPage(String url,int sort){
		  Document doc=getDoc(url);
		  //String imagePath=doc.select("div[align=center]>a[target=_blank]>img").first().absUrl("src").trim();
		  String showName=doc.select("div[align=center]>b>font").first().text().trim();
		  Element eleSite=doc.select("td.spheng>a[target=_blank]>u").first();
		  if(eleSite==null)
			  return;
		  String showSite=doc.select("td.spheng>a[target=_blank]>u").first().text().trim();
		  if(showSite.contains("待定"))
			  return;
		  String summary=PubFun.cleanElement(doc.select("td[bgcolor=FFF5EE]>div")).html().trim();
		  Map<String,List<TicketPrice>> map=getMap(doc,url);
		  Show show=new Show();
		  show.setAgent_id(agentID);
		  //show.setImage_path(imagePath);
		  show.setIntroduction(summary);
		  show.setName(showName);
		  show.setSiteName(showSite);
		  show.setTimeAndPrice(map);
		  show.setType(sort);
		  show.setWatermark(true);
		  try{
			  getDao().saveShow(show);
			  System.out.println(show);
		  }catch(Exception ee){
			  
		  }
	}
	public Map<String,List<TicketPrice>> getMap(Element doc,String url){
	    	Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
	    	Iterator<Element> trList=doc.select("body>table>tr>td[width=750]>table:eq(2)>tr:eq(2)>td>table[width=740]>tr:gt(0)").iterator();
	    	while(trList.hasNext()){
	    		Element eleTr=trList.next();
	    		Element eleShowTime=eleTr.select("td:eq(1)>div").first();
    		    if(eleShowTime==null)
	    			continue;
	    	    String showTime=eleShowTime.text().trim();
	    	    if(showTime.matches("([^星]*)星期[^$]+")){
	    			String tshowTime=PubFun.getRegex("([^星]*)星期[^$]+", showTime, 1).trim();
	    			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(tshowTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
	    			if(newshowTime!=null)
	    				showTime=newshowTime;
	    		}
			   	List<TicketPrice> list=new ArrayList<TicketPrice>();
			   	Iterator<Element> YespirceList=eleTr.select("td:eq(2)>div>a").iterator();
	    		while(YespirceList.hasNext()){
	    			Element elePrice=YespirceList.next();
	    			TicketPrice ticket=new TicketPrice();
	    			String price=elePrice.text().trim();
	    			if("".equals(price)||price.contains("待定")||price.contains("套票"))
	    				continue;
	    			ticket.setPrice(price);
	    			if(price.matches("([^\\(（{｛\\[]*)[\\(（{｛\\[]([^\\)）}｝\\[]*)[}｝）\\)\\]][^\\)）}｝\\]]*")){
	    				String newPrice=PubFun.getRegex("([^\\(（{｛\\[]*)[\\(（{｛\\[]([^\\)）}｝\\[]*)[}｝）\\)\\]][^\\)）}｝\\]]*", price, 1);
	    				String remark=PubFun.getRegex("([^\\(（{｛\\[]*)[\\(（{｛\\[]([^\\)）}｝\\[]*)[}｝）\\)\\]][^\\)）}｝\\]]*", price, 2);
	    				remark=remark.replace("}", "").replace("{", "").replace("｛", "").replace("｝", "");
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			if(price.matches("([\\d]+)[{]*([^\\d]+)[}]*")){//{VIP}
	    				String newPrice=PubFun.getRegex("([\\d]+)[{]*([^\\d]+)[}]*", price, 1);
	    				String remark=PubFun.getRegex("([\\d]+)[{]*([^\\d]+)[}]*", price, 2);
	    				remark=remark.replace("}", "").replace("{", "").replace("｛", "").replace("｝", "");
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			if(price.equalsIgnoreCase("999{380X3")){
	    				String newPrice=PubFun.getRegex("([\\d]+)\\{([^\\{]+)", price, 1);
	    				String remark=PubFun.getRegex("([\\d]+)\\{([^\\{]+)", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			String detailUrl=elePrice.absUrl("href").trim();
	    			ticket.setDetailURL(detailUrl);
	    			ticket.setExist(true);
	    			ticket.setMainURL(url);
	    			list.add(ticket);
	    		}
	    		Iterator<Element> nOpirceList=eleTr.select("td:eq(2)>div>font").iterator();
	    		while(nOpirceList.hasNext()){
	    			Element elePrice=nOpirceList.next();
	    			TicketPrice ticket=new TicketPrice();
	    			String price=elePrice.text().trim();
	    			if("".equals(price)||price.contains("待定")||price.contains("套票"))
	    				continue;
	    			ticket.setPrice(price);
	    			if(price.matches("([^\\(（\\{｛\\[]*)[\\(（\\{｛\\[]([^\\)）\\}｝\\[]*)[\\}｝）\\)\\]][^\\)）\\}｝\\]]*")){
	    				String newPrice=PubFun.getRegex("([^\\(（\\{｛\\[]*)[\\(（\\{｛\\[]([^\\)）\\}｝\\[]*)[\\}｝）\\)\\]][^\\)）\\}｝\\]]*", price, 1);
	    				String remark=PubFun.getRegex("([^\\(（\\{｛\\[]*)[\\(（\\{｛\\[]([^\\)）\\}｝\\[]*)[\\}｝）\\)\\]][^\\)）\\}｝\\]]*", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			if(price.matches("([\\d]+)[{]*([^\\d]+)[}]*")){//{VIP}
	    				String newPrice=PubFun.getRegex("([\\d]+)[{]*([^\\d]+)[}]*", price, 1);
	    				String remark=PubFun.getRegex("([\\d]+)[{]*([^\\d]+)[}]*", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			if(price.equalsIgnoreCase("999{380X3")){
	    				String newPrice=PubFun.getRegex("([\\d]+)\\{([^\\{]+)", price, 1);
	    				String remark=PubFun.getRegex("([\\d]+)\\{([^\\{]+)", price, 2);
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
		SouPiaoWangSpider spider=new SouPiaoWangSpider();
		spider.setDao(new DataDao());
		spider.extract();
		//spider.processLastPage("http://sopiao.com.cn/flist.asp?bid=409", 1);
	}

}
