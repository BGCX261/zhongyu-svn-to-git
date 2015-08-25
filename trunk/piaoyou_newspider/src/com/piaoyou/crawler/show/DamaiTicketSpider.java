package com.piaoyou.crawler.show;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.piaoyou.bean.CommonInfo;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;

public class DamaiTicketSpider extends TicketSpider implements SpiderTask {
	
	public static int count=0;
	private static final Log log = LogFactory.getLog(DamaiTicketSpider.class);
	
	private final static String HOME_PAGE = "http://www.damai.cn/projectlist.aspx";
	private final static String BASE_URL = "http://www.damai.cn/";
	private final static String AJAX = "http://www.damai.cn/ajax.aspx";
	private final static Map<String,Integer> typeCor = new HashMap<String,Integer>();
	
	private final static String agentID = new DataDao().searchBySQL("select id from agency_info where agency_url=?", BASE_URL)
	.get(0).get("id").toString();

	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy年M月d日 HH:mm");
	private final static SimpleDateFormat fmt = new SimpleDateFormat("yyyy.MM.dd HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	static{
		initTypeCor();
	}
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		Elements every = start.select("#performList").select("h2>a");
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
				every = start.select("#performList").select("h2>a");
			}
		}
	}
	
	/**
	 * 初始化演出类别对应
	 */
	private static void initTypeCor(){
		typeCor.put("1",Const.CONCERT);//演唱会
		typeCor.put("2",Const.SHOW);//音乐会
		typeCor.put("3",Const.DRAMA);//话剧歌剧
		typeCor.put("4",Const.DANCE);//舞蹈芭蕾
		typeCor.put("5",Const.OPERA);//曲苑杂坛
		typeCor.put("7",Const.CHILDREN);//儿童亲子
		typeCor.put("6",Const.SPORTS);//体育比赛
		typeCor.put("8",Const.HOLIDAY);//度假休闲
		typeCor.put("9",Const.OTHER);//其他
	}
	
	public static void main(String[] args) throws ParseException {
		DamaiTicketSpider damai = new DamaiTicketSpider();
		damai.setDao(new DataDao());
		try {
			damai.extract();
//			damai.extractEach("http://www.damai.cn/ticket_35018.html");
		} catch (Exception e) {
			log.info(e);
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
		
		String resultPrice = "";
		List<String> timeList = new ArrayList<String>();
		List<String> priceList = new ArrayList<String>();
		
		boolean flag = true;
		Document ticket = getDoc(url);
		CommonInfo ci = new CommonInfo();
		ci.setMainURL(url);
		ci.setName(ticket.select("h1").get(1).text());//演出标题
		String status = ticket.select("span[class=c4 floatleft]").text();
		ci.setStatus("售票中".equals((status))?1:0);
		if(!status.equals("退票中")&&!status.equals("待定")){
			ci.setSite_name(ticket.select("span.pl20 a.c3").first().text());//演出场馆
		}
		ci.setIs_first_agency(ticket.select("img[src=/images/zd.jpg]").isEmpty()==true?0:1);//是否总代
		Elements introduction = ticket.select("div.in.clear");//演出简介
		for(Element clear:introduction.select("script,noscript,embed,object")){
			clear.remove();
		}
		ci.setType(Const.COMMON_INFO_TYPE_SHOW);
		ci.setIntroduction(introduction.html());
		ci.setRemote_img_url(ticket.select("div.img_info img").attr("src"));//演出图片
		ci.setShow_type(typeCor.get(ticket.select("#CategoryID").val()).intValue());//演出类型
		
		//Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
//		Elements times = ticket.select("#perform a");
		int pageCount = Integer.valueOf(PubFun.getRegex("performPageCount=(\\d+);",ticket.select("head").html(),1));
		int currentPage = 1;
		Document ajaxDoc = Jsoup.connect(AJAX).data("type","11")
		.data("id", ticket.select("#ProjectID").val())
		.data("IsBuyFlow", ticket.select("#IsBuyFlow").val())
		.data("business", ticket.select("#Business").val())
		.data("pageIndex", String.valueOf(currentPage)).post();
		Elements times = ajaxDoc.select("a:not(a[price])");
		boolean isNewyear = false;
		Date lastDate = null;
		String startYear = PubFun.getCurrentYear();
		//查看起始年份
		do{
			for(Element time:times){
				//List<TicketPrice> priceList = new ArrayList<TicketPrice>();
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
				//timeAndPrice.put(timeTmp,priceList);
					timeList.add(timeTmp);
				Document ajaxPrice = Jsoup.connect(AJAX).data("type","12")
					.data("IsBuyFlow", ticket.select("#IsBuyFlow").val())
					.data("business",ticket.select("#Business").val())
					.data("performID",time.attr("pid")).timeout(1000*60).post();
				Elements prices = ajaxPrice.select("a");
				if(flag) {
					for(Element tmp:prices){
						//TicketPrice price = new TicketPrice();
						String tmpPrice = tmp.attr("price");
						//price.setPrice(tmpPrice);
						//price.setRemark(tmp.text().trim().replaceFirst(tmpPrice+"(?:\\.00)?", "").replaceAll("[(（）)]", ""));
						//price.setMainURL(url);
						//price.setExist(!tmp.hasClass("disable"));
						//priceList.add(price);
						priceList.add(tmpPrice);
						resultPrice = sortForPrices(priceList);
						flag = false;
					}
				}
			}
			//over
			ajaxDoc = Jsoup.connect(AJAX).data("type","11")
				.data("id", ticket.select("#ProjectID").val())
				.data("IsBuyFlow", ticket.select("#IsBuyFlow").val())
				.data("business", ticket.select("#Business").val())
				.data("pageIndex", String.valueOf(++currentPage)).timeout(1000*60).post();
			times = ajaxDoc.select("a:not(a[price])");
		}while(pageCount >= currentPage);
		
		if(timeList==null&&timeList.size()==0){   //如果演出时间不存在，返回
			return ;
		}
		if("".equals(resultPrice)){
			return ;
		}else{
			resultPrice = resultPrice.substring(0,resultPrice.length()-1);
		}
		ci.setMin_price(resultPrice.split(",")[0]);
		ci.setAgency_id(Integer.parseInt(agentID));
		ci.setShow_time(timeList);
		ci.setPrice(resultPrice);
		ci.setIs_check("0");
		
		//show.setTimeAndPrice(timeAndPrice);
		//getDao().saveShow(show);
		//System.out.println(show);
		//log.info(show);
		saveCommonInfo(ci);
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
	
	private static String sortForPrices(List<String> list) {
		int[] target = new int[list.size()];
		for (int i = 0; i < list.size(); i++) {
			target[i] = Integer.parseInt(list.get(i));
		}
		Arrays.sort(target);
		StringBuffer sb = new StringBuffer();
		for (int i : target) {
			sb.append(i+",");
		}
		return sb.toString();
	}

}
