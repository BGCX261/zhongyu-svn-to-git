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

public class AiPiaoWangSpider extends TicketSpider implements SpiderTask {
    private static Log log=LogFactory.getLog(AiPiaoWangSpider.class);
	@Override
	public void extract() {
		Document doc=getDoc("http://www.520piao.com/query.asp");
        Iterator<Element> tdEleList=doc.select("td.title14-b").iterator();
        while(tdEleList.hasNext()){
        	Element eleTd=tdEleList.next();
        	String sort=eleTd.text();
        	int sortType=9;
        	if(sort.contains("国家大剧院戏剧场")){
        		Element eleTable=eleTd.parent().parent().nextElementSibling();
            	Iterator<Element> trEleList=eleTable.select("tr:gt(0)").iterator();
            	while(trEleList.hasNext()){
	        		Element eleTr=trEleList.next();
	        		String href=eleTr.select("td:eq(0) a").first().absUrl("href").trim();
	        		String name=eleTr.select("td:eq(0) a").first().text().trim();
	        		String site=eleTr.select("td:eq(2)").first().text().trim();
	        		sortType = getChildType(sortType, name);
		    		 try{
		    			  processLastPage(href,sortType,site);
		    		  }catch(Exception ee){
		    			  log.error(Const.concat(href,sortType,site),ee);
		    		  }
 	  			}
 	  			continue;
 	  		}
 	  		else if(sort.contains("国家大剧院小剧场")){
 	  			Element eleTable=eleTd.parent().parent().nextElementSibling();
 	        	Iterator<Element> trEleList=eleTable.select("tr:gt(0)").iterator();
 	        	while(trEleList.hasNext()){
 	        		Element eleTr=trEleList.next();
 	        		String href=eleTr.select("td:eq(0) a").first().absUrl("href").trim();
 	        		String name=eleTr.select("td:eq(0) a").first().text().trim();
 	        		 String site=eleTr.select("td:eq(2)").first().text().trim();
 		    		 sortType = getChildType(sortType, name);
 		    		 try{
		    			  processLastPage(href,sortType,site);
		    		  }catch(Exception ee){
		    			  log.error(Const.concat(href,sortType,site),ee);
		    		  }
 	  			}
 	  			continue;
 	  		}
 	  		else if(sort.contains("国家大剧院音乐厅")){
 	  			Element eleTable=eleTd.parent().parent().nextElementSibling();
 	        	Iterator<Element> trEleList=eleTable.select("tr:gt(0)").iterator();
 	        	while(trEleList.hasNext()){
 	        		Element eleTr=trEleList.next();
 	        		String href=eleTr.select("td:eq(0) a").first().absUrl("href").trim();
 	        		String name=eleTr.select("td:eq(0) a").first().text().trim();
 	        		 String site=eleTr.select("td:eq(2)").first().text().trim();
 		    		  sortType = getChildType(sortType, name);
 		    		 try{
		    			  processLastPage(href,sortType,site);
		    		  }catch(Exception ee){
		    			  log.error(Const.concat(href,sortType,site),ee);
		    		  }
 	  			}
 	  			continue;
 	  		}
 	  		else if(sort.contains("国家大剧院歌剧院")){
 	  			Element eleTable=eleTd.parent().parent().nextElementSibling();
 	        	Iterator<Element> trEleList=eleTable.select("tr:gt(0)").iterator();
 	        	while(trEleList.hasNext()){
 	        		Element eleTr=trEleList.next();
 	        		String href=eleTr.select("td:eq(0) a").first().absUrl("href").trim();
 	        		String name=eleTr.select("td:eq(0) a").first().text().trim();
 	        		 String site=eleTr.select("td:eq(2)").first().text().trim();
 		    		  sortType = getChildType(sortType, name);
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
        	Element eleTable=eleTd.parent().parent().nextElementSibling();
        	Iterator<Element> trEleList=eleTable.select("tr:gt(0)").iterator();
        	while(trEleList.hasNext()){
        		Element eleTr=trEleList.next();
        		String href=eleTr.select("td:eq(0) a").first().absUrl("href").trim();
        	    String site=eleTr.select("td:eq(2)").first().text().trim();
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
    public void processLastPage(String url,int sort,String site){
    	Document doc=getDoc(url);
    	String showName=doc.select("#right>table.kuai>tr:eq(0)").first().text().trim();
        if(site==null||"".equals(site)||site.contains("待定"))
    	       return;
        String imagePath=doc.select("#left img").first().absUrl("src").trim();
        String summary=PubFun.cleanElement(doc.select("#right>table").get(2).select("tr:eq(1) td td").first()).html();
        Map<String,List<TicketPrice>> map=getMap(doc,url);
        Show show=new Show();
        show.setAgent_id("73");
        show.setImage_path(imagePath);
        show.setIntroduction(summary);
        show.setName(showName);
        show.setSiteName(site);
        show.setTimeAndPrice(map);
        show.setType(sort);
        try{
         	getDao().saveShow(show);
        }catch(Exception ee){
        	
        }
    }
	/**
	 * @param args
	 */
    public Map<String,List<TicketPrice>> getMap(Document doc,String url){
		 Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		 Iterator<Element> eleListTr=doc.select("table.biaoge2 tr td table[width=100%] tr[valign=center]").iterator();
		 while(eleListTr.hasNext()){
			 Element eleTr=eleListTr.next();
			 String showTime=eleTr.select("td:eq(0)").first().text().trim();
			 if(showTime.matches("([^星]*)星期[^$]+")){
	    			String tshowTime=PubFun.getRegex("([^星]*)星期[^$]+", showTime, 1);
	    			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(tshowTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
	    			if(newshowTime!=null)
	    				showTime=newshowTime;
	    	 }
			 List<TicketPrice> list=new ArrayList<TicketPrice>();
			 String html=eleTr.select("td:eq(2)").first().html();
			 html=html.replace("&nbsp; ", "&nbsp;");
		     String[] arrayPrice=html.split("&nbsp;");
		     for(String priceInfo:arrayPrice){
		    	TicketPrice ticket=new TicketPrice();
		        if(priceInfo.contains("<a href")){
		    	String price=PubFun.getRegex("<a[^>]*>([^<]*)</a>", priceInfo, 1);
		    	ticket.setPrice(price);
		    	if(price.matches("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*")){
    				String newPrice=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 1);
    				newPrice=newPrice.replace("套票", "").replace("套", "");
    				String remark=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 2);
    				ticket.setPrice(newPrice);
    				ticket.setRemark(remark);
    			}
		    	ticket.setExist(true);
		    	ticket.setMainURL(url);
		    	String detail="http://www.520piao.com/"+PubFun.getRegex("<a href=\"([^\"]*)\"[^>]*>([^<]*)</a>", priceInfo, 1);
		    	ticket.setDetailURL(detail);
		    	if(priceInfo.contains("<span")){
		    		   String remark=PubFun.getRegex("<span[^>]*>([^<]*)</span>", priceInfo, 1);
		    		   ticket.setRemark(remark);
		    	}
		        }else if(priceInfo.contains("<span")&&!priceInfo.contains("<a href")){
		    		String  price=PubFun.getRegex("<span[^>]*>([^<]*)</span>", priceInfo, 1);
		    	    ticket.setPrice(price);
		    		ticket.setPrice(price);
			    	if(price.matches("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*")){
	    				String newPrice=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 1);
	    				newPrice=newPrice.replace("套票", "").replace("套", "");
	    				String remark=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
			    	ticket.setExist(false);
			    	ticket.setMainURL(url);
		       }
		       list.add(ticket);
		    }
             map.put(showTime, list);
		 }
		 return map;
    }
	public static void main(String[] args) {
		AiPiaoWangSpider spider=new AiPiaoWangSpider();
		spider.setDao(new DataDao());
		spider.extract();
	//spider.processLastPage("http://www.520piao.com/ticket_look.asp?id=1492&type=3", 1, "aaa");

	}

}
