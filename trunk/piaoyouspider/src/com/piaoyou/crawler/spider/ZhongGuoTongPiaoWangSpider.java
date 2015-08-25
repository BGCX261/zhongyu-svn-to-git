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

public class ZhongGuoTongPiaoWangSpider extends TicketSpider implements SpiderTask {
	private static Log log=LogFactory.getLog(ZhongGuoTongPiaoWangSpider.class);
	@Override
	public void extract() {
        Document doc=getDoc("http://www.tongpiao.cn/all.asp");
        Iterator<Element> tdList=doc.select("td.white-14").iterator();
        while(tdList.hasNext()){
        	Element eleTd=tdList.next();
        	String sort=eleTd.text().trim();
        	int sortType=9;
        	if(sort.contains("国家大剧院")){
        		 Iterator<Element>  trEleList=eleTd.nextElementSibling().select("table>tr:gt(0)").iterator();
                 while(trEleList.hasNext()){
                 	Element eleTr=trEleList.next();
                 	String href=eleTr.select("td:eq(0) a").first().absUrl("href").trim();
           	        String site=eleTr.select("td:eq(1) a").first().text().trim();
           	        String name=eleTr.select("td:eq(0) a").first().text().trim();
           	        sortType = getChildType(sortType, name);
           	        try{
   	    			   processLastPage(href,sortType,site);
   	    		    }catch(Exception ee){
   	    			  log.error(Const.concat(href,sortType,site),ee);
   	    	        }
                  }
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
        	  Iterator<Element>  trEleList=eleTd.nextElementSibling().select("table>tr:gt(0)").iterator();
              while(trEleList.hasNext()){
            	Element eleTr=trEleList.next();
            	Element eleT=eleTr.select("td:eq(0) a").first();
            	if(null==eleT)
            		continue;
            	String href=eleTr.select("td:eq(0) a").first().absUrl("href").trim();
        	    String site=eleTr.select("td:eq(1) a").first().text().trim();
        	    try{
	    			  processLastPage(href,sortType,site);
	    		}catch(Exception ee){
	    			  log.error(Const.concat(href,sortType,site),ee);
	    	    }
              }
        }
	}
	public void processLastPage(String url,int sort,String site){
		Document doc=getDoc(url);
		String showName=doc.select("td[width=84%] strong.red-16").first().text();
		String summary=PubFun.cleanElement(doc.select("td[height=97].black-014").first()).html();
		String imagePath=doc.select("img[width=156]").first().absUrl("src").trim();
		  if(site==null||"".equals(site)||site.contains("待定"))
   	       return;
		Element eleTable=doc.select("table[width=100%]:contains(订票栏)>tr>td[valign=top]>table[width=100%]").first();
		Map<String,List<TicketPrice>> map=getMap(eleTable,url);
		Show show=new Show();
		show.setAgent_id("41");
		show.setImage_path(imagePath);
		show.setName(showName);
		show.setIntroduction(summary);
		show.setSiteName(site);
		show.setTimeAndPrice(map);
		show.setType(sort);
		try{
			getDao().saveShow(show);
		}catch(Exception ee){
			
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
	
	public Map<String,List<TicketPrice>> getMap(Element doc,String url){
		 Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		 Iterator<Element> eleTrList=doc.select("tr:gt(0)").iterator();
		 while(eleTrList.hasNext()){
			 Element eleTr=eleTrList.next();
			 //2011年9月16日19；30（星期五)  //2011年12月25日 14:30//2011-01-3117:30
			 String showTime=eleTr.select("td:eq(1)").first().text().trim();//2011-07-0819:30
			 if("".equals(showTime)||showTime.contains("待定"))
				 continue;
			 showTime=showTime.replace("；", ":").replace("：", ":").replaceAll("[\\(（]*星期[^期]{1}[\\)）]*", "").trim().replace(" ", "");
			 if(!showTime.contains("常年"))
			 showTime=showTime.replace("年", "-").replace("月","-").replace("日", " ").replace("号", " ");
			 showTime=showTime.replace("星期[^\\d]+", " ");
			 String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
			 if(newshowTime!=null)//2011-10-2819:30
				showTime=newshowTime;
			 List<TicketPrice> list=new ArrayList<TicketPrice>();
			 Iterator<Element> yesPriceList=eleTr.select("td:eq(2)>a").iterator();
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
	    				newPrice=newPrice.replace("套票", "").replace("套", "").replace("元", "");
	    				String remark=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			ticket.setDetailURL(elePrice.absUrl("href").trim());
	    			ticket.setExist(true);
	    			ticket.setMainURL(url);
	    			list.add(ticket);
			 }
			 Iterator<Element> noPriceList=eleTr.select("td:eq(2)>font").iterator();
             while(noPriceList.hasNext()){
           		Element elePrice=noPriceList.next();
	    			TicketPrice ticket=new TicketPrice();
	    			String price=elePrice.text().trim();
	    			price.replace("元", "");
	    			if("".equals(price)||price.contains("待定")||price.contains("北京管乐交响乐团音乐会"))
	    				continue;
	    			ticket.setPrice(price);
	    			if(price.matches("([\\d]+)([^\\d]+)")){
	    				String newPrice=PubFun.getRegex("([\\d]+)([^\\d]+)", price, 1);
	    				String remark=PubFun.getRegex("([\\d]+)([^\\d]+)", price, 2);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);//2011-07-2919:30
	    			}
	    			else if(price.matches("([^\\d]+)([\\d]+)")){
	    				String newPrice=PubFun.getRegex("([^\\d]+)([\\d]+)", price, 2);
	    				String remark=PubFun.getRegex("([^\\d]+)([\\d]+)", price, 1);
	    				ticket.setPrice(newPrice);
	    				ticket.setRemark(remark);
	    			}
	    			else if(price.matches("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*")){
	    				String newPrice=PubFun.getRegex("([^\\(（]*)[\\(（]([^\\)）]*)[）\\)][^\\)）]*", price, 1);
	    				newPrice=newPrice.replace("套票", "").replace("套", "").replace("元", "");
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
		// TODO Auto-generated method stub
		ZhongGuoTongPiaoWangSpider spider=new ZhongGuoTongPiaoWangSpider();
		spider.setDao(new DataDao());
		spider.extract();
    	//spider.processLastPage("http://www.tongpiao.cn/proview.asp?id=4373", 1, "aa");
	}

}
