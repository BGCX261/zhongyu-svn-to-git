package com.piaoyou.crawler.spider;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * 北京新文化票务网
 * @author Administrator
 *
 */
public class XinWenHuaSpider extends TicketSpider implements SpiderTask {
	
	private static final Log log = LogFactory.getLog(XinWenHuaSpider.class);
	
	private final static String HOME_PAGE = "http://www.xinwenhua.com.cn/item/ShowIndex.asp";
	private final static String BASE_URL = "http://www.xinwenhua.com.cn/";
	private final static String agentID = new DataDao().searchBySQL("select agency_id from t_agency_info where agency_url=?", BASE_URL)
							.get(0).get("agency_id").toString();
	private final static Map<String,Integer> typeCor = new HashMap<String,Integer>();
	private final static SimpleDateFormat oldDate = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	private final static SimpleDateFormat newDate = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	
	static{
		initTypeCor();
	}
	
	/**
	 * 初始化演出类别对应
	 */
	private static void initTypeCor(){
		typeCor.put("（演唱会）",ShowType.CONCERT);//演唱会
		typeCor.put("（话剧歌剧）",ShowType.DRAMA);//话剧歌剧
		typeCor.put("（朝阳9个剧场专区）",ShowType.DRAMA);//话剧歌剧
		typeCor.put("（人艺票品）",ShowType.DRAMA);//话剧歌剧
		typeCor.put("（国家大剧院歌剧院）",ShowType.DRAMA);//话剧歌剧
		typeCor.put("（国家大剧院音乐厅）",ShowType.SHOW);//音乐会
		typeCor.put("（音乐会）",ShowType.SHOW);//音乐会
		typeCor.put("（打开音乐之门）",ShowType.SHOW);//音乐会
		typeCor.put("（舞蹈芭蕾）",ShowType.DANCE);//舞蹈芭蕾
		typeCor.put("（魔术马戏）",ShowType.OPERA);//曲苑杂坛
		typeCor.put("（儿童亲子）",ShowType.CHILDREN);//儿童亲子
		typeCor.put("（戏曲综艺）",ShowType.OPERA);//曲苑杂坛
		typeCor.put("（温泉）",ShowType.HOLIDAY);//休闲
		typeCor.put("（休闲类）",ShowType.HOLIDAY);//休闲
		typeCor.put("（运动休闲）",ShowType.HOLIDAY);//休闲
		typeCor.put("（电影通票）",ShowType.HOLIDAY);//休闲
		
		typeCor.put("（国家大剧院戏剧场）",ShowType.OTHER);//未处理
		typeCor.put("（国家大剧院小剧场）",ShowType.OTHER);//未处理
		typeCor.put("（梅兰芳大剧院）",ShowType.OTHER);//未处理
		typeCor.put("（长安大戏院）",ShowType.OTHER);//未处理
	}
	
	@Override
	public void extract() {
		Document start = getDoc(HOME_PAGE);
		for(Element all:start.select("table.table")){
			String typeName = all.select("tr:eq(0) font").text();
			for(Element eachType:all.select("tr:gt(0)")){
				try {
					extractEach(eachType.select("td:eq(0) a").first().attr("abs:href"),typeCor.get(typeName));
				} catch (Exception e) {
					log.error(Const.concat(eachType.select("td:eq(0) a").first().attr("abs:href"),typeCor.get(typeName)),e);
				}
			}
		}
	}
	
	
	public static void main(String[] args) throws ParseException, SQLException, IOException {
		XinWenHuaSpider spider = new XinWenHuaSpider();
		spider.setDao(new DataDao());
		spider.extract();
		//spider.extractEach("http://www.xinwenhua.com.cn/item/ShowInfor.asp?id=9839", 3);
	}

	/**
	 * 通过目标url取得演出需要的元素
	 * @param url		演出页面url
	 * @param type		演出类型
	 * @throws SQLException 
	 * @throws IOException 
	 * @throws ParseException 
	 */
	private void extractEach(String url, int type) throws SQLException, IOException, ParseException {
		Document ticket = getDoc(url, "gb2312");
		Show show = new Show();
		show.setAgent_id(agentID);//代理商id
		show.setName(ticket.select("span[style=font-size:14px; color:#006633]").text());//演出标题
		show.setImage_path(ticket.select("td[width=200] img").first().attr("abs:src"));//演出图片
		show.setSiteName(ticket.select("span.STYLE9").get(0).parent().childNode(1).toString().replaceAll("：", ""));//场馆
		show.setIntroduction(PubFun.cleanElement(ticket.select("div.main")).html());//简介
		if(type == ShowType.OTHER){
			if(show.getName().contains("音乐")){
				type = ShowType.SHOW;
			}else if(show.getName().contains("剧")){
				type = ShowType.DRAMA;
			}else if(show.getName().contains("舞")){
				type = ShowType.DANCE;
			}
		}
		show.setType(type);//分类
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		show.setTimeAndPrice(timeAndPrice);//时间和票价
		for(Element priceElement:ticket.select("table[bordercolor=#DFDFDF] tr:gt(0)")){
			String time = priceElement.select("td:eq(0)").text();
			try {
				if(time.contains("号")){
					time=time.replace("号", "日");
				}
				if(time.contains("日")&& !time.contains("日 ") ){
					time=time.replace("日", "日 ");
				}
				time = newDate.format(oldDate.parse(time));
			} catch (Exception e) {
				
			}
			List<TicketPrice> priceList = new ArrayList<TicketPrice>();
			String[] prices = priceElement.select("td:eq(2)").text().split(",\\s*");
			for(String tmp:prices){
				if(tmp.matches("^\\d.*")){
					TicketPrice price = new TicketPrice();
					price.setPrice(tmp.replaceAll("(^\\d+).*", "$1"));
					price.setMainURL(url);
					price.setExist(!priceElement.select("td:eq(2) a:contains("+tmp+")").isEmpty());
					price.setRemark(tmp.replaceAll("(^\\d+)(.*)", "$2").replaceAll("[()（）]", ""));
					priceList.add(price);
				}
			}
			timeAndPrice.put(time, priceList);
		}
		getDao().saveShow(show);
	}
	
}
