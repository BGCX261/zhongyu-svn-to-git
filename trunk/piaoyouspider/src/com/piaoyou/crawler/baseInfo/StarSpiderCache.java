package com.piaoyou.crawler.baseInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.piaoyou.dao.DataDao;
import com.piaoyou.domain.StarMusic;
import com.piaoyou.domain.StarNews;
import com.piaoyou.domain.StarPhoto;
import com.piaoyou.domain.StarVedio;
import com.piaoyou.util.CacheKey;
import com.piaoyou.util.CacheUtil;

public class StarSpiderCache {
	private static CacheUtil util=CacheUtil.getInstance();

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	/**
	 * 清空明星的所有缓存
	 */
	public void  cleanAllStarCache(){
		String sql="select star_id  from  t_star_info where 1=1 ";
		DataDao dao=new DataDao();
		List<Map<String,Object>> list=dao.searchBySQL(sql);
		for(Map<String,Object> map:list){
			String star_id=String.valueOf(map.get("star_id"));
			//删除新闻缓存
			util.delCache(CacheKey.STAR_NEWS+star_id);
			util.delCache(CacheKey.STAR_NEWS_PAGE+star_id);
			util.delCache(CacheKey.STAR_NEWS_FRONT+star_id);
			//删除明星音乐缓存
			util.delCache(CacheKey.STAR_MUSIC+star_id);
			util.delCache(CacheKey.STAR_MUSIC_PAGE+star_id);
			//删除图片缓存
			util.delCache(CacheKey.STAR_PHOTO+star_id);
			util.delCache(CacheKey.STAR_PHOTO_PAGE+star_id);
			//删除视频缓存
			util.delCache(CacheKey.STAR_VIDEO+star_id);
			util.delCache(CacheKey.STAR_VIDEO_PAGE+star_id);
		}
	}
	/**
	 * 给所有明星新闻添加缓存
	 */
	public void addAllStarNewsCache(int flag){
		int pageCount=1000;
		int start=(flag-1)*pageCount;
		String starsql="select star_id  from  t_star_info where 1=1 limit "+start+" , "+pageCount;
		DataDao dao=new DataDao();
		List<Map<String,Object>> list=dao.searchBySQL(starsql);
		for(Map<String,Object> map:list){
			int star_id=(Integer)map.get("star_id");
			String newssql= "select NEWS_ID,STAR_ID,NEWS_TITLE,NEWS_PHOTO,NEWS_URL,CRAWLED_TIME,CREATE_DATE from t_star_news where  STAR_ID="+star_id +" order by CREATE_DATE desc,CRAWLED_TIME desc";
			String newssqlCount="select count(*) as allcount from  t_star_news where STAR_ID="+star_id;
			List<Map<String,Object>> newsListMap=dao.searchBySQL(newssql);
			List<StarNews> newslist=new ArrayList<StarNews>();
			for(Map<String,Object> newsMap: newsListMap){
				StarNews starNews=new StarNews();
				starNews.setSTAR_ID((Integer)newsMap.get("NEWS_ID"));
				starNews.setSTAR_ID(star_id);
				starNews.setNEWS_TITLE((String)newsMap.get("NEWS_TITLE"));
				starNews.setNEWS_PHOTO((String)newsMap.get("NEWS_PHOTO"));
				starNews.setNEWS_URL((String)newsMap.get("NEWS_URL"));
				starNews.setCRAWLED_TIME(newsMap.get("CRAWLED_TIME").toString().substring(0, 19));
				starNews.setCREATE_DATE((String)newsMap.get("CREATE_DATE"));
				newslist.add(starNews);
			}
			util.putCache(CacheKey.STAR_NEWS+star_id, newslist, new Date(new java.util.Date().getTime()+24*3*30*3600*1000));//将新闻放入缓存
			List<Map<String,Object>> newsSqlCountMap =dao.searchBySQL(newssqlCount);
			long newsAllCount = (Long)newsSqlCountMap.get(0).get("allcount");
			System.out.println("newsAllCount:"+newsAllCount);
			util.putCache(CacheKey.STAR_NEWS_PAGE+star_id, newsAllCount, new Date(new java.util.Date().getTime()+24*3*30*3600*1000));//将新闻放入缓存
		}
	}
	/**
	 * 给所有明星新闻删除缓存
	 * flag=1 表示1-1000个明星
	 * flag=2 表示1001-2000个明星
	 * .............
	 */
	public void cleanAllStarNews(int flag){
		int pageCount=1000;
		int start=(flag-1)*pageCount;
		String starsql="select star_id  from  t_star_info where 1=1 limit "+start+" , "+pageCount;
		DataDao dao=new DataDao();
		List<Map<String,Object>> list=dao.searchBySQL(starsql);
		for(Map<String,Object> map:list){
			int star_id=(Integer)map.get("star_id");
			util.delCache(CacheKey.STAR_NEWS+star_id);
			util.delCache(CacheKey.STAR_NEWS_PAGE+star_id);
			util.delCache(CacheKey.STAR_NEWS_FRONT+star_id);
		}
	}
	
