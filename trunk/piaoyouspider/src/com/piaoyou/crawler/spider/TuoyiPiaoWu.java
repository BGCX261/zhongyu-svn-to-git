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

public class TuoyiPiaoWu extends TicketSpider implements SpiderTask {
    private static Log log=LogFactory.getLog(TuoyiPiaoWu.class);
	private final static String BASE_URL = "http://www.toypiao.com/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
		.get(0).get("agency_id").toString();
	@Override
	public void extract() {
		Document doc=getDoc("http://www.toypiao.com/");
		Iterator<Element>  eleTableList=doc.select("body>table:eq(5)>tr>td:eq(0)>table:eq(0)>tr>td>table").subList(3, 18).iterator();
		while(eleTableList.hasNext()){
			Element eleTable=eleTableList.next();
			String url=eleTable.select("a").first().absUrl("href").trim();
			String sort=eleTable.select("a").first().text().trim();
			int sortType=ShowType.OTHER;
			if(sort.contains("演唱")){
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
		    else if(sort.contains("戏剧")||sort.contains("戏曲")||sort.contains("二人转") || sort.contains("魔术")||sort.contains("杂技") || sort.contains("相声")||sort.contains("小品") || sort.contains("马戏")){
				    sortType=ShowType.OPERA;
			}
			else if(sort.contains("儿童亲子")){
				 sortType=ShowType.CHILDREN;
			}
			else if(sort.contains("体育") ){
				 sortType=ShowType.SPORTS;
			}
			else if(sort.contains("休闲")||sort.contains("旅游")|| sort.contains("电影")){
				 sortType=ShowType.HOLIDAY;
			}
			try{
		    	processBySort(url,sortType);
			}catch(Exception ee){
				log.error(Const.concat(url,sortType),ee);
			}
		}
	}
	public void processBySort(String url,int sortType){
		Document doc=getDoc(url);
		List<Element> eleList=doc.select("table[width=971] td table:eq(1) tr");
		int size=eleList.size();
		Iterator<Element> eleTrList=eleList.subList(2, size).iterator();
		while(eleTrList.hasNext()){
			Element eleTr=eleTrList.next();
			String href=eleTr.select("td:eq(0) a").first().absUrl("href").trim();
			try{
			   processLastPage(href,sortType);
			}
			catch(Exception ee){
				log.error(Const.concat(href,sortType),ee);
			}
		}
	}

	public void processLastPage(String url,int sort){
		 Document doc=getDoc(url);
		 Element eleShow=doc.select("table[width=958]>tr>td[width=789]>table:eq(1) td[class=line_1] tr").first();
		 if(eleShow==null){
			return ;
		 }
		 Element image=eleShow.select("td:eq(0) table a img").first();
		 if(null==image){
			return;
		 }
		 String summary=PubFun.cleanElement(doc.select("table[width=958]>tr>td[width=789]>table:eq(3) div").first()).html();
		 String imagePath=image.absUrl("src").trim();
	     String showName=eleShow.select("td:eq(1)>table>tr:eq(0) td").first().text().trim();
	     Element site=eleShow.select("td:eq(1)>table>tr:eq(2) table[width=559] tr:eq(2) td.text1 a").first();
	     if(null==site||"".equals(site.text().trim())||site.text().trim().contains("待定"))
	    	 return;
	     Element time=eleShow.select("td:eq(1)>table>tr:eq(2) table[width=559] tr:eq(1) td.text1").first();
	     if(null==time||"".equals(time.text().trim())||time.text().trim().contains("待定"))
	    	 return;
	     String showSite=site.text().trim();
	     Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
	     String showTime=time.text().trim();
 	     showTime=showTime.replace("时 间： ", "").replace("时间", "").replace("： ", "").replace(" ", "");
 	     //2011年6月23日～6月26日19:30
	     if(PubFun.isMatchRegex(showTime, "[^\\d]*[\\d]{4}年[\\d]{1,2}月[\\d]{1,2}日～[\\d]{1,2}月[\\d]{1,2}日[^日]*")){
				String startDate=PubFun.getRegex("([\\d]{4}年[\\d]{1,2}月[\\d]{1,2}日)～[\\d]{1,2}月[\\d]{1,2}日[^日]*", showTime, 1).replace(".", "-");
				String endDate=PubFun.getCurrentYear()+"年"+PubFun.getRegex("([\\d]{4}年[\\d]{1,2}月[\\d]{1,2}日)～([\\d]{1,2}月[\\d]{1,2}日)[^日]*", showTime, 2).replace(".", "-");
				String newTtime=PubFun.getRegex("([\\d]{4}年[\\d]{1,2}月[\\d]{1,2}日)～[\\d]{1,2}月[\\d]{1,2}日([^日]*)", showTime, 2).replace(".", "-");
				SimpleDateFormat format=new SimpleDateFormat("yyyy年MM月dd日");
				//间隔在10天之内做处理
				try{
				   if((format.parse(endDate).getTime()-format.parse(startDate).getTime())/86400000<=9){
					   List<String> listDate=PubFun.getTwoDateAllDate(startDate.replace("年", "-").replace("月", "-").replace("日", ""), endDate.replace("年", "-").replace("月", "-").replace("日", ""), "", "yyyy-MM-dd");
					   for(String date:listDate){
						   List<TicketPrice> newlist = getList(url, eleShow);
						   map.put(date+" "+newTtime.trim(), newlist);
					   }
				   }
				   else{
					   List<TicketPrice> newlist = getList(url, eleShow);
					   map.put(showTime, newlist);
				   }
				}catch(Exception ee){
					
				}
		 }
	     else{
	 	     String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy年MM月dd日HH:mm"), "yyyy-MM-dd HH:mm");
	 	     if(newshowTime!=null)
	 	    	 showTime=newshowTime;
	 	     List<TicketPrice> list = getList(url, eleShow);
	 	     map.put(showTime, list);
	     }
	     Show show=new Show();
	     show.setAgent_id(agentID);
	     show.setImage_path(imagePath);
	     show.setIntroduction(summary);
	     show.setName(showName);
	     show.setSiteName(showSite);
	     show.setTimeAndPrice(map);
	     show.setType(sort);
	     try{
	    	 getDao().saveShow(show);
	     }catch(Exception ee){
	    	 
	     }
	}
	private List<TicketPrice> getList(String url, Element eleShow) {
		List<TicketPrice> list=new ArrayList<TicketPrice>();
	     Iterator<Element> nopriceList=eleShow.select("td:eq(1)>table>tr:eq(2) table[width=559] tr:eq(0) td.text1 span.noprice").iterator();
	     while(nopriceList.hasNext()){
	    	 Element eleNo=nopriceList.next();
	    	 TicketPrice ticket=new TicketPrice();
	    	 String price=eleNo.text().trim();
	    	 price=price.replace("、", "");
	    	 if(price.contains("待定"))
	    		 continue;
	    	 if(price.contains("暂未"))
	    		 continue;
	    	 ticket.setPrice(price);
	    	 ticket.setExist(false);
	    	 ticket.setMainURL(url);
	    	 list.add(ticket);
	     }
	     Iterator<Element> apriceList=eleShow.select("td:eq(1)>table>tr:eq(2) table[width=559] tr:eq(0) td.text1 a.link3").iterator();
	     while(apriceList.hasNext()){
	    	 Element eleNo=apriceList.next();
	    	 TicketPrice ticket=new TicketPrice();
	    	 String price=eleNo.text().trim();
	    	 price=price.replace("、", "");
	    	 if(price.contains("待定"))
	    		 continue;
	    	 if(price.contains("暂未"))
	    		 continue;
	    	 ticket.setPrice(price);
	    	 ticket.setExist(true);
	    	 ticket.setMainURL(url);
	    	 ticket.setDetailURL(url);
	    	 list.add(ticket);
	     }
		return list;
	}
	/**
	 * @param args
	 */
	
	public static void main(String[] args) {
		TuoyiPiaoWu spider=	new TuoyiPiaoWu();
		spider.setDao(new DataDao());
		//spider.processLastPage("http://www.toypiao.com/showper.php?id=1494", 1);
         spider.extract();
		
	}

}
