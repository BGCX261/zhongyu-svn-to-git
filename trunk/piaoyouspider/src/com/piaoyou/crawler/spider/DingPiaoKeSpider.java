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
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class DingPiaoKeSpider extends TicketSpider implements SpiderTask {
    private static Log log=LogFactory.getLog(DingPiaoKeSpider.class);
	@Override
	public void extract() {
		Document doc=getDoc("http://www.dingpiaoke.com/index.php");
		Iterator<Element> aEleList=doc.select("#daohang_1 a").iterator();
		while(aEleList.hasNext()){
			Element elea=aEleList.next();
			String sort=elea.text().trim();
			String url=elea.absUrl("href");
			 int sortType=9;
			 if(sort.contains("演唱")){
				    sortType=1;
			 }
			 else if(sort.contains("音乐")&&!sort.contains("剧")){
					sortType=2;
			 }
			  else if(sort.contains("话剧") || sort.contains("歌剧")||sort.contains("音乐剧")){
					sortType=3;
			  }
			  else if(sort.contains("芭蕾") || sort.contains("舞蹈")||sort.contains("舞")){
					sortType=4;
			  }
			  else if(sort.contains("戏剧")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
				    sortType=5;
			  }
			  else if(sort.contains("儿童亲子")){
					sortType=6;
			  }
			  else if(sort.contains("体育") ){
					sortType=7;
			  }
			  else if(sort.contains("休闲")||sort.contains("旅游")|| sort.contains("电影")){
					sortType=8;
			  }
			  try{
				    processPageOnSort(url,sortType);
			  }catch(Exception ee){
					log.error(Const.concat(url,sortType),ee);
			  }
		}
		Iterator<Element> aEleList1=doc.select("#daohang_2 a").iterator();
		while(aEleList1.hasNext()){
			Element elea=aEleList1.next();
			String url=elea.absUrl("href");
			int sortType=7;
			try{
			    processPageOnSort(url,sortType);
			}catch(Exception ee){
				log.error(Const.concat(url,sortType),ee);
			}
		}
		Iterator<Element> aEleList2=doc.select("#daohang_3 a").iterator();
		while(aEleList2.hasNext()){
			Element elea=aEleList2.next();
			String url=elea.absUrl("href");
			int sortType=8;
			try{
			    processPageOnSort(url,sortType);
			}catch(Exception ee){
				log.error(Const.concat(url,sortType),ee);
			}
		}
		Iterator<Element> aEleList3=doc.select("#daohang_4 a").iterator();
		while(aEleList3.hasNext()){
			Element elea=aEleList3.next();
			String url=elea.absUrl("href");
			int sortType=8;
			try{
			    processPageOnSort(url,sortType);
			}catch(Exception ee){
				log.error(Const.concat(url,sortType),ee);
			}
		}
	}
	/**
	 * 
	 * @param url
	 * @param sort
	 */
	public void processPageOnSort(String url ,int sort){
		Document doc=getDoc(url);
	    String pageStr=	doc.select(".fengye li").first().outerHtml();
		String strPageCount=PubFun.getRegex("<li class=\"dqy\">[^<]*<span[^>]*>[^<]*</span>/([^<]*)</li>", pageStr, 1);
		if(null==strPageCount || "0".equals(strPageCount.trim())){
			   return ;
		}
		int pageCount=1;
		try{
			pageCount=Integer.parseInt(strPageCount);
		}catch(Exception ee){
			pageCount=1;
		}
		for(int i=1;i<=pageCount;i++){
			if(i==1){
				String ConcentratePageurl=url;
				processConcentratePage(ConcentratePageurl,sort);
			}
			else{
				String ConcentratePageurl=url+"&ye="+i;
				try{
				   processConcentratePage(ConcentratePageurl,sort);
				}catch(Exception ee){
					log.error(Const.concat(ConcentratePageurl,sort),ee);
				}
			}
		}
	}
	/**
	 * 
	 * @param url
	 * @param sort
	 */
	public void processConcentratePage(String url,int sort){
		Document doc=getDoc(url);
		Iterator<Element> liEleList=doc.select(".left_list_piao>ul li a").iterator();
		while(liEleList.hasNext()){
			String href=liEleList.next().absUrl("href");
			try{
				processLastPage(href,sort);
			}catch(Exception ee){
				log.error(Const.concat(href,sort),ee);
			}
			
		}
	}
    public void processLastPage(String url,int sort){
    	Document doc=getDoc(url);
		String imagePath;
		Element imagePathE=doc.select(".piao_pic").first();//图片路径
		if(imagePathE==null){
			log.error(url+"有错误");
			return;
		}
		else
		  imagePath=imagePathE.absUrl("src").trim();
		 String showName=doc.select(".piao_n_title").first().text().trim();//演出名称
		 Element site=doc.select(".piao_t a").first();
		 if(null==site||"".equals(site.text().trim())||site.text().trim().contains("待定"))
		    	 return;
		String showSite=site.text().trim();//演出场馆
		String summary=PubFun.cleanElement(doc.select(".ycjs_content").first()).html().trim();//演出简介
		Map<String,List<TicketPrice>> map=getMap(doc,url);
		Show show=new Show();
		show.setAgent_id("20");
		show.setTimeAndPrice(map);
		show.setImage_path(imagePath);
		show.setIntroduction(summary);
		show.setName(showName);
		show.setSiteName(showSite);
		show.setType(sort);
		try{
			getDao().saveShow(show);
		}
		catch(Exception ee){
			
		}
    }
    public  Map<String,List<TicketPrice>> getMap(Document doc,String url){
    	Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
    	Iterator<Element> trEleList=doc.select(".piao_sx tr:gt(0)").iterator();
    	while(trEleList.hasNext()){
    		Element trEle=trEleList.next();
    		Element eShowTime=trEle.select(".piao_sx_left").first();
    		if(eShowTime==null)
    			continue;
    		String showTime=eShowTime.text().trim();
    		if(showTime.contains("待定"))
    			continue;
    		showTime=showTime.replace("：", ":");
    		if(PubFun.isMatchRegex(showTime, "[^\\d]*[\\d]{4}[/\\-\\.][\\d]{1,2}[/\\-\\.][\\d]{1,2}[^\\d]*星期[^\\d]*[\\d]{2}:[\\d]{2}[^\\d]*")){//2011/06/24 星期五 19:30
             	showTime=showTime.replaceAll("[^\\d]*星期[^\\d]*", " ");
     		}
    		if(PubFun.isMatchRegex(showTime, "[^\\d]*[\\d]{4}-[\\d]{1,2}-[\\d]{1,2}+[ ]*[\\d]{2}:[\\d]{2}[^\\d]*")){
    			showTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
    		}
            if(PubFun.isMatchRegex(showTime, "[^\\d]*[\\d]{4}/[\\d]{1,2}/[\\d]{1,2}[ ]*[\\d]{2}:[\\d]{2}[^\\d]*")){
            	showTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy/MM/dd HH:mm"), "yyyy-MM-dd HH:mm");
    		}
            if(PubFun.isMatchRegex(showTime, "[^\\d]*[\\d]{4}\\.[\\d]{1,2}\\.[\\d]{1,2}[ ]*[\\d]{2}:[\\d]{2}[^\\d]*")){
            	showTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy.MM.dd HH:mm"), "yyyy-MM-dd HH:mm");
    		}
            //06-21 19:30 星期二
            if(PubFun.isMatchRegex(showTime, "([^$]*)星期[^$]{1}")){//07-09 19:30 星期六07-09 19:30 星期六
    			showTime=PubFun.getCurrentYear()+"-"+PubFun.getRegex("([^$]*)星期[^$]{1}", showTime, 1).trim();
    		}
            if(PubFun.isMatchRegex(showTime, "[\\d]+月[\\d]+日[^\\d]*([\\d]+:[\\d]+)")){
            	String month=PubFun.getRegex("([\\d]+)月", showTime, 1);
				String day=PubFun.getRegex("([\\d]+)日", showTime, 1);
				String time=PubFun.getRegex("日[^\\d]*([\\d]+:[\\d]+)", showTime, 1).trim();
				showTime=PubFun.getCurrentYear()+"-"+(month.length()==1? ("0"+month):month)+"-"+(day.length()==1? ("0"+day):day)+" "+time;
    		}
            //7月1日 星期五 19:30
            //07-02 19:30
            if(PubFun.isMatchRegex(showTime, "[^\\d]*[\\d]{1,2}-[\\d]{1,2} [\\d]{2}:[\\d]{2}[^\\d]*")){
            	showTime=PubFun.getCurrentYear()+"-"+showTime;
            }//2011-06-19
           /* if(PubFun.isMatchRegex(showTime.trim(), "[\\d]{4}[\\.|-|/][\\d]{1,2}[\\.|-|/][\\d]{1,2}")){
            	showTime=showTime.replace(".", "-").replace("/", "-")+" 00:00";
            }*/
            showTime=showTime.replace(".", "-");
    		List<TicketPrice> list=new ArrayList<TicketPrice>();
    		Iterator<Element> fontList=trEle.select(".piao_sx_right font").iterator();
    		while(fontList.hasNext()){
    			Element eleFont=fontList.next();
    			TicketPrice ticket=new TicketPrice();
    			ticket.setExist(false);
    			String price=eleFont.text().trim();
    			String remark;
    			if(price!=null)
    				price=price.replace(" ", "").replace("套票：", "");
    			if(price.equalsIgnoreCase("vip"))
    				continue;
    			if((price.contains("vip")||price.contains("VIP")&&!price.contains("("))){
    				price=price.replace("vip", "").replace("VIP", "");
    				remark="vip";
    				ticket.setRemark(remark);
    			}
    			 if(PubFun.isMatchRegex(price, "[\\d]*[（|\\(]([^&]*)[）|\\)]")){
  			    	String newPrice=price;
  			    	price=PubFun.getRegex("([\\d]*)[（|\\(]([^&]*)[）|\\)]", newPrice, 1);
  			    	remark=PubFun.getRegex("([\\d]*)[（|\\(]([^&]*)[）|\\)]", newPrice, 2);
  			    	ticket.setRemark(remark);
  			    }
    			 if(PubFun.isMatchRegex(price, "([\\d]+)X([\\d]+)")){
    				String param1=PubFun.getRegex("([\\d]+)X([\\d]+)", price, 1);
    				String param2=PubFun.getRegex("([\\d]+)X([\\d]+)", price, 2);
    				try{
    				  int allPrice=Integer.parseInt(param1)*Integer.parseInt(param2);
    				  String strAllPrice=String.valueOf(allPrice);
    				  ticket.setRemark(price);
    				  price=strAllPrice;
    				
    				}catch(Exception ee){
    					log.error("取票价异常");
    				}
    			}
    			if(price.equals("学生票")){
        				price="100";
        		}
    			if(price.equals("276(红卡6次")){
    				price="276";
    				ticket.setRemark("红卡6次");
    			}
    			//学生票
    			ticket.setPrice(price);
    			ticket.setMainURL(url);
    			list.add(ticket);
    		}
    		Iterator<Element> aList=trEle.select(".piao_sx_right a").iterator();
    		while(aList.hasNext()){
    			Element elea=aList.next();
    			TicketPrice ticket=new TicketPrice();
    			ticket.setExist(true);
    			String price=elea.text().trim();
    			String remark;
    			if(price!=null)
    			price=price.replace(" ", "").replace("套票：", "").replace("单价：", "").trim();
    			if(price.equalsIgnoreCase("vip"))
    				continue;
    			if((price.contains("vip")||price.contains("VIP")&&!price.contains("("))){
    				price=price.replace("vip", "").replace("VIP", "");
    				remark="vip";
    				ticket.setRemark(remark);
    			}
    			if(PubFun.isMatchRegex(price, "学生票([\\d]+)")){//学生票50
    				remark="学生票";
    				price=PubFun.getRegex("学生票([\\d]+)", price, 1);
    				ticket.setRemark(remark);
    			}
    			 if(PubFun.isMatchRegex(price, "[\\d]*[（|\\(]([^&]*)[）|\\)]")){
  			    	String newPrice=price;
  			    	price=PubFun.getRegex("([\\d]*)[（|\\(]([^&]*)[）|\\)]", newPrice, 1);
  			    	remark=PubFun.getRegex("([\\d]*)[（|\\(]([^&]*)[）|\\)]", newPrice, 2);
  			    	ticket.setRemark(remark);
  			    }
    			if(PubFun.isMatchRegex(price, "([\\d]+)X([\\d]+)")){
    				String param1=PubFun.getRegex("([\\d]+)X([\\d]+)", price, 1);
    				String param2=PubFun.getRegex("([\\d]+)X([\\d]+)", price, 2);
    				try{
    				  int allPrice=Integer.parseInt(param1)*Integer.parseInt(param2);
    				  String strAllPrice=String.valueOf(allPrice);
    				  ticket.setRemark(price);
    				  price=strAllPrice;
    				}catch(Exception ee){
    					log.error("取票价异常");
    				}
    			}
    			if(price.equals("学生票")){
    				price="100";
    			}
    			if(price.equals("276(红卡6次")){
    				price="276";
    				ticket.setRemark("红卡6次");
    			}
    			ticket.setPrice(price);
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
		DingPiaoKeSpider ding=new DingPiaoKeSpider();
		ding.setDao(new DataDao());
        ding.extract();
	}

}
