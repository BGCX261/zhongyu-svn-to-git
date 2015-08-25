package com.piaoyou.crawler.merge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.dao.DataDao;
import com.piaoyou.text.TextSimilarity;
import com.piaoyou.util.PubFun;

/**
 * 合并相同演出或者为相似的演出设置标志位
 * 
 * @author Administrator
 * 
 */
public class MergeRecord {

	private static final Log log = LogFactory.getLog(MergeRecord.class);
	static TextSimilarity textSimilarity = new TextSimilarity();
	public static StringBuilder sb = new StringBuilder();// 提示信息

	public static void main(){
		merge();
	}
	/**
	 * 合并场馆和名称完全相同的演出(设置对应关系)
	 */
	@Deprecated
	public static void mergeSameRecords() {
		DataDao dao = new DataDao();
		StringBuffer findAndUpdate = new StringBuffer();// 合并相同记录
		findAndUpdate.append("update t_show_info,");
		findAndUpdate.append("(");
		findAndUpdate.append("select t.show_id,t1.id from t_show_info t ");
		findAndUpdate
				.append("join (select show_id id,show_name_clean,site_id from ");
		findAndUpdate
				.append(" (select * from t_show_info order by first_agency_id desc) tmp ");
		findAndUpdate
				.append(" group by show_name_clean,site_id having count(show_name_clean)>1) t1 ");
		findAndUpdate
				.append("on t1.show_name_clean=t.show_name_clean and t1.site_id=t.site_id and t1.id!=t.show_id ");
		findAndUpdate.append(") tmp ");
		findAndUpdate
				.append("set t_show_info.show_id_cor=tmp.id where t_show_info.show_id=tmp.show_id ");

		int count = dao.executeSQL(findAndUpdate.toString());
		log.info(count + "条演出被合并!");
	}

	/**
	 * 删除t_show_time表中重复数据.
	 */
	public static void delDupShowTime() {
		DataDao dao = new DataDao();
		StringBuffer sql = new StringBuffer();
		sql.append(" delete target from t_show_time target,( ");
		sql.append(" select t1.one_show_id from t_show_time t1  ");
		sql
				.append(" join (select min(one_show_id) one_show_id,show_id,show_time,last_show_id from t_show_time group by show_id,show_time,last_show_id having count(one_show_id)>1) t2 ");
		sql
				.append(" where  t1.show_id=t2.show_id and t1.show_time=t2.show_time and t1.last_show_id=t2.last_show_id and t1.one_show_id!=t2.one_show_id ");
		sql.append(" ) tmp ");
		sql.append(" where target.one_show_id=tmp.one_show_id ");
		int count = dao.executeSQL(sql.toString());
		log.info("删除重复的show_time数据" + count + "条!");
		sb.append("删除重复的show_time数据" + count + "条!\r\n");
	}

	/**
	 * 根据演出的对应关系,合并对应演出
	 */
	private static void mergeShowCorRecord() {
		DataDao dao = new DataDao();
		String mergeTime = "UPDATE t_show_time,t_show_info "
				+ "SET t_show_time.show_id = t_show_info.show_id_cor "
				+ "WHERE t_show_info.show_id_cor is not null and t_show_time.show_id = t_show_info.show_id";
		int count = dao.executeSQL(mergeTime);
		log.info(count + "条showtime被合并!");
		sb.append(count + "条showtime被合并!" + "\r\n");

		String mergeDate = "UPDATE t_show_date,t_show_info "
				+ "SET t_show_date.show_id = t_show_info.show_id_cor "
				+ "WHERE t_show_info.show_id_cor is not null and t_show_date.show_id = t_show_info.show_id";
		count = dao.executeSQL(mergeDate);
		log.info(count + "条showdate被合并!");
		sb.append(count + "条showdate被合并!" + "\r\n");
	}

