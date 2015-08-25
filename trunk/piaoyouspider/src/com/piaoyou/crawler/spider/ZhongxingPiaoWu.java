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
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class ZhongxingPiaoWu extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(ZhongxingPiaoWu.class);
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	@Override
	public void extract() {
		Document doc=getDoc("http://www.starpiao.com/Category/79-.aspx");
        Iterator<Element> liaEleList=doc.select("form#aspnetForm div.master-wrapper-page div.master-wrapper-content div.headermenu ul").first().select("li:not(#chncpa):gt(1) a").iterator();
        while(liaEleList.hasNext()){
        	Element liaEle=liaEleList.next();
        	String sort=liaEle.text().trim();
        	String url=liaEle.absUrl("href").trim();
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
		       processPageBySort(url,sortType);
		    }catch(Exception ee){
		    	log.error(Const.concat(url,sortType),ee);
		    }
        }
	}

	public void processPageBySort(String url,int sort){
		Document doc=getDoc(url);
		Iterator<Element> alist= doc.select("div.product-list1 div.item-box div.product-item h2.product-title a").iterator();
		while(alist.hasNext()){
			Element a=alist.next();
			String href=a.absUrl("href");
			processLastPage(href,sort);
		}
		Element pageNext=doc.select("div.product-list1 div.product-pager a:contains(下页)").first();
		if(pageNext!=null){
			 url=pageNext.absUrl("href");
			 try{
			   processPageBySort(url,sort);
			 }catch(Exception ee){
				 log.error(Const.concat(url,sort),ee);
			 }
		}
		else
			return;
	}
	public void processLastPage(String url,int sort){
		Document doc=getDoc(url);
		Element div=doc.select("div.product-details-page div.product-essential.product-details-info div.overview div.product-collateral div.attributes table").first();
		if(div==null)
		  return;
		//       中国心 大海情-2011吕继宏独唱音乐会 ［ 售票中 ］ 
		String showName=doc.select("div.product-details-page div.product-essential.product-details-info h3.productname").text().trim();
		//［ 售票中 ］
		int z=showName.indexOf("［");
	    if(z!=-1)
	    	showName=showName.substring(0,z);
		String imagePath=doc.select("div.product-details-page div.product-essential.product-details-info div.picture img").first().absUrl("src");
	    Element elesite=doc.select("div.product-details-page div.product-essential.product-details-info div.picture div.shortdescription").first();
	    if(null==elesite||"".equals(elesite.text().trim())||elesite.text().contains("待定"))
	    	return;
	    String siteName=elesite.text().trim();
	    String summary=PubFun.cleanElement(doc.select("div.product-details-page>div.product-collateral div.product-variant-line div.fulldescription").first()).text();
	    siteName=PubFun.getRegex("场馆：([^：]*)", siteName, 1);
		siteName=siteName.replace(" ", "");
		Show show=new Show();
		show.setAgent_id("15");
		show.setImage_path(imagePath);
		show.setName(showName);
		show.setSiteName(siteName);
		show.setType(sort);
	    show.setIntroduction(summary);
	    Map<String,List<TicketPrice>> map=getMap(doc,url);
	    show.setTimeAndPrice(map);
	    try{
	    	getDao().saveShow(show);
	    }catch(Exception ee){
	    	log.error(url+ee.getMessage());
	    }
	}
	public static Map<String,List<TicketPrice>> getMap(Document doc,String url){
		Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
			Iterator<Element> tdlist=doc.select("div.overview div.product-collateral div.attributes>div.attributediv").first().select("label").iterator();
			while(tdlist.hasNext()){
				Element tdEle=tdlist.next();
				String showTime=tdEle.text().trim();
				if(showTime.contains("星期")){
					showTime=showTime.replaceAll("星期[^$]{1}", " ");
					showTime=showTime.replace("：", ":");
				}
				else if(showTime.contains("国安")){//北京国安队VS山东鲁能队（6月18日 周六 19:30）
					showTime=PubFun.getRegex("[^（]*（([^）]*)）", showTime, 1);//[^（]*（[^）]*）
					String month=PubFun.getRegex("([\\d]+)月", showTime, 1);
					String day=PubFun.getRegex("([\\d]+)日", showTime, 1);
					String time=PubFun.getRegex("周六([^$]*)", showTime, 1).trim();
					showTime=PubFun.getCurrentYear()+"-"+(month.length()==1? ("0"+month):month)+"-"+(day.length()==1? ("0"+day):day)+" "+time;
				}
				String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
		    	if(newshowTime!=null){
		    		showTime=newshowTime;
		    	}
		    	if(showTime.matches("[^\\d]*[\\d]{1,2}-[\\d]{1,2}[\\s]*[\\d]{1,2}:[\\d]{1,2}[^\\d]*")){
		    		showTime=PubFun.getCurrentYear()+"-"+showTime.trim();
		    		//showTime=showTime.replaceAll("[\\s]*", " ");
		    	}
		    	//9月13日   19:30
		    	if(showTime.matches("[^\\d]*[\\d]{1,2}月[\\d]{1,2}日[\\s]*[\\d]{1,2}:[\\d]{1,2}[^\\d]*")){
		    		showTime=PubFun.getCurrentYear()+"-"+showTime.trim();
		    		showTime=showTime.replace("月", "-").replace("日", "");
		    		//showTime=showTime.replaceAll("[\\s]*", " ");
		    	}
		    	 try {
					 showTime = newDate.format(oldDate.parse(showTime));
				 } catch (Exception e1) {
						//TODO	时间暂时不处理
				 }
				List<TicketPrice> list=new ArrayList<TicketPrice>();
				Iterator<Element> elePriceList=doc.select("div.overview div.product-collateral div.attributes>div.attributediv").get(1).select("label").iterator();
				while(elePriceList.hasNext()){
					TicketPrice ticket=new TicketPrice();
					Element concenPrice=elePriceList.next();
					String price=concenPrice.text().trim();
					if(price.contains("售完"))
						continue;
					String remark;
					ticket.setExist(true);
					if(price.contains("无票")){
						ticket.setExist(false);
						ticket.setRemark("无票");
					}
					ticket.setMainURL(url);
					ticket.setDetailURL(url);
					if(PubFun.isMatchRegex(price, "[\\d]*[^\\d]*[（|\\(]([^&]*)[）|\\)]")){
						String newPrice=price;
						price=PubFun.getRegex("([\\d]*)[^\\d]*[（|\\(]([^&]*)[）|\\)]", newPrice, 1);
						remark=PubFun.getRegex("[\\d]*[^\\d]*[（|\\(]([^&]*)[）|\\)]", newPrice, 1);
						ticket.setPrice(price);
						ticket.setRemark(remark);
					}
					else{
						ticket.setPrice(price);
					}
					list.add(ticket);
				}
				map.put(showTime, list);
			}
		return map;
	}
	public static void main(String[] args) {
		ZhongxingPiaoWu spider=new ZhongxingPiaoWu();
		spider.setDao(new DataDao());
		spider.extract();
	}

}
