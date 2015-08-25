package com.piaoyou.crawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;

import com.piaoyou.bean.CommonInfo;
import com.piaoyou.dao.DataDao;


public abstract class TicketSpider {
	
	protected static final int tryTimes = 10;//重试次数
	protected volatile static boolean isRunning = true;
	protected static int timeout = 50*1000;
	protected static final String userAgent = "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1)";
	
	private long delay = 0;//抓取间隔
	private DataDao dao;
	
	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}

	public static int getTimeout() {
		return timeout;
	}

	public static void setTimeout(int timeout) {
		TicketSpider.timeout = timeout;
	}

	public DataDao getDao() {
		return dao;
	}

	public void setDao(DataDao dao) {
		this.dao = dao;
	}

	final protected void saveCommonInfo(CommonInfo commInfo) throws SQLException {
		dao.saveCommonInfo(commInfo);
	}
	
	/**
	 * 根据url地址，返回包装url页面内容的Document对象
	 * @param url
	 * @return
	 */
	public static Document getDoc(String url) {
		Document doc = null;
		for(int i=0;i<tryTimes;i++){
			try {
				doc = Jsoup.connect(url).referrer(url)
					.userAgent(userAgent).timeout(timeout).get();
			} catch (IOException e) {
				//ignore
			}
			if(doc != null){
				break;
			}
		}
		if(doc == null){
			LogFactory.getLog(TicketSpider.class).error(url);
		}
		return doc;
	}
	
	/**
	 * 根据url地址，返回包装url页面内容的Document对象,用于页面没有编码说明的页面
	 * @param url		目标url地址
	 * @param charset	目标页面的编码
	 * @return
	 */
	public static Document getDoc(String url,String charset){
		Document doc = null;
		Response res = null;
		for(int i=0;i<tryTimes;i++){
			try {
				res = Jsoup.connect(url).referrer(url)
					.userAgent(userAgent).timeout(timeout).execute();
			} catch (IOException e) {
				//ignore
			}
			if(res != null){
				break;
			}
		}
		if(res == null){
			LogFactory.getLog(TicketSpider.class).error(url);
		}else{
			try {
				doc = Jsoup.parse(new String(res.bodyAsBytes(),charset),url);
			} catch (UnsupportedEncodingException e) {
				LogFactory.getLog(TicketSpider.class).error("不支持的字符集"+charset);
			}
		}
		return doc;
	}

	/**
	 * 模拟post请求
	 * @param url		要请求的url地址
	 * @param dataMap	需要传递的post参数
	 * @return
	 */
	public static Document getDocPost(String url,Map<String,String> dataMap) {
		Document doc = null;
		Connection conn = Jsoup.connect(url).referrer(url)
			.userAgent(userAgent).timeout(timeout).data(dataMap);
		for(int i=0;i<tryTimes;i++){
			try {
				doc = conn.post();
			} catch (IOException e) {
				//ignore
			}
			if(doc != null){
				break;
			}
		}
		if(doc == null){
			LogFactory.getLog(TicketSpider.class).error(url);
		}
		return doc;
	}
	
	/**
	 * 保存网络上的图片
	 * @param url		图片的url地址
	 * @param name		图片要保存的文件名
	 * @param path		图片要保存的路径
	 * @return
	 * @throws Exception
	 */
	public static String saveImg(String url, String name ,String path) throws Exception{
		byte[] buffer = null;
		for(int i=0;i<tryTimes;i++){
			try {
				buffer = Jsoup.connect(url).timeout(timeout).execute().bodyAsBytes();
			} catch (IOException e) {
				//ignore
			}
			if(buffer != null){
				break;
			}
		}
		if(buffer == null){
			return null;
		}
		String fileName = "";
		OutputStream os = null;
		try {
			fileName = name+getType(buffer);
			os = new FileOutputStream(path + File.separator + fileName,false);
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
		return fileName;
	}
	
	/**
	 * 根据二进制流判断常见图片类型
	 * @param data
	 * @return
	 */
	public static String getType(byte[] data) {
        String type = "";
        if(data == null || data.length<3){
        	return type;
        }
        if (data[0] == (byte)0xff && data[1] == (byte)0xd8) {
        	//jpeg文件头标识 (2 bytes): $ff, $d8 (SOI) (JPEG 文件标识) 
        	type = ".jpg";
        }else if (data[0] == (byte)0x89 && data[1] == (byte)0x50) {
        	//文件头标识 (8 bytes)89 50 4E 47 0D 0A 1A 0A
            type = ".png";
        }else if (data[0] == (byte)0x47 && data[1] == (byte)0x49 && data[2] == (byte)0x46) {
        	//文件头标识 (6 bytes)   47 49 46 38 39(37) 61
            type = ".gif";
        }else if(data[0] == (byte)0x42 && data[2] == (byte)0x4d ){
        	//文件头标识 (2 bytes)   42 4D
        	type = ".bmp";
        }
        return type;
    }

}
