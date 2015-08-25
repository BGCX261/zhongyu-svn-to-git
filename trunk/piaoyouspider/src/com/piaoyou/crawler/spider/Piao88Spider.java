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

public class Piao88Spider extends TicketSpider implements SpiderTask {
    private static Log log=LogFactory.getLog(Piao88Spider.class);
	@Override
	public void extract() {
		Document doc=getDoc("http://www.piao88.com/all/");
        Iterator<Element> tableList=doc.select("table.tableCss").iterator();
		while (tableList.hasNext()) {
			Element tableEle = tableList.next();
			String sort = tableEle.select("tr:eq(0) a").text().trim();
			int sortType = 9;
			if (sort.contains("演唱")) {
				sortType = 1;
			} else if (sort.contains("音乐会") && !sort.contains("剧")&& !sort.contains("国家大剧院")) {
				sortType = 2;
			} else if ((sort.contains("话剧") || sort.contains("歌剧") || sort.contains("剧"))&& !sort.contains("国家大剧院")) {
				sortType = 3;
			} else if (sort.contains("芭蕾") || sort.contains("舞蹈")|| sort.contains("舞")) {
				sortType = 4;
			} else if (sort.contains("戏曲") || sort.contains("二人转")|| sort.contains("魔术") || sort.contains("杂技")|| sort.contains("相声") || sort.contains("小品")|| sort.contains("马戏")) {
				sortType = 5;
			} else if (sort.contains("儿童")) {
				sortType = 6;
			} else if (sort.contains("体育")) {
				sortType = 7;
			} else if (sort.contains("旅游") || sort.contains("电影")) {
				sortType = 8;
			} else if (sort.contains("音乐厅")) {
				sortType = 2;
			} else if (sort.contains("歌剧院")) {
				Iterator<Element> eleshowList = tableEle.select("tr:gt(1) td:eq(0) a").iterator();
				while (eleshowList.hasNext()) {
					Element eleShow = eleshowList.next();
					String name = eleShow.text().trim();
					if (name.contains("音乐会")) {
						sortType = 2;
					}
					if (name.contains("芭蕾") || name.contains("舞")) {
						sortType = 4;
					}
					if (name.contains("京剧")) {
						sortType = 5;
					} else if (name.contains("演唱会")) {
						sortType = 1;
					} else if (name.contains("歌剧")) {
						sortType = 3;
					}
					String url = eleShow.absUrl("href").trim();
					processConcentratPage(url, sortType);
				}
				continue;
			} else if (sort.contains("戏剧场")) {
				sortType = 5;
			} else if (sort.contains("小剧场")) {
				Iterator<Element> eleshowList = tableEle.select("tr:gt(1) td:eq(0) a").iterator();
				while (eleshowList.hasNext()) {
					Element eleShow = eleshowList.next();
					String name = eleShow.text().trim();
					if (name.contains("音乐会")) {
						sortType = 2;
					} else if (name.contains("芭蕾")) {
						sortType = 4;
					} else if (name.contains("京剧")) {
						sortType = 5;
					} else if (name.contains("演唱会")) {
						sortType = 1;
					} else if (name.contains("话剧")) {
						sortType = 3;
					}
					String url = eleShow.absUrl("href").trim();
					processConcentratPage(url, sortType);
				}
				continue;
			}
			Iterator<Element> eleshowList = tableEle.select("tr:gt(1) td:eq(0) a").iterator();
			while (eleshowList.hasNext()) {
				Element eleShow = eleshowList.next();
				String url = eleShow.absUrl("href").trim();
				try {
					processConcentratPage(url, sortType);
				} catch (Exception ee) {
					log.error(Const.concat(url, sortType), ee);
				}
			}
		}
	}

