package com.piaoyou.crawler.baseInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import net.sourceforge.pinyin4j.PinyinHelper;

import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.ImageUtil;
import com.piaoyou.util.PubFun;

/**
 * 下载图片工具类
 * @author jinquan
 * @date   2011-7-6
 * 1：演出信息标题图片
 * 2：场馆标题
 * 3：场馆更多图片
 * 4：明星标题图片
 * 5：明星更多图片
 * 6：明星视频图片
 */
public class DownPhotoSpider  extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(DownPhotoSpider.class);
    static String showType = "h";
	public static void show(){
		System.out.println("\n");
		System.out.println("    *****************************************************");
		System.out.println("        你好票务垂直搜素引擎图片下载工具  v1.0        ");
		System.out.println("          北京你好众娱文化传播有限公司           ");
		System.out.println("      参数说明   例如：java -jar down.jar show    ");
		System.out.println("          h 帮助                                                           ");
		System.out.println("          1  下载    《演出标题》    图片                                                           ");
		System.out.println("          2  下载    《场馆标题》   图片                                                           ");
		System.out.println("          3  下载    《场馆更多》    图片                                                           ");
		System.out.println("          4  下载    《明星更多》    图片                                                           ");
		System.out.println("          5  下载    《明星视频》    图片                                                           ");
		System.out.println("          6  下载     《明星》    图片                           ");
		System.out.println("          7  下载      《按顺序下载演出及明星全部》    图片 ");
		System.out.println("    *****************************************************\n");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		show();
		
		if(args.length>0) {
			showType = args[0].trim();
		}else{
			System.exit(0);
		}
		DownPhotoSpider downPhotoSpider=new DownPhotoSpider();
		downPhotoSpider.setDao(new DataDao());
		downPhotoSpider.extract();
//		downPhotoSpider.downStarPhoto();
	}

	@Override
	public void extract() {
		if(showType.contains("h")){
			System.exit(0);
		}else if(showType.contains("1")){
			downShowImg();
			System.exit(0);
		}else if(showType.contains("2")){
			downSiteTitleImg();
			System.exit(0);
		}else if(showType.contains("3")){
			downSiteMoreImg();
			System.exit(0);
		}else if(showType.contains("4")){
			downStarMoreImg();
			System.exit(0);
		}else if(showType.contains("5")){
			downStarVideoImg();
			System.exit(0);
		}else if(showType.contains("6")){
			downStarPhoto();
			System.exit(0);
			
		}else if(showType.contains("7")){
			downShowImg();
//			downSiteTitleImg();
//			downSiteMoreImg();
			downStarPhoto();
			downStarMoreImg();
			downStarVideoImg();
			System.exit(0);
		}else{
			System.out.println("    *****************************************************");
			System.out.println("                搞什么啊？不按套路出牌       GoodBy!!");
			System.out.println("    *****************************************************\n");
			System.exit(0);
		}
	}
	/**
	 * 明星图片路径
	 */
	private void downStarPhoto(){
		log.info("明星图片----------开始");
		List<Map<String, Object>> list =  getDao().searchBySQL("select star_id,first_word,cn_name,head_url,head_photo from t_star_info where head_photo is null");
		for (int v = 0; v < list.size(); v++) {
			String star_id = list.get(v).get("star_id").toString();
			String first_word = list.get(v).get("first_word").toString();
			String cn_name = list.get(v).get("cn_name").toString();
			if(list.get(v).get("head_url")==null){
				continue;
			}
			String photo_url=list.get(v).get("head_url").toString();
				String de=Const.IMG_SERVICE_ROOT_PATH+Const.STAR_TITLE_IMG_PAHT+first_word;
				 isExist(de);
				 String fileName=star_id+"_"+Const.IMG_FROM_SPIDER+"_"+new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;
				if(ImageUtil.compressImg(photo_url,de+"/"+fileName,Const.STAR_MORE_PHOTO_WIDTH,Const.STAR_MORE_PHOTO_HEIGHT)){
					new DataDao().executeSQL("update t_star_info set head_photo=? where star_id=?",Const.STAR_TITLE_IMG_PAHT+first_word+"/"+fileName,star_id);
					log.info("明星图片---"+star_id+"   :"+cn_name);
				}else{
					log.error("明星图片---"+star_id+"   :"+cn_name);
				}
			}
		log.info("明星图片----------结束");
	}
	
	/**
	 * 座位图
	 */
	public void downSeatPic(){
		log.info("座位图片----------开始");
		List<Map<String, Object>> list =  getDao().searchBySQL("select seatPic_path,show_id from t_show_info where seatPic_path is not null and left(seatPic_path, 3)='htt' ");
		for (int v = 0; v < list.size(); v++) {
			String show_id = list.get(v).get("show_id").toString();
			String photo_url=list.get(v).get("seatPic_path").toString();
			if(photo_url.indexOf("wutu.gif")!=-1){
				new DataDao().executeSQL("update t_show_info set seatPic_path=? where show_id=?",null,show_id);
			}
			else{	
				 String de=Const.IMG_SERVICE_ROOT_PATH+Const.SHOW_SEAT_IMG_PATH;
				 isExist(de);
				 String fileName=show_id+"_"+Const.IMG_FROM_SPIDER+"_"+new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;
				if(ImageUtil.compressImg(photo_url,de+"/"+fileName,Const.STAR_MORE_PHOTO_WIDTH,Const.STAR_MORE_PHOTO_HEIGHT)){
					new DataDao().executeSQL("update t_show_info set seatPic_path=? where show_id=?",Const.SHOW_SEAT_IMG_PATH+fileName,show_id);
					log.info("座位图片---"+show_id+"   :");
				}else{
					log.error("座位图片---"+show_id+"   :");
				}
			}
		}
		log.info("明星图片----------结束");
	}
	/**
	 * 下载的演出信息图片
	 * 
	 */
	public void downShowImg(){
		log.info("演出信息图片----------开始");
		String sql="SELECT show_id,show_name,show_photo FROM t_show_info where show_photo_path is null  and show_id_cor is null order by show_id";
		List<Map<String, Object>> list= getDao().searchBySQL(sql);	
		for(int i=0;i<list.size();i++){
			String showId=list.get(i).get("show_id").toString();
			String showName=list.get(i).get("show_name").toString();
			if(list.get(i).get("show_photo")==null){
				continue;
			}
			String show_photo_url=list.get(i).get("show_photo").toString();
			String first_word=PubFun.getHeadChar(showName);
			String de=Const.IMG_SERVICE_ROOT_PATH+Const.SHWO_TITLE_PATH+first_word;
			//目录不存在创建目录
			isExist(de);
			String fileName=showId+"_"+Const.IMG_FROM_SPIDER+"_"+new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;
		    if(ImageUtil.compressImg(show_photo_url,de+"/"+fileName,Const.SHOW_PHOTO_WIDTH,Const.SHOW_PHOTO_HEIGHT)){
		    	String updateSql="update t_show_info set show_photo_path=? where show_id=?";
		    	getDao().executeSQL(updateSql, Const.SHWO_TITLE_PATH+first_word+"/"+fileName,showId);
				log.info("演出信息图片---"+showId+"   :"+showName);
		    }else{
				log.error("演出信息图片---"+showId+"   :"+showName);
			}
		}
		log.info("演出信息图片----------结束");
	}
	
	/**
	 * 下载场馆标题图片
	 */
	private void downSiteTitleImg(){
		log.info("场馆标题图片----------开始");
		List<Map<String, Object>> list =  getDao().searchBySQL("select site_id,site_name,site_photo_url from t_site_info where site_photo_path is null");
		for (int v = 0; v < list.size(); v++) {
			String site_name = list.get(v).get("site_name").toString();
			String site_id = list.get(v).get("site_id").toString();
			if(list.get(v).get("site_photo_url")==null){
				continue;
			}
			String site_photo_url=list.get(v).get("site_photo_url").toString();
			String first_word=PubFun.getHeadChar(site_name);
			String de=Const.IMG_SERVICE_ROOT_PATH+Const.SITE_TITLE_IMG_PATH+first_word;
			//目录不存在创建目录
			isExist(de);
			String fileName=site_id+"_"+Const.IMG_FROM_SPIDER+"_"+new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;
			if(ImageUtil.compressImg(site_photo_url,de+"/"+fileName,Const.SITE_PHOTO_WIDTH,Const.SITE_PHOTO_HEIGHT)){
				new DataDao().executeSQL("update t_site_info set site_photo_path=? where site_id=?", Const.SITE_TITLE_IMG_PATH+first_word+"/"+fileName,site_id);
				log.info("场馆标题图片---"+site_id+"   :"+site_name);
			}else{
				log.error("场馆标题图片---"+site_id+"   :"+site_name);
			}
		}
		log.info("场馆标题图片----------结束");
	}
	
	/**
	 * 下载场馆更多图片
	 */
	private void downSiteMoreImg(){
		log.info("场馆更多图片----------开始");
		List<Map<String, Object>> list =  getDao().searchBySQL("SELECT t1.site_name,t1.site_id,t2.site_photo_url,t2.site_photo_id   FROM t_site_info t1 Inner Join t_site_photo t2 ON t1.site_id = t2.site_id where t2.site_photo_path is null ");
		for (int v = 0; v < list.size(); v++) {
			String site_id = list.get(v).get("site_id").toString();
			String site_name = list.get(v).get("site_name").toString();
			String site_photo_id = list.get(v).get("site_photo_id").toString();
			if(list.get(v).get("site_photo_url")==null){
				continue;
			}
			String site_photo_url=list.get(v).get("site_photo_url").toString();

			String first_word=PubFun.getHeadChar(site_name);
			String de=Const.IMG_SERVICE_ROOT_PATH+Const.SITE_MORE_IMG_PATH+first_word;
			//目录不存在创建目录
			isExist(de);
			String fileName=site_id+"_"+Const.IMG_FROM_SPIDER+"_"+new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;

			if(ImageUtil.compressImg(site_photo_url,de+"/"+fileName,Const.SITE_MORE_PHOTO_WIDTH,Const.SITE_MORE_PHOTO_HEIGHT)){
				new DataDao().executeSQL("update t_site_photo set site_photo_path=? where site_photo_id=?", Const.SITE_MORE_IMG_PATH+first_word+"/"+fileName,site_photo_id);
				log.info("场馆更多图片---"+site_id+"   :"+site_name);
			}else{
				log.error("场馆更多图片---"+site_id+"   :"+site_name);
			}
		}
		log.info("场馆更多图片----------结束");
	}
	
	/**
	 * 下载明星更多图片
	 */
	private void downStarMoreImg(){
		log.info("明星更多图片----------开始");
		List<Map<String, Object>> list =  getDao().searchBySQL("SELECT t1.star_id,t1.cn_name,t1.first_word,t2.PHOTO_ID,t2.PHOTO_URL FROM t_star_info t1 Inner Join t_star_photo t2 ON t1.star_id = t2.STAR_ID WHERE t2.PHOTO_PATH IS NULL ");
		for (int v = 0; v < list.size(); v++) {
			String star_id = list.get(v).get("star_id").toString();
			String photo_id = list.get(v).get("PHOTO_ID").toString();
			String first_word = list.get(v).get("first_word").toString();
			String cn_name = list.get(v).get("cn_name").toString();
			if(list.get(v).get("PHOTO_URL")==null){
				continue;
			}
			String photo_url=list.get(v).get("PHOTO_URL").toString();
			
			if(list.get(v).get("PHOTO_PATH")==null){
				String de=Const.IMG_SERVICE_ROOT_PATH+Const.STAR_MORE_IMG_PAHT+first_word;
				 isExist(de);
				 String fileName=star_id+"_"+Const.IMG_FROM_SPIDER+"_"+new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;
				if(ImageUtil.compressImg(photo_url,de+"/"+fileName,Const.STAR_MORE_PHOTO_WIDTH,Const.STAR_MORE_PHOTO_HEIGHT)){
					new DataDao().executeSQL("update t_star_photo set PHOTO_PATH=? where PHOTO_ID=?",Const.STAR_MORE_IMG_PAHT+first_word+"/"+fileName,photo_id);
					log.info("明星更多图片---"+star_id+"   :"+cn_name);
				}else{
					log.error("明星更多图片---"+star_id+"   :"+cn_name);
				}
			}
			
		}
		log.info("明星更多图片----------结束");
	}
	
	/**
	 * 下载明星视频图片
	 */
	private void downStarVideoImg(){
		log.info("明星视频图片----------开始");
		List<Map<String, Object>> list =  getDao().searchBySQL("SELECT t1.star_id,t1.cn_name,t2.VIDEO_PHOTO_URL,t2.VIDEO_ID,t1.first_word FROM t_star_info t1 Inner Join t_star_video t2 ON t1.star_id = t2.STAR_ID WHERE t2.VIDEO_PHOTO_PATH IS NULL ");
		for (int v = 0; v < list.size(); v++) {
			String cn_name = list.get(v).get("cn_name").toString();
			String videoe_id = list.get(v).get("VIDEO_ID").toString();
			String star_id = list.get(v).get("star_id").toString();
			String first_word = list.get(v).get("first_word").toString();
			if(list.get(v).get("VIDEO_PHOTO_URL")==null){
				continue;
			}
			String site_photo_url=list.get(v).get("VIDEO_PHOTO_URL").toString();
			if(list.get(v).get("VIDEO_PHOTO_PATH")==null){
				String de=Const.IMG_SERVICE_ROOT_PATH+Const.STAR_VIDEO_IMG_PAHT+first_word;
				 isExist(de);
				 String fileName=star_id+"_"+Const.IMG_FROM_SPIDER+"_"+new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;
				if(ImageUtil.compressImg(site_photo_url,de+"/"+fileName,Const.STAR_VIDEO_PHOTO_WIDTH,Const.STAR_VIDEO_PHOTO_HEIGHT)){
					new DataDao().executeSQL("update t_star_video set VIDEO_PHOTO_PATH=? where VIDEO_ID=?", Const.STAR_VIDEO_IMG_PAHT+first_word+"/"+fileName,videoe_id);
					log.info("明星视频图片---"+star_id+"   :"+cn_name);
				}else{
					log.error("明星视频图片---"+star_id+"   :"+cn_name);
				}
			}
		}
		log.info("明星视频图片----------结束");
	}
	
	/**
	 * 替换明星默认图片
	 */
	private  void replaceStarPhoto(){
		String sql="SELECT star_id,head_photo,first_word from t_star_info_0804 where first_word is not null order by star_id ";
		List<Map<String, Object>> list= getDao().searchBySQL(sql);
		  String defaultImg =Const.STAR_TITLE_IMG_PAHT+Const.STAR_DEFAULT_IMG_NAME+Const.IMG_TYPE;
		  String erroImg= md5(getByte("E:\\errojpg\\star_erro.jpg"));
		for(int i=0;i<list.size();i++){
			String star_id=list.get(i).get("star_id").toString();
			String head_photo=list.get(i).get("head_photo").toString();
			String first_word=list.get(i).get("first_word").toString();
		    String img = md5(getByte("E:\\star_image\\"+head_photo+".jpg"));
		    //如果是默认图片
		    if(img==null ||img.equals(erroImg)){
//		    	delFile(Const.IMG_SERVICE_ROOT_PATH+head_photo);
		    	String updateSql="update t_star_info_0804 set head_photo=? where star_id=?";
			    getDao().executeSQL(updateSql,defaultImg,star_id);
			    System.out.println("默认图片。。。。。。。。");
		    }else{
		    	String fileName=star_id+"_"+Const.IMG_FROM_SPIDER+"_"+new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;
		    	try {
		            FileInputStream is = new FileInputStream("E:\\star_image\\"+head_photo+".jpg");
		            isExist("E:\\img\\star\\"+first_word);
		            try{
		            	ImageUtil.createThumbnail(is, "E:\\img\\star\\"+first_word+"\\"+fileName, Const.STAR_MORE_PHOTO_WIDTH, Const.STAR_MORE_PHOTO_HEIGHT);
		            }catch(Exception e){
		            	String updateSql="update t_star_info_0804 set head_photo=? where star_id=?";
					    getDao().executeSQL(updateSql,defaultImg,star_id);
					    System.out.println("默认图片。。。。。。。。图片损坏。。。。。。");
		            }
		            String updateSql="update t_star_info_0804 set head_photo=? where star_id=?";
			    	getDao().executeSQL(updateSql,"img/star/"+first_word+"/"+fileName,star_id);
			    	 System.out.println("复制，，，，，图片。。。。。。。。");
		        } catch (Exception ex) {
		            ex.printStackTrace();
		        }
		    }
		}
	}
	
	/**
	 * 替换场馆默认图片
	 */
	private  void replaceSitePhoto(){
		String sql="SELECT site_id,site_photo_path from t_site_info where site_photo_path is not null and site_photo_path!=''  order by site_id ";
		List<Map<String, Object>> list= getDao().searchBySQL(sql);
		  String defaultImg =Const.SITE_TITLE_IMG_PATH+Const.SITE_DEFAULT_IMG_NAME+Const.IMG_TYPE;
		  String erroImg= md5(getByte("/root/site_erro.jpg"));
		for(int i=0;i<list.size();i++){
			String site_id=list.get(i).get("site_id").toString();
			String head_photo=list.get(i).get("site_photo_path").toString();
		    String img = md5(getByte(Const.IMG_SERVICE_ROOT_PATH+head_photo));
		    if(img.equals(erroImg)){
		    	delFile(Const.IMG_SERVICE_ROOT_PATH+head_photo);
		    	String updateSql="update t_site_info set site_photo_path=? where site_id=?";
			    getDao().executeSQL(updateSql,defaultImg,site_id);
			    System.out.println(i+"   :Ok");
		    }
		}
	}
	
	/**
	 * 判断文件夹是否存在,如果不存在则创建文件夹
	 */
	public static void isExist(String path) {
		  File file = new File(path);
		  if (!file.exists()) {
		   file.mkdirs();
		  }
	}
	/**
	* 加密
	* @param s
	* @return
	*/
	public static String md5(byte[] s){
        //16进制字符
        char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd','e', 'f' };
        try{
            byte[] strTemp = s;
            MessageDigest mdTemp = MessageDigest.getInstance("MD5");
            mdTemp.update(strTemp);
            byte[] md = mdTemp.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            //移位 输出字符串
            for (int i = 0; i < j; i++){
                byte byte0 = md[i];
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e){
            return null;
        }
    }
    /**
     * 读取字节数
     * @param name
     * @return
     */
    public static byte[] getByte(String name){
    //得到文件长度
        File file = new File(name);
        byte[] b = new byte[(int) file.length()];
        try{
            InputStream in = new FileInputStream(file);
            try {
     in.read(b);
    } catch (IOException e) {
     e.printStackTrace();
    }
        }catch (FileNotFoundException e){
//            e.printStackTrace();
            return null;
        }
        return b;
    }
    
	/**
	 * 删除文件
	 * @param path
	 */
	public static void delFile(String path){
		File file=new File(path); 
		if(file.exists()&&file.isFile()) 
		file.delete(); 
	}

}
