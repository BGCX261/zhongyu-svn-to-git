/**
 * 返回图片路径
 */
package com.piaoyou.util;
import java.io.File;
/**
 * @author jinquan
 *
 */

public class ImgPathUtil {	
	
	/**
	 * 创建并得到图片路径
	 * @param type
	 * @param user_id
	 * @return
	 */
	public static String setImgPath(String type,int user_id){
		String path=Const.IMG_PATH+type;
		if(user_id!=0){
			String temp=String.valueOf(user_id%10)+"/"+String.valueOf(user_id)+"/";
			path+=temp;	
		}
		File directory=new File(path);
		if(!directory.exists()){
			directory.mkdirs();
		}
		return directory.getPath();
	}
	
	/**
	 * 根据图片类型及用户ID得到图片路径
	 * @param type
	 * @param user_id
	 * @return
	 */
	public static String getImgPath(String type,int user_id){
		String path=type;
		if(user_id!=0){
			String temp=String.valueOf(user_id%10)+"/"+String.valueOf(user_id)+"/";
			path+=temp;	
		}
		return path;
	}
}