	public void processConcentratPage(String url, int sort) {
		Document doc = getDoc(url);
		String title = doc.select(".ycCont div.cont h1").text().trim();
		String showSite;
		Element span = doc.select(".ycCont div.cont>span").first();
		if (span == null) {
			Element site = doc.select(".ycCont div.cont p:eq(2) a[href]").first();
			if (null == site || "".equals(site.text().trim())|| site.text().trim().contains("待定")){
				return;
			}
			showSite = site.text().trim();// 演出场馆
		} else {
			Element site = doc.select(".ycCont div.cont p:eq(3) a[href]").first();
			if (null == site || "".equals(site.text().trim())|| site.text().trim().contains("待定")){
				return;
			}
			showSite = site.text().trim();// 演出场馆
		}

		String imagePath = doc.select(".ycCont div.img img").first().absUrl(
				"src").trim();
		String summary = PubFun.cleanElement(doc.select("#artibody").first())
				.html();
		Map<String, List<TicketPrice>> map = getMap(doc, url);
		Show show = new Show();
		show.setAgent_id("35");
		show.setImage_path(imagePath);
		show.setSiteName(showSite);
		show.setType(sort);
		show.setName(title);
		show.setIntroduction(summary);
		show.setTimeAndPrice(map);
		try {
			getDao().saveShow(show);
		} catch (Exception ee) {

		}

	}
	public  Map<String,List<TicketPrice>> getMap(Document doc,String url){
	    	Map<String,List<TicketPrice>> map=new HashMap<String,List<TicketPrice>>();
	    	List<Element> liEleList=doc.select(".cont ul.order li:gt(2)");
	    	if(liEleList==null ||liEleList.size()==0)
	    		return map;
	    	if(liEleList.size()<3)
	    		return map;
	    	for(int i=0;i<liEleList.size();i=i+3){
	    		 int j=i;
		    		Element eleLi=liEleList.get(j+1);
		    		String showTime=eleLi.text().trim();//2011年07月09日 19:30 //2011-12-24(星期六) 19:30
		    		if(showTime.contains("年")&&showTime.contains("月") &&showTime.contains("日")){
		    		   showTime=showTime.replace("年", "-").replace("月", "-").replace("日", "").replaceAll("\\([^\\)]*\\)", "");
		    		}
		    		showTime=showTime.replaceAll("\\([^\\)]*\\)", "");
		    		String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
			    	if(newshowTime!=null){
			    		showTime=newshowTime;
			    	}
		    		List<TicketPrice> list=new ArrayList<TicketPrice>();
		    		Element elePriceLi=liEleList.get(j+2);
		    		Iterator<Element> eleYesPriceList=elePriceLi.select("li>a").iterator();
		    		while(eleYesPriceList.hasNext()){
		    			TicketPrice ticket=new TicketPrice();
		    			Element eleYes=eleYesPriceList.next();
		    			String price=eleYes.text().trim();
		    			String remark=null;
		    			if(PubFun.isMatchRegex(price, "[\\d]+[元]?[^$]+")){
	    					String newPrce=price;
	    					price=PubFun.getRegex("([\\d]+)[元]?([^$]*)", newPrce,1);
	    					remark=PubFun.getRegex("([\\d]+)[元]?([^$]*)", newPrce, 2);
	    				}
	    				if(price.contains("结束")||price.contains("待定")){
	    					continue;
	    				}
	    				price=price.replace("元", "");
		    			String href=eleYes.absUrl("href").trim();
		    			ticket.setExist(true);
		    			ticket.setMainURL(url);
		    			ticket.setDetailURL(href);
		    			ticket.setPrice(price);
		    			ticket.setRemark(remark);
		    			list.add(ticket);
		    		}
		    		Iterator<Element> eleNoPriceList=elePriceLi.select("span").iterator();
	                while(eleNoPriceList.hasNext()){
	                    	TicketPrice ticket=new TicketPrice();
		    				Element eleYes=eleNoPriceList.next();
		    				String price=eleYes.text().trim();
		    				//price=price.replace("元", "");
		    				String remark=null;
		    				if(PubFun.isMatchRegex(price, "[\\d]+[元]?[^$]+")){
		    					String newPrce=price;
		    					price=PubFun.getRegex("([\\d]+)[元]?([^$]*)", newPrce,1);
		    					remark=PubFun.getRegex("([\\d]+)[元]?([^$]*)", newPrce, 2);
		    				}
		    				if(price.contains("结束")||price.contains("待定")){
		    					continue;
		    				}
		    				price=price.replace("元", "");
		    				String href=eleYes.absUrl("href").trim();
		    				ticket.setExist(false);
		    				ticket.setMainURL(url);
		    				ticket.setDetailURL(href);
		    				ticket.setPrice(price);
		    				ticket.setRemark(remark);
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
		Piao88Spider spider=new Piao88Spider();
	  spider.setDao(new DataDao());
		spider.extract();
		//spider.processConcentratPage("http://www.piao88.com/grsport/1133.html", 1);
	}

}
