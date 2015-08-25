package com.piaoyou.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.piaoyou.bean.CommonInfo;
import com.piaoyou.text.TextSimilarity;
import com.piaoyou.util.Const;
import com.piaoyou.util.PubFun;
import com.piaoyou.util.date.DateUtils;
import com.piaoyou.util.db.DBUtils;


public class DataDao {
	
	private Lock lock = new ReentrantLock();
	private static final Log log = LogFactory.getLog(DataDao.class);
	private TextSimilarity textSimilarity = new TextSimilarity();
	public  double compare(String name1,String name2) {
		if (name1.indexOf(name2) > -1 || name2.indexOf(name1) > -1) {
			return 1d;
		}
		return textSimilarity.similarity(name1, name2);
	}
	/**
	 * 根据sql语句查询结果。
	 * @param conn      数据库连接
	 * @param sql		要查询的语句
	 * @param param		参数
	 * @return			返回查询结果
	 */
	public List<Map<String,Object>> searchBySQLOnConn(Connection conn,String sql,Object... param){
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<Map<String,Object>> result = new ArrayList<Map<String,Object>>();
		try {
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
		}
		return result;
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
			DBUtils.closeConnection(conn);
			DBUtils.closeResultSet(rs);
			DBUtils.closeStatement(stmt);
		}
		return result;
	}
	
	/**
	 * 执行对应的sql语句
	 * @param sql		要执行的sql语句
	 * @param param		参数
	 * @return			返回影响的行数
	 * @throws SQLException 
	 */
	public int executeSQL(String sql,Object... param) throws SQLException{
		Connection conn = DBUtils.getConnection();
		PreparedStatement stmt = null;
		int result = 0;
		try {
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
	
	
	/**
	 * 执行对应的sql语句
	 * @param sql		要执行的sql语句
	 * @param param		参数
	 * @return			返回影响的行数
	 * @throws SQLException 
	 */
	public int executeSQLOnConn(Connection conn,String sql,Object... param) throws SQLException{
		PreparedStatement stmt = null;
		int result = 0;
		try {
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
		}
		return result;
	}
	
	/**
	 * 保存演出信息
	 * @param show
	 * @throws SQLException 
	 */
	
	public void saveCommonInfo(CommonInfo common) throws SQLException {
		Connection conn = DBUtils.getConnection();
		conn.setAutoCommit(false);
		PreparedStatement stmtInsertCommonInfo = null;
		
		ResultSet rs = null;
		//保存演出之前判断演出是否存在
		try {
			//如果场馆ID为无效场馆或者为待定，则不用保存，直接返回
			if(common.getSite_id()==1848||common.getSite_id()==200){
				return ;
			}
			int common_id =isCommonInfoExist(conn,common); 
			if (common_id == 0) { // 演出不存在，添加演出信息和时间票价信息
				// 1 添加演出信息
				String insertCommonInfo = "INSERT INTO common_info(name,"
						+ "						 name_clean,"
						+ "                        site_id,"
						+ "                        address,"
						+ "                        show_type,"
						+ "                        remote_img_url,"
						+ "                        heng_image_path,"
						+ "                        status,"
						+ "                        time,"
						+ "                        movie_type,"
						+ "                        is_first_agency,"
						+ "                        type,"
						+ "                        introduction," 
						+ "						   city_id,"
						+ "						   is_check,"
						+ "                        insert_time)"
						+ "		VALUES (?," + "        ?," + "        ?,"
						+ "        		?," + "        ?," + "        ?,"
						+ "        		?," + "        ?," + "        ?,"
						+ "        		?,?,?,?,?,?,now())";
				stmtInsertCommonInfo = conn.prepareStatement(insertCommonInfo);
				stmtInsertCommonInfo.setString(1, common.getName());
				stmtInsertCommonInfo.setString(2, PubFun.cleanString(common.getName()));
				stmtInsertCommonInfo.setInt(3, common.getSite_id());
				stmtInsertCommonInfo.setString(4, common.getAddress());
				stmtInsertCommonInfo.setInt(5, common.getShow_type());
				stmtInsertCommonInfo.setString(6, common.getRemote_img_url());
				stmtInsertCommonInfo.setString(7, common.getHeng_image_path());
				stmtInsertCommonInfo.setInt(8, common.getStatus());
				stmtInsertCommonInfo.setString(9, common.getTime());
				stmtInsertCommonInfo.setString(10, common.getMovie_type());
				stmtInsertCommonInfo.setInt(11, common.getIs_first_agency());
				stmtInsertCommonInfo.setInt(12, common.getType());
				stmtInsertCommonInfo.setString(13, common.getIntroduction());
				stmtInsertCommonInfo.setInt(14, common.getCityId());
				stmtInsertCommonInfo.setString(15, common.getIs_check());
				stmtInsertCommonInfo.execute();
				rs = stmtInsertCommonInfo.executeQuery("select LAST_INSERT_ID()");
				if (rs.next()) {
					common.setId(Integer.parseInt(rs.getString(1)));
				}
			} else {
				//判断该演出是否已经被删除 如果被删除 则直接返回，不进行其他操作
				List<Map<String,Object>> list = searchBySQLOnConn(conn, "select is_delete from common_info where id = ?",common_id);
				if("1".equals(list.get(0).get("is_delete"))){
					return ;
				}else{  //更新演出相关信息
					common.setId(common_id);
					if(common!=null&&common.getImg_url()!=null&&common.getImg_url().contains("http://pimg.damai.cn")){
						String updateCommonInfo = "UPDATE common_info SET status = ?,address= ? ,remote_img_url = ?,movie_type = ? WHERE id = ? ";
						executeSQLOnConn(conn, updateCommonInfo, common.getStatus(),common.getAddress(),common.getRemote_img_url(),common.getMovie_type(),common.getId());
					}else{
						String updateCommonInfo = "UPDATE common_info SET status = ?,address= ? ,time=?,movie_type=? WHERE id = ? ";
						executeSQLOnConn(conn, updateCommonInfo, common.getStatus(),common.getAddress(),common.getTime(),common.getMovie_type(),common.getId());
					}
					
				}
			}
			// 2添加时间信息
			if (common.getShow_time() != null) {
				List<String> timeList = common.getShow_time();
				for (int i = 0; i < timeList.size(); i++) {
					int id = common.getId();
					String time = timeList.get(i);
					String limitTime= "";
					if(time!=null){
						if(time.contains(",")){
							String tmp[] = time.split(",");
							time = tmp[0];
							limitTime = tmp[1];
						}
						if(common.getType()==Const.COMMON_INFO_TYPE_SHOW){
							if(!DateUtils.isStandard(time)){
								time="全年";
							}
						}
						if (isCommonTimeExist(conn,id, time) == 0) { // 时间不存在，添加时间
							String insertCommonTime = "INSERT INTO common_time(common_id,"
									+ "                        week,"
									+ "                        common_time,"
									+ "                        limit_time)"
									+ "VALUES (?,"
									+ "        ?,"
									+ "        ?,"
									+ "        ?)";
							executeSQLOnConn(conn, insertCommonTime, id,PubFun.getWeek(time),time,limitTime);
						}
					}
				}
			}
			//3 添加票价
			//添加票价之前首先删除票价
			String deleteCommonInfo = 
				"DELETE FROM common_price"+
				" WHERE common_id = ? and agency_id = ?";
			executeSQLOnConn(conn, deleteCommonInfo, common.getId(),common.getAgency_id());
			String insertCommonPrice = 
				"INSERT INTO common_price(common_id,"+
				"                         agency_id,"+
				"                         price,"+
				"                         min_price,"+
				"                         discount,"+
				"                         url)"+
				"VALUES (?,"+
				"        ?,"+
				"        ?,"+
				"        ?,"+
				"        ?,"+
				"        ?)";
			executeSQLOnConn(conn, insertCommonPrice, common.getId(),common.getAgency_id(),common.getPrice(),common.getMin_price(),common.getDiscount(),common.getMainURL());
			conn.commit();
		}catch(Exception e){
			e.printStackTrace();
			conn.rollback();
		}finally{
			DBUtils.closeConnection(conn);
		}
	}
	/**
	 * 判断演出是否存在，存在，返回演出ID，不存在，返回0
	 * @param common
	 * @return
	 */
	private int isCommonInfoExist(Connection conn,CommonInfo common){
		/**
		 * 1  。根据演出名称进行判断，完全相同，表示同一场演出
		 */
		int returnID =  0;
		DataDao dao = new DataDao();
		String sql = "select id,common_id_cor from common_info where name = ? and site_id = ? ";
		List<Map<String,Object>> commonInfoList = dao.searchBySQLOnConn(conn,sql,common.getName(),common.getSite_id());
		if(commonInfoList.size()>0){   
			String id = commonInfoList.get(0).get("id").toString();
			String common_id_cor = commonInfoList.get(0).get("common_id_cor").toString();
			if("0".equals(common_id_cor)){
				returnID =  Integer.parseInt(id); 
			}else{
				returnID = Integer.parseInt(common_id_cor);
			}
		}
		//针对团购和电影，只需要进行判断标题是否完全相同，如果相同，表示同一个演出
		if(common.getType()==Const.COMMON_INFO_TYPE_GROUP||common.getType()==Const.COMMON_INFO_TYPE_MOVIE){
			return returnID;
		} else if (common.getType() == Const.COMMON_INFO_TYPE_SHOW) {
			/**
			 * 针对演出，需要进行一下判断来确实是否属于同一场演出 1.演出名称和演出场馆完全相同 则属于同一场演出
			 */
			sql = "select id from common_info where name = ? and site_id = ?";
			List<Map<String, Object>> showList = dao.searchBySQLOnConn(conn,sql, common.getName(), common.getSite_id());
			if (showList.size() > 0) { // 演出名称和演出场馆完全相同
				returnID = Integer.parseInt(showList.get(0).get("id")
						.toString());
			} else {
				sql = "SELECT tmp.id,tmp.name,tmp.name_clean ,tmp.show_time FROM("
						+ "    SELECT common.id,common.name,common.name_clean,"
						+ "    group_concat(DISTINCT time.common_time ORDER BY time.common_time) show_time"
						+ "    FROM common_info common JOIN common_time time ON common.id=time.common_id   WHERE"
						+ "    common.site_id = ?   GROUP BY time.common_id"
						+ "    ) tmp where tmp.show_time=? ";
				List<String> timeList = common.getShow_time();
				Collections.sort(timeList);
				StringBuffer timeParam = new StringBuffer();
				for (String tmp : timeList) {
					timeParam.append(tmp.toString()).append(",");
				}
				timeParam.setLength(timeParam.length() - 1);
				showList = dao.searchBySQLOnConn(conn, sql,common.getSite_id(), timeParam.toString());
				if (showList.size() > 0) { // 场馆和演出时间完全相同 则表明是同一场演出
					returnID = Integer.parseInt(showList.get(0).get("id").toString());
				} else {
					returnID = 0;
				}
			}
		}
		return returnID;
	}
	
	/**
	 * 演出时间是否存在
	 * @param common
	 * @return
	 */
	private int isCommonTimeExist(Connection conn,int common_id,String common_time){
		int returnID =  0;
		DataDao dao = new DataDao();
		String sql = "select id from common_time where common_id = ? and common_time = ? ";
		List<Map<String,Object>> commonInfoList = dao.searchBySQLOnConn(conn,sql, common_id,common_time);
		if(commonInfoList.size()>0){
			returnID = Integer.parseInt(commonInfoList.get(0).get("id").toString());
		}
		return returnID;
	}
}
