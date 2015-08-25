package com.piaoyou.util;

import java.util.HashMap;
import java.util.Map;


/**
 * @author jinquan
 * @date   2011-7-28
 */
public class Const {
	/**
	 * 剧照图片地址
	 */
	public static final String STAGE_PATH="img/juzhao/stage/";
	
	/**
	 * 剧照大图地址600*600
	 */
	public static final String STAGE_BIG_PATH="img/juzhao/big/";
	/**
	 * 剧照小图地址100*100
	 */
	public static final String STAGE_SMALL_PATH="img/juzhao/small/";
	/**
	 * 剧照原始图片地址
	 */
	public static final String STAGE_DEFAULT_PATH="img/juzhao/default/";
	
	/**
	 * 演出基本信息解析失败
	 */
	
	public static final String SHOW_INFO_ERRO="演出基本信息解析失败：";
	
	/**
	 * 票价解析失败
	 */
	public static final String SHOW_PRICE_ERRO="票价解析失败：";
	
	/**
	 * 日期解析失败
	 */
	public static final String SHOW_DATE_ERRO="日期解析失败：";
	
	/**
	 * 解析类型失败
	 */
	public static final String SHOW_TYPE_ERRO="解析类型失败";
	
	/**
	 * 解析演出列表失败
	 */
	public static final String SHOW_LIST_ERRO="解析演出列表失败";
	
	/**
	 * 解析单场演出地址失败
	 */
	public static final String SHOW_ONE_PAGE_URL_ERRO="解析单场演出地址失败";
	
	/**
	 * 解析分页失败
	 */
	public static final String SHOW_PAGE_ERRO="解析分页失败";
	
	/**
	 * 图片来源于抓取
	 */
	public static final String IMG_FROM_SPIDER="S";
	/**
	 * 图片来源于用户上传
	 */
	public static final String IMG_FROM_USER="U";
	/**
	 * 图片来源于用户上传
	 */
	
	
	/**
	 * 场馆更多图片图宽度
	 */
	public static final int SITE_MORE_PHOTO_WIDTH=1024;
	/**
	 * 场馆更多图片图高度
	 */
	public static final int SITE_MORE_PHOTO_HEIGHT=768;
	
	
	
	/**
	 * 明星更多图片宽度
	 */
	public static final int STAR_MORE_PHOTO_WIDTH=1024;
	/**
	 * 明星更多图片高度
	 */
	public static final int STAR_MORE_PHOTO_HEIGHT=768;
	
	
	
	/**
	 * 场馆标题图片宽度
	 */
	public static final int SITE_PHOTO_WIDTH=1027;
	/**
	 * 场馆标题图片高度
	 */
	public static final int SITE_PHOTO_HEIGHT=768;
	
	
	
	/**
	 * 明星视频图宽度
	 */
	public static final int STAR_VIDEO_PHOTO_WIDTH=102;
	/**
	 * 明星视频图高度
	 */
	public static final int STAR_VIDEO_PHOTO_HEIGHT=73;
	
	
	/**
	 * 演出标题图片大图宽度
	 */
	public static final int SHOW_PHOTO_WIDTH=1024;
	/**
	 * 演出标题图片大图高度
	 */
	public static final int SHOW_PHOTO_HEIGHT=768;
	
	
	
	/**
	 * 图片服务器图片目录地址
	 */
	public static final String IMG_SERVICE_ROOT_PATH="/usr/local/tomcat6.0.33/webapps/piaoyouImg/";
	
	/**
	 * 明星默认图片名称
	 */
	public static final String STAR_DEFAULT_IMG_NAME="star_default_img";
	/**
	 * 场馆默认图片名称
	 */
	public static final String SITE_DEFAULT_IMG_NAME="site_default_img";
	
	/**
	 * 保存图片统一格式
	 */
	public static final String IMG_TYPE=".jpg";
	/**
	 * 演出标题图片地址
	 */
	public static final String SHWO_TITLE_PATH="img/show/";
	/**
	 * 场馆标题图片地址
	 */
	public static final String SITE_TITLE_IMG_PATH="img/site/";
	/**
	 * 场馆更多图片
	 */
	public static final String SITE_MORE_IMG_PATH="img/site/more/";
	/**
	 * 明星标题图片地址
	 */
	public static final String STAR_TITLE_IMG_PAHT="img/star/";
	/**
	 * 明星更多图片地址
	 */
	public static final String STAR_MORE_IMG_PAHT="img/star/more/";
	
	/**
	 * 明星视频图片地址
	 */
	public static final String STAR_VIDEO_IMG_PAHT="img/star/video/";
	
	//演出场馆座位图片
	public static final String SHOW_SEAT_IMG_PATH="img/show/seat/";
	public static final String separator = ",";
	
	public static Map<String,Integer> picLevel  = new HashMap<String,Integer>();//图片优先级
	static {
		picLevel.put("damai", 1);
		picLevel.put("image.t3", 2);
		picLevel.put("shuiniaoticket", 3);
	}
	/**
	 * 返回用separator连接传入的字符串
	 * @param params	参数1 错误基本信息 参数2...该方法的参数
	 * @return
	 */
	public static String concat(Object... params){
		int iMax = params.length - 1;
		if (iMax == -1){
			return "";
		}

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(params[i]));
            if (i == iMax){
            	return b.toString();
            }
            b.append(separator);
        }
	}

}
