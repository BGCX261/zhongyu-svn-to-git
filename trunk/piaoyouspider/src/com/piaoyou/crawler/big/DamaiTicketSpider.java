package com.piaoyou.crawler.big;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.PubFun;

public class DamaiTicketSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(DamaiTicketSpider.class);
	
	private final static String HOME_PAGE = "http://www.damai.cn/projectlist.aspx";
	private final static String BASE_URL = "http://www.damai.cn/";
	private final static String AJAX = "http://www.damai.cn/ajax.aspx";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
							.get(0).get("agency_id").toString();
	private final static Map<String,Integer> typeCor = new HashMap<String,Integer>();

	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy年M月d日 HH:mm");
	private final static SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	static{
		initTypeCor();
	}
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every = start.select("#plist").select("h2>a");
		for(;;){
			for(Element element:every){
				String tar = element.attr("abs:href");
				try {
					extractEach(tar);
				} catch (SQLException e) {
					log.error("数据库错误"+e.getMessage()+tar);
				}
				catch (IOException e) {
					log.error("IO异常"+e.getMessage()+tar);
				}
				catch (ParseException e) {
					log.error("解析错误"+e.getMessage()+tar);
				}
				catch (NullPointerException e) {
					log.error("解析空指针错误"+tar,e);
				}
				catch (Exception e) {
					log.error("其他错误"+e.getMessage()+tar);
				}
			}
			Elements nextPage = start.select("a:contains(下一页)");
			if(nextPage.isEmpty()){
				return;
			}else{
				start = getDoc(nextPage.first().attr("abs:href"));
				every = start.select("#plist").select("h2>a");
			}
		}
	}
	
	/**
	 * 初始化演出类别对应
	 */
	private static void initTypeCor(){
		typeCor.put("1",ShowType.CONCERT);//演唱会
		typeCor.put("2",ShowType.SHOW);//音乐会
		typeCor.put("3",ShowType.DRAMA);//话剧歌剧
		typeCor.put("4",ShowType.DANCE);//舞蹈芭蕾
		typeCor.put("5",ShowType.OPERA);//曲苑杂坛
		typeCor.put("100",ShowType.CHILDREN);//儿童亲子
		typeCor.put("6",ShowType.SPORTS);//体育比赛
		typeCor.put("7",ShowType.HOLIDAY);//度假休闲
		typeCor.put("8",ShowType.OTHER);//其他
	}
	
	public static void main(String[] args) throws ParseException {
		DamaiTicketSpider damai = new DamaiTicketSpider();
		damai.setDao(new DataDao());
//		damai.extract();
		try {
			damai.extractEach("http://www.damai.cn/ticket_28420.html");
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 通过目标url取得演出需要的元素
	 * @param url
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void extractEach(String url) throws SQLException, IOException, ParseException {
		Document ticket = getDoc(url);
		Show show = new Show();
		show.setAgent_id(agentID);
		show.setName(ticket.select("h1").get(1).text());//演出标题
		show.setFirstAgent(!ticket.select("img[src=/images/zd.jpg]").isEmpty());//是否总代
		show.setOnlineSeat(!ticket.select("#btnXuanZuo").not(".hidden").isEmpty());//支持在线选座
		if(!ticket.select("#pqt").isEmpty()){
			show.setSeatPic_path(ticket.select("#pqt").first().attr("abs:href"));//座位图
		}
		String status = ticket.select("span[class=c4 floatleft]").text();
		show.setStatus("售票中".equals((status))?1:0);
		if(!status.equals("退票中")&&!status.equals("待定")){
			show.setSiteName(ticket.select("span.ml30.pl20 a").first().text());//演出场馆
		}
		
		Elements introduction = ticket.select("div.in.clear");//演出简介
		for(Element clear:introduction.select("script,noscript,embed,object")){
			clear.remove();
		}
		show.setIntroduction(PubFun.cleanElement(introduction).html());
		show.setImage_path(ticket.select("div.img_info img").attr("src"));//演出图片
		show.setType(typeCor.get(ticket.select("#CategoryID").val()));//演出类型
		
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
//		Elements times = ticket.select("#perform a");
		int pageCount = Integer.valueOf(PubFun.getRegex("performPageCount=(\\d+);",ticket.select("head").html(),1))+1;
		int currentPage = 1;
		Document ajaxDoc = Jsoup.connect(AJAX).data("type","11")
		.data("id", ticket.select("#ProjectID").val())
		.data("IsBuyFlow", ticket.select("#IsBuyFlow").val())
		.data("business", ticket.select("#Business").val())
		.data("pageIndex", String.valueOf(currentPage)).post();
		Elements times = ajaxDoc.select("a:not(a[price])");
		boolean isNewyear = false;
		Date lastDate = null;
		//查看起始年份
		Element startTime = ticket.select("time").first();
		String startYear = startTime.text();
		if(startYear.contains("2012")&&!startYear.contains("2011")){
			startYear = "2012";
		}else{
			startYear = "2011";
		}
		do{
			for(Element time:times){
				List<TicketPrice> priceList = new ArrayList<TicketPrice>();
				String timeTmp = format(time.text().replaceAll("星期.\\s+", ""),ticket);
				if(timeTmp.length()>=4){
					timeTmp = startYear+timeTmp.substring(4);
				}
				Date newDate = PubFun.parseStringToDate(timeTmp, "yyyy-MM-dd HH:mm");
				if(!isNewyear&&lastDate!=null&&newDate!=null){
					if(lastDate.after(newDate)){
						isNewyear = true;
					}
				}
				lastDate = newDate;
				//开始年份+1
				if(isNewyear){
					Calendar c = Calendar.getInstance();
					c.setTime(newDate);
					c.add(Calendar.YEAR, 1);
					newDate = c.getTime();
					timeTmp = PubFun.parseDateToStr(newDate, "yyyy-MM-dd HH:mm");
				}
				timeAndPrice.put(timeTmp,priceList);
				Document ajaxPrice = Jsoup.connect(AJAX).data("type","12")
					.data("IsBuyFlow", ticket.select("#IsBuyFlow").val())
					.data("business",ticket.select("#Business").val())
					.data("performID",time.attr("pid")).timeout(1000*60).post();
				Elements prices = ajaxPrice.select("a");
				for(Element tmp:prices){
					TicketPrice price = new TicketPrice();
					String tmpPrice = tmp.attr("price");
					price.setPrice(tmpPrice);
					price.setRemark(tmp.text().trim().replaceFirst(tmpPrice+"(?:\\.00)?", "").replaceAll("[(（）)]", ""));
					price.setMainURL(url);
					if(show.isOnlineSeat()){
						price.setDetailURL("http://seat.damai.cn/xuanzuo/"+time.attr("pid"));
					}
					price.setExist(!tmp.hasClass("disable"));
					priceList.add(price);
				}
			}
			ajaxDoc = Jsoup.connect(AJAX).data("type","11")
				.data("id", ticket.select("#ProjectID").val())
				.data("IsBuyFlow", ticket.select("#IsBuyFlow").val())
				.data("business", ticket.select("#Business").val())
				.data("pageIndex", String.valueOf(++currentPage)).timeout(1000*60).post();
			times = ajaxDoc.select("a:not(a[price])");
		}while(pageCount >= currentPage);
		
		show.setTimeAndPrice(timeAndPrice);
		getDao().saveShow(show);
	}
	
	/**
	 * 格式化日期
	 * @param oldTime	要格式化的时间
	 * @param ticket 	如果oldTime无效，从ticket中取得其他时间
	 * @return
	 */
	private static String format(String oldTime, Document ticket){
		String result = "";
		if(oldTime.matches("^\\D+$")){
			if(oldTime.contains("常年")||oldTime.contains("每天")||oldTime.contains("每晚")){
				result = oldTime;
			}
			try {
				result = newDate.format(fmt.parse(PubFun.getRegex("时间：(.*)<span", ticket.select("div.detal").html(), 1)));
				result = result.substring(0,10);
			} catch (ParseException e) {
				result = oldTime;
			}
		}else if(oldTime.matches("^\\d{4}\\.\\d{1,2}\\.\\d{1,2}$")){
			try {
				result = newDate.format(fmt.parse(oldTime)).substring(0,10);
			} catch (ParseException e) {
				result = oldTime;
			}
		}else{
			String time = PubFun.getCurrentYear()+"年"+oldTime;
			try {
				result = newDate.format(oldDate.parse(time));
			} catch (ParseException e) {
				result = oldTime;
			}
		}
		return result;
	}

}