	/**
	 * 合并场馆相同,且标题相似度高的演出(设置对应关系)
	 */
	public static void mergeSimilarRecords() {
		DataDao dao = new DataDao();
		StringBuffer find = new StringBuffer();
		int count = 0;

		find
				.append("select site_id ,count(site_id) from t_show_info group by site_id having count(site_id)>1");

		List<Map<String, Object>> site = dao.searchBySQL(find.toString());
		for (Map<String, Object> tar : site) {
			String site_id = String.valueOf(tar.get("site_id"));
			List<Map<String, Object>> show = dao
					.searchBySQL(
							"select show_id,show_name_clean,first_agency_id from t_show_info where site_id=?",
							site_id);
			for (int i = 0; i < show.size(); i++) {
				for (int j = 0; j < show.size(); j++) {
					if (show.get(i) != show.get(j)
							&& show.get(i).get("first_agency_id") != null) {
						if (compare(show.get(i), show.get(j)) > 0.85) {
							count += dao
									.executeSQL(
											"update t_show_info set show_id_cor=? where show_id=?",
											show.get(i).get("show_id"), show
													.get(j).get("show_id"));
							show.remove(j);
							i = 0;
							j = 0;
						}
					}
				}
			}
		}
		log.info(count + "场演出被标记为相似演出!");
		sb.append(count + "场演出被标记为相似演出!" + "\r\n");
	}

	private static double compare(Map<String, Object> map,
			Map<String, Object> map2) {
		String name1 = map.get("show_name_clean").toString();
		String name2 = map2.get("show_name_clean").toString();
		if (name1.indexOf(name2) > -1 || name2.indexOf(name1) > -1) {
			return 1d;
		}
		return textSimilarity.similarity(name1, name2);
	}

	/**
	 * 自动处理演出时间
	 */
	public static void processShowDate() {
		DataDao dao = new DataDao();
		String autoSet = "update t_show_date set show_begin_date=?,show_end_date=? where "
				+ " show_begin_date is null and (show_time like '%每%' or show_time like '%常%' or show_time like '%全%')";
		Calendar c = Calendar.getInstance();
		int count = dao.executeSQL(autoSet, c.get(Calendar.YEAR) + "-01-01", c
				.get(Calendar.YEAR)
				+ "-12-30");
		log.info(count + "条常年演出自动处理!");
		sb.append(count + "条常年演出自动处理!" + "\r\n");
	}

	/**
	 * 备份演出时间表内的时间ID,用于回滚误操作
	 */
	private static void backupShowID() {
		DataDao dao = new DataDao();
		String backupDate = "update t_show_date set last_show_id=show_id where last_show_id is null";
		String backupTime = "update t_show_time set last_show_id=show_id where last_show_id is null";
		dao.executeSQL(backupDate);
		dao.executeSQL(backupTime);
		log.info("完成showID备份!");
		sb.append("完成showID备份!" + "\r\n");
	}

	/**
	 * 为showinfo演出标题提取关键字
	 */
	private static void setKeyName() {
//		DataDao dao = new DataDao();
//		List<Map<String, Object>> result = dao
//				.searchBySQL("select show_id,show_name from t_show_info where show_name like '%》%'");
//		Pattern p = Pattern.compile("《(.*?)》");
//		Matcher m = p.matcher("");
//		StringBuffer tmp = new StringBuffer();
//		for (Map<String, Object> record : result) {
//			String showID = record.get("show_id").toString();
//			String showName = record.get("show_name").toString();
//			m.reset(showName);
//			tmp.setLength(0);
//			while (m.find()) {
//				tmp.append(m.group(1));
//			}
//			if (tmp.length() > 0) {
//				dao
//						.executeSQL(
//								"update t_show_info set show_name_clean=? where show_id=?",
//								PubFun.cleanString(tmp.toString()), showID);
//			}
//		}
//		log.info("设置关键字完成!");
//		sb.append("设置关键字完成!" + "\r\n");
	}

