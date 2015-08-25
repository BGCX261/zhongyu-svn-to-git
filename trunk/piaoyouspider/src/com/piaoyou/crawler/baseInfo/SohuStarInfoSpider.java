package com.piaoyou.crawler.baseInfo;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.PubFun;
import com.piaoyou.util.db.DBUtils;

public class SohuStarInfoSpider extends TicketSpider implements SpiderTask {
	static final String SEARCH_HOME_PAGE = "http://data.yule.sohu.com/star/list/------s-";
	static String today="";
	static final int days=15;
	private static String showType = "all";
	static final Map<String,String> newsMap=new HashMap<String,String>();
	static final Map<String,String> photoMap=new HashMap<String,String>();
	static final Map<String,String> videoMap=new HashMap<String,String>();
	static final Map<String,String> soundMap=new HashMap<String,String>();
	private static final Log log = LogFactory.getLog(SohuStarInfoSpider.class);

	public static void main(String[] args) {
		if(args[0]!=null&&!args[0].trim().equals("")) {
			showType = args[0].trim();
		}
		SohuStarInfoSpider sohuStarInfoSpider = new SohuStarInfoSpider();
		sohuStarInfoSpider.setDao(new DataDao());
		sohuStarInfoSpider.extract();
	}
	@Override
	public void extract() {
		List<Map<String, Object>> list = new DataDao().searchBySQL("select start_id,cn_name,en_name from t_star_info");
		int starId = 0;
		for (int v = 0; v < list.size(); v++) {
			String name = "";
			String cnName = "";
			String enName = "";
			cnName = list.get(v).get("cn_name").toString();
			if(list.get(v).get("en_name")!=null){
				enName = list.get(v).get("en_name").toString();
			}
			starId = Integer.parseInt(list.get(v).get("start_id").toString());
			Document doc = null;
			try {
				doc = getDoc(SEARCH_HOME_PAGE+ java.net.URLEncoder.encode(cnName, "gbk") + "-1.html");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			// 如果中文姓名查不到则查询英文名称
			try{
				if (doc.getElementsByClass("pt").isEmpty()){
					try {
						doc = getDoc(SEARCH_HOME_PAGE+ java.net.URLEncoder.encode(enName, "gbk")+ "-1.html");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if (!doc.getElementsByClass("pt").isEmpty()) {
						name = enName;
					}else{
						log.error(cnName+"("+enName+")");
						continue;
					}
				} else {
					name = cnName;
				}
			}catch(Exception e){
				continue;
			}
			// 有明星信息
			String oneStarUrl = doc.getElementsByClass("pt").first().getElementsByTag("a").first().absUrl("href");
			doc = getDoc(oneStarUrl);
			if(doc==null){
				continue;
			}
			if ("1".equals(showType)) {// 1为图片
				getPhotoFrom3w(doc, starId);
			} else if ("2".equals(showType)) {// 2为视频
				getVideoFrom3w(doc, starId);
			} else if ("3".equals(showType)) {// 3为新闻
				getNewsFrom3w(doc, starId);
			} else if ("4".equals(showType)) {// 4为音频
				getSoundFrom3w(doc, starId, name);
			} else {
				getNewsFrom3w(doc, starId);
				getVideoFrom3w(doc, starId);
				getPhotoFrom3w(doc, starId);
				getSoundFrom3w(doc, starId, name);
			}
			log.info(cnName + "(" + enName + ")");
		}
	}
	// 明星最新动态
	private void getNewsFrom3w(Document doc,int starId) {
		getNews(days);
		getDateStr();
		if (!doc.getElementsByAttributeValue("class", "list12 clear").isEmpty()) {
			for (Element nesEls : doc.getElementsByAttributeValue("class",
					"list12 clear").first().getElementsByTag("li")) {
				String date = nesEls.getElementsByTag("span").first().text();
				String href = nesEls.getElementsByTag("a").first().absUrl(
						"href");
				String title = nesEls.getElementsByTag("a").first()
						.getElementsByAttribute("title").text();
				// 如果新闻生成日期小于指定天数则跳过
				if (PubFun.getDaysBetweenTwoDate(date, today, "yyyy-MM-dd") >= days) {
					continue;
				}
				if (newsMap.get(href + String.valueOf(starId)) == null) {
					String sql = "insert into t_star_news (STAR_ID,NEWS_TITLE,NEWS_URL,CRAWLED_TIME,CREATE_DATE)values(?,?,?,now(),?)";
					getDao().executeSQL(sql, starId, title, href, date);
				}
			}
		}
	}
	// 明星图片
	private void getPhotoFrom3w(Document doc, int starId) {
		getPhoto();
		if (!doc.getElementsByAttributeValue("class", "ppm clear").isEmpty()) {
			for (Element photoEls : doc.getElementsByAttributeValue("class",
					"ppm clear").first().getElementsByTag("li")) {
				String bigPhotoUrl = "";
				if (photoEls.getElementsByTag("a").first().absUrl("href")
						.split("phu=").length == 2) {
					bigPhotoUrl = photoEls.getElementsByTag("a").first()
							.absUrl("href").split("phu=")[1];
				} else {
					try {
						Document temp = getDoc(photoEls.getElementsByTag("a")
								.first().absUrl("href"));
						bigPhotoUrl = temp.getElementsByClass("big").first()
								.getElementsByTag("img").first().absUrl("src");
					} catch (Exception e) {
						bigPhotoUrl = photoEls.getElementsByTag("a").first()
								.absUrl("href");
						if (bigPhotoUrl
								.contains("http://pic.sogou.com/d?query=")) {
							try {
								bigPhotoUrl = doc.getElementsByTag("table")
										.first().getElementsByTag("tr").get(1)
										.getElementsByTag("a").first().absUrl(
												"href");
							} catch (Exception e1) {

							}
						}
					}
				}
				//如果照片存在不插入
				if(photoMap.get(bigPhotoUrl+String.valueOf(starId))==null){
					String sql="insert into t_star_photo (STAR_ID,PHOTO_URL,CRAWLED_TIME)values(?,?,now())";
					getDao().executeSQL(sql,starId,bigPhotoUrl);
				}
		}
	}
	}
	// 明星视频
	private void getVideoFrom3w(Document doc, int starId) {
		getVideo();
		if (doc.getElementById("videosDiv") != null) {
			for (Element videoEls : doc.getElementById("videosDiv")
					.getElementsByTag("li")) {
				String vdieoPhotoUrl = videoEls.getElementsByTag("img").first()
						.absUrl("src");
				String vdieoUrl = videoEls.getElementsByTag("a").first()
						.absUrl("href");
				String vdieotitle = videoEls.getElementsByTag("a").first()
						.attr("title");
				if (videoMap.get(vdieoUrl + String.valueOf(starId)) == null) {
					String sql = "insert into t_star_video (STAR_ID,VIDEO_TITLE,VIDEO_PHOTO_URL,VIDEO_URL,CRAWLED_TIME)values(?,?,?,?,now())";
					getDao().executeSQL(sql, starId, vdieotitle, vdieoPhotoUrl,
							vdieoUrl);
				}
			}
		}
	}
	//音频去百度抓取
	private void getSoundFrom3w(Document doc,int starId,String name) {
		getSound();
		try {
			doc = getDoc("http://mp3.baidu.com/m?lm=0&word="
					+ java.net.URLEncoder.encode(name, "gbk"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		if (!doc.getElementsByClass("table-song-list").isEmpty()) {
			int i = 0;
			for (Element voiceEls : doc.getElementsByClass("table-song-list")
					.first().getElementsByTag("tr")) {
				i++;
				// 第一行不取
				if (i == 1) {
					continue;
				}
				// 非标准格式不取
				if (voiceEls.getElementsByClass("third").isEmpty()) {
					continue;
				}
				String temp = "";
				try {
					temp = voiceEls.getElementsByClass("third").first().text();
				} catch (Exception e) {
					log.error(name + "====百度解析错误---");
				}
				// 如果包含艺人名称
				if (temp.contains(name)) {
					// 没有音频连接不取
					if (voiceEls.getElementsByClass("fifth").first()
							.getElementsByTag("a").isEmpty()) {
						continue;
					}
					try {
						String voiceUrl = voiceEls.getElementsByClass("fifth")
								.first().getElementsByTag("a").first().absUrl(
										"href").replace("amp;", "");
						String voiceName = voiceEls
								.getElementsByClass("second").first()
								.getElementsByTag("a").first().text();
						if (soundMap.get(voiceUrl + String.valueOf(starId)) == null) {
							String sql = "insert into t_star_sound (STAR_ID,SOUND_TITLE,SOUND_URL,CRAWLED_TIME)values(?,?,?,now())";
							getDao().executeSQL(sql, starId, voiceName,
									voiceUrl);
						}
					} catch (Exception e) {
						log.error(name + "===百度Mp3错误:" + temp);
						continue;
					}
				}
			}
		}
	}
	private void getNews(int day){
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			String sql = "select NEWS_URL,star_id from t_star_news where  DATEDIFF(now(),DATE (create_date))<=? order by news_id";
			try {
				connection = DBUtils.getConnection();
				statement = connection.prepareStatement(sql.toString());
				statement.setInt(1,day);
				statement.executeQuery();
				rs = statement.getResultSet();
				while(rs.next()){
					newsMap.put(rs.getString("NEWS_URL")+rs.getString("star_id"), "0");
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				DBUtils.closeResultSet(rs);
				DBUtils.closeStatement(statement);
				DBUtils.closeConnection(connection);
			}
		}
	
	private void getPhoto(){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "select star_id,photo_url from t_star_photo order by photo_id";
		try {
			connection = DBUtils.getConnection();
			statement = connection.prepareStatement(sql.toString());
			statement.executeQuery();
			rs = statement.getResultSet();
			while(rs.next()){
				photoMap.put(rs.getString("photo_url")+rs.getString("star_id"), "0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(statement);
			DBUtils.closeConnection(connection);
		}
	}
	
	private void getSound(){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "select star_id,SOUND_URL from t_star_sound order by sound_id";
		try {
			connection = DBUtils.getConnection();
			statement = connection.prepareStatement(sql.toString());
			statement.executeQuery();
			rs = statement.getResultSet();
			while(rs.next()){
				soundMap.put(rs.getString("SOUND_URL")+rs.getString("star_id"), "0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(statement);
			DBUtils.closeConnection(connection);
		}
	}
	
	private void getVideo(){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "select star_id,VIDEO_URL from t_star_video order by video_id";
		try {
			connection = DBUtils.getConnection();
			statement = connection.prepareStatement(sql.toString());
			statement.executeQuery();
			rs = statement.getResultSet();
			while(rs.next()){
				videoMap.put(rs.getString("VIDEO_URL")+rs.getString("star_id"), "0");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(statement);
			DBUtils.closeConnection(connection);
		}
	}
	
	private void getDateStr(){
	    Date dt = new Date();   
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");   
	    today=sdf.format(dt);     
	}
}
