package com.piaoyou.crawler.spider;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.ShowType;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.PubFun;

public class QQSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(QQSpider.class);
	private static final String BASE_URL = "http://piao.qq.com";
	private static final String URL = "http://piao.qq.com/yanchu/data/type/data_0_0_{i}.json?1463740";
	private static final Class<QQSpider> BASE_CLASS = QQSpider.class;
	private static final SimpleDateFormat newDate = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm");
	private final static String agentID = new DataDao().searchBySQL(
			"select agency_id from t_agency_info where agency_url=?", BASE_URL)
			.get(0).get("agency_id").toString();

	@Override
	public void extract() {
		int result = 1;
		for (int k = 0; k < result; k++) {
			Document document = getDoc(URL.replace("{i}", String.valueOf(k)));// 获取所有演唱会数据
			if (document == null) {
				break;
			}
			// 处理成标准JSON
			String unDealJson = document.text();
			int startPosition = unDealJson.indexOf("{");
			int endPosition = unDealJson.lastIndexOf(")");
			String dealJson = unDealJson.substring(startPosition, endPosition);
			try {
				dealJson = unDealJson.substring(startPosition, endPosition);
			} catch (Exception e) {
			}
			// 转换对象
			JSONObject jsonObject = JSONObject.fromObject(dealJson);
			if (k == 0) {
				int total = Integer.parseInt(jsonObject.getString("total_num"));
				result = total / 100;
				if (total % 100 != 0) {
					result = result + 1;
				}
				log.info("一共" + jsonObject.get("total_num") + "数据" + result
						+ "页等待处理...");
			}
			JSONArray arrays = jsonObject.getJSONArray("data");
			for (int i = 0; i < arrays.size(); i++) {
				JSONObject model = arrays.getJSONObject(i);
				parseEach(model);
			}
		}
	}

	public static void main(String[] args) {
		QQSpider tangshang = new QQSpider();
		tangshang.setDao(new DataDao());
		tangshang.extract();
	}

	public void parseEach(JSONObject model) {
		Show show = new Show();
		Map<String, List<TicketPrice>> timeAndPrice = new HashMap<String, List<TicketPrice>>();
		TicketPrice ticket = null;
		show.setTimeAndPrice(timeAndPrice);
		show.setAgent_id(agentID);// 设置机构ID
		show.setName(model.getString("tname"));// 设置标题
		show.setType(getShowType(model.getString("ttypename").toString()));
		show.setSiteName(model.getString("tphall").toString());
		show.setImage_path("http://piaoupload.17u.cn/piaoupload/"
				+ model.getString("tpic").toString().replace("s_", "").replace(
						",", ""));
		// 组合URL
		String url = "";
		String jsonData = "";
		String iShowId = model.getString("tid");
		int temp = Integer.parseInt(iShowId) % 100;
		url = "http://piao.qq.com/yanchu/detail/" + temp + "/detail_" + iShowId
				+ "_" + "101" + ".html";
		jsonData = "http://piao.qq.com/yanchu/data/detail/" + temp + "/detail_"
				+ iShowId + "_" + "101" + ".json";
//		url = "http://piao.qq.com/yanchu/detail/63/detail_12163_101.html";
//		jsonData= "http://piao.qq.com/yanchu/detail/63/detail_12163_101.html";
		try {
			Document document = getDoc(url);// 获取演唱会详细信息
			Elements infoElements = document.select("div[class=yc_info]");
			if (!infoElements.isEmpty())
				show.setIntroduction(PubFun.cleanElement(infoElements.first())
						.text());// 读取详细页面内容
			document = getDoc(jsonData);// 获取票价和时间信息
			// 转换对象
			// 处理成标准JSON
			String unDealJson = document.text();
			if(unDealJson.indexOf("停止售票")!=-1){
				ticket = new TicketPrice();
				List<TicketPrice> list = new ArrayList<TicketPrice>();
				ticket.setPrice("");
				list.add(ticket);
				timeAndPrice.put("2099-01-01 10:10:10",list);
				getDao().saveShow(show);
				return;
			}
			int startPosition = unDealJson.indexOf("[");
			int endPosition = unDealJson.lastIndexOf("]");
			String dealJson = unDealJson.substring(startPosition + 1,
					endPosition);
			JSONObject jsonObject = JSONObject.fromObject(dealJson);
			// 获取哪些时间段的票是没有票的
			String tickets = jsonObject.getString("tickets");
			if (tickets.length()>10) {
				String arrays[] = tickets.split("},");
				for (String string : arrays) {
					String subArrays[] = string.split(":\\{");
					String ticketId = subArrays[0].replace("{", "").replace(
							",", "").replace("'", "");
					String subSubArrays[] = subArrays[1].split(",");
					String price = "";
					String time = "";
					String remark = "";
					for (String data : subSubArrays) {
						String[] detail = data.split("\\:");
						if (detail[0].equals("price")) {
							price = detail[1];
						} else if (detail[0].equals("time")) {
							time = detail[1].substring(0, 4) + "-"
									+ detail[1].substring(4, 6) + "-"
									+ detail[1].substring(6, 8) + " "
									+ detail[1].substring(8, 10) + ":"
									+ detail[1].substring(10, 12);
							try {
								time = newDate.format(newDate.parse(time));
								if (newDate.parse(time).before(new Date())) {
									continue;
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else if (detail[0].equals("name")) {
							remark = detail[1];
						}
					}
//					'169081_101'
					//http://cgi.piao.qq.com/cgi-bin/yanchu/query/ticket.fcg?&sTicketsID=169081,101
					// 查询是否有票
					Document hasTicketDocument = getDoc("http://cgi.piao.qq.com/cgi-bin/yanchu/query/ticket.fcg?iPartnerID=101&sTicketsID="
							+ ticketId.replace("_", ","));// 获取演唱会详细信息
					String hasTicketStr = hasTicketDocument.text();
					int position = hasTicketStr.indexOf("flag\":\"");
					String hasTicketValue = hasTicketStr.substring(position
							+ "flag\":\"".length(), position
							+ "flag\":\"".length() + 1);
					ticket = new TicketPrice();
					ticket.setMainURL(url);
					ticket.setDetailURL(url);
					ticket.setExist(hasTicketValue.equals("2") ? false : true);
					ticket.setPrice(price);
					ticket.setRemark(remark.replace("}", "").replace("'", ""));
					if (timeAndPrice.get(time) == null) {
						List<TicketPrice> list = new ArrayList<TicketPrice>();
						list.add(ticket);
						timeAndPrice.put(time, list);
					} else {
						timeAndPrice.get(time).add(ticket);
					}
				}
			}
			show.setTimeAndPrice(timeAndPrice);
			getDao().saveShow(show);
		} catch (Exception e) {
			LogFactory.getLog(BASE_CLASS).error(url + "\r\n" + jsonData, e);
			e.printStackTrace();
		}
	}

	private int getShowType(String type) {
		int showType = 0;
		if ("演唱会".equals(type)) {
			showType = ShowType.CONCERT;
		} else if ("音乐会".equals(type)) {
			showType = ShowType.SHOW;
		} else if ("话剧歌剧".equals(type)) {
			showType = ShowType.DRAMA;
		} else if ("舞蹈芭蕾".equals(type)) {
			showType = ShowType.DANCE;
		} else if ("戏曲曲艺".equals(type)) {
			showType = ShowType.OPERA;
		} else if ("亲子家庭".equals(type)) {
			showType = ShowType.CHILDREN;
		} else if ("体育赛事".equals(type)) {
			showType = ShowType.SPORTS;
		} else if ("休闲娱乐".equals(type)) {
			showType = ShowType.HOLIDAY;
		}
		return showType;
	}

}