	/**
	 * 删除没有票价的数据(此类数据可能是抓取程序抓取错误或者页面本身没有数据)
	 */
	public static void delInvalidShow() {
		DataDao dao = new DataDao();
		String del = "delete from t_agency_price where agency_price=0";
		int count = dao.executeSQL(del);
		log.info("已经删除票价为0的数据" + count + "条!");
		sb.append("已经删除票价为0的数据" + count + "条!" + "\r\n");

		del = "delete from t_show_date where price=0";
		count = dao.executeSQL(del);
		log.info("已经删除票价为0的数据" + count + "条!");
		sb.append("已经删除票价为0的数据" + count + "条!" + "\r\n");

		del = "delete from t_show_time where not exists (select 1 from t_agency_price where t_agency_price.one_show_id=t_show_time.one_show_id)";
		count = dao.executeSQL(del);
		log.info("已经删除没有票价的时间" + count + "场!");
		sb.append("已经删除没有票价的时间" + count + "场!" + "\r\n");

		del = "delete from t_show_info where status!=3 and show_id_cor is null and show_id not in (select ti.show_id from t_show_time ti join t_agency_price price on price.one_show_id=ti.one_show_id) and show_id not in (select show_id from t_show_date)";
		count = dao.executeSQL(del);
		log.info("已经删除没有票价的演出" + count + "场!");
		sb.append("已经删除没有票价的演出" + count + "场!" + "\r\n");
		
		

		del = "delete from t_show_info where status!=3 and  exists (select 1 from t_site_info where site_name='无效场馆' and site_id=t_show_info.site_id)";
		count = dao.executeSQL(del);
		log.info("已经删除无效演出(此类演出一般为电影联票等等)" + count + "场!");
		sb.append("已经删除无效演出(此类演出一般为电影联票等等)" + count + "场!" + "\r\n");

		del = "delete from t_site_info where site_id not in (select site_id from t_show_info) and city_id is null";
		count = dao.executeSQL(del);
		log.info("已经删除没有演出的新增场馆" + count + "个!");
		sb.append("已经删除没有演出的新增场馆" + count + "个!" + "\r\n");

		del = "delete from t_site_cor where site_name_cor is null and site_name not in (select site_name from t_site_info)";
		count = dao.executeSQL(del);
		log.info("已经删除不存在演出的场馆对应" + count + "个!");
		sb.append("已经删除不存在演出的场馆对应" + count + "个!" + "\r\n");

		del = "delete from t_show_time where show_id not in(select show_id from t_show_info)";
		count = dao.executeSQL(del);
		log.info("已经删除无对应演出的时间time" + count + "个!");
		sb.append("已经删除无对应演出的时间time" + count + "个!" + "\r\n");

		del = "delete from t_show_date where show_id not in(select show_id from t_show_info)";
		count = dao.executeSQL(del);
		log.info("已经删除无对应演出的时间date" + count + "个!");
		sb.append("已经删除无对应演出的时间date" + count + "个!" + "\r\n");

		del = "delete from t_agency_price where one_show_id not in (select one_show_id from t_show_time)";
		count = dao.executeSQL(del);
		log.info("已经删除无对应演出的票价" + count + "个!");
		sb.append("已经删除无对应演出的票价" + count + "个!" + "\r\n");

		
		del = "delete from t_agency_price WHERE	one_show_id  IN(SELECT one_show_id FROM t_show_time tst,t_show_info  tsi " +
				"where tst.show_id=tsi.show_id and (tsi.status=3))";
		count = dao.executeSQL(del);
		log.info("已经删除过期的和演出已经删除的票价" + count + "个!");
		sb.append("已经删除过期的和演出已经删除的票价" + count + "个!" + "\r\n");
		
//		del = "delete from t_partner_show where show_id not in (select show_id from t_show_info)";
//		count = dao.executeSQL(del);
//		log.info("删除无对应演出的合作商show" + count + "个!");
//		sb.append("删除无对应演出的合作商show" + count + "个!" + "\r\n");
	}

