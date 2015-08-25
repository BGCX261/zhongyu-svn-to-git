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
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class PiaoWuZhiXingSpider extends TicketSpider implements SpiderTask {
	private static final Log log=LogFactory.getLog(PiaoWuZhiXingSpider.class);

	@Override
	public void extract() {
	   Document doc=getDoc("http://www.tickets365.com.cn/tk2005/usr/index.jsp");
	   Element temp=doc.select("td:containsOwn(分类查看)").first().parent().nextElementSibling();
	   Iterator<Element> AList= temp.select("a").iterator();
	   while(AList.hasNext()){
		   Element eleA=AList.next();
		   String sort=eleA.text().replace(" ", "").replace("　", "");
		   String url=eleA.absUrl("href").trim();
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
		   else if(sort.contains("亲子")){
				 sortType=ShowType.CHILDREN;
		   }
		   else if(sort.contains("体育") ){
				 sortType=ShowType.SPORTS;
		   }
		   else if(sort.contains("休闲")||sort.contains("旅游")|| sort.contains("电影")){
				 sortType=ShowType.HOLIDAY;
		   }
		   Document sortDoc=getDoc(url);
		   try{
		    	processBySort(sortDoc,sortType);
		   }catch(Exception ee){
				log.error(Const.concat(sortDoc,sortType),ee);
		   }
	   }
	}
	
	public void processBySort(Document doc,int sortType){
		Iterator<Element> it=doc.select("td[width=740]>table[width=740]>tr.down_line>td[width=50%]").iterator();
		while(it.hasNext()){
			Element eleTd=it.next();
			String conUrl=eleTd.select("tr[valign=bottom] a").first().absUrl("href");
			String site=eleTd.select("table>tr:eq(1)>td:eq(1)>table>tr:eq(0)>td:eq(1)").first().ownText();
			try{
			   processLatPage(conUrl,sortType,site);
			}catch(Exception ee){
				log.error(Const.concat(conUrl,sortType,site),ee);
			}
		}
		Element eleNext=doc.select("td[width=740]>table[width=100%] tr>td[align=left]>a:containsOwn(下一页)").first();
		if(eleNext!=null){
			Element form=doc.select("form[name=navform]").first();
			String  gotopage=form.select("input[name=gotopage]").first().attr("value");
			gotopage=String.valueOf(Integer.parseInt(gotopage)+1);
			String totalpages=form.select("input[name=totalpages]").first().attr("value");
			String totalrows=form.select("input[name=totalrows]").first().attr("value");
			String cat=form.select("input[name=cat]").first().attr("value");
			String keyword=form.select("input[name=keyword]").first().attr("value");
			try{
			   Document nextDoc = Jsoup.connect("http://www.tickets365.com.cn/tk2005/usr/ticket_list_search.jsp").data("gotopage",gotopage)
			  .data("totalpages",totalpages )
			  .data("totalrows",totalrows)
			  .data("cat",cat).data("keyword" ,keyword).post();
			   try{
			    	processBySort(nextDoc,sortType);
			   }catch(Exception ee){
					log.error(Const.concat(nextDoc,sortType),ee);
			   }
			}
			catch(Exception ee){
			}
		}
		else{
			return;
		}
	}
	
	public void processLatPage(String url,int sortType,String site){
		if(site==null ||"".equals(site)){
			return;
		}
		String imageUrl=null;
		Document doc=getDoc(url);
		Element elePic=doc.select("div[style=float:left; margin-right:8px;]>img").first();
		if(elePic!=null){
			imageUrl=elePic.absUrl("src").trim();
		}
		Element seatA=doc.select("a[href=#booking]").first().previousElementSibling();
		String seatUrl=null;
		if(seatA!=null){
			String str=seatA.attr("onclick").trim();
			seatUrl=PubFun.getRegex("window.open\\('([^']*\\.[^']*)',", str, 1);
		}
		String title=doc.select("td[valign=top]>span.font2").first().text();
		String summary=PubFun.cleanElement(doc.select("td[width=756]>table[height=138] td[valign=top]" ).first()).html();
		Map<String,List<TicketPrice>> map=getMap(doc,url);
		Show show=new Show();
		show.setAgent_id("134");
		show.setImage_path(imageUrl);
		show.setFirstAgent(false);
		show.setSeatPic_path(seatUrl);
		show.setOnlineSeat(false);
		show.setName(title);
		show.setSiteName(site);
		show.setType(sortType);
		show.setTimeAndPrice(map);
		show.setIntroduction(summary);
		try{
			this.getDao().saveShow(show);
		}
		catch(Exception ee){
			
		}
	}
	
	public  Map<String,List<TicketPrice>> getMap(Document doc,String url){
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
		Iterator<Element> it=doc.select("tr[bgcolor=#FFE8A2").first().parent().select("tr[bgcolor=#F1F1F1],tr[bgcolor=#FCFCFC]").iterator();
		while(it.hasNext()){
			Element trEle=it.next();
			String date=PubFun.getCurrentYear();
			String month=trEle.select("td[valign=bottom]>table>tr:eq(0)>td").first().text();
			month=month.replaceAll("[^\\d]*", "");
			String day=trEle.select("td[valign=bottom] table>tr:eq(1)").first().text();
			String time=trEle.select("td:eq(1)").first().text();
			String showTime=date+"-"+month+"-"+day+" "+time;
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Iterator<Element> havePriceIt=trEle.select("td:eq(3)>a").iterator();
			while(havePriceIt.hasNext()){
				Element eleHavePrice=havePriceIt.next();
				TicketPrice ticket=new TicketPrice();
	    		ticket.setExist(true);
	    		ticket.setPrice(eleHavePrice.select("span").first().text().trim().replace(" ", "").replace(" ", ""));
	    		Element remarkElement=eleHavePrice.nextElementSibling();
	    		if(remarkElement!=null){
		    		if(remarkElement.nodeName().equals("span")&&remarkElement.hasClass("pt9")){
		    			String remark=remarkElement.text().trim();
		    			if(remark.length()>50){
		    				remark=remark.substring(0,50);
		    			}
		    			ticket.setRemark(remark.replace("(", "").replace(")", ""));
		    		}
	    		}
	    		ticket.setMainURL(url);
	    		ticket.setDetailURL(eleHavePrice.absUrl("href").trim());
	    		list.add(ticket);
			}
			Iterator<Element> noPriceIt=trEle.select("td:eq(3)>span[class=f_gray_l]").iterator();
			while(noPriceIt.hasNext()){
				Element elenoPrice=noPriceIt.next();
				TicketPrice ticket=new TicketPrice();
	    		ticket.setExist(false);
	    		ticket.setPrice(elenoPrice.text().trim().replace(" ", "").replace(" ", ""));
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
		PiaoWuZhiXingSpider spider=new PiaoWuZhiXingSpider();
//		spider.setDao(new DataDao());
//		spider.extract();
        spider.processLatPage("http://www.tickets365.com.cn/tk2005/usr/ticket_detail.jsp?ticketid=78565", 8, "上海大剧院-小剧场");
	}

}
