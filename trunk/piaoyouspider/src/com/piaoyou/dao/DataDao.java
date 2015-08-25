package com.piaoyou.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.bean.Show;
import com.piaoyou.bean.TicketPrice;
import com.piaoyou.text.TextSimilarity;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;
import com.piaoyou.util.date.DateUtils;
import com.piaoyou.util.db.DBUtils;

public class DataDao {
	
	private Lock lock = new ReentrantLock();
	private static final Log log = LogFactory.getLog(DataDao.class);
	private TextSimilarity textSimilarity = new TextSimilarity();
	public static final  AtomicInteger dataPricesCount = new AtomicInteger();
	public static final  AtomicInteger timePricesCount = new AtomicInteger();
	public static final  AtomicInteger newshowCount = new AtomicInteger();
	public static final  AtomicInteger oldshowCount = new AtomicInteger();
	public static final  AtomicInteger existPricesCount = new AtomicInteger();
	public static final  AtomicInteger wuxiaoCount = new AtomicInteger();
	public static StringBuilder  deleteShowTime = new StringBuilder("");
	public static void main(String e[]){
		Show show = new Show();
		Map<String,List<TicketPrice>> timeAndPrice = new HashMap<String,List<TicketPrice>>();
		timeAndPrice.put("", new ArrayList<TicketPrice>());
		show.setTimeAndPrice(timeAndPrice);
		if(show.getTimeAndPrice().isEmpty()){
			System.out.println("===========");
		}
		boolean continueFlag = false;
		for(Entry<String,List<TicketPrice>> entry :timeAndPrice.entrySet()){
			String key = entry.getKey();
			List<TicketPrice> value =entry.getValue();
			if(key!=null&&!key.equals("")&&!value.isEmpty()){
				continueFlag = true;
			}
		}
		if(!continueFlag){
			System.out.println("===========1");
			return;
		}
		
	}
	public  double compare(String name1,String name2) {
		if (name1.indexOf(name2) > -1 || name2.indexOf(name1) > -1) {
			return 1d;
		}
		return textSimilarity.similarity(name1, name2);
	}
	/**
	 * 判断演出是否已经添加,如果未添加返回false,如果已经添加,刷新票价信息然后返回
	 * @param show
	 * @return
	 */
	private boolean isExistShow(Show show){
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtInsertPhoto = null;
		PreparedStatement updateStatusPs = null;
		ResultSet rs = null;
		boolean exist = false;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sql = new StringBuffer();
			//根据演出名称和演出场馆完全一致
			sql.append(" select info.show_id,info.show_id_cor,info.show_photo,info.status,info.show_name,info.standard_agency_id from t_show_info info");
			sql.append(" where info.show_name_clean=? and info.site_id=? ");
			sql.append(" union all ");
			//根据演出时间和演出场馆完全一致
			sql.append(" select tmp.show_id,tmp.show_id_cor,tmp.show_photo,tmp.status,tmp.show_name,tmp.standard_agency_id from( ");
			sql.append(" SELECT s.show_id,s.show_id_cor,s.show_photo,s.status,s.show_name,s.standard_agency_id,group_concat(distinct ti.show_time order by ti.show_time) show_time ");
			sql.append("   FROM t_show_info s JOIN t_show_time ti ON ti.show_id = s.show_id ");
			sql.append("  WHERE s.site_id = ? ");
			sql.append("  group by s.show_id ");
			sql.append("  ) tmp where tmp.show_time=? ");
			sql.append(" union all ");
			sql.append(" select tmp.show_id,tmp.show_id_cor,tmp.show_photo,tmp.status,tmp.show_name,tmp.standard_agency_id from( ");
			sql.append(" SELECT s.show_id,s.show_id_cor,s.show_photo,s.status,s.show_name,s.standard_agency_id,group_concat(distinct ti.show_time order by ti.show_time) show_time ");
			sql.append("   FROM t_show_info s JOIN t_show_date ti ON ti.show_id = s.show_id ");
			sql.append("  WHERE s.site_id = ? ");
			sql.append("  group by s.show_id ");
			sql.append("  ) tmp where tmp.show_time=? ");
			
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, PubFun.cleanString(show.getName()));
			stmt.setString(2, show.getSiteID());
			stmt.setString(3, show.getSiteID());
			stmt.setString(5, show.getSiteID());
			Object[] o = show.getTimeAndPrice().keySet().toArray();
			Arrays.sort(o);
			StringBuffer timeParam = new StringBuffer();
			for(Object tmp:o){
				timeParam.append(tmp.toString()).append(",");
			}
			timeParam.setLength(timeParam.length()-1);
			stmt.setString(4, timeParam.toString());
			stmt.setString(6, timeParam.toString());
			rs = stmt.executeQuery();
			exist = rs.next();
			//如果存在的话,还要判断根据名称相似度
			if(exist){
				String show_name = rs.getString("show_name");
				String compare_show_name  = show.getName();
				if(compare(show_name,compare_show_name)<0.3){
					log.info("通过演出标题相似度过滤"+show_name+"比较演出"+compare_show_name+"不是一场演出");
					exist = false;
				}
				
				show_name = PubFun.cleanString(show_name);
				compare_show_name  = PubFun.cleanString(show.getName());
				if(compare(show_name,compare_show_name)>0.8){
					log.info("通过演出标题相似度过滤"+show_name+"比较演出"+compare_show_name+"不是一场演出");
					exist = true;
				}
			}
			String standard_agency_id="";
			if(exist){
				show.setId(rs.getString(2)==null?rs.getString(1):rs.getString(2));
				if(show.isFirstAgent()){
					if(rs.getInt(4)==3){
						show.setStatus(3);
					}
					setFirstAgentIgNoreSeatPic(conn, show);
					standard_agency_id  = rs.getString(6);
					if(!StringUtils.isEmpty(standard_agency_id)){
						Iterator<Entry<String, List<TicketPrice>>> i = show.getTimeAndPrice().entrySet().iterator();
						StringBuffer prices = new StringBuffer();
						if(i.hasNext()){
							List<TicketPrice> price = i.next().getValue();
							Collections.sort(price, new Comparator<TicketPrice>() {
								@Override
								public int compare(TicketPrice o1, TicketPrice o2) {
									return Double.valueOf(o1.getPrice())>Double.valueOf(o2.getPrice())?0:1;
								}
							});
							for(TicketPrice p:price){
								if(!"-1".equals(p.getPrice())){
									if(prices.indexOf("/"+p.getPrice())==-1){
										prices.append("/").append(p.getPrice().replaceAll("\\.0*$", ""));
									}
								}
							}
						}
						String updateStatus = "update t_show_info set standard_agency_id=?,agency_show_price=?	where  show_id=? and standard_agency_id is not null";
						updateStatusPs = conn.prepareStatement(updateStatus);
						updateStatusPs.setString(1, show.getAgent_id());
						updateStatusPs.setString(2, prices.substring(1));
						updateStatusPs.setString(3, show.getId());
						updateStatusPs.executeUpdate();
					}
				}
				//没有水印,补充图片,优先级
				if(!show.isWatermark()){
					//判断图片优先级
					String dataPic = rs.getString(3);
					int dataPicLevel = 100;
					String showPic = show.getImage_path();
					int showPicLevel = 100;
					//便利所有图片优先级网站,判断图片优先级
					for(Entry<String,Integer> entry:Const.picLevel.entrySet()){
						if(dataPic!=null&&dataPic.indexOf(entry.getKey())!=-1){
							dataPicLevel = entry.getValue();
						}
						if(showPic!=null&&showPic.indexOf(entry.getKey())!=-1){
							showPicLevel =entry.getValue();
						}
					}
					//优先级越高,值越小.小值才能补充演出图片
					if(showPicLevel<dataPicLevel){
						String savePhoto = "update t_show_info set show_photo=? where show_id=? and standard_agency_id is null";
						stmtInsertPhoto = conn.prepareStatement(savePhoto);
						stmtInsertPhoto.setString(1, show.getImage_path());
						stmtInsertPhoto.setString(2, show.getId());
						stmtInsertPhoto.executeUpdate();
					}
					
				}
				if(standard_agency_id!=null&&!standard_agency_id.equals("")&&show.getAgent_id().equals(standard_agency_id)){
					notityTime(conn,show);
				}
				savePrice(conn,show);
			}
		} catch (SQLException e) {
			log.error(show.toString(),e);
			e.printStackTrace();
			return exist;
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(stmt);
			DBUtils.closeStatement(stmtInsertPhoto);
			DBUtils.closeConnection(conn);
		}
		return exist;
	}

	/**
	 * 设置总代ID,同时添加票的状态和标准票价
	 * @param conn	数据库连接
	 * @param show	演出数据
	 * @throws SQLException 
	 */
	private synchronized void setFirstAgent(Connection conn, Show show) throws SQLException {
		PreparedStatement stmt = null;
		try {
//			stmt = conn.prepareStatement("update t_show_info set first_agency_id=?,status=?,agency_show_price=?,is_online_seat=?,seatPic_path=?,standard_agency_id=? where show_id=?");
			stmt = conn.prepareStatement("update t_show_info set first_agency_id=?,status=?,agency_show_price=?,is_online_seat=?,seatPic_path=? where show_id=?");
			stmt.setString(1, show.getAgent_id());
			stmt.setInt(2, show.getStatus());
			Iterator<Entry<String, List<TicketPrice>>> i = show.getTimeAndPrice().entrySet().iterator();
			StringBuffer prices = new StringBuffer();
			if(i.hasNext()){
				List<TicketPrice> price = i.next().getValue();
				Collections.sort(price, new Comparator<TicketPrice>() {
					@Override
					public int compare(TicketPrice o1, TicketPrice o2) {
						return Double.valueOf(o1.getPrice())>Double.valueOf(o2.getPrice())?0:1;
					}
				});
				for(TicketPrice p:price){
					if(!"-1".equals(p.getPrice())){
						if(prices.indexOf("/"+p.getPrice())==-1){
							prices.append("/").append(p.getPrice().replaceAll("\\.0*$", ""));
						}
					}
				}
			}
			stmt.setString(3, prices.substring(1));
			stmt.setString(4, show.isOnlineSeat()?"1":"0");
			stmt.setString(5, show.getSeatPic_path());
//			stmt.setString(6, show.getAgent_id());
//			stmt.setString(7, show.getId());
			//机器不允许修改standard_agency_id
			stmt.setString(6, show.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			DBUtils.closeStatement(stmt);
		}
	}

	/**
	 * 保存每一场演出
	 * @param show
	 */
	public void saveShow(Show show) throws SQLException{
		if(show.getTimeAndPrice().isEmpty()||StringUtils.isEmpty(show.getName())){
			return;
		}
		boolean continueFlag = false;//继续的标志
		for(Entry<String,List<TicketPrice>> entry :show.getTimeAndPrice().entrySet()){
			String key = entry.getKey();
			List<TicketPrice> value =entry.getValue();
			if(key!=null&&!key.equals("")&&!value.isEmpty()){
				continueFlag = true;
			}
		}
		if(!continueFlag){
			return;
		}
		if(isExistShow(show)){
			oldshowCount.incrementAndGet();
			return;
		}
		lock.lock();
		if(isExistShow(show)){
			lock.unlock();
			return;
		}
		newshowCount.incrementAndGet();
		Connection conn = null;
		PreparedStatement stmt = null;
		PreparedStatement stmtDelPrice = null;
		PreparedStatement stmtTime = null;
		PreparedStatement stmtPrice = null;
		ResultSet rs = null;
		try {
			conn = DBUtils.getConnection();
			StringBuffer sql = new StringBuffer();
			sql.append("insert into t_show_info (show_name,show_info,show_photo,show_type_id,site_id,first_agency_id,crawled_time,show_name_clean,status,agency_show_price,is_online_seat,seatPic_path,standard_agency_id)");
			sql.append(" values (?,?,?,?,?,?,now(),?,?,?,?,?,?)");
			stmt = conn.prepareStatement(sql.toString());
			stmt.setString(1, show.getName());
			//内容抓取指导：以总代理为准。若找不到总代，则留空
			if(show.isFirstAgent()){
				stmt.setString(2, show.getIntroduction());
			}else{
				stmt.setString(2, null);
			}
			//如果图片有水印或者图片不是大麦网的设置为空
			stmt.setString(3, (show.isWatermark()||show.getImage_path().indexOf("damai")==-1)?null:show.getImage_path());
			stmt.setInt(4, show.getType());
			stmt.setString(5, show.getSiteID());
			stmt.setString(6, show.isFirstAgent()?show.getAgent_id():null);
			stmt.setString(7, PubFun.cleanString(show.getName()));
			stmt.setInt(8, show.getStatus());
			//设置t_show_info标准票价信息
			if(show.isFirstAgent()){
				Iterator<Entry<String, List<TicketPrice>>> i = show.getTimeAndPrice().entrySet().iterator();
				StringBuffer prices = new StringBuffer();
				if(i.hasNext()){
					List<TicketPrice> price = i.next().getValue();
					Collections.sort(price, new Comparator<TicketPrice>() {
						@Override
						public int compare(TicketPrice o1, TicketPrice o2) {
							return Double.valueOf(o1.getPrice())>Double.valueOf(o2.getPrice())?0:1;
						}
					});
					for(TicketPrice p:price){
						if(!"-1".equals(p.getPrice())){
							if(prices.indexOf("/"+p.getPrice())==-1){
								prices.append("/").append(p.getPrice().replaceAll("\\.0*$", ""));
							}
						}
					}
				}
				stmt.setString(9, prices.substring(1));
			}else{
				stmt.setString(9, null);
			}
			stmt.setString(10, show.isOnlineSeat()?"1":"0");
			stmt.setString(11, show.getSeatPic_path());
//			stmt.setString(12, show.isFirstAgent()?show.getAgent_id():null);
			//爬虫抓取统一设置为空
			stmt.setString(12, null);
			stmt.executeUpdate();
			rs = stmt.executeQuery("select LAST_INSERT_ID()");
			if(rs.next()){
				show.setId(rs.getString(1));
				saveTimeAndPrice(conn,show);
			}
		} catch (SQLException e) {
			log.error(show.toString(),e);
		} finally {
			lock.unlock();
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(stmt);
			DBUtils.closeStatement(stmtDelPrice);
			DBUtils.closeStatement(stmtTime);
			DBUtils.closeStatement(stmtPrice);
			DBUtils.closeConnection(conn);
		}
	}
	
	/**
	 * 保存时间和票价
	 * @param conn		数据库连接
	 * @param show		演出信息
	 * @throws SQLException
	 */
	private void saveTimeAndPrice(Connection conn,Show show) throws SQLException{
		PreparedStatement stmtDelPrice = null;
		PreparedStatement stmtTime = null;
		PreparedStatement stmtPrice = null;
		PreparedStatement stmtDate = null;
		try{
			String id = show.getId();
			String delPrice = "delete t_agency_price from t_agency_price "+
				" join t_show_time on  t_show_time.show_id=? "+
				" where t_show_time.one_show_id=t_agency_price.one_show_id";
			stmtDelPrice = conn.prepareStatement(delPrice);
			stmtDelPrice.setString(1, id);
			stmtDelPrice.executeUpdate();
			String insertDate = "insert into t_show_date (show_id,agency_id,url,remark,price,show_time,detail_url,standard_price) values (?,?,?,?,?,?,?,?)";
			String insertTime = "insert into t_show_time (show_id,show_time) values (?,?)";
			String insertPrice = "insert into t_agency_price (one_show_id,is_exist,agency_price,agency_id,main_url,detail_url,remark,standard_price)"
				+ " select time.one_show_id,?,?,?,?,?,?,? from t_show_time time where time.show_id=? and time.show_time=? limit 1";
			stmtDate = conn.prepareStatement(insertDate);
			stmtTime = conn.prepareStatement(insertTime);
			stmtPrice = conn.prepareStatement(insertPrice);
			
			for(String time:show.getTimeAndPrice().keySet()){
				if(!DateUtils.isStandard(time) && !"待定".equals(time.trim())){//如果日期不标准,插入日期表
					for(TicketPrice price:show.getTimeAndPrice().get(time)){
						if(price.getPrice() == null || "-1".equals(price.getPrice())){//没票不保存
							continue;//如果没有价格信息,直接跳过
						}
						dataPricesCount.decrementAndGet();
						String remark = price.getRemark();
						if(remark!=null){
							remark = remark.replaceAll("[×xX]", "*");
						}
						if(remark!=null&&!remark.contains("*")){
							remark  = null;
						}
						stmtDate.setString(1, id);
						stmtDate.setString(2, show.getAgent_id());
						stmtDate.setString(3, price.getMainURL());
						stmtDate.setString(4, remark);
						stmtDate.setString(5, price.getPrice());
						stmtDate.setString(6, time);
						stmtDate.setString(7, price.getDetailURL());
						stmtDate.setString(8, price.getPrice());
						stmtDate.addBatch();
					}
					continue;
				}else if("待定".equals(time.trim())){
					return;
				}else if(time.compareTo(DateUtils.now)<0){
					continue;//如果已经过期,直接跳过
				}
				stmtTime.setString(1, id);
				stmtTime.setString(2, time);
				stmtTime.addBatch();
				for(TicketPrice price:show.getTimeAndPrice().get(time)){
					if(price.getPrice() == null || "-1".equals(price.getPrice())){//没票不保存
						continue;//如果没有价格信息,直接跳过
					}
					timePricesCount.decrementAndGet();
					String remark = price.getRemark();
					if(remark!=null){
						remark = remark.replaceAll("[×xX]", "*");
					}
					if(remark!=null&&!remark.contains("*")){
						remark  = null;
					}
					stmtPrice.setString(1, price.isExist()?"1":"0");
					stmtPrice.setString(2, price.getPrice());
					stmtPrice.setString(3, show.getAgent_id());
					stmtPrice.setString(4, price.getMainURL());
					stmtPrice.setString(5, price.getDetailURL());
					stmtPrice.setString(6, remark);
					stmtPrice.setString(7, price.getPrice());
					stmtPrice.setString(8, id);
					stmtPrice.setString(9, time);
					stmtPrice.addBatch();
				}
			}
			stmtDate.executeBatch();
			stmtTime.executeBatch();
			stmtPrice.executeBatch();
		}catch(SQLException e){
			throw new SQLException(e);
		} finally{
			DBUtils.closeStatement(stmtDelPrice);
			DBUtils.closeStatement(stmtTime);
			DBUtils.closeStatement(stmtDate);
			DBUtils.closeStatement(stmtPrice);
		}
	}

	/**
	 * 删除大于或者今天的日期数据,用于重新抓取
	 * 解决问题:如果票商以前添加两场时间演出,后来修改了,导致页面显示不一致
	 * @param conn
	 * @param show
	 * @throws SQLException
	 */
	private  synchronized void deleteFulterTime(Connection conn,String one_show_id) throws SQLException{
//		PreparedStatement stmtQuery = null;
//		String selectTime = "delete from t_show_time  where one_show_id=?";
//		stmtQuery = conn.prepareStatement(selectTime);
//		stmtQuery.setString(1, one_show_id);
//		stmtQuery.execute();
//		deleteShowTime.append("delete from t_show_time  where one_show_id="+one_show_id+";\r\n");
	}
	/**
	 * 对于演出时间不同的,需要通知我和周飞
	 * 1:当演出时间!=抓取演出时间 ,就通知
	 */
	private  synchronized void notityTime(Connection conn, Show show)throws SQLException{
		PreparedStatement stmtQuery = null;
		//查询当前演出的时间
		String sql = "select DISTINCT(time.one_show_id),time.show_time from t_show_time time,t_agency_price ap where ap.one_show_id=time.one_show_id and ap.agency_id=? and time.show_id=? and show_time>=now()";
		stmtQuery = conn.prepareStatement(sql);
		stmtQuery.setString(1, show.getAgent_id());
		stmtQuery.setString(2, show.getId());
		ResultSet rs = stmtQuery.executeQuery();
		String time = "";
		while(rs.next()){
			time = rs.getString(2);
			if(show.getTimeAndPrice().get(time)==null){
				String one_show_id = rs.getString(1);
				deleteFulterTime(conn,one_show_id);
			}
			
		}
	}
	/**
	 * 刷新票价信息
	 * @param conn		数据库连接
	 * @param show		演出信息
	 * @throws SQLException
	 */
	private synchronized void savePrice(Connection conn, Show show) throws SQLException {
		PreparedStatement stmtDelPrice = null;//删除票价表
		PreparedStatement stmtInsertPrice = null;//添加新的票价信息到票价表
		PreparedStatement stmtInsertTime = null;//添加新的时间信息到时间表
		PreparedStatement stmtSaveContent = null;//保存人工维护的起止日期
		ResultSet rsContent = null;//保存人工维护的起止日期
		PreparedStatement stmtInsertDate = null;//如果时间不规则添加到date表
		PreparedStatement stmtDelDate = null;//删除date表旧的票价信息
		PreparedStatement stmtShowInfo = null;
		ResultSet rsShowInfo = null;//保存人工维护的起止日期
		int status = 0;
		try{
			String prices = "";
			String id = show.getId();
			String selectShow = "select agency_show_price,status from t_show_info where show_id=?";
			stmtShowInfo = conn.prepareStatement(selectShow);
			stmtShowInfo.setString(1, id);
			rsShowInfo = stmtShowInfo.executeQuery();
			//如果已经存在记录,记录人工编辑的时间,然后删除
			if(rsShowInfo.next()){
				prices = rsShowInfo.getString(1);
				if(prices==null){
					prices = "";
				}
				status = rsShowInfo.getInt(2);
			}
			if(status==3){//演出已经过期或者演出删除
				wuxiaoCount.incrementAndGet();
				return;
			}
			String delPrice = "delete t_agency_price from t_agency_price "+
				" join t_show_time on  t_show_time.show_id=? "+
				" where t_show_time.one_show_id=t_agency_price.one_show_id "+
				" and t_agency_price.agency_id=? and t_show_time.show_time=?";
			stmtDelPrice = conn.prepareStatement(delPrice);
			stmtDelPrice.setString(1, id);
			stmtDelPrice.setString(2, show.getAgent_id());
			
			String saveContent = "select distinct show_begin_date,show_end_date from t_show_date where show_id=? and agency_id=?";
			String startTime = null;
			String endTime = null;
			stmtSaveContent = conn.prepareStatement(saveContent);
			stmtSaveContent.setString(1, id);
			stmtSaveContent.setString(2, show.getAgent_id());
			rsContent = stmtSaveContent.executeQuery();
			//如果已经存在记录,记录人工编辑的时间,然后删除
			if(rsContent.next()){
				startTime = rsContent.getString(1);
				endTime = rsContent.getString(2);
				String delDate = "delete from t_show_date where show_id=? and agency_id=? ";
				stmtDelDate = conn.prepareStatement(delDate);
				stmtDelDate.setString(1, id);
				stmtDelDate.setString(2, show.getAgent_id());
				stmtDelDate.executeUpdate();
			}
			
			String insertPrice = "insert into t_agency_price (one_show_id,is_exist,agency_price,agency_id,main_url,detail_url,remark,standard_price)"
				+ " select time.one_show_id,?,?,?,?,?,?,? from t_show_time time where time.show_id=? and time.show_time=? limit 1";
			String insertTime = "insert into t_show_time (show_id,show_time)"
				+ "select ?,? from dual where not exists (select 1 from t_show_time ti where ti.show_id=? and ti.show_time=?)";
			stmtInsertTime = conn.prepareStatement(insertTime);
			stmtInsertTime.setString(1, id);
			stmtInsertTime.setString(3, id);
			stmtInsertPrice = conn.prepareStatement(insertPrice);
			String insertDate = "insert into t_show_date (show_id,agency_id,url,remark,price,show_time,show_begin_date,show_end_date,detail_url,standard_price) values (?,?,?,?,?,?,?,?,?,?)";
			stmtInsertDate = conn.prepareStatement(insertDate);
			for(String time:show.getTimeAndPrice().keySet()){
				if(!DateUtils.isStandard(time) && !"待定".equals(time.trim())){//如果日期不标准,插入日期表
					for(TicketPrice price:show.getTimeAndPrice().get(time)){
						if(price.getPrice() == null || "-1".equals(price.getPrice())){//没票不保存
							continue;//如果没有价格信息,直接跳过
						}
						dataPricesCount.incrementAndGet();
						String remark = price.getRemark();
						if(remark!=null){
							remark = remark.replaceAll("[×xX]", "*");
						}
						stmtInsertDate.setString(1, id);
						stmtInsertDate.setString(2, show.getAgent_id());
						stmtInsertDate.setString(3, price.getMainURL());
						stmtInsertDate.setString(4, remark);
						stmtInsertDate.setString(5, price.getPrice());
						stmtInsertDate.setString(6, time);
						stmtInsertDate.setString(7, startTime);
						stmtInsertDate.setString(8, endTime);
						stmtInsertDate.setString(9, price.getDetailURL());
						stmtInsertDate.setString(10, price.getPrice());
						stmtInsertDate.addBatch();
					}
					continue;
				}else if("待定".equals(time.trim())){
					return;
				}else if(time.compareTo(DateUtils.now)<0){
					continue;//如果已经过期,直接跳过
				}
				stmtInsertTime.setString(2, time);
				stmtInsertTime.setString(4, time);
				stmtInsertTime.addBatch();
				for(TicketPrice price:show.getTimeAndPrice().get(time)){
					if(price.getPrice() == null || "-1".equals(price.getPrice())){//没票不保存
						continue;
					}
					//总代是否包含价格,不包含跳出
					String comparePrice = price.getPrice();
					if(price.getPrice().contains(".")){
						comparePrice = comparePrice.substring(0,price.getPrice().indexOf("."));
					}
					if(!prices.contains(comparePrice)){
						existPricesCount.incrementAndGet();
						continue;
					}
					timePricesCount.incrementAndGet();
					String remark = price.getRemark();
					if(remark!=null){
						remark = remark.replaceAll("[×xX]", "*");
					}
					stmtInsertPrice.setString(1, price.isExist()?"1":"0");
					stmtInsertPrice.setString(2, price.getPrice());
					stmtInsertPrice.setString(3, show.getAgent_id());
					stmtInsertPrice.setString(4, price.getMainURL());
					stmtInsertPrice.setString(5, price.getDetailURL());
					stmtInsertPrice.setString(6, remark);
					stmtInsertPrice.setString(7, price.getPrice());
					stmtInsertPrice.setString(8, id);
					stmtInsertPrice.setString(9, time);
					stmtInsertPrice.addBatch();
					stmtDelPrice.setString(3, time);
					stmtDelPrice.addBatch();
				}
			}
			stmtInsertTime.executeBatch();
			stmtDelPrice.executeBatch();
			stmtInsertDate.executeBatch();
			stmtInsertPrice.executeBatch();
		}catch(SQLException e){
			throw new SQLException(e);
		} finally{
			DBUtils.closeStatement(stmtDelPrice);
			DBUtils.closeStatement(stmtDelDate);
			DBUtils.closeStatement(stmtInsertPrice);
			DBUtils.closeStatement(stmtInsertTime);
			DBUtils.closeStatement(stmtInsertDate);
			DBUtils.closeStatement(stmtSaveContent);
			DBUtils.closeResultSet(rsContent);
		}
	}
	
	
	/**
	 * 根据sql语句查询结果。
	 * @param sql		要查询的语句
	 * @param param		参数
	 * @return			返回查询结果
	 */
	public List<Map<String,Object>> searchBySQL(String sql,Object... param){
		Connection conn = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		try {
			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement(sql);
			if(param != null){
				for(int i=0;i<param.length;i++){
					stmt.setObject(i+1, param[i]);
				}
			}
			rs = stmt.executeQuery();
			ResultSetMetaData metaData = rs.getMetaData();
			Map<String,Object> row = null;
			while(rs.next()){
				row = new HashMap<String,Object>();
				for(int i=1;i<=metaData.getColumnCount();i++){
					row.put(metaData.getColumnName(i), rs.getObject(i));
				}
				result.add(row);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(stmt);
			DBUtils.closeConnection(conn);
		}
		return result;
	}
	
	/**
	 * 执行对应的sql语句
	 * @param sql		要执行的sql语句
	 * @param param		参数
	 * @return			返回影响的行数
	 */
	public int executeSQL(String sql,Object... param){
		Connection conn = null;
		PreparedStatement stmt = null;
		int result = 0;
		try {
			conn = DBUtils.getConnection();
			stmt = conn.prepareStatement(sql);
			if(param != null){
				for(int i=0;i<param.length;i++){
					stmt.setObject(i+1, param[i]);
				}
			}
			result = stmt.executeUpdate();
		} catch (SQLException e) {
			log.error("执行出错!",e);
			e.printStackTrace();
		} finally {
			DBUtils.closeStatement(stmt);
			DBUtils.closeConnection(conn);
		}
		return result;
	}
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("统计新演出数据"+newshowCount+"r\n");
		sb.append("已经存在演出数据"+oldshowCount+"r\n");
		sb.append("无效演出"+wuxiaoCount+"r\n");
		sb.append("统计新的时间票价数据"+timePricesCount+"r\n");
		sb.append("统计新的日期票价数据"+dataPricesCount+"r\n");
		sb.append("统计和总代不一直票价数据"+existPricesCount+"r\n");
		return sb.toString();
	}
	/**
	 * 设置总代ID,但不设置座位图
	 * @param conn	数据库连接
	 * @param show	演出数据
	 * @throws SQLException 
	 */
	private synchronized void setFirstAgentIgNoreSeatPic(Connection conn, Show show) throws SQLException {
		PreparedStatement stmt = null;
		try {
//			stmt = conn.prepareStatement("update t_show_info set first_agency_id=?,status=?,agency_show_price=?,is_online_seat=?,seatPic_path=?,standard_agency_id=? where show_id=?");
			stmt = conn.prepareStatement("update t_show_info set first_agency_id=?,status=?,agency_show_price=? where show_id=?");
			stmt.setString(1, show.getAgent_id());
			stmt.setInt(2, show.getStatus());
			Iterator<Entry<String, List<TicketPrice>>> i = show.getTimeAndPrice().entrySet().iterator();
			StringBuffer prices = new StringBuffer();
			if(i.hasNext()){
				List<TicketPrice> price = i.next().getValue();
				Collections.sort(price, new Comparator<TicketPrice>() {
					@Override
					public int compare(TicketPrice o1, TicketPrice o2) {
						return Double.valueOf(o1.getPrice())>Double.valueOf(o2.getPrice())?0:1;
					}
				});
				for(TicketPrice p:price){
					if(!"-1".equals(p.getPrice())){
						if(prices.indexOf("/"+p.getPrice())==-1){
							prices.append("/").append(p.getPrice().replaceAll("\\.0*$", ""));
						}
					}
				}
			}
			stmt.setString(3, prices.substring(1));
//			stmt.setString(4, show.isOnlineSeat()?"1":"0");
//			stmt.setString(5, show.getSeatPic_path());
//			stmt.setString(6, show.getAgent_id());
//			stmt.setString(7, show.getId());
			//机器不允许修改standard_agency_id
			stmt.setString(4, show.getId());
			stmt.executeUpdate();
		} catch (SQLException e) {
			throw new SQLException(e);
		} finally{
			DBUtils.closeStatement(stmt);
		}
	}
}