	/**
	 * 判断票是否已经过期,并设置标识
	 */
	public static void setFinishFlag() {
		DataDao dao = new DataDao();
		// String updateFlag = "update t_show_info left join ( "+
		// "select t.show_id from ( "+
		// "select show_id,max(show_time) show_time from t_show_time group by show_id "+
		// "union all "+
		// "select show_id,max(show_end_date) show_time from t_show_date where show_end_date is not null group by show_Id "+
		// ") t where TO_DAYS(now())-TO_DAYS(t.show_time)>0) tmp on t_show_info.show_id=tmp.show_id set t_show_info.status=2 where tmp.show_id is not null ";

		String updateFlag = "UPDATE    t_show_info" + " LEFT JOIN"
				+ " (SELECT t.show_id"
				+ "  FROM (SELECT tst.show_id, max(tst.show_time) show_time"
				+ "    FROM t_show_info tsi" + "     JOIN t_show_time tst"
				+ "        ON tst.show_id = tsi.show_id"
				+ "      JOIN t_agency_price tap"
				+ "       ON tap.one_show_id = tst.one_show_id"
				+ "          AND tap.agency_id = tsi.standard_agency_id"
				+ "   GROUP BY show_id" + "  UNION ALL"
				+ "   SELECT tsd.show_id, max(tsd.show_end_date) show_time"
				+ "     FROM t_show_date tsd"
				+ "    join t_show_info tsi on tsi.show_id = tsd.show_id "
				+ "    and tsi.standard_agency_id = tsd.agency_id"
				+ "   WHERE show_end_date IS NOT NULL" + "  GROUP BY show_Id"
				+ "   ) t"
				+ " WHERE TO_DAYS(now()) - TO_DAYS(t.show_time) > 0) tmp"
				+ " ON t_show_info.show_id = tmp.show_id"
				+ "  SET t_show_info.status = 2"
				+ "  WHERE tmp.show_id IS NOT NULL";
		dao.executeSQL(updateFlag);
		log.info("判断票是否已经过期,并设置标识");

	}
	/**
	 * 转移过期数据
	 */
	public static void removeFinisthPattern(){
		DataDao dao = new DataDao();
		String removeTimePriceSql = "insert into t_agency_price_finish select price.* from t_show_info info,t_show_time time ,t_agency_price price where info.status=2 " +
				"and info.show_id=time.show_id and price.one_show_id=time.one_show_id and price.id not in (select id from t_agency_price_finish)";
		String removeDatePriceSql = "insert into t_agency_price_finish select price.* from t_show_info info,t_show_date date ,t_agency_price price " +
				"where info.status=2 and info.show_id=date.show_id and price.one_show_id=date.one_show_id and price.id not in (select id from t_agency_price_finish)";
		String removeTimeSql = "insert into t_show_time_finish select time.* from t_show_info info,t_show_time time where info.status=2 " +
		"and info.show_id=time.show_id  and time.one_show_id not in (select one_show_id from t_show_time_finish)";
		String removeDateSql = "insert into t_show_data_finish select  date.* from t_show_info info,t_show_date date  " +
			"where info.status=2 and info.show_id=date.show_id  and date.one_show_id not in (select one_show_id from t_show_data_finish)";
		String removeShowInfoSql = "insert into t_show_info_finish select  info.* from t_show_info info " +
		"where info.status=2 and info.show_id not in (select show_id from t_show_info)";
		dao.executeSQL(removeTimePriceSql);
		dao.executeSQL(removeDatePriceSql);
		dao.executeSQL(removeTimeSql);
		dao.executeSQL(removeDateSql);
		dao.executeSQL(removeShowInfoSql);
	}
	/* 删除过期的票商 */
	public static void delFinishPattern() {
		DataDao dao = new DataDao();
		String sql = "DELETE from t_partner_show  where show_id in (select t.show_id from t_show_info t where t.status=2)";
		dao.executeSQL(sql);
		log.info("删除过期的票商");
	}

	/**
	 * 设置标准票价的代理商ID
	 */
	private static void setShowStandardID() {
		DataDao dao = new DataDao();
		// 只有人工审核才能重设
		// dao.executeSQL("update t_show_info set standard_agency_id=first_agency_id where standard_agency_id is null and first_agency_id is not null and show_id_cor is null and TO_DAYS(now())-TO_DAYS(crawled_time)>1");
		// 删除无效的标准票价和标准代理商ID
		dao
				.executeSQL("update t_show_info set standard_agency_id=null,agency_show_price=null where show_id_cor is not null and standard_agency_id is not null");
	}

//	public static void setStandardPrice() {
//		DataDao dao = new DataDao();
//		String sql = " update t_show_info, "
//				+ " (select group_concat(distinct cast(round(t1.price,0) as char) order by t1.price desc SEPARATOR '/') price,t1.show_id,t1.agency_id "
//				+ " from( "
//				+ " select distinct t.agency_price price,ti.show_id,t.agency_id from t_agency_price t "
//				+ " join t_show_time ti on ti.one_show_id=t.one_show_id "
//				+ " join t_show_info info on info.show_id=ti.show_id and info.first_agency_id is null and info.standard_agency_id is not null "
//				+ " where info.standard_agency_id=t.agency_id "
//				+ " union all "
//				+ " select distinct t.price,t.show_id,t.agency_id from t_show_date t "
//				+ " join t_show_info info on info.show_id=t.show_id and info.first_agency_id is null and info.standard_agency_id is not null "
//				+ " where info.standard_agency_id=t.agency_id "
//				+ " ) t1 group by t1.show_id,t1.agency_id) tmp set t_show_info.agency_show_price=tmp.price "
//				+ " where tmp.show_id=t_show_info.show_id ";
//		int count = dao.executeSQL(sql);
//		log.info("为没有总代的" + count + "场演出添加标准票价完成!");
//		sb.append("为没有总代的" + count + "场演出添加标准票价完成!" + "\r\n");
//	}

