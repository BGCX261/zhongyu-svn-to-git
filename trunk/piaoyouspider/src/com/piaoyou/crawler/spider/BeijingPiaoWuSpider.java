package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
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

public class BeijingPiaoWuSpider extends TicketSpider implements SpiderTask {
    private static Log log=LogFactory.getLog(BeijingPiaoWuSpider.class);
    private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	@Override
	public void extract() {
		  Document doc=getDoc("http://www.zgpww.com.cn/all.asp");
	      Iterator<Element> trEleList=doc.select("body>table.table>tr:gt(1)").iterator();
	      while(trEleList.hasNext()){
	    	  Element eleTr=trEleList.next();
	    	  String sort=eleTr.select("td>table>tr:eq(2)>td>table>tr>td.txt14").first().text().trim();
	    	  int sortType=ShowType.OTHER;
	  		  if(sort.contains("梅兰芳大剧院演出季")){
	  			 Iterator<Element> aEleList=eleTr.select("td>table>tr:eq(2)>td>table>tr>td[width=947]>table>tr:gt(0)>td:eq(0) a").iterator();
		    	  while(aEleList.hasNext()){
		    		  Element eleA=aEleList.next();
		    		  String href=eleA.absUrl("href").trim();
		    		  String name=eleA.text().trim();
		    		  sortType = getChildType(sortType, name);
		    		  String site=eleA.parent().nextElementSibling().nextElementSibling().select("a").text();
		    		  try{
		    			  processLastPage(href,sortType,site);
		    		  }catch(Exception ee){
		    			  log.error(Const.concat(href,sortType,site),ee);
		    		  }
	  				}
	  				continue;
	  			}
	  		  else  if(sort.contains("国家大剧院戏剧场")){
	  			 Iterator<Element> aEleList=eleTr.select("td>table>tr:eq(2)>td>table>tr>td[width=947]>table>tr:gt(0)>td:eq(0) a").iterator();
		    	  while(aEleList.hasNext()){
		    		  Element eleA=aEleList.next();
		    		  String href=eleA.absUrl("href").trim();
		    		  String name=eleA.text().trim();
		    		  sortType = getChildType(sortType, name);
		    		  String site=eleA.parent().nextElementSibling().nextElementSibling().select("a").text();
		    		  try{
		    			  processLastPage(href,sortType,site);
		    		  }catch(Exception ee){
		    			  log.error(Const.concat(href,sortType,site),ee);
		    		  }
	  			}
	  			continue;
	  		}
	  		else if(sort.contains("国家大剧院小剧场")){
	  			 Iterator<Element> aEleList=eleTr.select("td>table>tr:eq(2)>td>table>tr>td[width=947]>table>tr:gt(0)>td:eq(0) a").iterator();
		    	  while(aEleList.hasNext()){
		    		  Element eleA=aEleList.next();
		    		  String href=eleA.absUrl("href").trim();
		    		  String name=eleA.text().trim();
		    		  sortType = getChildType(sortType, name);
		    		  String site=eleA.parent().nextElementSibling().nextElementSibling().select("a").text();
		    		  try{
		    			  processLastPage(href,sortType,site);
		    		  }catch(Exception ee){
		    			  log.error(Const.concat(href,sortType,site),ee);
		    		  }
	  			}
	  			continue;
	  		}
	  		else if(sort.contains("国家大剧院音乐厅")){
	  			 Iterator<Element> aEleList=eleTr.select("td>table>tr:eq(2)>td>table>tr>td[width=947]>table>tr:gt(0)>td:eq(0) a").iterator();
		    	  while(aEleList.hasNext()){
		    		  Element eleA=aEleList.next();
		    		  String href=eleA.absUrl("href").trim();
		    		  String name=eleA.text().trim();
		    		  sortType = getChildType(sortType, name);
		    		  String site=eleA.parent().nextElementSibling().nextElementSibling().select("a").text();
		    		  try{
		    			  processLastPage(href,sortType,site);
		    		  }catch(Exception ee){
		    			  log.error(Const.concat(href,sortType,site),ee);
		    		  }
	  			}
	  			continue;
	  		}
	  		else if(sort.contains("国家大剧院歌剧院")){
	  			 Iterator<Element> aEleList=eleTr.select("td>table>tr:eq(2)>td>table>tr>td[width=947]>table>tr:gt(0)>td:eq(0) a").iterator();
		    	  while(aEleList.hasNext()){
		    		  Element eleA=aEleList.next();
		    		  String href=eleA.absUrl("href").trim();
		    		  String name=eleA.text().trim();
		    		  sortType = getChildType(sortType, name);
		    		  String site=eleA.parent().nextElementSibling().nextElementSibling().select("a").text();
		    		  try{
		    			  processLastPage(href,sortType,site);
		    		  }catch(Exception ee){
		    			  log.error(Const.concat(href,sortType,site),ee);
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
	    	  Iterator<Element> aEleList=eleTr.select("td>table>tr:eq(2)>td>table>tr>td[width=947]>table>tr:gt(0)>td:eq(0) a").iterator();
	    	  while(aEleList.hasNext()){
	    		  Element eleA=aEleList.next();
	    		  String site=eleA.parent().nextElementSibling().nextElementSibling().select("a").text();
	    		  String href=eleA.absUrl("href").trim();
	    		  try{
	    			  processLastPage(href,sortType,site);
	    		  }catch(Exception ee){
	    			  log.error(Const.concat(href,sortType,site),ee);
	    		  }
	    	  }
	      }
	}
	private int getChildType(int sortType, String name) {
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
			else if(name.contains("歌剧")||name.contains("话剧")||name.contains("音乐剧")){
				sortType=ShowType.DRAMA;
			}
		return sortType;
	}
	/**
	 * @param args
	 */
	
	public void processLastPage(String url,int sortType,String site){
		Document doc=getDoc(url);
		if(site==null||"".equals(site)||site.contains("待定"))
			return;
		Element tableEle=doc.select("body>table[width=968]>tr:eq(1)>td:eq(0)>table").first();
		String imagePath=tableEle.select("tr:eq(0)>td.line>table>tr>td>img").first().absUrl("src").trim();
		String showName=tableEle.select("tr:eq(0)>td.line>table>tr>td[valign=top]>table>tr:eq(0)>td").first().text().trim();
		String summary=PubFun.cleanElement(tableEle.select("td.table>table.table>tr>td").first()).text().trim();
		Element eleTime=tableEle.select("tr:eq(2)>td.line>table>tr:eq(1)>td>table").first();
		Map<String,List<TicketPrice>> map=getMap(eleTime,url);
		Show show=new Show();
		show.setImage_path(imagePath);
		show.setSiteName(site);
		show.setIntroduction(summary);
		show.setType(sortType);
		show.setAgent_id("68");
		show.setName(showName);
		show.setTimeAndPrice(map);
		try{
			getDao().saveShow(show);
		}catch(Exception ee){
			ee.printStackTrace();
		}
	}
	public Map<String,List<TicketPrice>> getMap(Element doc,String url){
		 Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		 Iterator<Element> eleTrList=doc.select("tr:gt(0)").iterator();
		 while(eleTrList.hasNext()){
			 Element eleTr=eleTrList.next();
			 String showTime=eleTr.select("td:eq(1)").first().text().trim();
			 if("".equals(showTime)||showTime.contains("待定"))
				 continue;
			 //2011-8-9 ：19:30:00
			 showTime=showTime.replace(" ：", " ");
			 String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm:ss"), "yyyy-MM-dd HH:mm");
			 if(newshowTime!=null){
				showTime=newshowTime;
			 }
			 showTime=showTime.replace("：", ":");
			 showTime=showTime.replace(" ", " ");
			 try {
				 showTime = newDate.format(oldDate.parse(showTime));
			 } catch (Exception e1) {
					//TODO	时间暂时不处理
			 }
			 List<TicketPrice> list=new ArrayList<TicketPrice>();
			 Iterator<Element> yesPriceList=eleTr.select("td:eq(3)>a").iterator();
			 while(yesPriceList.hasNext()){
					Element elePrice=yesPriceList.next();
	    			TicketPrice ticket=new TicketPrice();
	    			String price=elePrice.text().trim();
	    			if("".equals(price)||price.contains("待定")||price.contains("北京管乐交响乐团音乐会"))
	    				continue;
	    			ticket.setPrice(price);
	    			if(price.matches("([\\d]+)([^\\d]+)")){
	    				String newPrice=PubFun.getRegex("([\\d]+)([^\\d]+)", price, 1);
	    				String remark=PubFun.getRegex("([\\d]+)([^\\d]+)", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			else if(price.matches("([^\\d]+)([\\d]+)")){
	    				String newPrice=PubFun.getRegex("([^\\d]+)([\\d]+)", price, 2);
	    				String remark=PubFun.getRegex("([^\\d]+)([\\d]+)", price, 1);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			else if(price.matches("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*")){
	    				String newPrice=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 1);
	    				newPrice=newPrice.replace("套票", "").replace("套", "");
	    				String remark=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			ticket.setDetailURL(elePrice.absUrl("href").trim());
	    			ticket.setExist(true);
	    			ticket.setMainURL(url);
	    			list.add(ticket);
			 }
			 Iterator<Element> noPriceList=eleTr.select("td:eq(3)>font").iterator();
             while(noPriceList.hasNext()){
            		Element elePrice=noPriceList.next();
	    			TicketPrice ticket=new TicketPrice();
	    			String price=elePrice.text().trim();
	    			if("".equals(price)||price.contains("待定")||price.contains("北京管乐交响乐团音乐会"))
	    				continue;
	    			ticket.setPrice(price);
	    			if(price.matches("([\\d]+)([^\\d]+)")){
	    				String newPrice=PubFun.getRegex("([\\d]+)([^\\d]+)", price, 1);
	    				String remark=PubFun.getRegex("([\\d]+)([^\\d]+)", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			else if(price.matches("([^\\d]+)([\\d]+)")){
	    				String newPrice=PubFun.getRegex("([^\\d]+)([\\d]+)", price, 2);
	    				String remark=PubFun.getRegex("([^\\d]+)([\\d]+)", price, 1);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			else if(price.matches("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*")){
	    				String newPrice=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 1);
	    				newPrice=newPrice.replace("套票", "").replace("套", "");
	    				String remark=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 2);
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
	public static void main(String[] args) {
		BeijingPiaoWuSpider spider=new BeijingPiaoWuSpider();
	    spider.setDao(new DataDao());
		spider.extract();
		//spider.processLastPage("http://www.zgpww.com.cn/piao.asp?id=1691", 1, "aa");
	}

}
