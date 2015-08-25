package com.piaoyou.analyzer.sample;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DbMysql {
	public Connection getConn(){
		try {
			return DriverManager.getConnection("jdbc:mysql://192.168.0.253/piaoyou?useUnicode=true&characterEncoding=UTF-8", "root","123456");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	public List<ShowInfo> getShowInfo(String show_id) throws SQLException{
		String sql="select show_id,show_name_clean,site_id,show_info from t_show_info where show_id > '"+show_id+"'";
		Connection conn = getConn();
		Statement st = conn.createStatement();
		ResultSet rs = st.executeQuery(sql);
		List<ShowInfo> list = new ArrayList<ShowInfo>();
		while(rs.next()){
			ShowInfo sh = new ShowInfo();
			sh.setShow_id(rs.getString(1));
			sh.setSite_id(rs.getString(2));
			sh.setShow_name_clean(rs.getString(3));
			sh.setShow_info(rs.getString(4));
			list.add(sh);
		}
		return list;
		
	}
}
