
package com.piaoyou.crawler.spider;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

/**
 * 中国票务网  8
 * @author jinquan
 * @date   2011-6-23
 */
public class ZhongGuoTicketSpider  extends TicketSpider implements SpiderTask{
//http://www.piao.com/index.php?app=search&cate_id=1210
	private static final Log log = LogFactory.getLog(ZhongGuoTicketSpider.class);
	static final String ZHONGGUO_HOME_PAGE="http://www.piao.com/index.php?app=search&cate_id=1210";
	static final  Map<String, Integer> typeMap=new HashMap<String, Integer>();
	static{initShowType();}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ZhongGuoTicketSpider zhongGuo=new ZhongGuoTicketSpider();
		zhongGuo.setDao(new DataDao());
		zhongGuo.extract();

	}
	/**
	 * 初始化演出类型
	 */
	private static void initShowType(){
		typeMap.put("1217", 1);//演唱会
		typeMap.put("1218", 2);//音乐会
		typeMap.put("1220", 3);//话剧
		typeMap.put("1223", 4);//芭蕾
		typeMap.put("1260", 4);//舞蹈
		typeMap.put("1224", 5);//戏曲
		typeMap.put("1225", 5);//杂技
		typeMap.put("1227", 6);//儿童亲子
		typeMap.put("1222", 4);//舞剧
		typeMap.put("1228", 9);//其他
		typeMap.put("1226", 5);//相声
		typeMap.put("1219", 3);//音乐剧
		typeMap.put("1221", 3);//歌剧
	}

	@SuppressWarnings("unchecked")
	@Override
	public void extract() {
		Document doc=getDoc(ZHONGGUO_HOME_PAGE);
		try{
			int pageCount=0;
			String pageUrl=doc.getElementsByClass("page_hover").get(0).absUrl("href").substring(0,doc.getElementsByClass("page_hover").get(0).absUrl("href").length()-1);
			pageCount=Integer.parseInt(doc.getElementsByClass("nonce").get(0).text().split("/")[1].trim());
			String onePageUrl="";
			for(int i=1;i<=pageCount;i++){
				if(i!=1){
					doc=getDoc(pageUrl+i);
				}
				Elements els=doc.getElementsByClass("right_con_botfont");
				for(int x=0;x<els.size();x++){
					try{
						String showName=els.get(x).getElementsByTag("span").get(0).getElementsByTag("a").get(0).text();
//						if(showName.equals("北京老舍茶馆")){
//							continue;
//						}
						String siteName=els.get(x).getElementsByTag("ul").get(0).getElementsByTag("li").get(1).getElementsByTag("a").get(0).text().trim();
						onePageUrl=els.get(x).getElementsByTag("span").get(0).getElementsByTag("a").get(0).absUrl("href");
						extractEach(onePageUrl,showName,siteName);
					}catch(Exception e){
						log.error(Const.SHOW_ONE_PAGE_URL_ERRO,e);
						continue;
					}
				}
			}
		}catch(Exception e){
			log.error(Const.SHOW_PAGE_ERRO,e);
			return;
		}
	}
	private void extractEach(String onePageUrl,String showName,String siteName){
		
		TicketPrice price=null;
		List<TicketPrice> list=null;
		Show show=null;
		Map<String,List<TicketPrice>> timeAndPrice=null;
		Document docOne=getDoc(onePageUrl);
		show=new Show();
		try{
			String typeTemp=docOne.getElementsByClass("nav_bot_font").get(0).getElementsByTag("a").last().absUrl("href");
			typeTemp=typeTemp.substring(typeTemp.lastIndexOf("=")+1,typeTemp.length()).trim();
			show.setName(showName);
			show.setSiteName(siteName);
			show.setType(typeMap.get(typeTemp));
			show.setImage_path(docOne.getElementsByClass("main_left_img").get(0).getElementsByTag("img").get(0).absUrl("src"));
			show.setIntroduction(PubFun.cleanElement(docOne.getElementsByClass("cont_right_cont").get(0).children()).toString());
			show.setAgent_id("8");
		}catch(Exception e){
			log.error(Const.concat(onePageUrl,showName,siteName),e);
			return;
		}
		//得到所有日期价格
		Elements elsPrice=docOne.getElementsByClass("cont_right_bor").get(0).getElementsByAttributeValue("ectype", "showtimes");
		timeAndPrice=new HashMap<String,List<TicketPrice>>();
		for(int z=0;z<elsPrice.size();z++){
			Map <String,String>map=new HashMap<String,String>();
			try{
				//单票
				String[] tdValue=elsPrice.get(z).getElementsByTag("td").get(3).text().split(" ");
				//套票 
				String[] tdValues=elsPrice.get(z).getElementsByTag("td").get(4).text().split(" ");
				for(int v=0;v<tdValue.length;v++){
					map.put(tdValue[v], "");
				}
				if(tdValues.length==1&!"暂无套票".equals(tdValues[0].trim())){
					for(int v=0;v<tdValues.length;v++){
						map.put(tdValues[v], "");
					}
				}
				if(!elsPrice.get(z).getElementsByTag("td").get(3).getElementsByTag("a").isEmpty()){
					String[] aValue=elsPrice.get(z).getElementsByTag("td").get(3).getElementsByTag("a").text().split(" ");
					for(int v=0;v<aValue.length;v++){
						map.put(aValue[v], elsPrice.get(z).getElementsByTag("td").get(3).getElementsByTag("a").get(v).absUrl("href"));
					}
				}
				if(!elsPrice.get(z).getElementsByTag("td").get(4).getElementsByTag("a").isEmpty()){
					String[] aValue=elsPrice.get(z).getElementsByTag("td").get(4).getElementsByTag("a").text().split(" ");
					for(int v=0;v<aValue.length;v++){
						map.put(aValue[v], elsPrice.get(z).getElementsByTag("td").get(4).getElementsByTag("a").get(v).absUrl("href"));
					}
			}
			
			}catch(Exception e){
				log.error(Const.concat(onePageUrl,showName,siteName),e);
				continue;
			}
			list= new ArrayList<TicketPrice>();
			 Iterator<?> it = map.entrySet().iterator();   
			    while (it.hasNext()) {
			    	try{
			    		price=new TicketPrice();
				        Map.Entry entry = (Map.Entry) it.next();   
				        String key = PubFun.ToDBC((String)entry.getKey());   
				        String value = (String)entry.getValue();
				        if(key.indexOf("(")!=-1){
				        	 price.setPrice(key.substring(0,key.indexOf("(")));
				        	 price.setRemark(key.substring(key.indexOf("("),key.length()));
				        }else{
				        	 
				        	 if(PubFun.isMatchRegex(key,"\\d*\\D*")){
				        		 price.setPrice(key.replaceAll("\\D*", ""));
				        		 price.setRemark(key.replace(price.getPrice(), ""));
				        	 }else{
				        		 price.setPrice(key);
				        	 }
				        }
				        price.setMainURL(onePageUrl);
				        if("".equals(value)){
				        	price.setExist(false);
				        }else{
				        	price.setExist(true);
				        	price.setDetailURL(value);
				        }
				        list.add(price);
			    	}catch(Exception e){
			    		log.error(Const.concat(onePageUrl,showName,siteName),e);
						continue;
			    	}
			    }
			    try{
			    	  String dateTemp=PubFun.ToDBC(elsPrice.get(z).getElementsByTag("td").get(0).text());
					    List<String> dateList=new ArrayList<String>();
					    //跨度实时间
					    if(dateTemp.indexOf("~")!=-1){
//					    	String startDate=dateTemp.split("~")[0].trim();
//					    	String endDate=dateTemp.split("~")[1].trim();
//					    	dateList=PubFun.getTwoDateAllDate(startDate, endDate, "", "yyyy-MM-dd");
					    	dateList.add(dateTemp);
					    }else{
					    	dateList.add(dateTemp.substring(0,dateTemp.indexOf("(")));
					    }
					    for(int u=0;u<dateList.size();u++){
					    	timeAndPrice.put(dateList.get(u),list);
					    }
			    }catch(Exception e){
			    	log.error(Const.concat(onePageUrl,showName,siteName),e);
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
