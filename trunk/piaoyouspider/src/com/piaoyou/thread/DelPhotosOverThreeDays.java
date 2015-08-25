package com.piaoyou.thread;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.bean.Show2;
import com.piaoyou.dao.DataDao;
import com.piaoyou.util.Const;
import com.piaoyou.util.db.DBUtils;

public class DelPhotosOverThreeDays {
	private static final Log log = LogFactory.getLog(DelPhotosOverThreeDays.class);
	/**
	 * @param args
	 */
	public static void main(String[] args) {
			log.info("开始删除用户超过3天未全部编辑的图片");
			String sql="select id,imag_path  from t_still where   t_still.edit_time<=DATE_ADD(now(),INTERVAL -3*24*3600 SECOND) ";
			DataDao dao=new DataDao();
			List<Map<String,Object>> list=dao.searchBySQL(sql);
			for(Map<String,Object> map:list){
				int id=(Integer)map.get("id");
				String myTime=(String)map.get("imag_path");
				String delSql="delete from t_still where id=?";
				dao.executeSQL(delSql, id);
				String smallPath=Const.IMG_SERVICE_ROOT_PATH+Const.STAGE_SMALL_PATH+"small"+myTime+".jpg";
				String bigPath=Const.IMG_SERVICE_ROOT_PATH+Const.STAGE_BIG_PATH +"big"+myTime+".jpg";
				String defaultPath=Const.IMG_SERVICE_ROOT_PATH+Const.STAGE_DEFAULT_PATH +"default"+myTime+".jpg";
				try{
					File small=new File(smallPath);
					if(small.exists()){
						small.delete();
					}
					File big=new File(bigPath);
					if(big.exists()){
						big.delete();
					}
					File def=new File(defaultPath);
					if(def.exists()){
						def.delete();
					}
				}catch(Exception ee){
					
				}
			}
			log.info("已经删除用户超过3天未全部编辑的图片");

	}

}
