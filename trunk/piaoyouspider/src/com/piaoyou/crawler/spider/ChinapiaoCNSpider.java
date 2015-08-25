package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;
@SuppressWarnings("unused")
public class ChinapiaoCNSpider extends TicketSpider implements SpiderTask {
	
	private static final String BASE_URL = "http://www.chinapiao.com.cn/";
	private static final String BEIJING_URL ="http://www.chinapiao.com.cn/query.php";
	private static final String SHANGHAI_URL="";
	private static final Class<ChinapiaoCNSpider> BASE_CLASS=ChinapiaoCNSpider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	
	@Override
	public void extract() {
		Map<String,Integer> map =  new HashMap<String,Integer>();
		map = getURL();
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			try {
				parseEach(url, map.get(url));
			} catch (Exception e) {
				LogFactory.getLog(BASE_CLASS).error(Const.concat(url,map.get(url)),e);
			}
		}
		/*try {
			parseEach("",1);
		} catch (Exception e) {
			e.printStackTrace();
		}*/
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ChinapiaoCNSpider chinaSpider = new ChinapiaoCNSpider();
		chinaSpider.setDao(new DataDao());
		try {
			chinaSpider.parseEach("http://www.chinapiao.com.cn/ticket/8583.html", 1);
		} catch (Exception e) {
			e.printStackTrace();
		}
//		chinaSpider.extract();
	}
	
	public  Map<String,Integer> getURL(){
		Map<String,Integer> map = new HashMap<String,Integer>();
		Document document  = getDoc(BEIJING_URL);
		for(Element element :document.select("table[width=970]")){
				String showType = element.select("tr td[width=24]").text();
				if("演唱会".equals(showType)){
					for(Element ele:element.select("tr td table tr td a")){
						String showName = ele.text();
						if(!"".equals(showName)){
						    map.put(ele.attr("abs:href"), ShowType.CONCERT);
						}
					}
				}else if("话剧歌剧".equals(showType)||"国家大剧院・歌剧院".equals(showType)||"国家大剧院・戏剧场".equals(showType)){
					for(Element ele:element.select("tr td table tr td a")){
						String showName = ele.text();
						if(!"".equals(showName)){
							 map.put(ele.attr("abs:href"), ShowType.DRAMA);
						}
					}
				}else if("音乐会".equals(showType)||"国家大剧院・音乐厅".equals(showType)||"国家大剧院・小剧场".equals(showType)||"打开音乐之门".equals(showType)||"打开艺术之门".equals(showType)){
					for(Element ele:element.select("tr td table tr td a")){
						String showName = ele.text();
						if(!"".equals(showName)){
							 map.put(ele.attr("abs:href"), ShowType.SHOW);
						}
					}
				}else if("舞蹈芭蕾".equals(showType)){
					for(Element ele:element.select("tr td table tr td a")){
						String showName = ele.text();
						if(!"".equals(showName)){
							 map.put(ele.attr("abs:href"), ShowType.DANCE);
						}
					}
				}else if("戏曲综艺".equals(showType)||"梅兰芳大剧院演出季".equals(showType)||"魔术马戏".equals(showType)){
					for(Element ele:element.select("tr td table tr td a")){
						String showName = ele.text();
						if(!"".equals(showName)){
							 map.put(ele.attr("abs:href"), ShowType.OPERA);
						}
					}
				}else if("儿童亲子".equals(showType)){
					for(Element ele:element.select("tr td table tr td a")){
						String showName = ele.text();
						if(!"".equals(showName)){
							 map.put(ele.attr("abs:href"), ShowType.CHILDREN);
						}
					}
				}else if("运动休闲".equals(showType)){
					for(Element ele:element.select("tr td table tr td a")){
						String showName = ele.text();
						if(!"".equals(showName)){
							 map.put(ele.attr("abs:href"), ShowType.SPORTS);
						}
					}
				}else if("电影".equals(showType)){
					for(Element ele:element.select("tr td table tr td a")){
						String showName = ele.text();
						if(!"".equals(showName)){
							 map.put(ele.attr("abs:href"), ShowType.HOLIDAY);
						}
					}
				}
			}
		return map;
	}
	
	public  void parseEach(String url,int showType) throws Exception{
			Document document  = getDoc(url);
			Show show   = new Show();
			//代理商ID
			show.setAgent_id(agentID);
			//演出类型
			show.setType(showType);
			//宣传海报连接
			show.setImage_path(document.select("table tr td table.biaoge img").first().attr("abs:src"));
		    //演出名称
			show.setName(document.select("table tr td table tr td table tr td.font1").text());
			//演出简介
			String introduction= PubFun.cleanElement(document.select("table[width=558] tr td table[width=95%] tr  td.font_nei").first()).html();
			//售票状态
			String status = document.select("strong:contains(售票状态)").parents().first().childNode(1).toString().replaceAll("[:：：]", "");
			if("已出票".equals(status)){
				show.setStatus(1);
			}else if("未出票".equals(status)){
				show.setStatus(0);
			}
			//根据不同的格式获取信息
			if("".equals(introduction)){
		    	   introduction  = PubFun.cleanElement(document.select("div font[color=#003366]")).text();
		    }
			if("".equals(introduction)){
				introduction = PubFun.cleanElement(document.select("p font[color=#003366]")).text();
			}
			show.setIntroduction(introduction);
			//演出场馆
			String site = "";
			try{
			Elements eles = document.select("table[width=770] tr td table[width=558] tr td.font_nei:contains(场馆)");
			if(eles==null||eles.isEmpty()){
				  eles = document.select("table[width=770] tr td table[width=558] tr td.font_nei:contains(地点)");
			}
			site=eles.first().text();
			}catch(Exception e){
				try{
				site = document.select("span.f9-1").first().text();
				}catch(Exception e1){
					site = document.select("strong").text();
				}
			}
			try {
				if(site.contains("场馆")){
					site=site.substring(site.indexOf("场馆：")+3,site.indexOf("票价"));
				}
				if(site.contains("地点")){
					site=site.substring(site.indexOf("地点")+3,site.indexOf("票价"));
				}
			} catch (Exception e1) {
				return;
			}
			if("待定".equals(site.trim())){
				return;
			}
			show.setSiteName(site);
			Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
			List<TicketPrice> list = null ;
			TicketPrice ticketPrice = null;
			Elements eles =document.select("table tr td table tr[height=26]");
			for(Element ele :eles){
				String showTime =  ele.select("td").first().text();
				if(showTime.indexOf("星期")!=-1){
					showTime = showTime.replaceAll("[*\\s星期(一|二|三|四|五|六|日)]", " ");
				}
				showTime = showTime.replace("：", ":");
				showTime = showTime.replaceAll("\\.", "-");
				showTime = showTime.replaceAll(" +", " ");
				 String remk = "";
					try{
						  showTime = newDate.format(newDate.parse(showTime));
					}catch(Exception e){
					}
				list = new ArrayList<TicketPrice>();
				for(Element ele1 : ele.select("a")){
					String price = ele1.text();
					if("待定".equals(price.trim())){
							continue;
					}
					try {
						Integer.parseInt(price);
					} catch (NumberFormatException e) {
						continue;
					}
					ticketPrice = new TicketPrice();
					ticketPrice.setMainURL(url);
					ticketPrice.setDetailURL(ele1.attr("abs:href"));
					ticketPrice.setExist(true);
					ticketPrice.setPrice(price);
					if("span".equals(ele1.parent().nodeName())&&"span".equals(ele1.parent().nextElementSibling().nodeName())){
						ticketPrice.setRemark(ele1.parent().nextElementSibling().text());
					}else{
						ticketPrice.setRemark(remk);
					}
					list.add(ticketPrice);
				}
				for(Element ele1:ele.select("span.font7")){
					String price = ele1.text();
					if("待定".equals(price.trim())){
						continue;
					}
					ticketPrice = new TicketPrice();
					ticketPrice.setMainURL(url);
					ticketPrice.setExist(false);
					ticketPrice.setPrice(ele1.text());
					ticketPrice.setRemark(remk);
					list.add(ticketPrice);
				}
				if(list.isEmpty()||list.size()==0){
					continue;
				}
				timeAndPrice.put(showTime, list);
			}
			if(timeAndPrice.isEmpty()||timeAndPrice.size()==0){
				return;
			}
			show.setTimeAndPrice(timeAndPrice);
			getDao().saveShow(show);
	}
}
