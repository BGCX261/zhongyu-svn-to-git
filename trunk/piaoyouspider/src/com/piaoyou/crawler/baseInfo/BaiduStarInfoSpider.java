package com.piaoyou.crawler.baseInfo;

import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sourceforge.pinyin4j.PinyinHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import com.piaoyou.bean.Star;
import com.piaoyou.domain.StarMusic;
import com.piaoyou.domain.StarNews;
import com.piaoyou.domain.StarPhoto;
import com.piaoyou.domain.StarVedio;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.CacheKey;
import com.piaoyou.util.CacheUtil;
import com.piaoyou.util.PubFun;
import com.piaoyou.util.db.DBUtils;

public class BaiduStarInfoSpider extends TicketSpider {
	static final String SEARCH_HOME_PAGE = "http://data.yule.sohu.com/star/list/------s-";
	static final Map<String,String> newsMap=new HashMap<String,String>();
	private static CacheUtil util=CacheUtil.getInstance();
	static final Map<String,String> photoMap=new HashMap<String,String>();
	static final Map<String,String> videoMap=new HashMap<String,String>();
	static final Map<String,String> soundMap=new HashMap<String,String>();
	private static final Log log = LogFactory.getLog(SohuStarInfoSpider.class);
	public void extract(){
		for(int i=1;i<=304;i++){
			String url="http://data.yule.baidu.com/index_star_s0_p"+i+".html?tags=&values=";
			Document doc=getDoc(url);
			int n=0;
			while(doc==null){
				++n;
				if (tryTimes <= n) {
					log.error(url);
					break;
				}
				doc = getDoc(url);
			}
			Iterator<Element> eleLiArray=doc.select("ul#container>li:has(a)").iterator();
			while(eleLiArray.hasNext()){
				Element eleLi=eleLiArray.next();
				String starUrl=eleLi.select("a.imgb").first().absUrl("href").trim();
				try{
				      processStar(starUrl);
				    //  Thread.sleep(1000);
				}
				catch(Exception ee){
					
				}
			}
		}
	}
	public void processStar(String url){
		Connection connection = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String sql = "INSERT INTO t_star_info (cn_name,en_name,region,vocation,info,head_url,sex,blood,star_seat,height,newsUrl,vedioUrl,photoUrl,url)"
				+ " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
		Document doc=getDoc(url);
		int n=0;
		while(doc==null){
			++n;
			if (tryTimes <= n) {
				log.error(url);
				break;
			}
			doc = getDoc(url);
		}
		String starDetailUrl=doc.select("div.modl>a").first().absUrl("href").trim();
		String photoUrl=doc.select("#img .modl a").first().absUrl("href").trim();
		String vedioUrl=doc.select("#video .modl a").first().absUrl("href").trim();
		String newsUrl=doc.select("#news .modl a").first().absUrl("href").trim();
		Map<String,String> map=extractStarDetail(starDetailUrl);
		try{
			connection = DBUtils.getConnection();
			st = connection.prepareStatement(sql.toString());
			st.setString(1, map.get("cn_name"));
			st.setString(2, map.get("en_name"));
			st.setString(3, map.get("region"));
			st.setString(4, map.get("vocation"));
			st.setString(5, map.get("info"));
			st.setString(6, map.get("head_photo"));
			st.setString(7, map.get("sex"));
			st.setString(8, map.get("blood"));
			st.setString(9, map.get("star_seat"));
			st.setString(10, map.get("height"));
			st.setString(11, newsUrl);
			st.setString(12, vedioUrl);
			st.setString(13, photoUrl);
			st.setString(14, url);
			st.executeUpdate();
		}catch(Exception ee){
			ee.printStackTrace();
		}finally{
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(st);
			DBUtils.closeConnection(connection);
		}
	}
	public Map<String,String> extractStarDetail(String url){
		Document doc=getDoc(url);
		String head_photo=doc.select("#photo img").first().absUrl("src").trim();
		String cn_name=doc.select("#photo h2").first().text().trim();
		String info=doc.select("div.intro").first().html();
		List<Node> list=doc.select("div.infolink.infohome").first().childNodes();
		Map<String,String> map=new HashMap<String,String>();
		map.put("head_photo", head_photo);
		map.put("info", info);
		map.put("cn_name", cn_name);
		for(int i=0;i<list.size();i++){
             Node temp=list.get(i);
             if(temp instanceof TextNode ){
            	String text=temp.outerHtml();
            	if(text.contains("姓名")){
            		String[] array=text.split("/");
            		if(array.length>=2){
            			map.put("en_name",array[array.length-1]);
            		}
            	}else if(text.contains("地区")&&text.contains("职业")){
            		int end=i+1;
            		for(int j=i+1;j<list.size();j++){
            			if((list.get(j) instanceof TextNode) && (!list.get(j).outerHtml().trim().equals(""))){
            				end=j;
            				break;
            			}
            		}
            		String vocation="";
            		for(int k=i+1;k<end;k++){
            			if(!(list.get(k) instanceof TextNode)){
            				String  ele=((Element)list.get(k)).text();
            			    vocation=vocation+ele+" ";
            			}
            		}
            		vocation=vocation.trim();
            		vocation=getRealRealVocation(vocation);
            		map.put("vocation", vocation);
            	}
            	else if(text.contains("地区")&&!text.contains("职业")){
            		String region=((Element)list.get(i+1)).text().replace("地区：", "").replace("&nbsp;", "");
            		region=getRealRegion(region);
            		map.put("region",region );
            	}else if(text.contains("职业")&&!text.contains("地区")){
            		int end=i+1;
            		for(int j=i+1;j<list.size();j++){
            			if((list.get(j) instanceof TextNode) && (!list.get(j).outerHtml().trim().equals(""))){
            				end=j;
            				break;
            			}
            		}
            		String vocation="";
            		for(int k=i+1;k<end;k++){
            			if(!(list.get(k) instanceof TextNode)){
            				String  ele=((Element)list.get(k)).text();
            			    vocation=vocation+ele+" ";
            			}
            		}
            		vocation=vocation.trim();
            		vocation=getRealRealVocation(vocation);
            		map.put("vocation", vocation);
            	}else if(text.contains("性别")){
            		map.put("sex", ((Element)list.get(i+1)).text());
            	}else if(text.contains("星座")){
            		map.put("star_seat", ((Element)list.get(i+1)).text());
            	}else if(text.contains("血型")){
            		map.put("blood", ((Element)list.get(i+1)).text());
            	}else if(text.contains("身高")){
            		map.put("height",text.replace("身高：", "").replace("&nbsp;", "").trim());
            	}
             }
		}
		return map;
	}
	public static void main(String[] arges) {
		BaiduStarInfoSpider spider=new BaiduStarInfoSpider();
		//spider.cleanAllStarCache();
		//spider.extract();//抓取所有明星基本信息
		//spider.extractMainPhoto();//抓取所有明星图像
		//spider.extractMainNews(4);//抓取明星新闻
		//spider.extractMainVedio();//抓取明星视频
		//spider.updateVocation();//
		//spider.extractMainMusic();//抓取明星的音乐
		//spider.updateStarRank();//跟新明星的排名
	/*	try{
			spider.setFirstWord();//设置明星的首字母
		}catch(Exception ee){
			
		}*/
		//spider.processStar("http://data.yule.baidu.com/star/%CA%C7%D4%AA%BD%E9");
		//spider.addAllStarMusicCache();
		//spider.cleanAllStarCache();
		//spider.addAllStarPhotoCache();//给图片添加缓存
		 //spider.cleanAllStarCache();//清空所有明星缓存
		// spider.addAllStarNewsCache();
	    // spider.addAllStarNewsFrontCache();
		//spider.addAllStarMusicCache();
		//spider.addAllStarPhotoCache();
		//spider.cleanAllStarNews(1);
		//spider.extractMainNews(1); 
		//spider.addAllStarNewsCache(1);
		//spider.addAllStarNewsFrontCache(1);
		
	}
	/**
	 * 抓取图片
	 */
	public void  extractMainPhoto(){
		List<Star> list=extractStarList();
		for(Star star:list){
			try{
				extractPhoto(star.getCn_name(),star.getStar_id());
			}catch(Exception ee){
				log.error(star.getCn_name()+"-"+star.getStar_id());
			}
		}
	}
	public List<Star> extractStarList(){
		Connection connection = null;
		PreparedStatement stat = null;
		ResultSet rs= null;
		List<Star> list=new ArrayList<Star>();
		String sql="select star_id,cn_name,newsUrl,vedioUrl,vocation from t_star_info";
		try{
			connection=DBUtils.getConnection();
			stat=connection.prepareStatement(sql);
			rs=stat.executeQuery();
			while(rs.next()){
				Star star=new Star();
				int id=rs.getInt(1);
				String name=rs.getString(2);
				String newsUrl=rs.getString(3);
				String vedioUrl=rs.getString(4);
				String vocation=rs.getString(5);
				star.setStar_id(id);
				star.setCn_name(name);
				star.setNewsUrl(newsUrl);
				star.setVedioUrl(vedioUrl);
				star.setVocation(vocation);
				list.add(star);
			}
		}catch(Exception ee){
			ee.printStackTrace();
		}finally{
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(stat);
			DBUtils.closeConnection(connection);
		}
		return list;
	}
	public void extractPhoto(String name,int id) {
		  getPhoto(id);
		  String cn_name="";
		  try{
			   cn_name=java.net.URLEncoder.encode(name,"gbk");
		  }catch(Exception ee){
			  ee.printStackTrace();
		  }
		  String url="http://image.baidu.com/i?ct=201326592&lm=-1&tn=baiduimagenojs&pv=&word="+cn_name+"&z=0&pn=0&rn=20&cl=2";
		  Document doc=getDoc(url);
		  Iterator<Element> it=doc.select("a").iterator();
		  while(it.hasNext()){
			  Element eleIt=it.next();
			  String href=eleIt.absUrl("href").trim();
			  if(href.contains("u=http:")){
				 String pic=PubFun.getRegex("[^$]*&u=([^&]*)&f=http[^$]*", href, 1);
				 String title=eleIt.text().trim();
				 if(photoMap.get(pic+id)==null){
					 new DataDao().executeSQL("insert into t_star_photo (STAR_ID,PHOTO_URL,PHOTO_TITLE,CRAWLED_TIME)values(?,?,?,now())", id,pic,title);
				 }
			  }
		  }
	}
	/**
	 * 抓取新闻flag=0 表示从前往后抓取所有明星
	 * flag=1 表示抓取1-1000个明星
	 * flag=2 表示抓取1001-2000个明星
	 * flag=3 表示抓取2001-3000个明星
	 * flag=4 表示抓取3001-4000个明星
	 * flag=5 表示抓取4001-5000个明星
	 * flag=6 表示抓取5001-6000个明星
	 */
	public void extractMainNews(int flag){
		List<Star> list=extractStarList();
		int size=list.size();
		int pageCount=1000;
		int pageNum=0;
		pageNum=size/pageCount;
		if(size % 1000!=0){
			pageNum+=1;
		}
		int start=(flag-1)*pageCount;
		int end=(start+pageCount)>size ? size:(start+pageCount);
		for(int i=start;i<end;i++){
			Star star=list.get(i);
			try{
				extractNews(star.getNewsUrl(),star.getStar_id());
			}catch(Exception ee){
				log.error(star.getNewsUrl()+"-"+star.getStar_id());
			}
		}
	}
	public void extractNews(String url,int star_id){
		getNews(15,star_id);
		Document doc=getDoc(url);
		Iterator<Element> it=doc.select("#r>table td.text").iterator();
		while(it.hasNext()){
			Element ele=it.next();
			String href=ele.select("a").first().absUrl("href").trim();
			String title=ele.select("a").first().text().trim();
			String date=ele.select("font>nobr").first().text();
			String newDate=PubFun.getRegex("[^@]*([\\d]{4}-[\\d]{1,2}-[\\d]{1,2}[^@]*)", date, 1).trim();//2011-8-17 15:29
			String endDate="";
			try{
				SimpleDateFormat formate=new SimpleDateFormat("yyyy-MM-dd HH:mm");
				endDate=formate.format(new Date());
			}catch(Exception ee){
			}
			long days=0;
			try{
				days=PubFun.getDaysBetweenTwoDate(newDate, endDate, "yyyy-MM-dd HH:mm");
			}catch(Exception ee){
			}
			if(days>15){
				continue;
			}
			try{
				Date ndate=PubFun.parseStringToDate(newDate, "yyyy-MM-dd HH:mm");
				newDate=PubFun.parseDateToStr(ndate, "yyyy-MM-dd");
			}catch(Exception ee){
				newDate=newDate.substring(0,10);
			}
			if(newsMap.get(href+star_id)==null){
				String sql="insert into t_star_news (STAR_ID,NEWS_TITLE,NEWS_URL,CRAWLED_TIME,CREATE_DATE)values(?,?,?,now(),?)";
				new DataDao().executeSQL(sql, star_id,title,href,newDate);
			}
		}
	}
	
