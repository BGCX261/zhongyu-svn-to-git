package com.piaoyou.crawler.basicInfo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.crawler.SpiderTask;
import com.piaoyou.crawler.TicketSpider;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.ImageUtil;
import com.piaoyou.util.PubFun;

public class DownPhotoSpider extends TicketSpider implements SpiderTask {
	private static final Log log = LogFactory.getLog(DownPhotoSpider.class);
	private  static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    static String showType = "h";
	public static void show(){
		System.out.println("\n");
		System.out.println("    *****************************************************");
		System.out.println("        你好票务垂直搜素引擎图片下载工具  v1.0        ");
		System.out.println("          北京你好众娱文化传播有限公司           ");
		System.out.println("      参数说明   例如：java -jar down.jar show    ");
		System.out.println("          h 帮助                                                           ");
		System.out.println("          1  下载    《演出标题》    图片                                                           ");
		System.out.println("    *****************************************************\n");
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		show();
		DownPhotoSpider downPhotoSpider=new DownPhotoSpider();
		downPhotoSpider.setDao(new DataDao());
		downPhotoSpider.extract();
	}

	@Override
	public void extract() {
		try {
			downShowImg();
			downShowShuImg();
			downShowHengImg();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 下载的演出原图图片
	 * @throws SQLException 
	 */
	public void downShowImg() throws SQLException{
		log.info("演出原图图片----------开始");
		String SQL = 
			" select id,remote_img_url"+
			" from"+
			"  common_info"+
			" where"+
			"  common_id_cor = 0 and"+
			"  status in (0, 1, 9) and"+
			"  remote_img_url is not null and"+
			"  remote_img_url != '' and"+
			"  ( img_url not like 'http://img2.piaoyouhui.com%'  "+
			"    or img_url is null "+
			"    or img_url = '')";
		List<Map<String, Object>> list= new DataDao().searchBySQL(SQL);	
		for(int i=0;i<list.size();i++){
			String id=list.get(i).get("id").toString();
			if(list.get(i).get("remote_img_url")==null){
				continue;
			}
			String img_url=list.get(i).get("remote_img_url").toString();
			String path=Const.IMG_PATH;
			//目录不存在创建目录
			isExist(path);
			String fileName=new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;
		    if(ImageUtil.compressImg(img_url,Const.IMG_PATH+fileName,Const.SHOW_PHOTO_WIDTH,Const.SHOW_PHOTO_HEIGHT)){
		    	String updateSql="update common_info set img_url=? where id=?";
		    	getDao().executeSQL(updateSql, Const.RETURN_IMG_PATH+fileName,id);
				log.info("演出原图图片---"+id+"\t本地路径："+Const.IMG_PATH+fileName+"\t数据库路径:"+Const.RETURN_IMG_PATH+fileName);
		    }
		}
		log.info("演出原图图片----------结束");
	}
	
	
	/**
	 * 下载的演出竖图信息图片
	 * @throws SQLException 
	 */
	public void downShowShuImg() throws SQLException{
		log.info("演出信息竖图图片----------开始");
		String SQL = 
			" select id,remote_img_url"+
			" from"+
			"  common_info"+
			" where"+
			"  common_id_cor = 0 and"+
			"  status in (0, 1, 9) and"+
			"  remote_img_url is not null and"+
			"  remote_img_url != '' and"+
			"  ( shu_image_path not like 'http://img2.piaoyouhui.com%'  "+
			"    or shu_image_path is null "+
			"    or shu_image_path = '')";
		List<Map<String, Object>> list= new DataDao().searchBySQL(SQL);	
		for(int i=0;i<list.size();i++){
			String id=list.get(i).get("id").toString();
			if(list.get(i).get("remote_img_url")==null){
				continue;
			}
			String img_url=list.get(i).get("remote_img_url").toString();
			String path=Const.IMG_PATH;
			//目录不存在创建目录
			isExist(path);
			String fileName=new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;
		    if(ImageUtil.compressImg(img_url,Const.IMG_PATH+fileName,Const.SHOW_PHOTO_WIDTH,Const.SHOW_PHOTO_HEIGHT)){
		    	String updateSql="update common_info set shu_image_path=? where id=?";
		    	getDao().executeSQL(updateSql, Const.RETURN_IMG_PATH+fileName,id);
				log.info("演出信息竖图图片---"+id+"\t本地路径："+Const.IMG_PATH+fileName+"\t数据库路径:"+Const.RETURN_IMG_PATH+fileName);
		    }
		}
		log.info("演出信息竖图图片----------结束");
	}
	
	
	/**
	 * 下载的演出横图信息图片
	 * @throws SQLException 
	 * 
	 */
	public void downShowHengImg() throws SQLException{
		log.info("演出信息横图图片----------开始");
		String SQL = 
			" select id,heng_image_path"+
			" from"+
			"  common_info"+
			" where"+
			"  common_id_cor = 0 and"+
			"  status in (0, 1, 9) and"+
			"  heng_image_path is not null and"+
			"  heng_image_path != '' and"+
			"  heng_image_path not like 'http://img2.piaoyouhui.com%'   -- 以前没有抓取过该张图片"+
			"";
		List<Map<String, Object>> list= new DataDao().searchBySQL(SQL);	
		for(int i=0;i<list.size();i++){
			String id=list.get(i).get("id").toString();
			if(list.get(i).get("heng_image_path")==null){
				continue;
			}
			String img_url=list.get(i).get("heng_image_path").toString();
			String path=Const.IMG_PATH;
			//目录不存在创建目录
			isExist(path);
			String fileName=new SimpleDateFormat("yyyyMMddhhmmssSSS").format(new Date())+Const.IMG_TYPE;
		    if(ImageUtil.compressImg(img_url,Const.IMG_PATH+fileName,Const.SHOW_PHOTO_WIDTH,Const.SHOW_PHOTO_HEIGHT)){
		    	String updateSql="update common_info set heng_image_path=? where id=?";
		    	getDao().executeSQL(updateSql, Const.RETURN_IMG_PATH+fileName,id);
				log.info("演出信息横图图片---"+id+"\t本地路径："+Const.IMG_PATH+fileName+"\t数据库路径:"+Const.RETURN_IMG_PATH+fileName);
		    }
		}
		log.info("演出信息横图图片----------结束");
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
