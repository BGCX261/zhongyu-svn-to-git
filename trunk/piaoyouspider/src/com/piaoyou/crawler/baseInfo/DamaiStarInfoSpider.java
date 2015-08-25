package com.piaoyou.crawler.baseInfo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.db.DBUtils;
import com.piaoyou.util.proxy.ProxyUtil;

/**
 * 大麦抓取明星信息
 * @author Administrator
 *
 */
public class DamaiStarInfoSpider extends TicketSpider implements SpiderTask {

	private static final Log log = LogFactory.getLog(DamaiStarInfoSpider.class);
	
	static final String HOME_PAGE = "http://www.damai.cn/projectlist.aspx";
	static final String STAR_HOME_PAGE = "http://www.damai.cn/ajaxArtist.aspx?type=6";
	static final String preURL = "http://www.damai.cn/artistintro_";
	static final String imagePath = "e:/star_image/";
	
	static final HashSet<String> containsURL = new HashSet<String>();
	
	public static void main(String[] args) {
		saveStarInfo();
	}
	
	/**
	 * 从大麦抓取明星信息
	 */
	public static void saveStarInfo(){
		getExistsURL();
		
		DamaiStarInfoSpider spider = new DamaiStarInfoSpider();
		
		int count = 1;//并发数量
		int over = spider.getStarCount();//取得明星总数
		int avg = over / count;
		for (int i = 1; i <= count; i++) {
			new Thread(spider.new ExtractStarInfo((i - 1) * avg + 1, i * avg)).start();
		}
		if (count * avg < over) {
			new Thread(spider.new ExtractStarInfo(count * avg + 1, over)).start();
		}
		
		//停止抓取
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Scanner scan = new Scanner(System.in);
				while(DamaiStarInfoSpider.isRunning){
					if("exit".equalsIgnoreCase(scan.next())){
						DamaiStarInfoSpider.isRunning = false;
					}
				}
			}
		}).start();
	}

	private int getStarCount() {
		Document homePage = getDoc(STAR_HOME_PAGE);
		Document endPage = getDoc(STAR_HOME_PAGE+"&pageIndex="+homePage.text().split("\\^")[0]);
		return Integer.valueOf(endPage.select("a").last().attr("href").replaceAll("\\D", ""));
	}

	/**
	 * 取得已经抓取过的链接
	 */
	private static void getExistsURL() {
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		
		String sql = "select head_photo from t_star_info ";
		try {
			connection = DBUtils.getConnection();
			statement = connection.prepareStatement(sql.toString());
			statement.execute();
			rs = statement.getResultSet();
			while(rs.next()){
				containsURL.add(rs.getString(1));
			}
			
		} catch (SQLException e) {
			return;
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(statement);
			DBUtils.closeConnection(connection);
		}
	}
	
	public static Document getDocByProxy(String url){
		System.out.println(url);
		String html = "";
		HttpClient client = null;
		GetMethod get = null;
		Document everyStar = null;
		try {
			client = new HttpClient();
			ProxyUtil.setProxyHost(client);
			get = new GetMethod(url);
			get.getParams().setContentCharset("UTF-8");
			client.executeMethod(get);
			html = get.getResponseBodyAsString();
			everyStar = Jsoup.parse(html);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if(get != null)  get.releaseConnection();
		}
		return everyStar;
	
	}
	
	static void saveImgByProxy(String url,String name) throws Exception{
		
		String fileName = name+".jpg";
		OutputStream os = null;
		HttpClient client = null;
		GetMethod get = null;
		try {
			client = new HttpClient();
			ProxyUtil.setProxyHost(client);
			get = new GetMethod(url);
			client.executeMethod(get);
			byte[] buffer = get.getResponseBody();
			os = new FileOutputStream(imagePath+fileName,false);
			os.write(buffer);
			os.flush();
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		} finally {
			if(os != null){
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	class ExtractStarInfo implements Runnable{

		private int start,end;
		
		public ExtractStarInfo(int start, int end){
			this.start = start;
			this.end = end;
		}
		
		@Override
		public void run() {
			Connection connection = null;
			PreparedStatement statement = null;
			ResultSet rs = null;
			
			String sql = "INSERT INTO t_star_info (cn_name,en_name,region,vocation,info,head_photo,sex,first_word,birthday,blood,star_seat,height,weight,company)"
					+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			try {
				connection = DBUtils.getConnection();
				statement = connection.prepareStatement(sql.toString());
				
				for (int i = start; DamaiStarInfoSpider.isRunning && i <= end; i++) {
					if(DamaiStarInfoSpider.containsURL.contains(String.valueOf(i))){
						continue;
					}
					Document everyStar = getDoc(preURL + i + ".html");
					int n = 0;
					while (everyStar == null) {
						++n;
						if (tryTimes <= n) {
							log.error(preURL + i + ".html");
							break;
						}
						everyStar = getDoc(preURL + i + ".html");
					}
					try {
						String namec = everyStar.select("div.info").text().split(
								"\\s+")[0];
						statement.setString(1, namec);
						Element englishName=everyStar.select("div.info+ul li>span:contains(英文名)").first();
						if(englishName==null){
							englishName=everyStar.select("div.info+ul li:contains(英文名)").first();
						}
						statement.setString(2,englishName==null?null: englishName.text().replace(
								"英文名:", ""));
						Element region=everyStar.select("div.info+ul li>span:contains(地区)").first();
						if(region==null){
							region=everyStar.select("div.info+ul li:contains(地区)").first();
						}
						statement.setString(3,region==null? null:region.text().replace(
								"地区:", ""));
						Element professional=everyStar.select("div.info+ul li>span:contains(职业)").first();
						if(professional==null){
							professional=everyStar.select("div.info+ul li:contains(职业)").first();
						}
						statement.setString(4,professional==null? null: professional.text().replace(
								"职业:", ""));
						statement.setString(5, everyStar.select(
								"div.artist_intro p:contains(" + namec + ")")
								.text());
						saveImg(everyStar.select("#artist_photo").attr("src"),
								String.valueOf(i),imagePath );
						statement.setString(6, String.valueOf(i));
						statement.setString(7, everyStar.select("a[href~=(?i)artistlist\\.html\\?sex.*]").text());
						statement.setString(8, everyStar.select("a:contains(首字母)").text().replace(
								"首字母", "").toUpperCase());
						Element birthday=everyStar.select("div.info+ul li>span:contains(生日)").first();
						if(birthday==null){
							birthday=everyStar.select("div.info+ul li:contains(生日)").first();
						}
						statement.setString(9, birthday==null? null:birthday.text().replace("生日:", ""));
						Element blood=everyStar.select("div.info+ul li>span:contains(血型)").first();
						if(blood==null){
							blood=everyStar.select("div.info+ul li:contains(血型)").first();
						}
						statement.setString(10,blood==null? null: blood.text().replace("血型:", ""));
						Element starSeat=everyStar.select("div.info+ul li>span:contains(星座)").first();
						if(starSeat==null){
							starSeat=everyStar.select("div.info+ul li:contains(星座)").first();
						}
						statement.setString(11, starSeat==null? null:starSeat.text().replace("星座:", ""));
						Element height=everyStar.select("div.info+ul li>span:contains(身高)").first();
						if(height==null){
							height=everyStar.select("div.info+ul li:contains(身高)").first();
						}
						statement.setString(12,height==null? null:height.text().replace("身高:", ""));
						Element weight=everyStar.select("div.info+ul li>span:contains(体重)").first();
						if(weight==null){
							weight=everyStar.select("div.info+ul li:contains(体重)").first();
						}
						statement.setString(13, weight==null?null:weight.text().replace("体重:", ""));
						Element commpany=everyStar.select("div.info+ul li>span:contains(所属公司)").first();
						if(commpany==null){
							commpany=everyStar.select("div.info+ul li:contains(所属公司)").first();
						}
						statement.setString(14,commpany==null?null:commpany.text().replace("所属公司:", ""));
						statement.executeUpdate();
						Thread.sleep(900);
					} catch (Exception e) {
						log.error(preURL + i + ".html");
					}
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			} finally {
				DBUtils.closeResultSet(rs);
				DBUtils.closeStatement(statement);
				DBUtils.closeConnection(connection);
			}
		}
		
	}

	@Override
	public void extract() {
		saveStarInfo();
	}
	
	public void setFirstWord() throws Exception{
		DataDao dao = new DataDao();
		List<Map<String,Object>> result = dao.searchBySQL("select start_id,cn_name from t_star_info");
		Connection conn = DBUtils.getConnection();
		PreparedStatement stmt = conn.prepareStatement("update t_star_info set first_word=? where start_id=?");
		for(Map<String,Object> tar:result){
			char tmp = tar.get("cn_name").toString().charAt(0);
			String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(tmp);
			if(pinyin != null){
				stmt.setString(1, String.valueOf(Character.toUpperCase(pinyin[0].charAt(0))));
				stmt.setString(2, tar.get("start_id").toString());
				stmt.executeUpdate();
			}else if(Character.isLetter(tmp)){
				stmt.setString(1, String.valueOf(Character.toUpperCase(tmp)));
				stmt.setString(2, tar.get("start_id").toString());
				stmt.executeUpdate();
			}else if(Character.isDigit(tmp)){
				stmt.setString(1, String.valueOf(tmp));
				stmt.setString(2, tar.get("start_id").toString());
				stmt.executeUpdate();
			}
		}
		stmt.close();
		conn.close();
	}
}