	public void extractMainVedio(){
		List<Star> list=extractStarList();
		for(Star star:list){
			String oneStarUrl="";
			try {
				Document doc = getDoc(SEARCH_HOME_PAGE+ java.net.URLEncoder.encode(star.getCn_name(), "gbk") + "-1.html");
				String url=SEARCH_HOME_PAGE+ java.net.URLEncoder.encode(star.getCn_name(), "gbk") + "-1.html";
				try{
					 oneStarUrl = doc.getElementsByClass("pt").first().getElementsByTag("a").first().absUrl("href");
				}catch(Exception ee){
					continue;
				}
				getVideoFrom3w(oneStarUrl,star.getStar_id());
			} catch (Exception e) {
				log.error(oneStarUrl+"-"+star.getStar_id());
			}
		}
	}
	/***
	 * 抓取视频信息
	 */
	public void getVideoFrom3w(String url, int starId) {
		Document doc=getDoc(url);
		getVideo(starId);//videosDiv
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
					new DataDao().executeSQL(sql, starId, vdieotitle, vdieoPhotoUrl,
							vdieoUrl);
				}
			}
		}
	}
    /**
     * 抓取音乐
     */
    public void extractMainMusic(){
    	List<Star> list=extractStarList();
    	for(Star star:list){
    		try{
    			extractMusic(star.getCn_name(),star.getStar_id());
    		}catch(Exception ee){
    			ee.printStackTrace();
    		}
    	}
    }
    public void extractMusic(String name,int id){
    	  String cn_name="";
		  try{
			   cn_name=java.net.URLEncoder.encode(name,"gbk");
		  }catch(Exception ee){
			  ee.printStackTrace();
		  }
		  getSound(id);
		  Document doc=null;
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
							if (soundMap.get(voiceUrl + String.valueOf(id)) == null) {
								String sql = "insert into t_star_sound (STAR_ID,SOUND_TITLE,SOUND_URL,CRAWLED_TIME)values(?,?,?,now())";
								new DataDao().executeSQL(sql, id, voiceName,
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
	public void getNews(int day,int id){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "select NEWS_URL,star_id from t_star_news where star_id="+ id +"  and  DATEDIFF(now(),DATE (create_date))<=? order by news_id";
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
	public void getPhoto(int id){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "select star_id,photo_url from t_star_photo where star_id="+ id +"  order by photo_id";
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
	public void getSound(int id){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "select star_id,SOUND_URL from t_star_sound where star_id="+ id +"  order by sound_id";
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
	public void getVideo(int id){
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet rs = null;
		String sql = "select star_id,VIDEO_URL from t_star_video where star_id="+ id +"  order by video_id";
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
	private String getRealRegion(String region){
		if(region==null){
			return "其他";
		}
		if(region.contains("内地")||region.contains("中国")){
			region="大陆";
		}else if(region.contains("香港")||region.contains("台湾")){
			region="港台";
		}else if(region.contains("韩国")||region.contains("日本")){
			region="日韩";
		}else if(region.contains("意大利")||region.contains("爱尔兰")||region.contains("美国")||region.contains("英国")|| region.contains("加拿大")||region.contains("法国")||region.contains("德国")||region.contains("欧美")){
			region="欧美";
		}
		else{
			region="其他";
		}
		return region;
	}
	private String getRealRealVocation(String vocation){
		if(vocation==null||"".equals(vocation)){
			return "其他";
		}
		if(vocation.contains("主持")){
			vocation="主持人";
		}else if(vocation.contains("歌手")||vocation.contains("演员")||vocation.contains("模特")||vocation.contains("编辑")||vocation.contains("音乐家")||vocation.contains("作曲家")||vocation.contains("主持人")||vocation.contains("导演")){
			
		}else{
			vocation="其他";
		}
		return vocation;
	}
	/**
	 * 设置明星的排名
	 */
	public void updateVocation(){
		List<Star> list=extractStarList();
		String sql="update t_star_info set vocation=? where star_id=?";
		for(Star star:list){
			new DataDao().executeSQL(sql, getRealRealVocation(star.getVocation()),star.getStar_id());
		}
	}
	/**
	 * 设置明星的首字母
	 */
	public void setFirstWord() throws Exception{
		DataDao dao = new DataDao();
		List<Map<String,Object>> result = dao.searchBySQL("select star_id,cn_name from t_star_info");
		Connection conn = DBUtils.getConnection();
		PreparedStatement stmt = conn.prepareStatement("update t_star_info set first_word=? where star_id=?");
		for(Map<String,Object> tar:result){
			char tmp = tar.get("cn_name").toString().charAt(0);
			String[] pinyin = PinyinHelper.toHanyuPinyinStringArray(tmp);
			if(pinyin != null){
				stmt.setString(1, String.valueOf(Character.toUpperCase(pinyin[0].charAt(0))));
				stmt.setString(2, tar.get("star_id").toString());
				stmt.executeUpdate();
			}else if(Character.isLetter(tmp)){
				stmt.setString(1, String.valueOf(Character.toUpperCase(tmp)));
				stmt.setString(2, tar.get("star_id").toString());
				stmt.executeUpdate();
			}else if(Character.isDigit(tmp)){
				stmt.setString(1, String.valueOf(tmp));
				stmt.setString(2, tar.get("star_id").toString());
				stmt.executeUpdate();
			}
		}
		stmt.close();
		conn.close();
	}
	/**
	 * 设置明星的排名
	 */
	public void updateStarRank(){
		String sql="SELECT distinct star_id,create_date FROM t_star_news order by create_date desc,star_id asc";
		String updateSql="update t_star_info set rank=? where star_id=?";
		List<Map<String, Object>> starList=new DataDao().searchBySQL(sql);
		for(int i=0;i<starList.size();i++){
			new DataDao().executeSQL(updateSql, i,Integer.parseInt(starList.get(i).get("star_id").toString()));
		}
	}
}