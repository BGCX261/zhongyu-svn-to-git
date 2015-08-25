package com.piaoyou.crawler.big;

import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class MypiaoSpider  extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(MypiaoSpider.class);
	private static final String BASE_URL = "http://www.mypiao.com/";
	private static final Class<MypiaoSpider> BASE_CLASS=MypiaoSpider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
	.get(0).get("agency_id").toString();
	protected static int timeout = 4*50*1000; 
	//定义一个内部类，通过gson获取相关票价信息
	class Price{
		private String otherticket_id;
		private String datetime;
		private List<String> prices;
		private List<String> left;
		private boolean selling;
		
		public String getOtherticket_id() {
			return otherticket_id;
		}
		public void setOtherticket_id(String otherticketId) {
			otherticket_id = otherticketId;
		}
		public String getDatetime() {
			return datetime;
		}
		public void setDatetime(String datetime) {
			this.datetime = datetime;
		}
		public List<String> getPrices() {
			return prices;
		}
		public void setPrices(List<String> prices) {
			this.prices = prices;
		}
		public List<String> getLeft() {
			return left;
		}
		public void setLeft(List<String> left) {
			this.left = left;
		}
		public boolean isSelling() {
			return selling;
		}
		public void setSelling(boolean selling) {
			this.selling = selling;
		}
	}
	@Override
	public void extract() {
		Map<String,Integer> map = this.getURL();
		for (Iterator<String> iterator = map.keySet().iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			
			try {
				extractEach(url,map.get(url));
			} catch (SQLException e) {
				log.error("数据库错误"+e.getMessage()+ url);
			}
			catch (IOException e) {
				log.error("IO异常"+e.getMessage()+ url);
			}
			catch (ParseException e) {
				log.error("解析错误"+e.getMessage()+ url);
			}
			catch (NullPointerException e) {
				log.error("解析空指针错误"+ url,e);
			}
			catch (Exception e) {
				log.error("其他错误"+e.getMessage()+ url);
			}
		}
		
//		try {
//			parseEach("",1);
//		} catch (Exception e) {
//			e.printStackTrace();
//	}
		
	}
	public  Map<String,Integer> getURL(){
		 Map<String ,Integer>  mainURL = new HashMap<String,Integer>();
		 Map<String,Integer> detailURL = new HashMap<String,Integer>();
		 
		 
		 Map<String,Integer> tmp = new HashMap<String,Integer>();
		 tmp.put("http://www.mypiao.com/all_performance/1/Show.showtime2/asc/5/3", ShowType.CONCERT);  //演唱会
		 tmp.put("http://www.mypiao.com/all_performance/1/Show.showtime2/asc/6/3", ShowType.DRAMA);//话剧
		 tmp.put("http://www.mypiao.com/all_performance/1/Show.showtime2/asc/9/3", ShowType.SHOW);//音乐剧
		 tmp.put("http://www.mypiao.com/all_performance/1/Show.showtime2/asc/8/3", ShowType.OPERA);//戏曲
		 tmp.put("http://www.mypiao.com/all_performance/1/Show.showtime2/asc/3/3", ShowType.SPORTS);//体育
		 
		 mainURL.put("http://www.mypiao.com/all_performance/1/Show.showtime2/asc/5/3", ShowType.CONCERT);  //演唱会
		 mainURL.put("http://www.mypiao.com/all_performance/1/Show.showtime2/asc/6/3", ShowType.DRAMA);//话剧
		 mainURL.put("http://www.mypiao.com/all_performance/1/Show.showtime2/asc/9/3", ShowType.SHOW);//音乐剧
		 mainURL.put("http://www.mypiao.com/all_performance/1/Show.showtime2/asc/8/3", ShowType.OPERA);//戏曲
		 mainURL.put("http://www.mypiao.com/all_performance/1/Show.showtime2/asc/3/3", ShowType.SPORTS);//体育
		 
		 for (Iterator<String> iterator = tmp.keySet().iterator(); iterator.hasNext();) {
			String url = (String) iterator.next();
			Document doc = getDoc(url);
			for(Element element :doc.select("div.title a")){
				 detailURL.put(element.attr("abs:href"), mainURL.get(url));
			}
			while(doc.select("div.pagination a:contains(下一页)").size()>0){
				String nextPage = doc.select("div.pagination a:contains(下一页)").first().attr("abs:href");
				doc = getDoc(nextPage);
					for(Element element :doc.select("div.title a")){
						 detailURL.put(element.attr("abs:href"), mainURL.get(url));
					}
			}
		}
		 return detailURL;
	}
	
	/**
	 * @param url
	 * @param showType
	 * @throws Exception
	 */
	public void extractEach(String url, int showType) throws SQLException, IOException, ParseException  {
		Document doc = getDoc(url);
		Show show = new Show();
		Map<String, List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
		// 代理商ID
		show.setAgent_id(agentID);
		// 演出名称
		show.setName(doc.select("div.info div.content div.title").first().text());
		// 演出类型
		show.setType(showType);
		// 获取演出海报信息
		show.setImage_path(doc.select("div.img img").first().attr("abs:src"));
		// 获取演出 城市 场馆信息
		show.setSiteName(doc.select("div.place").first().childNode(1).toString());
		// 判断是否总代理
		String mainAgency = doc.select("#perform_mark  li:contains(在线选位)").attr("class");
		boolean flag = "yes".equals(mainAgency)?true:false;
		//获取售票状态
		if(doc.select("div.control_buy_icons input.item").size()>0){
			 show.setStatus(1);
		}else {
			show.setStatus(0);
		}
		show.setFirstAgent(flag);
		show.setOnlineSeat(flag);
		// 演出介绍
		show.setIntroduction(PubFun.cleanElement(doc.select("div.description")).html());
		// 解析票价相关信息
		String def = PubFun.getRegex("(?i)var\\s+tickets\\s+=\\s+(.*);", doc.html(), 1);
		Gson gson = new Gson();
		Type t = new TypeToken<List<MypiaoSpider.Price>>() {}.getType();
		List<MypiaoSpider.Price> list = gson.fromJson(def, t);
		if (list == null) {
			list = new ArrayList<MypiaoSpider.Price>();
		}
		for (MypiaoSpider.Price price : list) {
			// 演出票价
			List<TicketPrice> priceList = new ArrayList<TicketPrice>();
			List<String> prices = price.getPrices();
			List<String> left = price.getLeft();
			String showTime = price.getDatetime();
			try {
				showTime = newDate.format(newDate.parse(showTime));
				if(newDate.parse(showTime).before(new Date())){
					continue;
				}
			} catch (Exception e) {
			}
			// 如果演出时间没有限制，则将演出时间设置为all
			for (int i = 0; i < prices.size(); i++) {
				TicketPrice ticketPrice = new TicketPrice();
				// 因为本网站的演出信息和票价信息在同一个页面，故的URL地址相同
				// 演出详细信息URL
				ticketPrice.setMainURL(url);
				// 票价详细信息URL
				ticketPrice.setDetailURL(url);
				// 处理票价信息
				String pri = prices.get(i);
				if(pri.contains("待定")){
					continue;
				}
				// 过滤到票价信息中的括号
				String pri1 = "";
				String remark1 = "";
				// 获取票价
				pri1 = PubFun.getRegex("((^\\d+\\.+\\d+)|(^\\d+)).*", pri, 1);
				// 获取备注
				if (pri1 == null) {
					continue;
				}
				remark1 = prices.get(i).replaceAll(pri1, "");
				remark1 = remark1.replaceAll("[()（）]", "");
				ticketPrice.setPrice(pri1);
				ticketPrice.setRemark(remark1);
				// 判断该价位的票是否还有剩余
				flag = true;
				if (Integer.parseInt(left.get(i)) == 0) {
					flag = false;
				}
				ticketPrice.setExist(flag);
				priceList.add(ticketPrice);
			}
			if(priceList.size()==0||priceList.isEmpty()){
				continue;
			}
			timeAndPrice.put(showTime, priceList);
		}
		if(timeAndPrice.size()==0||timeAndPrice.isEmpty()){
			return ;
		}
		show.setTimeAndPrice(timeAndPrice);
		getDao().saveShow(show);
	}
	
	public static void main(String args[]){
		MypiaoSpider mypiao = new MypiaoSpider();
		mypiao.setDao(new DataDao());
		mypiao.extract();
	}
}
