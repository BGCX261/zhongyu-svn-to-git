package com.piaoyou.util.DataExecute;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.dao.DataDao;

public class UpdateSpiderData {
	private static final Log log = LogFactory.getLog(UpdateSpiderData.class);

	//删除过期演出关系信息
	public static void deletePastDate(){
		DataDao dao = new DataDao();
		//删除演出和电影
		String SQL1 = 
			" delete from category_common_item where category_common_item.common_info_id in ( "+
			" select"+
			" ct.common_id"+
			" from"+
			"  common_time ct"+
			"  join common_info ci on ci.id = ct.common_id and ci.type in(0,1) "+
			" where"+
			"  ct.common_time != '全年'"+
			" group by"+
			"  ct.common_id"+
			" having"+
			"  max(ct.common_time) < now()"+
			"  )";
		//删除团购
		String SQL2 = 
			" delete from category_common_item where category_common_item.common_info_id in ("+
			" select"+
			" ct.common_id"+
			" from"+
			"  common_time ct"+
			"  join common_info ci on ci.id = ct.common_id and ci.type = 2"+
			" where"+
			"  ct.common_time != '全年'"+
			" group by"+
			"  ct.common_id"+
			" having"+
			"  max(ct.limit_time) < now()"+
			"  )";
		//更新电影和演出的状态
		String SQL3 = 
			" update common_info ci,("+
			" select"+
			" ct.common_id"+
			" from"+
			"  common_time ct"+
			"  join common_info ci on ci.id = ct.common_id and ci.type in(0,1)"+
			" where"+
			"  ct.common_time != '全年'"+
			" group by"+
			"  ct.common_id"+
			" having"+
			"  max(ct.common_time) < now()"+
			"  ) tmp set ci.status = 2 where ci.id = tmp.common_id";
		//更新团购状态
		String SQL4 = 
			" update common_info ci,("+
			" select"+
			" ct.common_id"+
			" from"+
			"  common_time ct"+
			"  join common_info ci on ci.id = ct.common_id and ci.type = 2"+
			" where"+
			"  ct.common_time != '全年'"+
			" group by"+
			"  ct.common_id"+
			" having"+
			"  max(ct.limit_time) < now()"+
			"  ) tmp set ci.status = 2 where ci.id = tmp.common_id";
			try {
				dao.executeSQL(SQL1);
				log.info("演出和电影关系树删除成功");
				dao.executeSQL(SQL2);
				log.info("团购关系树删除成功");
				dao.executeSQL(SQL3);
				log.info("电影和演出状态更新成功");
				dao.executeSQL(SQL4);
				log.info("团购状态更新成功");
			} catch (SQLException e) {
				e.printStackTrace();
			}
	}
	public static void main(String[] args) {
		log.info("更新过期演出时间开始");
		deletePastDate();
		log.info("更新过期演出时间结束");
	}

}