	/**
	 * 格式化套票的备注字段
	 */
	public static void formatPriceRemark() {
		DataDao dao = new DataDao();

		String searchPrice = "select id,remark from t_agency_price where remark regexp '[[:digit:]]+\\\\*[[:digit:]]+' "
				+ "and remark not regexp '^[[:digit:]]+\\\\*[[:digit:]]+$'";
		String searchDate = "select one_show_id id,remark from t_show_date where remark regexp '[[:digit:]]+\\\\*[[:digit:]]+' "
				+ "and remark regexp '^[[:digit:]]+\\\\*[[:digit:]]+$'=0";
		String updatePrice = "update t_agency_price set remark=? where id=?";
		String updateDate = "update t_show_date set remark=? where one_show_id=?";
		String update1 = "update t_agency_price set remark=replace(remark,'×','*') where remark regexp '[[:digit:]]([Xx]|×)[[:digit:]]'";
		String update2 = "update t_agency_price set remark=replace(remark,'x','*') where remark regexp '[[:digit:]]([Xx]|×)[[:digit:]]'";
		String update3 = "update t_agency_price set remark=replace(remark,'X','*') where remark regexp '[[:digit:]]([Xx]|×)[[:digit:]]'";
		String update4 = "update t_show_date set remark=replace(remark,'×','*') where remark regexp '[[:digit:]]([Xx]|×)[[:digit:]]'";
		String update5 = "update t_show_date set remark=replace(remark,'x','*') where remark regexp '[[:digit:]]([Xx]|×)[[:digit:]]'";
		String update6 = "update t_show_date set remark=replace(remark,'X','*') where remark regexp '[[:digit:]]([Xx]|×)[[:digit:]]'";

		dao.executeSQL(update1);
		dao.executeSQL(update2);
		dao.executeSQL(update3);
		dao.executeSQL(update4);
		dao.executeSQL(update5);
		dao.executeSQL(update6);

		List<Map<String, Object>> tmp = dao.searchBySQL(searchPrice);
		for (Map<String, Object> price : tmp) {
			String id = price.get("id").toString();
			String remark = price.get("remark").toString();
			dao.executeSQL(updatePrice, remark.replaceAll(
					"^.*?(\\d+\\*\\d).*$", "$1"), id);
		}

		tmp = dao.searchBySQL(searchDate);
		for (Map<String, Object> price : tmp) {
			String id = price.get("id").toString();
			String remark = price.get("remark").toString();
			dao.executeSQL(updateDate, remark.replaceAll("^.*?(\\d+\\*\\d).*$",
					"$1"), id);
		}

		//没有定义标准票价的时候自动设置标准票价等于原价
		dao
				.executeSQL("update t_agency_price set standard_price=agency_price where standard_price is  null");
		dao
				.executeSQL("update t_show_date set standard_price=price where standard_price is  null");
	}

