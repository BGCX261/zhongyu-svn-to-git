package com.piaoyou.crawler.spider;

import java.sql.SQLException;
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
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

/**
 * 51票子网 116
 * @author jinquan
 * @date   2011-6-23
 */
public class Piao51ZiTicketSpider  extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(Piao51ZiTicketSpider.class);
	static final String HOME_PAGE="http://www.51piaozi.com/PageList/PageAll.htm";
	static final Map <Integer,String>typeMap=new HashMap<Integer,String>();
	public static void main(String[] args) {
		Piao51ZiTicketSpider piao51ZiTicketSpider=new Piao51ZiTicketSpider();
		piao51ZiTicketSpider.setDao(new DataDao());
		piao51ZiTicketSpider.extract();
	}
	private static void initShowType(){
		typeMap.put(ShowType.CONCERT, "http://www.51piaozi.com/PageList/Page1.htm");
		typeMap.put(ShowType.SHOW, "http://www.51piaozi.com/PageList/Page2.htm");
		typeMap.put(ShowType.DRAMA, "http://www.51piaozi.com/PageList/Page3.htm");
		typeMap.put(ShowType.DANCE, "http://www.51piaozi.com/PageList/Page4.htm");
		typeMap.put(ShowType.CHILDREN, "http://www.51piaozi.com/PageList/Page5.htm");
		typeMap.put(ShowType.SPORTS, "http://www.51piaozi.com/PageList/Page7.htm");
	}

	@Override
	public void extract() {
		initShowType();
		Iterator iter = typeMap.entrySet().iterator();
		//类型循环
        while(iter.hasNext()){
        	try{
                Entry<Integer, String> entry = (Entry<Integer, String>) iter.next();
                Integer key =  entry.getKey();
                String v = entry.getValue();
                Document doc=getDoc(v);
                int pageCount=1;
                String EVENTTARGET="";
                String VIEWSTATE="";
                Map<String,String> dataMap=new HashMap<String,String>();
                if(!doc.getElementsByClass("pages").isEmpty()){
                	String temp=doc.getElementsByClass("pages").get(0).getElementsByTag("a").last().attr("href");
                	EVENTTARGET=temp.substring(temp.indexOf("'")+1,temp.indexOf(",")-1);
                	VIEWSTATE=doc.getElementsByAttributeValue("name", "__VIEWSTATE").attr("value");
                	pageCount=Integer.parseInt(temp.substring(temp.indexOf("','")+3,temp.length()-2));
                	dataMap.put("__EVENTTARGET", EVENTTARGET);
                	dataMap.put("__VIEWSTATE", VIEWSTATE);
                }
                for(int i=1;i<=pageCount;i++){
                	//不是第一页需要获取数据
                	if(i>1){
                		dataMap.put("__EVENTARGUMENT", String.valueOf(i));
                		doc=getDocPost(v,dataMap);
                	}
                	for(Element els:doc.getElementsByClass("tt")){
                		try{
                			String onePageUrl=els.getElementsByTag("a").first().absUrl("href");
                    		String showName=els.getElementsByTag("img").first().attr("alt");
                    		extractEach( onePageUrl, showName, key);
                		}catch(Exception e){
                			log.error(Const.SHOW_ONE_PAGE_URL_ERRO,e);
                    		continue;
                		}
                	}
                }
        	}catch(Exception e){
        		log.error(Const.SHOW_TYPE_ERRO,e);
        		continue;
        	}
        }
	}
	private void extractEach(String onePageUrl,String showName,int key){
		TicketPrice price=null;
		List<TicketPrice> list=null;
		Show show=null;
		Map<String,List<TicketPrice>> timeAndPrice=null;
		Document doc=getDoc(onePageUrl);
		show=new Show();
		try{
			String photoUrl=doc.getElementById("im").absUrl("src");
			String showInfo=PubFun.cleanElement(doc.getElementsByClass("ycInfor").first()).html();
			String siteName=doc.getElementsByClass("pdInforR").first().getElementsByTag("span").first().text().replace("场馆：", "");
			show.setImage_path(photoUrl);
			show.setIntroduction(showInfo);
			show.setName(showName);
			show.setSiteName(siteName);
			show.setType(key);
			show.setAgent_id("116");
		}catch(Exception e){
			log.error(Const.concat(onePageUrl,showName,key),e);
    		return;
		}
		int c=0;
		timeAndPrice=new HashMap<String, List<TicketPrice>>();
		for(Element dateEls:doc.getElementsByClass("items").first().getElementsByTag("li")){
			try{
				String date=dateEls.getElementsByTag("a").first().attr("ddt");
				list=new ArrayList<TicketPrice>();
				//第一个时间不需要再获取价格数据
				if(c==0){
					for(Element priceEls:doc.getElementsByClass("pdp").first().getElementsByTag("a")){
						try{
							price=new TicketPrice();
							price.setPrice(priceEls.attr("jiage"));
							if(priceEls.attr("status").equals("1")){
								price.setExist(false);
							}else{
								price.setExist(true);
							}
						    price.setDetailURL(onePageUrl);
						    price.setMainURL(onePageUrl);
						    list.add(price);
						}catch(Exception e){
							log.error(Const.concat(onePageUrl,showName,key),e);
							continue;
						}
					}
				}else{
					try{
						//再get请求
						price=new TicketPrice();
						String sid=dateEls.getElementsByTag("a").first().attr("id");
						doc=getDoc("http://www.51piaozi.com/Handler/Price.ashx?sid="+sid+"&date="+date.replace(" ", "%20"));
						for(Element priceEls:doc.getElementsByTag("table")){
						    if(priceEls.getElementsByTag("status").first().text().equals("1")){
						    	price.setExist(false);
							}else{
								price.setExist(true);
							}
						    price.setPrice(priceEls.getElementsByTag("price").first().text());
						    price.setDetailURL(onePageUrl);
						    price.setMainURL(onePageUrl);
						    list.add(price);
						}
					}catch(Exception e){
						log.error(Const.concat(onePageUrl,showName,key),e);
						continue;
					}

				}
				c++;
				timeAndPrice.put(PubFun.dateFormat(date, true),list);
			}catch(Exception e){
				log.error(Const.SHOW_DATE_ERRO,e);
				continue;
			}
		}
		show.setTimeAndPrice(timeAndPrice);
		try {
			getDao().saveShow(show);
		} catch (SQLException e) {
			log.error(e.getMessage());
		}
	}

}