	/**
	 * 给明星音乐添加缓存
	 */
	public void addAllStarMusicCache(){
		String starsql="select star_id  from  t_star_info";
		DataDao dao=new DataDao();
		List<Map<String,Object>> list=dao.searchBySQL(starsql);
		for(Map<String,Object> map:list){
			int star_id=(Integer)map.get("star_id");
			String musicssql= "   select SOUND_ID,STAR_ID,SOUND_TITLE,SOUND_URL,CRAWLED_TIME from t_star_sound where  STAR_ID="+star_id +" order by SOUND_ID desc";
			String musicsqlCount="select count(*) as allcount from  t_star_sound where STAR_ID="+star_id;
			List<Map<String,Object>> musicListMap=dao.searchBySQL(musicssql);
			List<StarMusic> musiclist=new ArrayList<StarMusic>();
			for(Map<String,Object> musicMap: musicListMap){
				StarMusic starMusic=new StarMusic();
				starMusic.setSTAR_ID((Integer)musicMap.get("SOUND_ID"));
				starMusic.setSTAR_ID(star_id);
				starMusic.setSOUND_TITLE((String)musicMap.get("SOUND_TITLE"));
				starMusic.setSOUND_URL((String)musicMap.get("SOUND_URL"));
				starMusic.setCRAWLED_TIME(musicMap.get("CRAWLED_TIME").toString().substring(0, 19));
				musiclist.add(starMusic);
			}
			util.putCache(CacheKey.STAR_MUSIC+star_id, musiclist, new Date(new java.util.Date().getTime()+24*3*30*3600*1000));//将新闻放入缓存
			List<Map<String,Object>> musicSqlCountMap =dao.searchBySQL(musicsqlCount);
			long musicAllCount = (Long)musicSqlCountMap.get(0).get("allcount");
			util.putCache(CacheKey.STAR_MUSIC_PAGE+star_id, musicAllCount, new Date(new java.util.Date().getTime()+24*3*30*3600*1000));//将新闻放入缓存
		}
	}
	/**
	 * 
	 * 给明星音乐删除缓存
	 */
	public void delAllStarMusicCache(){
		String sql="select star_id  from  t_star_info where 1=1 ";
		DataDao dao=new DataDao();
		List<Map<String,Object>> list=dao.searchBySQL(sql);
		for(Map<String,Object> map:list){
			String star_id=String.valueOf(map.get("star_id"));
			util.delCache(CacheKey.STAR_MUSIC+star_id);
			util.delCache(CacheKey.STAR_MUSIC_PAGE+star_id);
		}
		
	}
	/**
	 * 给明星视频添加缓存
	 * 
	 */
	public void addAllStarVedioCache(){
		String starsql="select star_id  from  t_star_info";
		DataDao dao=new DataDao();
		List<Map<String,Object>> list=dao.searchBySQL(starsql);
		for(Map<String,Object> map:list){
			int star_id=(Integer)map.get("star_id");
			String vediossql= "   select VIDEO_ID,STAR_ID,VIDEO_TITLE,VIDEO_PHOTO_URL,VIDEO_PHOTO_PATH,VIDEO_URL,CRAWLED_TIME from t_star_video where  STAR_ID="+star_id +" order by VIDEO_ID desc ";
			String vediosqlCount="select count(*) as allcount from  t_star_video where STAR_ID="+star_id;
			List<Map<String,Object>> vedioListMap=dao.searchBySQL(vediossql);
			List<StarVedio> vediolist=new ArrayList<StarVedio>();
			for(Map<String,Object> vedioMap: vedioListMap){
				StarVedio starVedio=new StarVedio();
				starVedio.setVIDEO_ID((Integer)vedioMap.get("VIDEO_ID"));
				starVedio.setSTAR_ID(star_id);
				starVedio.setVIDEO_TITLE((String)vedioMap.get("VIDEO_TITLE"));
				starVedio.setVIDEO_PHOTO_URL((String)vedioMap.get("VIDEO_PHOTO_URL"));
				starVedio.setVIDEO_PHOTO_PATH((String)vedioMap.get("VIDEO_PHOTO_PATH"));
				starVedio.setVIDEO_URL((String)vedioMap.get("VIDEO_URL"));
				starVedio.setCRAWLED_TIME(vedioMap.get("CRAWLED_TIME").toString().substring(0,19));
				vediolist.add(starVedio);
			}
			util.putCache(CacheKey.STAR_VIDEO+star_id, vediolist, new Date(new java.util.Date().getTime()+24*3*30*3600*1000));//将新闻放入缓存
			List<Map<String,Object>> vedioSqlCountMap =dao.searchBySQL(vediosqlCount);
			long vedioAllCount = (Long)vedioSqlCountMap.get(0).get("allcount");
			util.putCache(CacheKey.STAR_VIDEO_PAGE+star_id, vedioAllCount, new Date(new java.util.Date().getTime()+24*3*30*3600*1000));//将新闻放入缓存
		}
	}
	/**
	 * 删除明星视频缓存
	 */
	public void cleanAllStarVedioCache(){
		String sql="select star_id  from  t_star_info where 1=1 ";
		DataDao dao=new DataDao();
		List<Map<String,Object>> list=dao.searchBySQL(sql);
		for(Map<String,Object> map:list){
			String star_id=String.valueOf(map.get("star_id"));
			util.delCache(CacheKey.STAR_VIDEO+star_id);
			util.delCache(CacheKey.STAR_VIDEO_PAGE+star_id);
		}
	}
	/**
	 * 给图片添加缓存
	 */
	public void addAllStarPhotoCache(){
		String starsql="select star_id  from  t_star_info";
		DataDao dao=new DataDao();
		List<Map<String,Object>> list=dao.searchBySQL(starsql);
		for(Map<String,Object> map:list){
			int star_id=(Integer)map.get("star_id");
			String photosql= "select PHOTO_ID,STAR_ID,PHOTO_TITLE,PHOTO_URL,PHOTO_PATH,CRAWLED_TIME from t_star_photo where  STAR_ID="+star_id +" order by PHOTO_ID desc ";
			String photosqlCount="select count(*) as allcount from  t_star_photo where STAR_ID="+star_id;
			List<Map<String,Object>> photoListMap=dao.searchBySQL(photosql);
			List<StarPhoto> photolist=new ArrayList<StarPhoto>();
			for(Map<String,Object> photoMap: photoListMap){
				StarPhoto starPhoto=new StarPhoto();
				starPhoto.setPHOTO_ID((Integer)photoMap.get("PHOTO_ID"));
				starPhoto.setSTAR_ID(star_id);
				starPhoto.setPHOTO_TITLE((String)photoMap.get("PHOTO_TITLE"));
				starPhoto.setPHOTO_URL((String)photoMap.get("PHOTO_URL"));
				starPhoto.setPHOTO_PATH((String)(photoMap.get("PHOTO_PATH")));
				starPhoto.setCRAWLED_TIME((photoMap.get("CRAWLED_TIME").toString().substring(0, 19)));
				photolist.add(starPhoto);
			}
			util.putCache(CacheKey.STAR_PHOTO+star_id, photolist, new Date(new java.util.Date().getTime()+24*3*30*3600*1000));//将新闻放入缓存
			List<Map<String,Object>> photoSqlCountMap =dao.searchBySQL(photosqlCount);
			long photoAllCount = (Long)photoSqlCountMap.get(0).get("allcount");
			util.putCache(CacheKey.STAR_PHOTO_PAGE+star_id, photoAllCount, new Date(new java.util.Date().getTime()+24*3*30*3600*1000));//将新闻放入缓存
		}
	}
	/**
	 * 给所有明星图片删除缓存
	 * 
	 */
	public void cleanAllStarPhotoCache(){
		String sql="select star_id  from  t_star_info where 1=1 ";
		DataDao dao=new DataDao();
		List<Map<String,Object>> list=dao.searchBySQL(sql);
		for(Map<String,Object> map:list){
			String star_id=String.valueOf(map.get("star_id"));
			util.delCache(CacheKey.STAR_PHOTO+star_id);
			util.delCache(CacheKey.STAR_PHOTO_PAGE+star_id);
		}
	}
	/**
	 * 给明星最新新闻添加缓存(前10条)
	 */
	public void addAllStarNewsFrontCache(int flag){
		int pageCount=1000;
		int start=(flag-1)*pageCount;
		String starsql="select star_id  from  t_star_info where 1=1 limit "+start+" , "+pageCount;
		DataDao dao=new DataDao();
		List<Map<String,Object>> list=dao.searchBySQL(starsql);
		for(Map<String,Object> map:list){
			int star_id=(Integer)map.get("star_id");
			String newssql= "select NEWS_ID,STAR_ID,NEWS_TITLE,NEWS_PHOTO,NEWS_URL,CRAWLED_TIME,CREATE_DATE from t_star_news where  STAR_ID="+star_id +" order by CREATE_DATE desc limit 0 , 10";
			List<Map<String,Object>> newsListMap=dao.searchBySQL(newssql);
			List<StarNews> newslist=new ArrayList<StarNews>();
			for(Map<String,Object> newsMap: newsListMap){
				StarNews starNews=new StarNews();
				starNews.setSTAR_ID((Integer)newsMap.get("NEWS_ID"));
				starNews.setSTAR_ID(star_id);
				starNews.setNEWS_TITLE((String)newsMap.get("NEWS_TITLE"));
				starNews.setNEWS_PHOTO((String)newsMap.get("NEWS_PHOTO"));
				starNews.setNEWS_URL((String)newsMap.get("NEWS_URL"));
				starNews.setCRAWLED_TIME(newsMap.get("CRAWLED_TIME").toString().substring(0,19));
				starNews.setCREATE_DATE((String)newsMap.get("CREATE_DATE"));
				newslist.add(starNews);
			}
			util.putCache(CacheKey.STAR_NEWS_FRONT+star_id, newslist, new Date(new java.util.Date().getTime()+24*3*30*3600*1000));//将新闻放入缓存
		}
	
	}
}