	/**
	 * 根据场馆和时间合并演出(这个步骤在添加的时候已经做过,此处是用来在调整了场馆对应后,重新对应)
	 */
	public static void mergeSiteAndTime() {
		DataDao dao = new DataDao();
		String getShowIDsTime = " SELECT t.site_id,tmp.show_time,group_concat(Convert(t.show_id , char) ORDER BY t.first_agency_id DESC) show_id FROM t_show_info t "
				+ " JOIN ( "
				+ " SELECT show_id,group_concat(show_time ORDER BY show_time) show_time FROM ( "
				+ " SELECT DISTINCT show_id,show_time FROM t_show_time "
				+ " ) t GROUP BY show_id) tmp ON tmp.show_id=t.show_id "
				+ " GROUP BY t.site_id,tmp.show_time HAVING (count(t.show_id)>1) ";
		String getShowIDsDate = " SELECT t.site_id,tmp.show_time,group_concat(Convert(t.show_id , char) ORDER BY t.first_agency_id DESC) show_id FROM t_show_info t "
				+ " JOIN ( "
				+ " SELECT show_id,group_concat(show_time ORDER BY show_time) show_time FROM ( "
				+ " SELECT DISTINCT show_id,show_time FROM t_show_date "
				+ " ) t GROUP BY show_id) tmp ON tmp.show_id=t.show_id "
				+ " GROUP BY t.site_id,tmp.show_time HAVING (count(t.show_id)>1) ";

		String updateTime = "update t_show_time set show_id=? where show_id=?";
		String updateDate = "update t_show_date set show_id=? where show_id=?";

		List<Map<String, Object>> records = dao.searchBySQL(getShowIDsTime);
		for (Map<String, Object> show_ids : records) {
			String[] params = show_ids.get("show_id").toString().split(",");
			for (int i = 1; i < params.length; i++) {
				dao.executeSQL(updateTime, params[0], params[i]);
			}
		}
		records = dao.searchBySQL(getShowIDsDate);
		for (Map<String, Object> show_ids : records) {
			String[] params = show_ids.get("show_id").toString().split(",");
			for (int i = 1; i < params.length; i++) {
				dao.executeSQL(updateDate, params[0], params[i]);
			}
		}
		log.info("根据场馆和时间合并演出");
		delInvalidShow();
	}

	/**
	 * 根据演出对应链重新设置演出ID和演出对应ID,重设时间和日期表
	 */
	public static void mergeLoopShow() {
		DataDao dao = new DataDao();

		String check = "select show_id from t_show_info where exists (select info2.show_id from t_show_info info2 "
				+ "where t_show_info.show_id=info2.show_id_cor and info2.show_id=t_show_info.show_id_cor)";
		String del = "delete from t_show_info where show_id=?";
		List<Map<String, Object>> result = dao.searchBySQL(check);
		if (result.size() > 0) {
			log.info("有自身对应自身的循环数据" + result.size() + "条!");
			sb.append("有自身对应自身的循环数据" + result.size() + "条!" + "\r\n");
		}
		// 删除有自身对应自身的死循环
		for (Map<String, Object> show : result) {
			dao.executeSQL(del, show.get("show_id"));
		}
		String sql = "select t.show_id from t_show_info t where exists "
				+ "(select tmp.show_id_cor from t_show_info tmp where tmp.show_id=t.show_id_cor and tmp.show_id_cor is not null)";
		String getCorId = "select show_id_cor from t_show_info where show_id=? and show_id_cor is not null";
		String updateDate = "update t_show_date set show_id=? where show_id=?";
		String updateTime = "update t_show_time set show_id=? where show_id=?";
		String updateShowInfo = "update t_show_info set show_id_cor=? where show_id=?";

		List<Map<String, Object>> data = dao.searchBySQL(sql);
		for (Map<String, Object> target : data) {
			List<String> taskList = new ArrayList<String>();
			String parent = target.get("show_id").toString();
			taskList.add(parent);
			List<Map<String, Object>> tmp = dao.searchBySQL(getCorId, parent);
			do {
				String cor = tmp.get(0).get("show_id_cor").toString();
				taskList.add(cor);
				tmp = dao.searchBySQL(getCorId, cor);
			} while (tmp.size() > 0);

			String finalCorID = taskList.remove(taskList.size() - 1);
			for (String oldID : taskList) {
				dao.executeSQL(updateDate, finalCorID, oldID);
				dao.executeSQL(updateTime, finalCorID, oldID);
				dao.executeSQL(updateShowInfo, finalCorID, oldID);
			}
		}
	}

	/**
	 * 抓取完成后,合并整理数据
	 */
	public static void merge() {
		log.info("正在合并");
		backupShowID();
		setKeyName();

		 mergeSameRecords();//此步骤移动到判断是否同一演出的逻辑中
		// mergeSimilarRecords();
		mergeLoopShow();
		mergeShowCorRecord();

		processShowDate();
		formatPriceRemark();

		delInvalidShow();
		setFinishFlag();
		// 只有人工审核后,才会设置标准ID,目前只是删除无效
		setShowStandardID();
//		// 删除过期的票商数据
//		delFinishPattern();
//		//转移过期数据
//		removeFinisthPattern();
	}

	public static void main(String e[]){
		removeFinisthPattern();
	}
	
}
