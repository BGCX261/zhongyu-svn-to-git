package com.piaoyou.crawler.big;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

public class Yongle2Spider extends TicketSpider implements SpiderTask {

	
	private static final Log log = LogFactory.getLog(Yongle2Spider.class);
    public static HashMap<String,String> map=new HashMap<String,String>();
	public static String encode="utf-8";
	public static String preUrl="http://www.228.com.cn";
	static{
		map.put("南京", "http://www.228.com.cn/nj/ticketmenu.do?areaid=025&tag=Bb");//问题
		map.put("成都", "http://www.228.com.cn/cd/ticketmenu.do?areaid=028&tag=Bb");///问题
		map.put("重庆", "http://www.228.com.cn/cq/ticketmenu.do?areaid=023&tag=Bb");//问题
	    map.put("广州", "http://www.228.com.cn/gz/ticketmenu.do?areaid=020&tag=Bb");//问题
		map.put("哈尔滨", "http://www.228.com.cn/heb/ticketmenu.do?areaid=0451&tag=Bb");//问题
	}
	@Override
	public void extract()  {
	    java.util.Iterator<Entry<String,String>> it = map.entrySet().iterator();
		while(it.hasNext()){	
			Entry<String,String> entry = (Entry<String,String>)it.next();
			String city=(String)entry.getKey();//得到要的城市
			String url=(String)entry.getValue();//得到Url
			 Document firstdoc=getDoc(url);
			 if(null!=firstdoc){
			     //获取首页的文档对象
			      Element ulEle= firstdoc.select("div.layout ul[class*=search_table p_b_20]").first();
			      if(ulEle==null){
			    	  log.error(url+"Element ulEle= firstdoc.select(\"div.layout ul[class*=search_table p_b_20]\").first();"+"此程序语句有问题"); 
			      }
			      Iterator<Element> liEles=ulEle.select("li").iterator();
			      int i=0;
			      boolean isexit=false;
			      int sortType=8;
			      while(liEles.hasNext()){
			    	 i++;
			    	 Element liEle=liEles.next();
			    	 String  sort=null;
			    	 if(PubFun.isOdd(i)){
			    		sort=liEle.select("span a").first().attr("name");//取出分类名称
			    		if(sort==null)
			    			continue;
			    		if(sort.contains("宫") ||sort.contains("剧场") ||sort.contains("馆") ||sort.contains("中心") ){	
			    			isexit=true;
			    		    continue;
			    		}
			    		if(sort.contains("演唱")){
							sortType=1;
						}
			    		else if((sort.contains("音乐")&&!sort.contains("剧"))){
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
						else if(sort.contains("休闲")||sort.contains("旅游")||sort.contains("电影通票") ){
							sortType=8;
						}
						else if(sort.contains("综艺晚会") ){
							sortType=9;
						}
						else
							sortType=9;
			    	    }
			    	   else{
			    		   if(isexit){
			    			   isexit=false;
			    			   continue; 
			    		   }
			    		Iterator<Element> trEles=liEle.select("tr").not(".table_title").iterator();//获取tr级元素具体到场馆
			    		while(trEles.hasNext()){
			    			Element showInfoEle=trEles.next();	
			    			String showName=showInfoEle.select("td.width380 a[href]").first().text();
			    			String concentrateUrl=preUrl+showInfoEle.select("td a").first().attr("href");//具体的链接页面
			    			String site=showInfoEle.select("td.width140").first().text().trim();//取演出场馆
			    			if(site.contains("待定")||"".equals(site))
			    				continue;
			    		    try{
			    		    	processLastPage(concentrateUrl,showName,site,city,sortType);
			    		    }
			    		    catch(Exception ee){
			    		    	log.error(Const.concat(concentrateUrl,showName,site,city,sortType),ee);
			    		    }
			    		}
			    	 }
			      }
			 }
			 else{
				log.error(url+"该页面内容为空");
			 }//if
		}//while
	}//extract
	
	public void processLastPage(String concentrateUrl,String showName,String site,String city,int sortType){
		Document concentrateDoc=getDoc(concentrateUrl);
		String imgUrl="";//演出图片
		String summary="";//摘要
		boolean isFirstAgent=false;
		Element style=concentrateDoc.select("img[src~=(?i)/[\\w]*/images/ticket_actfor.jpg]").first();
		if(style!=null){
		   isFirstAgent=true;
	    }
		else{
			 isFirstAgent=false;
		}
		String seatUrl=null;
		try{
			seatUrl=concentrateDoc.select("li.right_text.line24.p_t_10>a:containsOwn(票区图)").first().absUrl("href").trim();
		}
		catch(Exception ee){
			
		}
		site=concentrateDoc.select("ul.product>li.right_text.line24").get(1).ownText().trim().replace("［］", "").replace("场馆：", "").replace("场馆", "").replace("：", "").replace(":", "").replace(" ", "");
		imgUrl=concentrateDoc.select("ul.layout li.left_1 ul.product li.actor_img img").first().attr("src");
		summary=PubFun.cleanElement(concentrateDoc.select("ul.layout li.left_1 ul li[class*=actout_text line20]:has(p)").first()).text();
		Show show=new Show();//保存演出信息
		show.setSeatPic_path(seatUrl);
	    show.setCity(city);
	    show.setImage_path(imgUrl);
	    show.setIntroduction(summary);
	    show.setName(showName);
	    show.setSiteName(site);
	    show.setType(sortType);
	    show.setAgent_id("27");
	    show.setWatermark(true);
	    show.setFirstAgent(isFirstAgent);
	    Map<String, List<TicketPrice>> timeAndPrice = processMap(city,
	    		concentrateUrl, showName, concentrateUrl, site,
				concentrateDoc);
	    show.setTimeAndPrice(timeAndPrice);
	    try{
	        getDao().saveShow(show);
	    }catch(Exception ee){
	    	
	    }
	}
	private Map<String, List<TicketPrice>> processMap(String city, String url,
			String showName, String concentrateUrl, String site,
			Document concentrateDoc) {
		    Map<String,List<TicketPrice>> timeAndPrice=new HashMap<String,List<TicketPrice>>();
		    Iterator<Element> test1DivEles=concentrateDoc.select("li#test1 div").iterator();
		    while(test1DivEles.hasNext()){
			Element test1DivEle=test1DivEles.next();
			String showTime=test1DivEle.select("span.span_2").first().text().trim();//演出时间 
			showTime=showTime.replaceAll("\\.", "-");
			showTime=showTime.replace("：", ":");
			String newshowTime=PubFun.parseDateToStr(PubFun.parseStringToDate(showTime, "yyyy-MM-dd HH:mm"), "yyyy-MM-dd HH:mm");
			if(newshowTime!=null)
				showTime=newshowTime;
			List<TicketPrice> list=new ArrayList<TicketPrice>();
			Iterator<Element> ExistsPriceEles=test1DivEle.select("span[id~=p__j__[\\d]*] a[href]").iterator();
			String localURL = concentrateDoc.select("form").first().attr("action").split("/")[1];
			while(ExistsPriceEles.hasNext()){
				TicketPrice ticketPrice=new TicketPrice();
				ticketPrice.setExist(true);
				Element aEle=ExistsPriceEles.next();
				ticketPrice.setMainURL(concentrateUrl);
				String conUrl=aEle.attr("href");
			    String price=aEle.text().trim();//取票价
//				String preStr="";
//				if(city.equals("哈尔滨")){
//					preStr=url.substring(0,25);
//				}
//				else{
//					preStr=url.substring(0,24);
//				}
				String urlPrice=preUrl+"/"+localURL+conUrl;
				ticketPrice.setDetailURL(urlPrice);
			    String realPrice="";
			    String remark;
				if(PubFun.isMatchRegex(price.trim().toLowerCase(), "[^v]*vip[\\d]*")){
					realPrice=getRealPrice(urlPrice,showName,site);
					remark=price.trim();
				}
				else{
					realPrice=PubFun.getRegex("[^\\d]*([\\d]*)[^\\(]*", price, 1);
					remark=PubFun.getRegex("[\\d]*\\(([^\\)]*)\\)", price, 1);
				}
				ticketPrice.setPrice(realPrice);
				ticketPrice.setRemark(remark);
				list.add(ticketPrice);
			}
		    String test1DivElestr =test1DivEle.select("span[id~=(?i)p__j__[\\d]?]").first().html();//取没有票的
			if(test1DivElestr==null ||test1DivElestr.equals(""))
			               continue;
			String notExistsEles=test1DivElestr.replaceAll("<a[^>]*><font[^>]*>[^<]*</font></a>&nbsp;", "");
			if(notExistsEles!=null){
				notExistsEles=notExistsEles.replaceAll("<a[^>]*>[^<]*</a>&nbsp;", "");
			}
			String[] notExistsPriceArray=notExistsEles.split("&nbsp;");
			for(int count=0;count<notExistsPriceArray.length;count++){
				String price=notExistsPriceArray[count];
				if(null!=price &&!"".equals(price.trim())){
					TicketPrice ticketPrice=new TicketPrice();
					ticketPrice.setExist(false);
					ticketPrice.setMainURL(concentrateUrl);
					String norealPrice=PubFun.getRegex("[^\\d]*([\\d]*)[^\\(]*", price, 1);
					String noremark=PubFun.getRegex("[\\d]*\\(([^\\)]*)\\)", price, 1);
					if(norealPrice==null)norealPrice=price;
					ticketPrice.setPrice(norealPrice);
					ticketPrice.setRemark(noremark);
					list.add(ticketPrice);
				}
			}
			timeAndPrice.put(showTime, list);
		}
		return timeAndPrice;
	}
	/**
	 * @param args
	 */
	
	public String  getRealPrice(String url,String title,String showsite ){
		Document priceDoc=getDoc(url);
		String realPrice="";
		Iterator<Element> priceList=priceDoc.select("li.cart_table table tr").not("tr.table_title_1").iterator();
		while(priceList.hasNext()){
			Element elePri=priceList.next();
		    List<Element> listTd=elePri.select("td");
			String name=listTd.get(0).children().first().text().trim();
			String site=listTd.get(1).children().first().text().trim();
			realPrice=listTd.get(3).text().trim();
		    if(title.equals(name)&& showsite.equals(site)){
		    	return realPrice;
		    }
		}
		return realPrice;
	}
	public static void main(String[] args) {
		 Yongle2Spider yongle2 = new Yongle2Spider();
		 yongle2.setDao(new DataDao());
		 yongle2.processLastPage("http://www.228.com.cn/product/023detail-45465.html", "“Oh, Mamma Mia！”ABBA-2012幸福新春音乐会", "重庆大剧院", "2", 1);
	}

}
